package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewDTO {

    @Min(value = 1,message = "Indalid mark")
    @Max(value = 10,message = "Invalid mark")
    private Integer mark;

    @Size(max = 250,message = "Description too big")
    private String description;
    @NotNull(message = "Item id must be written")
    private Long item_id;

    public ReviewDTO(Integer mark, String description, Long item_id) {
        this.mark = mark;
        this.description = description;
        this.item_id = item_id;
    }

    public ReviewDTO() {
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }
}
