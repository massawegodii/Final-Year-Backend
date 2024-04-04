package com.massawe.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;
    private String productName;
    @Column(length = 2000)
    private String productDescription;
    private Double productPrice;
    private String productModel;
    private String productStatus;
    private Integer productSerialNo;
    private String productDepartment;
    private String productCategory;
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date productDate;

    @ManyToOne(cascade = CascadeType.PERSIST)// Many products can belong to one user
    @JoinColumn(name = "user_name") // Name of the column in Product table that references User
    private User user;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_images",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private Set<ImageModel> productImages;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "assets_status",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "status_id")
            }
    )
    private Set<Status> assetsStatus;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "assets_department",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "department_id")
            }
    )
    private Set<Department> assetsDepartment;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "assets_category",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "category_id")
            }
    )
    private Set<Category> assetsCategory;


}
