package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.OrderDTO;
import com.example.RESTftulSN.enums.SHIPPING_STATUS;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SHIPPING_STATUS status;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;
    @ManyToMany
    @JoinTable(name = "order_items"
    ,joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
    List<Item> items;

    public Order() {
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(Item item) {
        if(items== null){
            items = new ArrayList<>(List.of(item));
            return;
        }
        items.add(item);
    }
    public OrderDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        OrderDTO orderDTO = modelMapper.map(this,OrderDTO.class);
        orderDTO.setUser_id(this.getUser().getId());
        return orderDTO;
    }
}
