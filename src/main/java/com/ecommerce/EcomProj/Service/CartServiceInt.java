package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Model.Users;
import com.ecommerce.EcomProj.PayLoad.CartDTO;

import java.util.List;

public interface CartServiceInt {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getCartItems();

    CartDTO getUserCart();

    CartDTO updateProductQuantityInCart(Long productId, Integer operation);

    String deleteProductFromCart(Long cartId,Long productId);

    void updateProductInCart(Long cartId, Long productId);
}
