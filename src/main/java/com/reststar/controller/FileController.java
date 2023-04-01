package com.reststar.controller;

import com.reststar.dto.FileUploadResponse;
import com.reststar.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public FileUploadResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
        String imageUUid = this.storageService.uploadImage(file);
        return new FileUploadResponse(imageUUid);
    }
}
