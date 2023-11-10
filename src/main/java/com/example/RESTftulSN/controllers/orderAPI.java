package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.OrderDTO;
import com.example.RESTftulSN.enums.SHIPPING_STATUS;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Order;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.services.OrderService;
import com.example.RESTftulSN.services.UserService;
import com.example.RESTftulSN.util.ErrorResponseEntity;
import com.example.RESTftulSN.util.InvalidDataException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class orderAPI{
    private final UserService userService;
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public orderAPI(UserService userService, OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id){
        OrderDTO orderDTO = orderService.getOrderById(id).toDTO();
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemDTO>> getOrderItems(@PathVariable("id") Long id){
        Order order = orderService.getOrderById(id);
        List<ItemDTO> items= order.getItems().stream().map(Item::toDto).toList();
        return new ResponseEntity<>(items,HttpStatus.OK);
    }
    @PostMapping("/make/{user_id}")
    public HttpEntity<HttpStatus> makeAnOrder(@PathVariable("user_id") Long user_id){
        Users user = userService.getById(user_id);
        Order order = orderService.orderACart(user);
        rabbitTemplate.convertAndSend("Fanout-Exchange","",order);
        return new HttpEntity<>(HttpStatus.OK);
    }

    @PatchMapping("{id}/status/{new_status}")
    public HttpEntity<HttpStatus> changeStatus(@PathVariable("id")Long id
            ,@PathVariable("new_status") String shippingStatus){

        orderService.changeStatus(id,SHIPPING_STATUS.findByName(shippingStatus));
        return new HttpEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public HttpEntity<HttpStatus> deleteOrder(@PathVariable("id") Long id){
        orderService.deleteById(id);
        return new HttpEntity<>(HttpStatus.OK);
    }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    /////// NO UPDATE FOR ORDER
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    @ExceptionHandler
    public ResponseEntity<ErrorResponseEntity> invalidDataHandler(InvalidDataException invalidDataException){
        return new ResponseEntity<>(new ErrorResponseEntity(invalidDataException.getMessage(), LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }
}
