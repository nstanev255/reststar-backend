package com.reststar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Image {
    @Id
    private Long id;
    private String url;
    private String token;

    private Instant uploadDate;
}
