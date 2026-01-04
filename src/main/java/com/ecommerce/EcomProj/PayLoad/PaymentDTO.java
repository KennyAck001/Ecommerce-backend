package com.ecommerce.EcomProj.PayLoad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private String paymentMethod;
    private String pgPaymentId;
    private String pgPaymentStatus;
    private String pgResponseMessage;
    private String pgName;

}
