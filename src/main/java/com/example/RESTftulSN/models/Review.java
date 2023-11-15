package com.example.RESTftulSN.models;

import com.example.RESTftulSN.DTO.ReviewDTO;
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
    private Long mark;
    @Column(name = "description")
    private String description;
    @Column(name = "creation_date")
    private LocalDateTime creation_date;
    @ManyToOne
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ReviewDTO.Request.Create toDto(){
        return new ReviewDTO.Request.Create(this.getMark(),this.getDescription(),this
                .getItem().getId(),this.getUser().getId());
    }
}
