package com.ecommerce.EcomProj.PayLoad;

import com.ecommerce.EcomProj.Model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTORequest {
    private Long productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private Integer productQuantity;
    private Long productPrice;
    private Double productSpecialPrice;
    private Long productDiscount;
}
