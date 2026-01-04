package com.ecommerce.EcomProj.PayLoad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTORequest {
    private Long categoryId;

    @NotBlank(message="Category Name cannot be blank")
    @Size(min = 5, message ="Category Name must contain atleast 5 char")
    private String categoryName;

}
