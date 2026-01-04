package com.ecommerce.EcomProj.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE,  CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItems> items = new ArrayList<>();

    private Double totalPrice = 0.0;
}
