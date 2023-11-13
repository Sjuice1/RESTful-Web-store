package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.enums.Order.SHIPPING_STATUS;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Order;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.OrderRepository;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderService(UserService userService, OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    public Order getById(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new InvalidDataException("There's no order with that id");
        }
        return order.get();
    }
    @Transactional
    public ResponseEntity<?> changeStatus(Long id, SHIPPING_STATUS shippingStatus) {
        Order order = getById(id);
        order.setStatus(shippingStatus);
        orderRepository.save(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> deleteById(Long id) {
        Order order = getById(id);
        orderRepository.delete(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> orderACart(Long user_id) {
        Users user = userService.getById(user_id);
        if(user.getCart().isEmpty()){
            throw new InvalidDataException("Cart is empty");
        }
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(SHIPPING_STATUS.PROCESSED);
        order.setUser(user);
        user.getCart().stream().forEach(order::setItems);
        user.getCart().clear();
        orderRepository.save(order);
        userService.saveUser(user);
        rabbitTemplate.convertAndSend("Fanout-Exchange","",order);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> getOrderById(Long id){
        return new ResponseEntity<>(getById(id).toDTO(), HttpStatus.OK);
    }

    public ResponseEntity<?> getOrderItems(Long id) {
        Order order = getById(id);
        List<ItemDTO> items= order.getItems().stream().map(Item::toDto).toList();
        return new ResponseEntity<>(items,HttpStatus.OK);
    }
}
