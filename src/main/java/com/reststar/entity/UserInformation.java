package com.reststar.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    @ManyToMany
    @JoinTable(
            name = "profile_image_user_information",
            joinColumns = @JoinColumn(name = "user_information_id"),
            inverseJoinColumns = @JoinColumn(name = "image_Id")
    )
    private List<Image> profilePictures;
    @ManyToMany
    @JoinTable(
            name = "banner_image_user_information",
            joinColumns = @JoinColumn(name = "user_information_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> banners;
}
