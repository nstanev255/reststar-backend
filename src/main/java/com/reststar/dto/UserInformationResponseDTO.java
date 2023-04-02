package com.reststar.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInformationResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private List<String> profilePictureUrl;
    private List<String> bannerPictureUrl;

}
