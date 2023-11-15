package com.example.RESTftulSN.controllers;


import com.example.RESTftulSN.DTO.OrderDTO;
import com.example.RESTftulSN.enums.Order.SHIPPING_STATUS;
import com.example.RESTftulSN.services.OrderService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.exceptions.ForbiddenAccessException;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> getOrder(@RequestBody @Validated OrderDTO.Request.Id orderDTO){
        return orderService.getOrderById(orderDTO.getId());
    }
    ///////////////////////
    ////Get order items by order id
    ///////////////////////
    @GetMapping("items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> getOrderItems(@RequestBody @Validated OrderDTO.Request.Id orderDTO){
        return orderService.getOrderItems(orderDTO.getId());
    }
    ///////////////////////
    ////Make an order
    ////Vanish user cart and make order using all cart items
    ///////////////////////
    @PostMapping("/make")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR','ROLE_VERIFIED')")
    public ResponseEntity<?> makeAnOrder(@RequestBody @Validated OrderDTO.Request.UserId orderDTO){
        return  orderService.orderACart(orderDTO.getUserId());
    }
    ///////////////////////
    ////Update order status
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PatchMapping("/status")
    public ResponseEntity<?> changeStatus(@RequestBody @Validated OrderDTO.Request.Status orderDTO){
        return orderService.changeStatus(orderDTO.getId(),SHIPPING_STATUS.findByName(orderDTO.getStatus().toString()));
    }
    ///////////////////////
    ////Delete order by id(Not recommended to use)
    ///////////////////////
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@RequestBody @Validated OrderDTO.Request.Id orderDTO){
        return orderService.deleteById(orderDTO.getId());
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
