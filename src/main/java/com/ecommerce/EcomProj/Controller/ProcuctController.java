package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.PayLoad.ProductDTORequest;
import com.ecommerce.EcomProj.PayLoad.ProductResponse;
import com.ecommerce.EcomProj.Service.ProductServiceInt;
import com.ecommerce.EcomProj.config.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProcuctController {

    @Autowired
    ProductServiceInt productServiceInt;

    @GetMapping("/public/product")
    public ResponseEntity<ProductResponse> getProduct(@RequestParam(name = "pageNumber", defaultValue= AppConstants.PAGE_NUMBER) Integer pageNum,
                                                      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                      @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                                                      @RequestParam(name = "sortIn", defaultValue = AppConstants.SORT_IN) String sortIn){

        ProductResponse response = productServiceInt.getProduct(pageNum,pageSize,sortBy,sortIn);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/product/")
    public ResponseEntity<ProductResponse> getProductOnCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue= AppConstants.PAGE_NUMBER) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortIn", defaultValue = AppConstants.SORT_IN) String sortIn){

        ProductResponse response = productServiceInt.getProductOnCategory(categoryId,pageNum,pageSize,sortBy,sortIn);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue= AppConstants.PAGE_NUMBER) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortIn", defaultValue = AppConstants.SORT_IN) String sortIn
    ){
        ProductResponse response = productServiceInt.getProductByKeyword(keyword,pageNum,pageSize,sortBy,sortIn);
        return new  ResponseEntity<>(response,HttpStatus.OK);
    }


    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTORequest> addProduct(@Valid @RequestBody ProductDTORequest productDTO,
                                                        @PathVariable Long categoryId){

        ProductDTORequest productDTORequest = productServiceInt.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(productDTORequest, HttpStatus.CREATED);
    }



    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTORequest> deleteProduct(@PathVariable Long productId){
        ProductDTORequest request = productServiceInt.deletePrduct(productId);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/admin/category/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDTORequest> updateProduct(
            @PathVariable Long categoryId,
            @PathVariable Long productId, @RequestBody ProductDTORequest productDTO){
        ProductDTORequest request = productServiceInt.updateProduct(categoryId,productId, productDTO);
        return new ResponseEntity<>(request,HttpStatus.OK);
    }


    @PutMapping("/admin/product/{productId}/image")
    public ResponseEntity<ProductDTORequest> updateImage(@PathVariable Long productId, @RequestParam("image") MultipartFile file) throws IOException {
        ProductDTORequest updatedImg = productServiceInt.updateImage(productId, file);
        return new ResponseEntity<>(updatedImg, HttpStatus.OK);
    }


}
