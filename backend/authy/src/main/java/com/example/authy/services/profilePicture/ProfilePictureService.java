package com.example.authy.services.profilePicture;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfilePictureService {
    String storeProfilePicture(MultipartFile file) throws IOException;
}
