package com.lmkhanhs.home_net.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.lmkhanhs.home_net.dtos.apps.ImageServiceResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadImageUtills {

    WebClient webClient;
    public ImageServiceResponse upload(MultipartFile file) {

        File tempFile = createTempFile(file);

        try {
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", new FileSystemResource(tempFile));

            return webClient.post()
                    .uri("/api/media/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(ImageServiceResponse.class)
                    .block();

        } finally {
            tempFile.delete(); 
        }
    }

    public String uploadAsUrl(MultipartFile file) {

        ImageServiceResponse response = upload(file);
        return response != null ? response.getUrl() : null;
    }

    private File createTempFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload-", "-" + file.getOriginalFilename());
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create temp file", e);
        }
    }
}
