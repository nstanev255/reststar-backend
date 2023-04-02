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

import java.util.*;

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
        userInformation.setBanners(Collections.singletonList(banner));
        userInformation.setProfilePictures(Collections.singletonList(profileImage));
        userInformation.setEmail(email);

        return userInformationRepository.save(userInformation);
    }


    private UserInformationResponseDTO handleCreateUserInformation(UserInformationDTO userInformationDTO) {
        UserEntity userEntity = userEntityService.findByIdAndThrow(userInformationDTO.getUserId());

        Image profileImage = handleImage(userInformationDTO.getImageId(), imageService.getDefaultProfileImage());
        Image bannerImage = handleImage(userInformationDTO.getBannerId(), imageService.getDefaultBannerImage());

        UserInformation userInformation = createUserInformation(userInformationDTO.getEmail(), userEntity, bannerImage, profileImage);

        return mapUserInformationToResponse(userInformation);
    }

    private UserInformationResponseDTO mapUserInformationToResponse(UserInformation userInformation) {
        UserInformationResponseDTO dto = new UserInformationResponseDTO();

        dto.setUsername(userInformation.getUserEntity().getUsername());
        dto.setEmail(userInformation.getEmail());
        dto.setUserId(userInformation.getUserEntity().getId());
        dto.setBannerPictureUrl(mapImagesToStrings(userInformation.getProfilePictures()));
        dto.setProfilePictureUrl(mapImagesToStrings(userInformation.getBanners()));

        return dto;
    }

    private List<String> mapImagesToStrings(List<Image> images) {
        List<String> imageStrings = new ArrayList<>();

        if (images == null) {
            return imageStrings;
        }

        for (Image image : images) {
            imageStrings.add(image.getUrl());
        }

        return imageStrings;
    }

    private Image handleImage(String imageUUID, Image defaultImage) {
        Image image = null;
        if (StringUtils.isEmpty(imageUUID)) {
            image = defaultImage;
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

        List<Image> banners = userInformation.getBanners();
        List<Image> profilePictures = userInformation.getProfilePictures();

        if (banners == null) {
            banners = new ArrayList<>();
        }

        if (profilePictures == null) {
            profilePictures = new ArrayList<>();
        }

        handleUpdateImage(profilePictures, request.getImageId(), this.imageService.getDefaultProfileImage());
        handleUpdateImage(banners, request.getBannerId(), this.imageService.getDefaultBannerImage());

        return userInformationRepository.save(userInformation);
    }

    private void handleUpdateImage(List<Image> pictures, String imageId, Image defaultImage) {
        if (StringUtils.isEmpty(imageId) && pictures.isEmpty()) {
            pictures.add(defaultImage);
        } else {
            Image found = pictures.stream().filter(i -> StringUtils.equals(i.getToken().toString(), imageId)).findFirst().orElse(null);
            if (found == null) {
                pictures.add(imageService.createImage(imageId));
            }
        }
    }
}
