package com.reststar.dto;

import lombok.Data;

@Data
public class UserInformationResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String bannerPictureUrl;

}
