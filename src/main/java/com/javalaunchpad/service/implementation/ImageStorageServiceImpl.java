package com.javalaunchpad.service.implementation;

import com.javalaunchpad.entity.Image;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.repository.ImageRepository;
import com.javalaunchpad.service.ImageStorageService;
import com.javalaunchpad.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final String BUCKET_NAME = "your-bucket-name";

    @Autowired
    private S3Client s3Client ;

    @Autowired
    private ImageRepository imageRepository ;

    @Autowired
    private PostService postService ;

    @Override
    public Long storeImage(MultipartFile file, Long postId) throws IOException, RessourceNotFoundException {
        String imageName = generateUniqueImageName(file.getOriginalFilename());
        String postFolder = "post_" + postId;
        String imagePath = postFolder + "/" + imageName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(imagePath)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        // post
        Post post = postService.getPostById(postId);
        Image image = new Image();
        image.setImageName(imageName);
        image.setImagePath(imagePath);
        image.setPost(post);
        imageRepository.save(image);
        return image.getId();
    }

    private String generateUniqueImageName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }

}
