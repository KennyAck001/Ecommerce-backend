package com.ecommerce.EcomProj.Service;

import com.ecommerce.EcomProj.PayLoad.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderServiceInt {
    @Transactional
    OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgPaymentStatus, String pgResponseMessage);
}
