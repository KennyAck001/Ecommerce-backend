package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Exception.APIException;
import com.ecommerce.EcomProj.Exception.ResourceNotFound;
import com.ecommerce.EcomProj.Model.Category;
import com.ecommerce.EcomProj.PayLoad.CategoryDTORequest;
import com.ecommerce.EcomProj.PayLoad.CategoryResponse;
import com.ecommerce.EcomProj.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceInt {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String orderBy, String orderIn){

        Sort sort = "asc".equalsIgnoreCase(orderIn) ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending() ;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
//      List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty())
            throw new APIException("No categories has created yet.");

        // All these so that api can be formed in paggination way or something, didn't get what was it but
        // its working...
        List<CategoryDTORequest> categoryDTORequests = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTORequest.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse(pageNumber, pageSize,categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.isLast());
        categoryResponse.setContent(categoryDTORequests);
        return categoryResponse;
    }

    @Override
    public CategoryDTORequest addCategory(CategoryDTORequest categoryDTORequest) {

        Category category = modelMapper.map(categoryDTORequest, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDB != null){
            throw new APIException("Category with name "+ category.getCategoryName() +" already exist");
        }

        Category saveCategory = categoryRepository.save(category);
        CategoryDTORequest savedCategoryDTO = modelMapper.map(saveCategory, CategoryDTORequest.class);
        return savedCategoryDTO;

    }



    @Override
    public CategoryDTORequest deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFound("Category","categoryId",categoryId));

        categoryRepository.delete(category);
        CategoryDTORequest deletedCategory = modelMapper.map(category, CategoryDTORequest.class);

        return deletedCategory;

    }


    // Can do same as we did in the delete but this one is new way if you like you can use the delete methods logic
    @Override
    public CategoryDTORequest updateCategory(CategoryDTORequest categoryDTORequest, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFound("Category","categoryId",categoryId));

        category.setCategoryName(categoryDTORequest.getCategoryName());
        Category updatedCategory = categoryRepository.save(category);
        CategoryDTORequest request = modelMapper.map(updatedCategory, CategoryDTORequest.class);
        return request;
    }


}
