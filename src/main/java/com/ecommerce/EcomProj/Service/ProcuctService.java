package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Exception.APIException;
import com.ecommerce.EcomProj.Exception.ResourceNotFound;
import com.ecommerce.EcomProj.Model.Cart;
import com.ecommerce.EcomProj.Model.Category;
import com.ecommerce.EcomProj.Model.Product;
import com.ecommerce.EcomProj.PayLoad.CartDTO;
import com.ecommerce.EcomProj.PayLoad.ProductDTORequest;
import com.ecommerce.EcomProj.PayLoad.ProductResponse;
import com.ecommerce.EcomProj.Repository.CartRepository;
import com.ecommerce.EcomProj.Repository.CategoryRepository;
import com.ecommerce.EcomProj.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProcuctService implements ProductServiceInt{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${application.path}")
    String path;


    @Override
    public ProductResponse getProduct(Integer pageNum, Integer pageSize, String sortBy, String sortIn) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("No product has been created");
        }
        List<ProductDTORequest> productDTORequests = products.stream().
                map(product -> modelMapper.map(product, ProductDTORequest.class))
                .toList();

        ProductResponse response = new ProductResponse(productPage.getTotalPages(),productPage.getSize(),productPage.getTotalElements(),productPage.getTotalPages(),productPage.isLast());
        response.setContent(productDTORequests);
        return  response;

    }

    @Override
    public ProductDTORequest addProduct(ProductDTORequest productDTO, Long categoryId) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFound("Category","categoryId",categoryId));

        Product product = modelMapper.map(productDTO, Product.class);
        Product existingProduct = productRepository.findByProductName(product.getProductName());
        if (existingProduct != null){
            throw new APIException("Product already exist");
        }

        product.setCategory(category);
        product.setProductImage("Default.png");
        Double specialPrice = product.getProductPrice() -
                ((product.getProductDiscount() * 0.01)*product.getProductPrice());

        product.setProductSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        ProductDTORequest productDTORequest = modelMapper.map(savedProduct, ProductDTORequest.class);

        return productDTORequest;
    }

    @Override
    public ProductResponse getProductOnCategory(Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortIn) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()->new ResourceNotFound("Category","categoryId",categoryId));

        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Product> productPage = productRepository.findByCategory(pageable,category);

        List<Product> filteredProducts = productPage.getContent();

        if (filteredProducts.isEmpty()) throw new APIException("Product is not created yet");

        List<ProductDTORequest> request = filteredProducts.stream().
                map(p->modelMapper.map(p,ProductDTORequest.class)).toList();

        ProductResponse response = new ProductResponse(productPage.getTotalPages(),
                productPage.getSize(),productPage.getTotalElements(),
                productPage.getTotalPages(),productPage.isLast());

        response.setContent(request);
        return  response;
    }


    @Override
    public ProductDTORequest deletePrduct(Long productId) {
        Product deleteProduct = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFound("Product","productId",productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(deleteProduct);

        ProductDTORequest deletedProduct = modelMapper.map(deleteProduct, ProductDTORequest.class);
        return deletedProduct;
    }


    @Override
    public ProductDTORequest updateProduct(Long categoryId,Long productId, ProductDTORequest productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound(
                        "Product", "productId", productId));

        Category newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFound("Category","categoryId",categoryId));

        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductPrice(product.getProductPrice());
        existingProduct.setProductDiscount(product.getProductDiscount());
        existingProduct.setCategory(newCategory);

        Double specialPrice = existingProduct.getProductPrice()
                -  ((existingProduct.getProductDiscount() * 0.01)
                * existingProduct.getProductPrice());
        existingProduct.setProductSpecialPrice(specialPrice);

        Product newProduct = productRepository.save(existingProduct);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTORequest> productDto = cart.getItems().stream().
                    map(item -> modelMapper.map(item, ProductDTORequest.class)).toList();
            cartDTO.setProducts(productDto);
            return cartDTO;
        }).toList();
        cartDTOs.forEach(cart->cartService.updateProductInCart(cart.getCartId(), productId));

        ProductDTORequest updatedProduct = modelMapper.map(newProduct, ProductDTORequest.class);
        return updatedProduct;

    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, Integer pageNum, Integer pageSize, String sortBy, String sortIn) {
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(pageable, keyword);

        List<Product> filteredProducts = productPage.getContent();



        if (filteredProducts.isEmpty()) throw new APIException("Product is not created yet");

        List<ProductDTORequest> request = filteredProducts.stream().
                map(p->modelMapper.map(p,ProductDTORequest.class)).toList();

        ProductResponse response = new ProductResponse(productPage.getTotalPages(),
                productPage.getSize(),productPage.getTotalElements(),
                productPage.getTotalPages(),productPage.isLast());

        response.setContent(request);
        return  response;
    }

    @Override
    public ProductDTORequest updateImage(Long productId, MultipartFile file) throws IOException {
        // Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));

        // Upload image to server
        // Get the file name of uploaded images
        String path = this.path;
        String fileName = fileHandler.uploadImage(path, file);

        // Updating the new file name to the product
        productFromDb.setProductImage(fileName);

        // Save updated product
        Product updatedProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTORequest.class);
    }
}
