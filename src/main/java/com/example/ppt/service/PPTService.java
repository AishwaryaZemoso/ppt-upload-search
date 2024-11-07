package com.example.ppt.service;

import com.example.ppt.dto.UploadResponse;
import com.example.ppt.entity.FileMetadata;
import com.example.ppt.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
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

        // Ensure the filename doesn't contain unsafe characters or path traversal sequences
        String sanitizedFileName = sanitizeFileName(Objects.requireNonNull(file.getOriginalFilename()));
        // Create a unique file name to prevent collisions
        String uniqueFileName = System.currentTimeMillis() + "_" + sanitizedFileName;
        // Create a safe path by combining the upload directory and sanitized/unique filename
        Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);
        // Ensure the directory exists
        Files.createDirectories(filePath.getParent());

        // Save the file to the destination
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
    private String sanitizeFileName(String fileName) {
        // Remove any special characters or path traversal attempts
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_"); // Only allows alphanumeric, dot, and dash
    }
}
