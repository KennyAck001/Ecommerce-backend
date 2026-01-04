package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Model.Category;
import com.ecommerce.EcomProj.PayLoad.CategoryDTORequest;
import com.ecommerce.EcomProj.PayLoad.CategoryResponse;

import java.util.List;

public interface CategoryServiceInt {
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String orderBy, String orderIn);
    public CategoryDTORequest addCategory(CategoryDTORequest categoryDTORequest);
    public CategoryDTORequest deleteCategory(Long categoryId);

    CategoryDTORequest updateCategory(CategoryDTORequest categoryDTORequest, Long categoryId);
}
