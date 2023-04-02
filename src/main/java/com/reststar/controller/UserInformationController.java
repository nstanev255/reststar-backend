package com.reststar.controller;

import com.reststar.dto.ImageRequestDTO;
import com.reststar.dto.UserInformationDTO;
import com.reststar.dto.UserInformationResponseDTO;
import com.reststar.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user-information")
public class UserInformationController {

    @Autowired
    private UserInformationService userInformationService;

    @PostMapping("/")
    public UserInformationResponseDTO createUserInformation(@RequestBody UserInformationDTO request) {
        return userInformationService.createUserInformationFromDTO(request);
    }

    @PutMapping("/")
    public UserInformationResponseDTO updateUserInformation(@RequestBody UserInformationDTO request) {
        return userInformationService.updateUserInformationFormDTO(request);
    }

    @PutMapping("/{id}/profile-picture")
    public UserInformationResponseDTO updateProfilePicture(@PathVariable(name = "id") Long id, @RequestBody ImageRequestDTO request) {
        return userInformationService.updateProfilePicture(id, request);
    }

}
