package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.enums.USER_ROLE;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "creation_date")
    private LocalDateTime creation_date;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private USER_ROLE userRole;
    @OneToMany(mappedBy = "seller")
    private List<Item> items;
    @ManyToMany
    @JoinTable(
           name = "cart",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Item> cart;
    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public USER_ROLE getUserRole() {
        return userRole;
    }

    public void setUserRole(USER_ROLE userRole) {
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getCart() {
        return cart;
    }

    public void setCart(Item item) {
        if(this.cart==null){
            cart=new ArrayList<>(List.of(item));
            return;
        }
        this.cart.add(item);
    }

    public UserDTO toDto(){
        return new ModelMapper().map(this,UserDTO.class);
    }
}