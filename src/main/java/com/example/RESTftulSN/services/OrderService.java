package com.example.RESTftulSN.services;

import com.example.RESTftulSN.enums.SHIPPING_STATUS;
import com.example.RESTftulSN.models.Order;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.OrderRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(UserService userService, OrderRepository orderRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
    }
    @Transactional
    public void changeStatus(Long id, SHIPPING_STATUS shippingStatus) {
        Order order = getOrderById(id);
        order.setStatus(shippingStatus);
        orderRepository.save(order);
    }
    @Transactional
    public void deleteById(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
    @Transactional
    public Order orderACart(Users user) {
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
        return order;
    }
    public Order getOrderById(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new InvalidDataException("There's no order with that id");
        }
        return order.get();
    }
}
