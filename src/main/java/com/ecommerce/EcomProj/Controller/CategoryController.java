package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.PayLoad.CategoryDTORequest;
import com.ecommerce.EcomProj.PayLoad.CategoryResponse;
import com.ecommerce.EcomProj.Service.CategoryServiceInt;
import com.ecommerce.EcomProj.config.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") //  this is used when all the mapping starts with the same api for example here api is the starting point
public class CategoryController {

    private final CategoryServiceInt categoryServiceInt;

    public CategoryController(CategoryServiceInt categoryServiceInt){
        this.categoryServiceInt = categoryServiceInt;
    }

    // Works same as the other mappings
  //  @RequestMapping(value = "/api/public/category", method = RequestMethod.GET)
    @GetMapping("/public/category")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue= AppConstants.PAGE_NUMBER) Integer pageNum,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                                                             @RequestParam(name = "sortIn", defaultValue = AppConstants.SORT_IN) String sortIn){
        CategoryResponse categoryResponse =  categoryServiceInt.getAllCategories(pageNum, pageSize,sortBy, sortIn);
        return new  ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }


    @PostMapping("/public/category")
    public ResponseEntity<CategoryDTORequest> addCategory(@Valid @RequestBody CategoryDTORequest categoryDTORequest){

            CategoryDTORequest savedCategoryDTO = categoryServiceInt.addCategory(categoryDTORequest);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }
    

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTORequest> deleteCategory(@PathVariable Long categoryId){

            CategoryDTORequest deletedCategoryDTO = categoryServiceInt.deleteCategory(categoryId);
            return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

//           2 other ways to return the ResponseEntity with message
//           return ResponseEntity.ok(isCategory);
//           return ResponseEntity.status(HttpStatus.OK).body(isCategory);
    }

    
    @PutMapping("/public/category/{categoryId}")
    public ResponseEntity<CategoryDTORequest> updateCategory(@Valid @RequestBody CategoryDTORequest categoryDTORequest, @PathVariable Long categoryId){

            CategoryDTORequest savedDTO = categoryServiceInt.updateCategory(categoryDTORequest,categoryId);
            return new ResponseEntity<>(savedDTO, HttpStatus.OK);

    }
}
