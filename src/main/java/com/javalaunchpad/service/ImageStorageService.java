package com.javalaunchpad.service;

import com.javalaunchpad.exception.RessourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    Long storeImage(MultipartFile file, Long postId) throws IOException, RessourceNotFoundException;

}
