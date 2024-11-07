package com.example.ppt.service;

import com.example.ppt.dto.UploadResponse;
import com.example.ppt.entity.FileMetadata;
import com.example.ppt.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PPTService {

    private final FileMetadataRepository fileMetadataRepository;
    // Define the base directory where files will be saved
    @Value("${file.upload-dir}") // Read directory from application properties
    private String uploadDir;

    @Autowired
    public PPTService(FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
    }

    //Method to extract and save metadata from uploaded file
    public UploadResponse saveFileMetadata(MultipartFile file) throws IOException {
        // Check if file with same name exists
        Optional<FileMetadata> existingFile = fileMetadataRepository.findByFileName(file.getOriginalFilename());
        if (existingFile.isPresent()) {
            return new UploadResponse("File already exists", existingFile.get().getId());
        }

        // Save the file locally
        Path filePath = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
        Files.createDirectories(filePath.getParent()); // Create directories if they don't exist
        file.transferTo(filePath.toFile());
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(file.getOriginalFilename());
        fileMetadata.setFileType(file.getContentType());
        fileMetadata.setFileSize(file.getSize());
        fileMetadata.setUploadDate(LocalDateTime.now());

        // Assuming file storage path is set after saving the file
        fileMetadata.setFilePath(filePath.toString()); // Set path as per your setup
        FileMetadata savedFile = fileMetadataRepository.save(fileMetadata);

        return new UploadResponse("File uploaded successfully", savedFile.getId());
    }

}
