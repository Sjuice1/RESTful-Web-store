package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.NotNull;

public class CartDTO {
    @NotNull
    private Long user_id;
    @NotNull
    private Long item_id;

    public CartDTO() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }
}
