package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.enums.STATE_OF_ITEM;
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
    private String imgSource;
    @Column(name = "item_count")
    private Long itemCount;
    @ManyToOne
    @JoinColumn(name = "seller_id",referencedColumnName = "id")
    private Users seller;
    @ManyToMany(mappedBy = "cart")
    private List<Users> usersWithItemInCart;

    @OneToMany(mappedBy = "item")
    List<Review> reviews;
    @ManyToMany(mappedBy = "items")
    List<Order> orderList;
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

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String img_source) {
        this.imgSource = img_source;
    }

    public Users getSeller() {
        return seller;
    }

    public void setSeller(Users seller) {
        this.seller = seller;
    }


    public List<Users> getCart() {
        return usersWithItemInCart;
    }

    public void setCart(List<Users> cart) {
        this.usersWithItemInCart = cart;
    }


    public List<Users> getUsersWithItemInCart() {
        return usersWithItemInCart;
    }

    public void setUsersWithItemInCart(List<Users> usersWithItem) {
        this.usersWithItemInCart = usersWithItem;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Long getItemCount() {
        return itemCount;
    }

    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    public ItemDTO toDto() {
        ModelMapper modelMapper = new ModelMapper();
        ItemDTO itemDTO = modelMapper.map(this,ItemDTO.class);
        itemDTO.setSellerId(seller.getId());
        return itemDTO;
    }
}
