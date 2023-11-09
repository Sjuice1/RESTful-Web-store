package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.enums.STATE_OF_ITEM;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

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
    @ManyToOne
    @JoinColumn(name = "seller_id",referencedColumnName = "id")
    private Users seller;

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

    public Users getSeller() {
        return seller;
    }

    public void setSeller(Users seller) {
        this.seller = seller;
    }

    public ItemDTO toDto() {
        ModelMapper modelMapper = new ModelMapper();
        ItemDTO itemDTO = modelMapper.map(this,ItemDTO.class);
        itemDTO.setSeller_id(seller.getId());
        return itemDTO;
    }
}
