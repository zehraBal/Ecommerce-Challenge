package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT c FROM CartItem c WHERE c.product.id= :productId")
    CartItem findByProductId(long productId);

    @Query("SELECT c FROM CartItem c WHERE c.shoppingCart.id= :cartId")
    List<CartItem> findByShoppingCart(long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.shoppingCart = :shoppingCart AND ci.product = :product")
    CartItem findByShoppingCartAndProduct( ShoppingCart shoppingCart,  Product product);
}
