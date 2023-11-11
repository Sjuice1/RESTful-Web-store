package com.example.RESTftulSN.DTO;

import com.example.RESTftulSN.enums.Order.SHIPPING_STATUS;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private SHIPPING_STATUS status;
    private Long user_id;

    public OrderDTO(Long id, LocalDateTime orderDate, SHIPPING_STATUS status, Long user_id) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.user_id = user_id;
    }


    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public SHIPPING_STATUS getStatus() {
        return status;
    }

    public void setStatus(SHIPPING_STATUS status) {
        this.status = status;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
