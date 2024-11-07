package com.example.ppt.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UploadResponseTest {

    @Test
     void testUploadResponseConstructor() {
        // Arrange
        String expectedMessage = "File uploaded successfully";
        int expectedFileId = 123;

        // Act
        UploadResponse response = new UploadResponse(expectedMessage, expectedFileId);

        // Assert
        assertEquals(expectedMessage, response.getMessage());
        assertEquals(expectedFileId, response.getFileId());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UploadResponse response = new UploadResponse("Initial message", 1);

        // Act
        response.setMessage("Updated message");
        response.setFileId(456);

        // Assert
        assertEquals("Updated message", response.getMessage());
        assertEquals(456, response.getFileId());
    }
}
