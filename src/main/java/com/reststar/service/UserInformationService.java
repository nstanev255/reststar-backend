package com.reststar.service;

import com.reststar.dto.ImageRequestDTO;
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

    private UserInformation findById(Long id) {
        Optional<UserInformation> userInformation = userInformationRepository.findById(id);
        return userInformation.orElse(null);
    }

    private UserInformation findByIdAndThrow(Long id) {
        UserInformation userInformation = findById(id);

        if (userInformation == null) {
            throw new RuntimeException("User information not found");
        }

        return userInformation;
    }

    public UserInformation findByEmail(String email) {
        return userInformationRepository.findByEmail(email);
    }

    public UserInformationResponseDTO createUserInformationFromDTO(UserInformationDTO userInformationDTO) {
        validateNewUserInformation(userInformationDTO);
        return handleCreateUserInformation(userInformationDTO);

    }

    private void validateNewUserInformation(UserInformationDTO userInformationDTO) {
        if (StringUtils.isEmpty(userInformationDTO.getEmail())) {
            throw new RuntimeException("Email is empty.");
        }

        if (userInformationDTO.getUserId() == null) {
            throw new RuntimeException("User id is empty.");
        }

        UserInformation userInformationInfo = findByUserId(userInformationDTO.getUserId());
        if (userInformationInfo != null) {
            throw new RuntimeException("User already has user information.");
        }

        UserInformation userInformation = findByEmail(userInformationDTO.getEmail());
        if (userInformation != null) {
            throw new RuntimeException("User information already exists.");
        }
    }

    private UserInformation findByUserId(Long userId) {
        return userInformationRepository.findByUserEntity_Id(userId);
    }

    @Transactional
    public UserInformation createUserInformation(String email, UserEntity userEntity, Image profilePicture, Image bannerPicture) {
        UserInformation userInformation = new UserInformation();
        userInformation.setUserEntity(userEntity);
        userInformation.setEmail(email);
        userInformation.setProfilePictures(Collections.singletonList(profilePicture));
        userInformation.setBanners(Collections.singletonList(bannerPicture));

        return userInformationRepository.save(userInformation);
    }


    private UserInformationResponseDTO handleCreateUserInformation(UserInformationDTO userInformationDTO) {
        UserEntity userEntity = userEntityService.findByIdAndThrow(userInformationDTO.getUserId());

        Image profilePicture = imageService.getDefaultProfileImage();
        if (!StringUtils.isEmpty(userInformationDTO.getImageId())) {
            profilePicture = imageService.createImage(userInformationDTO.getImageId());
        }

        Image bannerPicture = imageService.getDefaultBannerImage();
        if (!StringUtils.isEmpty(userInformationDTO.getBannerId())) {
            bannerPicture = imageService.createImage(userInformationDTO.getBannerId());
        }

        UserInformation userInformation = createUserInformation(userInformationDTO.getEmail(), userEntity, profilePicture, bannerPicture);

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

    public UserInformationResponseDTO updateUserInformationFormDTO(UserInformationDTO request) {
        validateNewUserInformation(request);
        return mapUserInformationToResponse(handleUpdateUserInformation(request));
    }

    private UserInformation handleUpdateUserInformation(UserInformationDTO request) {
        UserInformation userInformation = userInformationRepository.findByUserEntity_Id(request.getUserId());
        if (userInformation == null) {
            throw new RuntimeException("User Information does not exist");
        }

        if (!StringUtils.isEmpty(request.getEmail()) && !Objects.equals(request.getEmail(), userInformation.getEmail())) {
            userInformation.setEmail(request.getEmail());
        }

        return userInformationRepository.save(userInformation);
    }

    private void handleUpdateImage(List<Image> pictures, String imageId) {
        Image found = pictures.stream().filter(i -> StringUtils.equals(i.getToken().toString(), imageId)).findFirst().orElse(null);
        if (found == null) {
            pictures.add(imageService.createImage(imageId));
        }
    }

    public UserInformationResponseDTO updateProfilePicture(Long id, ImageRequestDTO request) {
        UserInformation userInformation = findByIdAndThrow(id);

        List<Image> profilePictures = userInformation.getProfilePictures();
        if (profilePictures == null) {
            profilePictures = new ArrayList<>();
        }

        handleUpdateImage(profilePictures, request.getImageUUID());
        userInformationRepository.save(userInformation);

        return mapUserInformationToResponse(userInformation);
    }
}
