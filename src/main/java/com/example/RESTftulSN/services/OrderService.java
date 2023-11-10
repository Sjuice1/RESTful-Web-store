package com.example.RESTftulSN.services;

import com.example.RESTftulSN.enums.SHIPPING_STATUS;
import com.example.RESTftulSN.models.Order;
import com.example.RESTftulSN.repositories.OrderRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// TODO transaction
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order getOrderById(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new InvalidDataException("There's no order with that id");
        }
        return order.get();
    }


    public void changeStatus(Long id, SHIPPING_STATUS shippingStatus) {
        Order order = getOrderById(id);
        order.setStatus(shippingStatus);
        orderRepository.save(order);
    }

    public void deleteById(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}
