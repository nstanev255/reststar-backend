package com.reststar.service;

import com.reststar.dto.UserInformationDTO;
import com.reststar.dto.UserInformationResponseDTO;
import com.reststar.entity.Image;
import com.reststar.entity.UserEntity;
import com.reststar.entity.UserInformation;
import com.reststar.repository.UserInformationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInformationService {
    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private ImageService imageService;

    public UserInformationResponseDTO createUserInformationFromDTO(UserInformationDTO userInformationDTO) {
        validateUserInformation(userInformationDTO);
        return handleCreateUserInformation(userInformationDTO);

    }

    private void validateUserInformation(com.reststar.dto.UserInformationDTO userInformationDTO) {
        if (StringUtils.isEmpty(userInformationDTO.getEmail())) {
            throw new RuntimeException("Email is empty.");
        }

        if (userInformationDTO.getUserId() == null) {
            throw new RuntimeException("User id is empty.");
        }
    }

    @Transactional
    public UserInformation createUserInformation(String email, UserEntity userEntity, Image banner, Image profileImage) {
        UserInformation userInformation = new UserInformation();
        userInformation.setUser(userEntity);
        userInformation.setBanner(banner);
        userInformation.setProfilePicture(profileImage);
        userInformation.setEmail(email);

        return userInformationRepository.save(userInformation);
    }

    @Transactional
    private UserInformationResponseDTO handleCreateUserInformation(UserInformationDTO userInformationDTO) {
        UserEntity userEntity = userEntityService.findByIdAndThrow(userInformationDTO.getUserId());

        Image profileImage = handleImage(userInformationDTO.getImageId());
        Image bannerImage = handleImage(userInformationDTO.getBannerId());

        UserInformation userInformation = createUserInformation(userInformationDTO.getEmail(), userEntity, bannerImage, profileImage);

        return mapUserInformationToResponse(userInformation);
    }

    private UserInformationResponseDTO mapUserInformationToResponse(UserInformation userInformation) {
        UserInformationResponseDTO dto = new UserInformationResponseDTO();

        dto.setUsername(userInformation.getUser().getUsername());
        dto.setEmail(userInformation.getEmail());
        dto.setUserId(userInformation.getUser().getId());
        dto.setBannerPictureUrl(userInformation.getProfilePicture().getUrl());
        dto.setProfilePictureUrl(userInformation.getBanner().getUrl());

        return dto;
    }

    private Image handleImage(String imageUUID) {
        Image image = null;
        if (StringUtils.isEmpty(imageUUID)) {
            image = imageService.getDefaultProfileImage();
        } else {
            image = imageService.createImage(imageUUID);
        }

        return image;
    }
}
