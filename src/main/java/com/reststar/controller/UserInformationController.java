package com.reststar.controller;

import com.reststar.dto.UserInformationResponseDTO;
import com.reststar.entity.UserInformation;
import com.reststar.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user-information")
public class UserInformationController {

    @Autowired
    private UserInformationService userInformationService;

    @PostMapping
    public UserInformationResponseDTO createUserInformation(@RequestBody com.reststar.dto.UserInformationDTO request) {
        return userInformationService.createUserInformationFromDTO(request);
    }
}
