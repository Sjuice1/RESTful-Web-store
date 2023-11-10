package com.example.RESTftulSN.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;

    public VerificationToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
