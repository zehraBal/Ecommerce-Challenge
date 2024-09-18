package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.OrderConverter;
import com.workintech.ecommerce.dto.OrderRequest;
import com.workintech.ecommerce.dto.OrderResponse;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.OrderService;
import com.workintech.ecommerce.service.ShoppingCartService;
import com.workintech.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {


    private OrderService orderService;
    private ShoppingCartService scService;
    private UserService userService;

    @GetMapping
    public List<OrderResponse>  getAll(){
        return OrderConverter.convertToOrderResponseList(orderService.findAll());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable long id){
        return OrderConverter.convertToOrderResponse(orderService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public OrderResponse getByUser(@PathVariable long userId){
        User user= userService.findById(userId);
        return OrderConverter.convertToOrderResponse(orderService.findByUser(user));
    }

    @PostMapping("/create")
    public OrderResponse createOrderFromCart(@RequestBody OrderRequest orderRequest){
        return OrderConverter.convertToOrderResponse(orderService.createOrderFromCart(orderRequest.getShoppingCartId(),orderRequest.getPaymentDetails()));
    }

    @PostMapping
    public OrderResponse save(@RequestBody Order order){
        return OrderConverter.convertToOrderResponse(orderService.save(order));
    }

    @PutMapping("/{id}")
    public OrderResponse update(@PathVariable long id,@RequestBody Order order){
        return OrderConverter.convertToOrderResponse(orderService.update(id,order));
    }

    @DeleteMapping("/{id}")
    public OrderResponse delete(@PathVariable long id){
        return OrderConverter.convertToOrderResponse(orderService.delete(id));
    }
}
