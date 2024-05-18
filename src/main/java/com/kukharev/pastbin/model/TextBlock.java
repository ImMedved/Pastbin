package com.kukharev.pastbin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class TextBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hash;

    private String text;

    private LocalDateTime expiryTime;

    public void setText(String text) {
    }

    public void setExpiryTime(LocalDateTime plusSeconds) {
    }

    public void setHash(String hash) {
    }

    // Getters and setters
}