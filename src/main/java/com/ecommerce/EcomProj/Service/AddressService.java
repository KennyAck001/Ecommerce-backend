package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Exception.APIException;
import com.ecommerce.EcomProj.Exception.ResourceNotFound;
import com.ecommerce.EcomProj.Model.Address;
import com.ecommerce.EcomProj.Model.Users;
import com.ecommerce.EcomProj.PayLoad.AdressDTO;
import com.ecommerce.EcomProj.Repository.AdressRepository;
import com.ecommerce.EcomProj.Repository.UserRepository;
import com.ecommerce.EcomProj.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements AddressInt {

    @Autowired
    AdressRepository adressRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    UserRepository userRepository;

    @Override
    public AdressDTO postAddress(AdressDTO adressDTO) {
        Address address = modelMapper.map(adressDTO, Address.class);
        Users loggedUser = authUtils.loggedInUser();

        // getting the user's addresses and adding the new address from the request into it
        List<Address> addressList = loggedUser.getAddresses();
        addressList.add(address);
        // updating the addresses and setting the user
        loggedUser.setAddresses(addressList);
        address.setUsers(loggedUser);
        Address savedAddress = adressRepository.save(address);

        return modelMapper.map(savedAddress, AdressDTO.class);

    }

    @Override
    public List<AdressDTO> getAllAddresses() {
        List<Address>  addressList = adressRepository.findAll();
        if (addressList.isEmpty()) {
            throw new APIException("Address not found");
        }
        List<AdressDTO> getAddressDTO = addressList.stream().map(item->
                modelMapper.map(item, AdressDTO.class)).toList();

        return getAddressDTO;
    }

    @Override
    public List<AdressDTO> getUserAddresses() {
        Users loggedUser = authUtils.loggedInUser();
        List<Address> addressList = adressRepository.findByUsers(loggedUser);
        if (addressList.isEmpty()) {
            throw new APIException("Address not found");
        }
        List<AdressDTO> adressDTOS = addressList.stream().map(address ->
                modelMapper.map(address, AdressDTO.class)).toList();
        return adressDTOS;
    }

    @Override
    public AdressDTO getAddressById(Long addressId) {
        Address address = adressRepository.findById(addressId).
                orElseThrow(()->new ResourceNotFound("Address", "addressId", addressId));

        return modelMapper.map(address, AdressDTO.class);
    }

    @Override
    public AdressDTO updateAddress(Long addressId, AdressDTO adressDTO) {
        Address checkAddress = adressRepository.findById(addressId).
                orElseThrow(()-> new ResourceNotFound("Address", "addressId", addressId));

        checkAddress.setCountry(adressDTO.getCountry());
        checkAddress.setCity(adressDTO.getCity());
        checkAddress.setState(adressDTO.getState());
        checkAddress.setStreet(adressDTO.getStreet());
        checkAddress.setPincode(adressDTO.getPincode());
        checkAddress.setBuilding(adressDTO.getBuilding());

        Address savedAddress = adressRepository.save(checkAddress);

        Users loggedUser = checkAddress.getUsers();
        loggedUser.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        loggedUser.getAddresses().add(savedAddress);
        userRepository.save(loggedUser);

        return modelMapper.map(savedAddress, AdressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB = adressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFound("Address", "addressId", addressId));

        Users loggedUser = addressFromDB.getUsers();
        loggedUser.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(loggedUser);

        adressRepository.delete(addressFromDB);

        return "Address Deleted Successfully";
    }
}
