package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.Exception.APIException;
import com.ecommerce.EcomProj.Exception.ResourceNotFound;
import com.ecommerce.EcomProj.Model.*;
import com.ecommerce.EcomProj.PayLoad.OrderDTO;
import com.ecommerce.EcomProj.PayLoad.OrderItemDTO;
import com.ecommerce.EcomProj.Repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceInt{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    AdressRepository adressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository  orderItemRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CartService cartService;

    @Autowired
    ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgPaymentStatus, String pgResponseMessage) {
        Cart cart =cartRepository.findCartByEmail(email);
        if(cart==null){
            throw new ResourceNotFound("Cart", "email",email);
        }

        Address address = adressRepository.findById(addressId).
                orElseThrow(()-> new ResourceNotFound("Address","id",addressId));

        Order order = new Order();
        order.setEmail(email);
        order.setAddress(address);
        order.setLocalDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setStatus("Order Accepted");

        Payment payment = new Payment(paymentMethod,pgPaymentId, pgPaymentStatus,pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        List<CartItems> cartItems = cart.getItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItems cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getItems().forEach(cartItem -> {
            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();

            product.setProductQuantity(product.getProductQuantity()-quantity);

            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(), cartItem.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(orderItem ->
            orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class))
        );
        orderDTO.setAddressId(addressId);
        return orderDTO;
    }
}
