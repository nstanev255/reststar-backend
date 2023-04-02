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

import java.util.Objects;

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
        userInformation.setUserEntity(userEntity);
        userInformation.setBanner(banner);
        userInformation.setProfilePicture(profileImage);
        userInformation.setEmail(email);

        return userInformationRepository.save(userInformation);
    }


    private UserInformationResponseDTO handleCreateUserInformation(UserInformationDTO userInformationDTO) {
        UserEntity userEntity = userEntityService.findByIdAndThrow(userInformationDTO.getUserId());

        Image profileImage = handleImage(userInformationDTO.getImageId());
        Image bannerImage = handleImage(userInformationDTO.getBannerId());

        UserInformation userInformation = createUserInformation(userInformationDTO.getEmail(), userEntity, bannerImage, profileImage);

        return mapUserInformationToResponse(userInformation);
    }

    private UserInformationResponseDTO mapUserInformationToResponse(UserInformation userInformation) {
        UserInformationResponseDTO dto = new UserInformationResponseDTO();

        dto.setUsername(userInformation.getUserEntity().getUsername());
        dto.setEmail(userInformation.getEmail());
        dto.setUserId(userInformation.getUserEntity().getId());
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

    public UserInformationResponseDTO updateUserInformationFormDTO(UserInformationDTO request) {
        validateUserInformation(request);
        return mapUserInformationToResponse(handleUpdateUserInformation(request));
    }

    private UserInformation handleUpdateUserInformation(UserInformationDTO request) {
        UserInformation userInformation = userInformationRepository.findByUserEntity_Id(request.getUserId());
        if (userInformation == null) {
            throw new RuntimeException("User Information does not exist");
        }

        UserEntity userEntity = userInformation.getUserEntity();
        if (!StringUtils.isEmpty(request.getUsername()) && !Objects.equals(userEntity.getUsername(), request.getUsername())) {
            userEntity.setUsername(userEntity.getUsername());
        }
        if (!StringUtils.isEmpty(request.getEmail()) && !Objects.equals(request.getEmail(), userInformation.getEmail())) {
            userInformation.setEmail(request.getEmail());
        }
        if (!Objects.equals(userInformation.getProfilePicture().getToken().toString(), request.getImageId())) {
            Image profilePicture = handleImage(request.getImageId());
            userInformation.setProfilePicture(profilePicture);
        }

        if(!Objects.equals(userInformation.getBanner().getToken().toString(), request.getBannerId())) {
            Image bannerPicture = handleImage(request.getBannerId());
            userInformation.setBanner(bannerPicture);
        }

        return userInformationRepository.save(userInformation);
    }
}
