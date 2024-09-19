package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService{

    private CartItemRepository cartItemRepository;
    @Override
    public CartItem findById(long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ApiException("Item not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    @Transactional
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem delete(long id) {
        CartItem item = findById(id);
        cartItemRepository.delete(item);
        return item;
    }

    @Override
    @Transactional
    public CartItem update(long id, CartItem cartItem) {
        CartItem item = findById(id);
        item.setShoppingCart(cartItem.getShoppingCart());
        item.setProduct(cartItem.getProduct());
        item.setQuantity(cartItem.getQuantity());
        item.setPrice(cartItem.getPrice());
        return save(item);
    }

    @Override
    public CartItem findByProductId(long productId) {
        CartItem item=cartItemRepository.findByProductId(productId);
        if(item==null){
            throw new ApiException("Cart item with product ID not found", HttpStatus.NOT_FOUND);
        }
        return item;
    }

    @Override
    public List<CartItem> findByShoppingCart(long cartId) {
        List<CartItem> items = cartItemRepository.findByShoppingCart(cartId);
        if (items.isEmpty()) {
            throw new ApiException("No cart items found for the given shopping cart ID", HttpStatus.NOT_FOUND);
        }
        return items;
    }

    @Override
    @Transactional
    public void clearCart(ShoppingCart shoppingCart) {
        List<CartItem> items = cartItemRepository.findByShoppingCart(shoppingCart.getId());
        if (items.isEmpty()) {
            throw new ApiException("No items found in the cart to clear", HttpStatus.NOT_FOUND);
        }
        cartItemRepository.deleteAll(items);
    }

    @Override
    public List<CartItem> getItemsInCart(ShoppingCart shoppingCart) {
        List<CartItem> items = cartItemRepository.findByShoppingCart(shoppingCart.getId());
        if (items.isEmpty()) {
            throw new ApiException("No items found in the cart", HttpStatus.NOT_FOUND);
        }
        return items;
    }

    @Transactional
    public CartItem addProductToCart(ShoppingCart shoppingCart, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
        }

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeProductFromCart(ShoppingCart shoppingCart, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);
        if (cartItem != null) {
            if (cartItem.getQuantity() - quantity > 0) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            } else {
                cartItemRepository.delete(cartItem);
            }
        } else {
            throw new ApiException("Cart item with the given product not found", HttpStatus.NOT_FOUND);
        }
    }
}
