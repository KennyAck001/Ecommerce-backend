package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.Model.Address;
import com.ecommerce.EcomProj.PayLoad.AdressDTO;
import com.ecommerce.EcomProj.Service.AddressInt;
import com.ecommerce.EcomProj.Service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressInt addressService;

    @PostMapping("/address")
    public ResponseEntity<AdressDTO> postAddress(@Valid @RequestBody AdressDTO adressDTO) {
        AdressDTO addressDTO = addressService.postAddress(adressDTO);
        return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AdressDTO>> getAllAddresses() {
        List<AdressDTO> adressDTO = addressService.getAllAddresses();
        return new ResponseEntity<>(adressDTO,HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AdressDTO>> getAllUserAddresses() {
        List<AdressDTO> adressDTOS = addressService.getUserAddresses();
        return new ResponseEntity<>(adressDTOS,HttpStatus.OK);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AdressDTO> getAddressById(@PathVariable("addressId") Long addressId) {
        AdressDTO adressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(adressDTO,HttpStatus.OK);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AdressDTO> updateAddress(@PathVariable Long addressId,
                                                   @RequestBody AdressDTO adressDTO) {
        AdressDTO savedAddress = addressService.updateAddress(addressId, adressDTO);
        return new ResponseEntity<>(savedAddress,HttpStatus.OK);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
         String status  = addressService.deleteAddress(addressId);
         return new  ResponseEntity<>(status,HttpStatus.OK);

    }
}
