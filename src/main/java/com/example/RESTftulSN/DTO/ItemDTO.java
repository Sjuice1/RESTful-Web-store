package com.example.RESTftulSN.DTO;

import com.example.RESTftulSN.enums.Item.STATE_OF_ITEM;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

public class ItemDTO {
    private interface id {
        @NotNull
        Long getId();
    }
    private interface name {
        @Size(min = 5, message = "Name must be from 5 letters")
        @Size(max = 30, message = "Name must be less than 30 letters")
        String getName();
    }
    private interface description {
        @Size(min = 15, message = "Description must be from 15 letters")
        @Size(max = 1000, message = "Description must be less than 1000 letters")
        String getDescription();
    }
    private interface price {
        @Max(value = 99999999, message = "Invalid price")
        @Min(value = 10, message = "Price is too low")
        Double getPrice();
    }
    private interface stateOfItem {
        @NotNull(message = "Invalid state of item")
        STATE_OF_ITEM getStateOfItem();
    }
    private interface itemCount {
        @Min(value = 0, message = "Item count can't be below 0")
        @Max(value = 1000000, message = "Items count must be less than 1000000")
        Long getItemCount();
    }
    private interface imgSource {
        @Size(max = 200, message = "IMG source too large")
        String getImgSource();
    }
    private interface sellerId {
        @NotNull(message = "Seller id must be written")
        Long getSellerId();
    }

    public enum Request{;
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Id implements id{
            Long id;
        }
        @Value
        public static class Create implements id,name,description,price,stateOfItem,itemCount,imgSource,sellerId{
            Long id;
            String name;
            String description;
            Double price;
            STATE_OF_ITEM stateOfItem;
            Long itemCount;
            String imgSource;
            Long sellerId;
        }
    }
}
