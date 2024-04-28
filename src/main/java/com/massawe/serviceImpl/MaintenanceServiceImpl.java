package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.MaintenanceDao;
import com.massawe.entity.Maintenance;
import com.massawe.service.MaintenanceService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    @Autowired
    MaintenanceDao maintenanceDao;

    @Override
    public ResponseEntity<String> add(Map<String, String> requestMap) {
        try {
            String selectedDateStr = requestMap.get("selectedDate");
            String selectedTimeStr = requestMap.get("selectedTime");
            String note = requestMap.get("note");

            // Validate date format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate selectedDate = null;
            try {
                selectedDate = LocalDate.parse(selectedDateStr, dateFormatter);
            } catch (Exception e) {
                return MyUtils.getResponseEntity("Invalid date format. Please use yyyy-MM-dd.", HttpStatus.OK);
            }

            // Validate time format
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime selectedTime = null;
            try {
                selectedTime = LocalTime.parse(selectedTimeStr, timeFormatter);
            } catch (Exception e) {
                return MyUtils.getResponseEntity("Invalid time format. Please use check the time format!", HttpStatus.OK);
            }

            // Check if the note already exists
            if (maintenanceDao.existsByNote(note)) {
                return MyUtils.getResponseEntity("Maintenance Schedule already exists.", HttpStatus.OK);
            }

            // Create Maintenance object
            Maintenance maintenance = new Maintenance();
            maintenance.setSelectedDate(selectedDate);
            maintenance.setSelectedTime(selectedTime);
            maintenance.setNote(requestMap.get("note"));
            // Set other properties

            // Save Maintenance object
            maintenanceDao.save(maintenance);

            return MyUtils.getResponseEntity("Maintenance Schedule added successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getAllMaintenance() {
        try {
            List<Maintenance> maintenanceList = (List<Maintenance>) maintenanceDao.findAll();
            if (maintenanceList.isEmpty()) {
                return MyUtils.getResponseEntity("No Maintenance Schedule found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(maintenanceList);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
