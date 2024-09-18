package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService{

    private ShoppingCartRepository sCRepository;
    private ProductService productService;
    private CartItemService cartItemService;

    @Override
    @Transactional
    public ShoppingCart addProductToCart(long id, long productId,int quantity) {
        ShoppingCart cart=findById(id);
        Product product=productService.findById(productId);
        cartItemService.addProductToCart(cart,product,quantity);
        return cart;
    }

    @Override
    @Transactional
    public ShoppingCart removeProductFromCart(long id, long productId,int quantity) {
        ShoppingCart cart=findById(id);
        Product product=productService.findById(productId);
        cartItemService.removeProductFromCart(cart,product,quantity);
        return cart;
    }

    @Override
    public ShoppingCart findById(long id) {
        return sCRepository.findById(id).orElseThrow(()-> new ApiException("Cart not found",HttpStatus.NOT_FOUND));
    }

//    @Override
//    public ShoppingCart findByUser(User user) {
//        return sCRepository.findByUser(user);
//    }

    @Override
    public List<ShoppingCart> findAll() {
        return sCRepository.findAll();
    }

    @Override
    public ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart cart=new ShoppingCart();
        cart.setUser(user);
        return sCRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCart delete(long id) {
        ShoppingCart cart=findById(id);
        sCRepository.delete(cart);
        return cart;
    }

    @Override
    @Transactional
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return sCRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart update(long id, ShoppingCart shoppingCart) {
        ShoppingCart cart=findById(id);
        cart.setUser(shoppingCart.getUser());
        cart.setOrder(shoppingCart.getOrder());
        cart.setItems(shoppingCart.getItems());
        return  sCRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCart clearCart(long id) {
       ShoppingCart cart=findById(id);
      cartItemService.clearCart(cart);
        return cart;
    }

    @Override
    public double calculateTotalPrice(long id) {
        double total=0;
        ShoppingCart cart=findById(id);

        List<CartItem> items=cart.getItems();

        for(CartItem i:items){
            total+=i.getPrice()*i.getQuantity();
        }
        return total;
    }
}
