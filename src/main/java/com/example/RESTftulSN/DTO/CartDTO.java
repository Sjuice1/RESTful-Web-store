package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.NotNull;

public class CartDTO {
    @NotNull
    private Long userId;
    @NotNull
    private Long itemId;

    public CartDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
