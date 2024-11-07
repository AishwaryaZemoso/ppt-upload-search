package com.example.ppt.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileMetadataTest {

    @Test
    void testFileMetadataSettersAndGetters() {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();

        String expectedFileName = "test.ppt";
        String expectedFileType = "application/vnd.ms-powerpoint";
        Long expectedFileSize = 1024L;
        LocalDateTime expectedUploadDate = LocalDateTime.now();
        String expectedFilePath = "/uploads/test.ppt";

        // Act
        fileMetadata.setFileName(expectedFileName);
        fileMetadata.setFileType(expectedFileType);
        fileMetadata.setFileSize(expectedFileSize);
        fileMetadata.setUploadDate(expectedUploadDate);
        fileMetadata.setFilePath(expectedFilePath);

        // Assert
        assertEquals(expectedFileName, fileMetadata.getFileName());
        assertEquals(expectedFileType, fileMetadata.getFileType());
        assertEquals(expectedFileSize, fileMetadata.getFileSize());
        assertEquals(expectedUploadDate, fileMetadata.getUploadDate());
        assertEquals(expectedFilePath, fileMetadata.getFilePath());
    }

    @Test
    void testFileMetadataDefaultValues() {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();

        // Act & Assert
        assertEquals(0, fileMetadata.getId());  // Default value for int (ID is 0 initially)
        assertNull(fileMetadata.getFileName());  // Default value for String
        assertNull(fileMetadata.getFileType());  // Default value for String
        assertNull(fileMetadata.getFileSize());  // Default value for Long
        assertNull(fileMetadata.getUploadDate());  // Default value for LocalDateTime
        assertNull(fileMetadata.getFilePath());  // Default value for String
    }
}
