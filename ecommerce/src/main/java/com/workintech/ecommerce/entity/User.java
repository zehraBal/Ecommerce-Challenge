package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user",schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotNull
    @NotBlank
    @Max(50)
    private String username;

    @Column(name = "password")
    @NotNull
    @NotBlank
    @Max(150)
    private String password;

    @Column(name = "first_name")
    @Max(50)
    private String firstName;

    @Column(name = "last_name")
    @Max(50)
    private String lastName;

    @Column(name = "email")
    @Max(150)
    private String email;

    @Column(name = "phone_number")
    @Max(12)
    private String phoneNumber;

    @Column(name = "created_at")
    @NotNull
    private Timestamp createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user")
    private List<OrderDetails> orderDetails;


}
