package com.ecommerce.EcomProj.Repository;

import com.ecommerce.EcomProj.Model.Address;
import com.ecommerce.EcomProj.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdressRepository extends JpaRepository<Address,Long> {

    List<Address> findByUsers(Users loggedUser);
}
