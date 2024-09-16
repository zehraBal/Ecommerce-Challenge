package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService{

    private ShoppingCartRepository sCRepository;

    @Override
    public ShoppingCart addProductToCart(long id, Product product) {
       ShoppingCart cart= findById(id);
       List<Product> cartItems=cart.getProducts();
       cartItems.add(product);
       cart.setProducts(cartItems);
       return sCRepository.save(cart);
    }

    @Override
    public ShoppingCart findById(long id) {
     return sCRepository.findById(id).orElseThrow(()->new ApiException("Shopping cart not found", HttpStatus.NOT_FOUND));    }

    @Override
    public ShoppingCart removeProductFromCart(long id, Product product) {
        ShoppingCart cart= findById(id);
        List<Product> cartItems=cart.getProducts();
        cartItems.removeIf(product1 -> product1.getId().equals(product.getId()));
        cart.setProducts(cartItems);
        return sCRepository.save(cart);
    }

    @Override
    public ShoppingCart findByUser(User user) {
    return sCRepository.findByUser(user);
    }

    @Override
    public List<ShoppingCart> findAll() {
        return sCRepository.findAll();
    }

    @Override
    public ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart cart =new ShoppingCart();
        cart.setUser(user);
        return sCRepository.save(cart);
    }

    @Override
    public ShoppingCart delete(long id) {
        ShoppingCart cart= findById(id);
        sCRepository.delete(cart);
        return cart;
    }

    @Override
    public ShoppingCart update(long id, ShoppingCart shoppingCart) {
        return null;
    }

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return sCRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart clearCart(long id) {
        ShoppingCart cart= findById(id);
        List<Product> cartItems=cart.getProducts();
        cartItems.clear();
        cart.setProducts(cartItems);
        return sCRepository.save(cart);
    }

    @Override
    public double calculateTotalPrice(long id) {
        ShoppingCart shoppingCart = findById(id);
        return shoppingCart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
