package com.app.book_network.file;

import com.app.book_network.book.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile file, @NonNull Long userId) {
        final String fileUploadSubPath = "users"+ File.separator + userId;
        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile file, @NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
            }
        }
        final String fileExtension = getExtensionFile(file.getOriginalFilename());
        //for example = ./uploads/users/1/2383838387373.jpg
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath,file.getBytes());
            log.info("File saved to "+targetFilePath);
            return targetFilePath;
        }catch (IOException e){
            log.error("File was not saved ",e);
        }
        return null;
    }

    private String getExtensionFile(String fileName) {
        if (fileName == null || fileName.isEmpty()){
            return "";
        }
        int lastDoIndex = fileName.lastIndexOf(".");
        if (lastDoIndex == -1){
            return "";
        }
        return fileName.substring(lastDoIndex + 1).toLowerCase();
    }
}
