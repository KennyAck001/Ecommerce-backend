package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.PayLoad.AdressDTO;

import java.util.List;

public interface AddressInt {
    AdressDTO postAddress(AdressDTO adressDTO);

    List<AdressDTO> getAllAddresses();

    List<AdressDTO> getUserAddresses();

    AdressDTO getAddressById(Long addressId);

    AdressDTO updateAddress(Long addressId, AdressDTO adressDTO);

    String deleteAddress(Long addressId);
}
