package com.ecommerce.EcomProj.PayLoad;

import com.ecommerce.EcomProj.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private ProductDTORequest product;
    private Integer quantity;
    private Double discount;
    private Double orderedProductPrice;
}
