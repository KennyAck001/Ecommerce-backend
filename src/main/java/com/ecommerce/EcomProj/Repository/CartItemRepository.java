package com.ecommerce.EcomProj.Repository;

import com.ecommerce.EcomProj.Model.CartItems;
import com.ecommerce.EcomProj.Model.Product;
import com.ecommerce.EcomProj.Model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    @Query("""
           SELECT ci
           FROM CartItems ci
           WHERE ci.product.productId = :productId
             AND ci.cart.cartId = :cartId
           """)
    CartItems findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM CartItems ci
    WHERE ci.cart.cartId = :cartId
      AND ci.product.productId = :productId
""")
    void deleteByCartIdAndProductId(
             Long cartId,
            Long productId
    );
}
