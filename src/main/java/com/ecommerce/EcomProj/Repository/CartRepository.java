package com.ecommerce.EcomProj.Repository;

import com.ecommerce.EcomProj.Model.Cart;
import com.ecommerce.EcomProj.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("""
       SELECT DISTINCT c
       FROM Cart c
       JOIN FETCH c.items ci
       JOIN FETCH ci.product p
       WHERE p.productId = :productId
       """)
    List<Cart> findCartsByProductId(@Param("productId") Long productId);

}
