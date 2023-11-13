package com.example.RESTftulSN.controllers;


import com.example.RESTftulSN.enums.Order.SHIPPING_STATUS;
import com.example.RESTftulSN.services.OrderService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/order")
public class orderAPI{
    private final OrderService orderService;
    @Autowired
    public orderAPI(OrderService orderService) {
        this.orderService = orderService;
    }
    ///////////////////////
    ////Get order by id
    ///////////////////////
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id){
        return orderService.getOrderById(id);
    }
    ///////////////////////
    ////Get order items by order id
    ///////////////////////
    @GetMapping("/{id}/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> getOrderItems(@PathVariable("id") Long id){
        return orderService.getOrderItems(id);
    }
    ///////////////////////
    ////Make an order
    ////Vanish user cart and make order using all cart items
    ///////////////////////
    @PostMapping("/make/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> makeAnOrder(@PathVariable("user_id") Long user_id){
        return  orderService.orderACart(user_id);
    }
    ///////////////////////
    ////Update order status
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PatchMapping("{id}/status/{new_status}")
    public ResponseEntity<?> changeStatus(@PathVariable("id")Long id
            ,@PathVariable("new_status") String shippingStatus){
        return orderService.changeStatus(id,SHIPPING_STATUS.findByName(shippingStatus));
    }
    ///////////////////////
    ////Delete order by id(Not recommended to use)
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id){
        return orderService.deleteById(id);
    }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    /////// YOU CAN'T UPDATE ORDER
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    @ExceptionHandler
    public ResponseEntity<?> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> forbiddenAccessException(ForbiddenAccessException forbiddenAccessException){
        return new ResponseEntity<>(new ErrorResponseEntity(forbiddenAccessException.getMessage(), LocalDateTime.now()),HttpStatus.FORBIDDEN);
    }

}
