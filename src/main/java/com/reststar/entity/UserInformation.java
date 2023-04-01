package com.reststar.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserInformation {
    @Id
    private Long id;
    private String email;

    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;
    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Image banner;
}
