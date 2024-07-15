package com.massawe.JwtService;

import com.massawe.dao.AuthLogDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.UserTrack;
import com.massawe.entity.JwtRequest;
import com.massawe.entity.JwtResponse;
import com.massawe.entity.User;
import com.massawe.exception.BlockedUserException;
import com.massawe.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthLogDao authLogDao;

    public JwtResponse createJwtToken(JwtRequest jwtRequest, String ipAddress) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();

        // Check if the user is blocked
        if (isUserBlocked(userName)) {
            throw new BlockedUserException("User is blocked. Please contact the administrator.");
        }

        try {
            authenticate(userName, userPassword);
            logAuthAttempt(userName, ipAddress, true);
        } catch (Exception e) {
            logAuthAttempt(userName, ipAddress, false);
            throw e;
        }

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userDao.findById(userName).get();
        return new JwtResponse(user, newGeneratedToken);
    }

    private void logAuthAttempt(String username, String ipAddress, boolean success) {
        UserTrack lastLog = authLogDao.findFirstByUsernameOrderByTimestampDesc(username);
        int attemptCount = lastLog != null ? lastLog.getAttemptCount() + 1 : 1;

        UserTrack log = new UserTrack();
        log.setUsername(username);
        log.setIpAddress(ipAddress);
        log.setAttemptCount(attemptCount);
        log.setSuccess(success);

        // Format LocalDateTime to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        log.setTimestamp(formattedDateTime);

        authLogDao.save(log);
    }


    public boolean isUserBlocked(String username) {
        User user = userDao.findById(username).orElse(null);
        return user != null && user.isBlocked();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("Username is not valid: " + username);
        }
    }


    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    public void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // Method to block a user
    public void blockUser(String username) {
        User user = userDao.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setBlocked(true);
        userDao.save(user);
    }

    // Method to unblock a user
    public void unblockUser(String username) {
        User user = userDao.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setBlocked(false);
        userDao.save(user);
    }


}
