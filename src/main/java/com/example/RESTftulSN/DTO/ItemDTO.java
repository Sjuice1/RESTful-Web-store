package com.example.RESTftulSN.DTO;

import com.example.RESTftulSN.enums.STATE_OF_ITEM;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ItemDTO {
    @Size(min = 5,message = "Name must be from 5 letters")
    @Size(max = 30,message = "Name must be less than 30 letters")
    private String name;
    @Size(min = 15,message = "Description must be from 15 letters")
    @Size(max = 1000,message = "Description must be less than 1000 letters")
    private String description;
    @Max(value = 99999999,message = "Invalid price")
    @Min(value = 10,message = "Price is too low")
    private Double price;
    @NotNull(message = "Invalid state of item")
    private STATE_OF_ITEM stateOfItem;
    @Size(max = 200,message = "IMG source too large")
    private String img_source;
    @NotNull(message = "Seller id must be written")
    private Long seller_id;

    public ItemDTO(String name, String description, Double price, STATE_OF_ITEM stateOfItem, String img_source,Long seller_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stateOfItem = stateOfItem;
        this.seller_id = seller_id;
        this.img_source = img_source;
    }

    public ItemDTO() {
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

    public Long getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Long seller_id) {
        this.seller_id = seller_id;
    }

}
