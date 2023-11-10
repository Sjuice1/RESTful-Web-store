package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.enums.STATE_OF_ITEM;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @Column(name = "publication_time")
    private LocalDateTime publicationTime;
    @Column(name = "state_of_item")
    @Enumerated(EnumType.STRING)
    private STATE_OF_ITEM stateOfItem;
    @Column(name = "img_source")
    private String img_source;
    @ManyToOne
    @JoinColumn(name = "seller_id",referencedColumnName = "id")
    private Users seller;
    @ManyToMany(mappedBy = "cart")
    private List<Users> usersWithItem;

    @OneToMany(mappedBy = "item")
    List<Review> reviews;
    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(LocalDateTime publicationTime) {
        this.publicationTime = publicationTime;
    }

    public STATE_OF_ITEM getStateOfItem() {
        return stateOfItem;
    }

    public void setStateOfItem(STATE_OF_ITEM stateOfItem) {
        this.stateOfItem = stateOfItem;
    }

    public String getImg_source() {
        return img_source;
    }

    public void setImg_source(String img_source) {
        this.img_source = img_source;
    }

    public Users getSeller() {
        return seller;
    }

    public void setSeller(Users seller) {
        this.seller = seller;
    }


    public List<Users> getCart() {
        return usersWithItem;
    }

    public void setCart(List<Users> cart) {
        this.usersWithItem = cart;
    }


    public List<Users> getUsersWithItem() {
        return usersWithItem;
    }

    public void setUsersWithItem(List<Users> usersWithItem) {
        this.usersWithItem = usersWithItem;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public ItemDTO toDto() {
        ModelMapper modelMapper = new ModelMapper();
        ItemDTO itemDTO = modelMapper.map(this,ItemDTO.class);
        itemDTO.setSeller_id(seller.getId());
        return itemDTO;
    }
}
