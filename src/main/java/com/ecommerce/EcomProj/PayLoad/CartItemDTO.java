package com.ecommerce.EcomProj.PayLoad;

import com.ecommerce.EcomProj.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemsId;
    private CartDTO cart;
    private Product product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
