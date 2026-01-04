package com.ecommerce.EcomProj.PayLoad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdressDTO {
    private Long addressId;
    private String street;
    private String building;
    private String city;
    private String state;
    private int pincode;
    private String country;
}
