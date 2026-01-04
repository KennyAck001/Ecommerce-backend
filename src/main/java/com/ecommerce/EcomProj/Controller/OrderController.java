package com.ecommerce.EcomProj.Controller;

import com.ecommerce.EcomProj.PayLoad.OrderDTO;
import com.ecommerce.EcomProj.PayLoad.OrderRequestDTO;
import com.ecommerce.EcomProj.Service.OrderServiceInt;
import com.ecommerce.EcomProj.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderServiceInt orderService;

    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/order/user/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String email = authUtils.loggedInEmail();

        OrderDTO order = orderService.placeOrder(
                email,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgPaymentStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
