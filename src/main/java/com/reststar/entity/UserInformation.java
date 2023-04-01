package com.reststar.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserInformation {
    @Id
    private Long id;
    private String username;
    private String email;
    @ManyToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;
    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Image banner;
}
