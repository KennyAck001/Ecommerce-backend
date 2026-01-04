package com.ecommerce.EcomProj.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 0f 5 characters.")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be atleast 0f 5 characters.")
    private String building;

    @NotBlank
    @Size(min = 3, message = "Building name must be atleast 0f 3 characters.")
    private String city;

    @NotBlank
    @Size(min = 3, message = "State name must be atleast 0f 3 characters.")
    private String state;

    @NotNull
    @Min(value = 100000, message = "Pincode must be at least 6 digits")
    @Max(value = 999999, message = "Pincode must be at most 6 digits")
    @Column(nullable = false)
    private int pincode;

    @NotBlank
    @Size(min = 2, message = "Country name must be atleast 0f 2 characters.")
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;


    public Address(String street, String building, String city, String state, Integer pincode, String country) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
    }
}
