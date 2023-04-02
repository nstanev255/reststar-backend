package com.reststar.service;

import com.reststar.constants.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class StorageService {
    @Value("${file.upload.temporary.dir}")
    private String temporaryDir;

    @Value("${file.upload.images.subdir}")
    private String imagesSubdir;

    private final Tika tika = new Tika();

    public String uploadImage(MultipartFile multipartFile) {
        this.validateImage(multipartFile);
        return this.uploadFile(multipartFile, this.temporaryDir + this.imagesSubdir);
    }

    @Async
    private String uploadFile(MultipartFile multipartFile, String path) {
        String uuid = UUID.randomUUID().toString();

        Path pathUri = Path.of(path + "/" + uuid + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        File file = pathUri.toFile();

        try {
            // Create directory if not exists.
            Files.createDirectories(pathUri.getParent());

            // Save the file.
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(multipartFile.getBytes());
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to upload file.");
        }

        return uuid;
    }

    private void validateImage(MultipartFile multipartFile) {
        // Check the content types.
        try {
            String detect = tika.detect(multipartFile.getBytes());
            String found = Arrays.stream(Constants.ALLOWED_IMAGE_CONTENT_TYPES).filter(ct -> StringUtils.equals(detect, ct)).findFirst().orElse(null);
            if (found == null) {
                throw new RuntimeException("This file does not meet the requirements.");
            }

        } catch (IOException ioException) {
            throw new RuntimeException("error getting files");
        }

        // Check the file size.
        long size = multipartFile.getSize();
        if (size > Constants.MAX_IMAGE_UPLOAD_ALLOWED) {
            throw new RuntimeException("The file size exceeds the limit.");
        }
    }

    private File loadFile(String baseDir, String filename) {
        Path path = Paths.get(baseDir, filename);
        File file = path.toFile();

        if(!file.isFile()) {
            file = null;
        }

        return file;
    }

    public File loadTemporaryImage(String imageId) {
        Path temporaryDir = Paths.get(this.temporaryDir, this.imagesSubdir);
        File image = loadFile(temporaryDir.toString(), imageId + ".jpg");

        if(image == null) {
            throw new RuntimeException("Image not uploaded");
        }

        return image;
    }
}
