package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.CartItemConverter;
import com.workintech.ecommerce.converter.ShoppingCartConverter;
import com.workintech.ecommerce.dto.CartItemResponse;
import com.workintech.ecommerce.dto.ShoppingCartResponse;
import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.CartItemService;
import com.workintech.ecommerce.service.ProductService;
import com.workintech.ecommerce.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/cart")
public class CartController {

    private ShoppingCartService scService;
    private CartItemService cartItemService;
    private ProductService productService;

    @GetMapping
    public List<ShoppingCartResponse> getAll(){
        return ShoppingCartConverter.convertToCartResponseList(scService.findAll());
    }

    @GetMapping("/{id}")
    public ShoppingCartResponse getById(@PathVariable long id){
        return ShoppingCartConverter.convertToCartResponse(scService.findById(id));
    }

    @GetMapping("/{id}/items")
    public List<CartItemResponse> getItemsInCart(@PathVariable long id){
        ShoppingCart cart =scService.findById(id);
        List<CartItem> items=cartItemService.getItemsInCart(cart);
        return CartItemConverter.convertToCartItemResponseList(items);
    }

//    @GetMapping("/user")
//    public ShoppingCartResponse getByUser(@RequestBody User user){
//        return ShoppingCartConverter.convertToCartResponse(scService.findByUser(user));
//    }

    @PostMapping
    public ShoppingCartResponse save(@RequestBody ShoppingCart cart){
        return ShoppingCartConverter.convertToCartResponse(scService.save(cart));
    }

    @PostMapping("/{cartId}/add/{productId}")
    public CartItemResponse addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity){
        ShoppingCart cart=scService.findById(cartId);
        Product product=productService.findById(productId);
        CartItem item=cartItemService.addProductToCart(cart,product,quantity);
        return CartItemConverter.convertToCartItemResponse(item);
    }

    @PutMapping("/{id}")
    public ShoppingCartResponse update(@PathVariable long id,@RequestBody ShoppingCart cart){
        return ShoppingCartConverter.convertToCartResponse(scService.update(id,cart));
    }

    @PutMapping("/{cartId}/remove/{productId}")
    public CartItemResponse removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
         ShoppingCart cart= scService.findById(cartId);
         Product product=productService.findById(productId);
         CartItem item=cartItemService.findByProductId(productId);
         cartItemService.removeProductFromCart(cart,product,quantity);
         return CartItemConverter.convertToCartItemResponse(item);
    }

    @DeleteMapping("/{id}")
    public ShoppingCartResponse delete(@PathVariable long id){
        return ShoppingCartConverter.convertToCartResponse(scService.delete(id));
    }

    @DeleteMapping("/{cartId}/clear")
    public ShoppingCartResponse clearCart(@PathVariable Long cartId){
        ShoppingCart shoppingCart = scService.findById(cartId);
        cartItemService.clearCart(shoppingCart);
        return ShoppingCartConverter.convertToCartResponse(shoppingCart);
    }


}