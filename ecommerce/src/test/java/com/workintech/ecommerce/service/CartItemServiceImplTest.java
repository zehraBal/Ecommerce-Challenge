package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemService = new CartItemServiceImpl(cartItemRepository);
    }

    @Test
    void findById_WhenItemExists_ShouldReturnItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.findById(1L);

        assertEquals(cartItem, result);
        verify(cartItemRepository).findById(1L);
    }

    @Test
    void findById_WhenItemNotFound_ShouldThrowException() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.findById(1L));
        assertEquals("Item not found", exception.getMessage());
        verify(cartItemRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfItems() {
        CartItem cartItem = new CartItem();
        when(cartItemRepository.findAll()).thenReturn(List.of(cartItem));

        List<CartItem> result = cartItemService.findAll();

        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartItemRepository).findAll();
    }

    @Test
    void save_ShouldSaveCartItem() {
        CartItem cartItem = new CartItem();
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        CartItem result = cartItemService.save(cartItem);

        assertEquals(cartItem, result);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void delete_WhenItemExists_ShouldDeleteItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.delete(1L);

        assertEquals(cartItem, result);
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void delete_WhenItemNotFound_ShouldThrowException() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.delete(1L));
        assertEquals("Item not found", exception.getMessage());
        verify(cartItemRepository).findById(1L);
    }

    @Test
    void update_ShouldUpdateCartItem() {
        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(1L);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setQuantity(3);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenReturn(existingCartItem);

        CartItem result = cartItemService.update(1L, updatedCartItem);

        assertEquals(3, existingCartItem.getQuantity());
        verify(cartItemRepository).findById(1L);
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void findByProductId_WhenItemExists_ShouldReturnCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        when(cartItemRepository.findByProductId(1L)).thenReturn(cartItem);

        CartItem result = cartItemService.findByProductId(1L);

        assertEquals(cartItem, result);
        verify(cartItemRepository).findByProductId(1L);
    }

    @Test
    void findByProductId_WhenItemNotFound_ShouldThrowException() {
        when(cartItemRepository.findByProductId(1L)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.findByProductId(1L));
        assertEquals("Cart item with product ID not found", exception.getMessage());
        verify(cartItemRepository).findByProductId(1L);
    }

    @Test
    void findByShoppingCart_WhenItemsExist_ShouldReturnList() {
        CartItem cartItem = new CartItem();
        when(cartItemRepository.findByShoppingCart(1L)).thenReturn(List.of(cartItem));

        List<CartItem> result = cartItemService.findByShoppingCart(1L);

        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartItemRepository).findByShoppingCart(1L);
    }

    @Test
    void findByShoppingCart_WhenNoItemsFound_ShouldThrowException() {
        when(cartItemRepository.findByShoppingCart(1L)).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.findByShoppingCart(1L));
        assertEquals("No cart items found for the given shopping cart ID", exception.getMessage());
        verify(cartItemRepository).findByShoppingCart(1L);
    }

    @Test
    void clearCart_WhenItemsExist_ShouldClearCart() {
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        when(cartItemRepository.findByShoppingCart(1L)).thenReturn(List.of(cartItem));

        cartItemService.clearCart(shoppingCart);

        verify(cartItemRepository).findByShoppingCart(1L);
        verify(cartItemRepository).deleteAll(anyList());
    }

    @Test
    void clearCart_WhenNoItemsExist_ShouldThrowException() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        when(cartItemRepository.findByShoppingCart(1L)).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.clearCart(shoppingCart));
        assertEquals("No items found in the cart to clear", exception.getMessage());
        verify(cartItemRepository).findByShoppingCart(1L);
    }

    @Test
    void addProductToCart_WhenProductExistsInCart_ShouldIncreaseQuantity() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setId(1L);

        CartItem existingCartItem = new CartItem();
        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(1);

        when(cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)).thenReturn(existingCartItem);
        when(cartItemRepository.save(existingCartItem)).thenReturn(existingCartItem);

        CartItem result = cartItemService.addProductToCart(shoppingCart, product, 2);

        assertEquals(3, result.getQuantity());
        verify(cartItemRepository).findByShoppingCartAndProduct(shoppingCart, product);
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void addProductToCart_WhenProductNotInCart_ShouldAddProduct() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        when(cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)).thenReturn(null);

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(2);
        newCartItem.setPrice(100.0);

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(newCartItem);

        CartItem result = cartItemService.addProductToCart(shoppingCart, product, 2);

        assertEquals(2, result.getQuantity());
        assertEquals(100.0, result.getPrice());
        verify(cartItemRepository).findByShoppingCartAndProduct(shoppingCart, product);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void removeProductFromCart_WhenQuantityGreaterThanZero_ShouldDecreaseQuantity() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        cartItem.setProduct(product);

        when(cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)).thenReturn(cartItem);

        cartItemService.removeProductFromCart(shoppingCart, product, 3);

        assertEquals(2, cartItem.getQuantity());
        verify(cartItemRepository).findByShoppingCartAndProduct(shoppingCart, product);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void removeProductFromCart_WhenQuantityBecomesZero_ShouldRemoveCartItem() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(3);
        cartItem.setProduct(product);

        when(cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)).thenReturn(cartItem);

        cartItemService.removeProductFromCart(shoppingCart, product, 3);

        verify(cartItemRepository).findByShoppingCartAndProduct(shoppingCart, product);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void removeProductFromCart_WhenProductNotInCart_ShouldThrowException() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setId(1L);

        when(cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> cartItemService.removeProductFromCart(shoppingCart, product, 3));
        assertEquals("Cart item with the given product not found", exception.getMessage());
        verify(cartItemRepository).findByShoppingCartAndProduct(shoppingCart, product);
    }
}
