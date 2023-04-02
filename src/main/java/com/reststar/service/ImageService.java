package com.reststar.service;

import com.reststar.entity.Image;
import com.reststar.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StorageService storageService;

    @Value("${default.profile.image.uuid}")
    private String profileImageUUID;

    @Value("${default.banner.image.uui}")
    private String bannerImageUUID;

    public Image findImageById(String uuid) {
        UUID imageUUID = UUID.fromString(uuid);
        Optional<Image> image = imageRepository.findById(imageUUID);
        return image.orElse(null);
    }

    public Image findImageByIdAndThrow(String uuid) {
        Image image = this.findImageById(uuid);
        if(image == null) {
            throw new RuntimeException("Image not found");
        }

        return image;
    }

    public Image getDefaultProfileImage() {
        return findImageByIdAndThrow(profileImageUUID);
    }

    public Image getDefaultBannerImage(){
        return findImageByIdAndThrow(bannerImageUUID);
    }

    @Autowired
    public Image createImage(String imageId) {
        File file = this.storageService.loadTemporaryImage(imageId);
        UUID uuid = UUID.fromString(imageId);

        Image image = new Image();
        image.setToken(uuid);
        image.setUrl(file.getAbsolutePath());
        image.setUploadDate(Instant.now());

        return imageRepository.save(image);
    }
}
