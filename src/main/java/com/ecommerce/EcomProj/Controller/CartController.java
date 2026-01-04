package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.Model.Users;
import com.ecommerce.EcomProj.PayLoad.CartDTO;
import com.ecommerce.EcomProj.PayLoad.ProductDTORequest;
import com.ecommerce.EcomProj.Service.CartServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartServiceInt cartService;

    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartDTO>> getCart(){
        List<CartDTO> cartDTO = cartService.getCartItems();
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @GetMapping("/user/cart")
    public ResponseEntity<CartDTO> getUserCart(){
    CartDTO cartDTO = cartService.getUserCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/user/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantityToCart(@PathVariable Long productId,
                                                               @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @DeleteMapping("/user/cart/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        String cartDTO = cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
