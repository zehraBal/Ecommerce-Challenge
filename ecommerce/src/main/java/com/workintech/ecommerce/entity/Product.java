package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product",schema = "fsweb")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    @NotNull
    @Size(min = 3,max = 100)
    private String name;

    @Column(name = "stock_quantity")
    @Min(1)
    private int stockQuantity;

    @Column(name = "price")
    @NotNull
    @Min(0)
    private double price;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "user_shopping_cart",schema = "fsweb",joinColumns = @JoinColumn(name = "product_id"),inverseJoinColumns = @JoinColumn(name = "shopping_cart_id"))
    private List<ShoppingCart> shoppingCarts=new ArrayList<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}
