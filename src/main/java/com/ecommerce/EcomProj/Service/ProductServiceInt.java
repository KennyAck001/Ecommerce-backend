package com.ecommerce.EcomProj.Service;


import com.ecommerce.EcomProj.Model.Product;
import com.ecommerce.EcomProj.PayLoad.ProductDTORequest;
import com.ecommerce.EcomProj.PayLoad.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductServiceInt {
    ProductResponse getProduct(Integer pageNum, Integer pageSize, String sortBy, String sortIn);
    ProductDTORequest addProduct(ProductDTORequest productDTO, Long categoryId);

    ProductResponse getProductOnCategory(Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortIn);

    ProductDTORequest deletePrduct(Long productId);

    ProductDTORequest updateProduct(Long categoryId,Long productId, ProductDTORequest productDTO);

    ProductResponse getProductByKeyword(String keyword, Integer pageNum, Integer pageSize, String sortBy, String sortIn);

    ProductDTORequest updateImage(Long productId, MultipartFile file) throws IOException;
}
