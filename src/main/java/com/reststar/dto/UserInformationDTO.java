package com.reststar.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserInformationDTO {
    private String email;
    private Long userId;
    @JsonIgnore
    private String username;
    private String imageId;
    private String bannerId;
}
