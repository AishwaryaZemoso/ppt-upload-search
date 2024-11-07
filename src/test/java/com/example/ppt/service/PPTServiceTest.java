package com.example.ppt.service;

import com.example.ppt.dto.UploadResponse;
import com.example.ppt.entity.FileMetadata;
import com.example.ppt.repository.FileMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PPTServiceTest {

    @InjectMocks
    private PPTService pptService;  // Service being tested

    @Mock
    private FileMetadataRepository fileMetadataRepository;  // Mocked dependency

    @BeforeEach
    public void setup() {
        // Initialize the mock objects
        MockitoAnnotations.openMocks(this);
        // Hardcode or mock the uploadDir
        pptService.uploadDir = "./temp";
    }

    @Test
    void testSaveFileMetadata_FileAlreadyExists() throws IOException {
        // Arrange
        String fileName = "test.ppt";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/vnd.ms-powerpoint", "content".getBytes());

        FileMetadata existingFileMetadata = new FileMetadata();
        existingFileMetadata.setId(1);
        existingFileMetadata.setFileName(fileName);

        // Mock repository to return an existing file
        when(fileMetadataRepository.findByFileName(fileName)).thenReturn(Optional.of(existingFileMetadata));

        // Act
        UploadResponse response = pptService.saveFileMetadata(file);

        // Assert
        assertEquals("File already exists", response.getMessage());
        assertEquals(1, response.getFileId());
        verify(fileMetadataRepository, times(1)).findByFileName(fileName);  // Verify repository call
        verify(fileMetadataRepository, never()).save(any(FileMetadata.class));  // Ensure save was not called
    }

    @Test
     void testSaveFileMetadata_FileUploadedSuccessfully() throws IOException {
        // Arrange
        String fileName = "new_file.ppt";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/vnd.ms-powerpoint", "content".getBytes());

        FileMetadata savedFileMetadata = new FileMetadata();
        savedFileMetadata.setId(1);
        savedFileMetadata.setFileName(fileName);
        savedFileMetadata.setFilePath("path/to/file");

        // Mock repository to return empty for the file check (indicating file doesn't exist)
        when(fileMetadataRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        // Mock the save method to return the saved file metadata
        when(fileMetadataRepository.save(any(FileMetadata.class))).thenReturn(savedFileMetadata);

        // Act
        UploadResponse response = pptService.saveFileMetadata(file);

        // Assert
        assertEquals("File uploaded successfully", response.getMessage());
        assertEquals(1, response.getFileId());
        verify(fileMetadataRepository, times(1)).findByFileName(fileName);  // Verify repository call
        verify(fileMetadataRepository, times(1)).save(any(FileMetadata.class));  // Ensure save was called once
    }

    @Test
    void testSaveFileMetadata_ShouldCreateDirectoriesAndTransferFile() throws IOException {
        // Arrange
        String fileName = "new_file.ppt";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/vnd.ms-powerpoint", "content".getBytes());

        // Simulate the directory path where the file would be saved
        String dirPath = "some/directory/path/";
        Path filePath = Paths.get(dirPath + fileName);

        // Create the necessary directories if they don't exist
        Files.createDirectories(filePath.getParent()); // This creates the parent directories

        // Mock repository
        when(fileMetadataRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        // Mock save behavior
        FileMetadata savedFileMetadata = new FileMetadata();
        savedFileMetadata.setId(1);
        savedFileMetadata.setFilePath(filePath.toString());
        when(fileMetadataRepository.save(any(FileMetadata.class))).thenReturn(savedFileMetadata);

        // Act
        UploadResponse response = pptService.saveFileMetadata(file);

        // Assert
        assertEquals("File uploaded successfully", response.getMessage());
        assertEquals(1, response.getFileId());

        // Verify the file transfer (mock transferTo logic)
        file.transferTo(new File(filePath.toString()));

        // Clean up the directory after the test
        Files.deleteIfExists(filePath);  // Delete the test file if it exists
        Files.deleteIfExists(filePath.getParent());  // Delete the directory after test if needed
    }

}
