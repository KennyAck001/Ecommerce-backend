package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Exception.APIException;
import com.ecommerce.EcomProj.Exception.ResourceNotFound;
import com.ecommerce.EcomProj.Model.Cart;
import com.ecommerce.EcomProj.Model.CartItems;
import com.ecommerce.EcomProj.Model.Product;
import com.ecommerce.EcomProj.PayLoad.CartDTO;
import com.ecommerce.EcomProj.PayLoad.ProductDTORequest;
import com.ecommerce.EcomProj.Repository.CartItemRepository;
import com.ecommerce.EcomProj.Repository.CartRepository;
import com.ecommerce.EcomProj.Repository.ProductRepository;
import com.ecommerce.EcomProj.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CartService implements CartServiceInt{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtils authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Finding existing cart or creating a new one
        Cart cart = createCart();

        // Retrieving product details using its id
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->   new ResourceNotFound("Product","productId",productId));

        // performing validations
        CartItems cartItems = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());

        if (cartItems != null) {
            throw new APIException("Product with id "+product.getProductId()+"already in Cart");
        }
        if (product.getProductQuantity() == 0) {
            throw new APIException("Product with id "+product.getProductId()+" is not available");
        }

        if (product.getProductQuantity() < quantity) {
            throw new APIException("Please make an order of the "
                                    +product.getProductName()+"less than or  equal to quantity"
                                    +product.getProductQuantity());
        }

        // Create cart item
        CartItems newCartItem = new CartItems();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getProductDiscount());
        newCartItem.setProductPrice(product.getProductPrice());

        // save cart item
        cartItemRepository.save(newCartItem);

        // can reduce the product quantity here, but we are only reducing the quantity till
        // the product is placed.
        // You can minus the product quantity with the quantity u got in the function
        product.setProductQuantity(product.getProductQuantity());
        cart.setTotalPrice(cart.getTotalPrice()+(product.getProductPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItems> itemsList = cart.getItems();
        Stream<ProductDTORequest> productStream = itemsList.stream().map(item->{
                ProductDTORequest map = modelMapper.map(item.getProduct(), ProductDTORequest.class);
                map.setProductQuantity(item.getQuantity());
                return map;
                });
        cartDTO.setProducts(productStream.toList());

        // return the cart
        return cartDTO;

    }

    @Override
    public List<CartDTO> getCartItems() {
        List<Cart>  carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No carts found");
        }
        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
                CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                List<ProductDTORequest> products = cart.getItems().stream()
                        .map(item-> {
                            ProductDTORequest productDTO = modelMapper.map(item.getProduct(), ProductDTORequest.class);
                            productDTO.setProductQuantity(item.getQuantity());
                            return productDTO;
                        }).toList();

                cartDTO.setProducts(products);
                return cartDTO;
        }).toList();
        return cartDTOS;
    }

    @Override
    public CartDTO getUserCart() {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        if (cart == null) {
            throw new ResourceNotFound("Cart", "cartId", cart.getCartId());
        }

        List<CartItems> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getItems().forEach(cartItem -> cartItem.setQuantity(cartItem.getQuantity()));

        List<ProductDTORequest> products = cartItems.stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTORequest.class))
                .toList();
        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        if (cart == null) {
            throw new ResourceNotFound("Cart", "cartId", cart.getCartId());
        }

        Product product = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFound("Product","productId",productId));

        if (product.getProductQuantity() == 0) {
            throw new APIException("Product with id "+product.getProductId()+" is not available");
        }
        if (product.getProductQuantity() < quantity) {
            throw new APIException("Please, make an order of the "
                    +product.getProductName()+"less than or  equal to quantity:"
                    +product.getProductQuantity());
        }

        CartItems cartItem = cartItemRepository
                .findCartItemByProductIdAndCartId(productId, cart.getCartId());
        if (cartItem == null) {
            throw new APIException("Product with id "+product.getProductId()+" is not available");
        }
        cartItem.setProductPrice(product.getProductPrice());
        cartItem.setDiscount(product.getProductDiscount());
        cartItem.setQuantity(product.getProductQuantity()+quantity);
        cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getProductPrice() * quantity));
        cartRepository.save(cart);

        CartItems newCartItem = cartItemRepository.save(cartItem);
        if (newCartItem.getQuantity() == 0) {
            throw new APIException("Product with id "+product.getProductId()+" is not available");
        }

        CartDTO cartDTO = modelMapper.map(newCartItem, CartDTO.class);
        List<CartItems> itemsList = cart.getItems();

        Stream<ProductDTORequest> streamProduct = itemsList.stream().
                map(item->{
                    ProductDTORequest map = modelMapper.map(item.getProduct(), ProductDTORequest.class);
                    map.setProductQuantity(item.getQuantity());
                    return map;
                });
        cartDTO.setProducts(streamProduct.toList());

        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(Long cartId,Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->
                new ResourceNotFound("Cart", "cartId", cartId));

        CartItems cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());
        if (cartItem == null) {
            throw new ResourceNotFound("product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteByCartIdAndProductId(cartId, productId);

        return "Product" + cartItem.getProduct().getProductName()+" removed from cart";
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->
                new ResourceNotFound("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFound("Product","productId",productId));

        CartItems cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if (cartItem == null) {
            throw new ResourceNotFound("product","productId",productId);
        }

        double cartPrice = cart.getTotalPrice() -  (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getProductPrice());

        cart.setTotalPrice(cartPrice +  (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem =  cartItemRepository.save(cartItem);

    }


    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
