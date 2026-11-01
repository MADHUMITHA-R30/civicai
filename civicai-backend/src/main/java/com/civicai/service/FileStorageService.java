package com.civicai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${civicai.app.uploadDir:uploads}")
    private String uploadDir;

    public String saveBase64Image(String base64Content, String prefix) {
        if (base64Content == null || base64Content.trim().isEmpty()) {
            return null;
        }

        try {
            String cleanBase64 = base64Content;
            if (base64Content.contains(",")) {
                cleanBase64 = base64Content.split(",")[1];
            }

            byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64);
            
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = prefix + "_" + UUID.randomUUID().toString() + ".jpg";
            Path filePath = uploadPath.resolve(filename);

            Files.write(filePath, decodedBytes);
            return "/uploads/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
