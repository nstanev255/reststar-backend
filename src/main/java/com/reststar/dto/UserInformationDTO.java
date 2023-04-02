package com.reststar.dto;

import lombok.Data;

@Data
public class UserInformationDTO {
    private String email;
    private Long userId;
    private String imageId;
    private String bannerId;
}
