package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.DTO.UserDTO;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "mark")
    private Integer mark;
    @Column(name = "description")
    private String description;
    @Column(name = "creation_date")
    private LocalDateTime creation_date;
    @ManyToOne
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private Item item;

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public ReviewDTO toDto(){
       ModelMapper modelMapper = new ModelMapper();
        ReviewDTO reviewDTO = modelMapper.map(this,ReviewDTO.class);
        reviewDTO.setItem_id(item.getId());
        return reviewDTO;
    }
}
