package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.*;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    private ShoppingCartService shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemService cartItemService;
    @BeforeEach
    void setUp(){
        shoppingCartService=new ShoppingCartServiceImpl(shoppingCartRepository,productService,cartItemService);
    }

    @Test
    void addProductToCart() {
        long cartId = 1L;
        long productId = 1L;
        int quantity = 1;

        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setItems(new ArrayList<>());  // Boş liste ile items alanını başlatıyoruz.
        Product mockProduct = new Product();

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        when(productService.findById(productId)).thenReturn(mockProduct);

        shoppingCartService.addProductToCart(cartId, productId, quantity);

        verify(shoppingCartRepository).findById(cartId);
        verify(productService).findById(productId);
        verify(cartItemService).addProductToCart(mockCart, mockProduct, quantity);
        verify(productService).decreaseQuantity(productId, quantity);
    }

    @Test
    void removeProductFromCart() {
        long cartId = 1L;
        long productId = 1L;
        int quantity = 1;

        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setItems(new ArrayList<>());  // Boş liste ile items alanını başlatıyoruz.
        Product mockProduct = new Product();

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        when(productService.findById(productId)).thenReturn(mockProduct);

        shoppingCartService.removeProductFromCart(cartId, productId, quantity);

        verify(shoppingCartRepository).findById(cartId);
        verify(productService).findById(productId);
        verify(cartItemService).removeProductFromCart(mockCart, mockProduct, quantity);
        verify(productService).increaseQuantity(productId, quantity);
    }

    @Test
    void findById() {
        long cartId = 1L;
        ShoppingCart mockCart = new ShoppingCart();

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        ShoppingCart result = shoppingCartService.findById(cartId);

        assertEquals(mockCart, result);
        verify(shoppingCartRepository).findById(cartId);
    }

    @Test
    void findAll_WhenNoCartsFound_ShouldThrowException() {
        when(shoppingCartRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ApiException.class, () -> shoppingCartService.findAll());
        verify(shoppingCartRepository).findAll();
    }
    @Test
    void findAll_WhenCartsFound_ShouldReturnList() {
        ShoppingCart mockCart=new ShoppingCart();
        mockCart.setId(1L);
        when(shoppingCartRepository.findAll()).thenReturn(List.of(mockCart));
        List<ShoppingCart> carts=shoppingCartService.findAll();
        assertEquals(1,carts.size());
        assertEquals(mockCart,carts.get(0));
        verify(shoppingCartRepository).findAll();
        }

    @Test
    void createNewShoppingCart() {
        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setId(1L);
        User mockUser = new User();

        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(mockCart);

        ShoppingCart result = shoppingCartService.createNewShoppingCart(mockUser);

        assertEquals(mockCart, result);
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    void delete() {
        long cartId = 1L;
        ShoppingCart mockCart = new ShoppingCart();
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        shoppingCartService.delete(cartId);
        verify(shoppingCartRepository).findById(cartId);
        verify(shoppingCartRepository).delete(mockCart);
    }

    @Test
    void save() {
        ShoppingCart shoppingCart=new ShoppingCart();
        List<CartItem> items=new ArrayList<>();
        Order order=new Order();
        User user = new User();
        user.setUsername("janeD");
        user.setShoppingCart(shoppingCart);
        user.setFirstName("Jane");
        user.setLastName("Doe");
        shoppingCart.setItems(items);
        shoppingCart.setOrder(order);
        shoppingCart.setUser(user);
        shoppingCartService.save(shoppingCart);
        verify(shoppingCartRepository.save(shoppingCart));

    }

    @Test
    void update() {
        long cartId = 1L;
        ShoppingCart mockCart = new ShoppingCart();
        ShoppingCart updatedCart = new ShoppingCart();

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        when(shoppingCartRepository.save(mockCart)).thenReturn(updatedCart);

        ShoppingCart result = shoppingCartService.update(cartId, updatedCart);

        assertEquals(updatedCart, result);
        verify(shoppingCartRepository).findById(cartId);
        verify(shoppingCartRepository).save(mockCart);
    }

    @Test
    void clearCart() {
        long cartId = 1L;

        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setItems(new ArrayList<>());  // Boş liste ile items alanını başlatıyoruz.

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        shoppingCartService.clearCart(cartId);

        verify(shoppingCartRepository).findById(cartId);
        verify(cartItemService).clearCart(mockCart);
    }

    @Test
    void calculateTotalPrice() {
        long cartId = 1L;
        ShoppingCart mockCart = new ShoppingCart();

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        shoppingCartService.calculateTotalPrice(cartId);

        verify(shoppingCartRepository).findById(cartId);
    }
}