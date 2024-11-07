package com.example.ppt.controller;

import com.example.ppt.dto.UploadResponse;
import com.example.ppt.service.PPTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PPTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PPTService pptService;  // Mock the service layer

    @InjectMocks
    private PPTController pptController;  // Inject the mock service into the controller

    @BeforeEach
    void setup() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pptController).build();  // Set up MockMvc to test the controller
    }

    @Test
    void testUploadPPT_ShouldReturnBadRequest_WhenFileIsNotPPT() throws Exception {
        // Create a mock file with a non-PPT extension
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        // Perform the request and assert the response
        mockMvc.perform(multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.message").value("Only .ppt files are allowed."));  // Validate response message
    }

    @Test
    void testUploadPPT_ShouldReturnSuccess_WhenFileIsPPT() throws Exception {
        // Create a mock file with a .ppt extension
        MockMultipartFile file = new MockMultipartFile("file", "test.ppt", "application/vnd.ms-powerpoint", "test content".getBytes());

        // Mock the service call to save file metadata
        when(pptService.saveFileMetadata(any())).thenReturn(new UploadResponse("File uploaded successfully.", 1));

        // Perform the request and assert the response
        mockMvc.perform(multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isOk())  // Expect 200 OK
                .andExpect(jsonPath("$.message").value("File uploaded successfully."))  // Validate success message
                .andExpect(jsonPath("$.fileId").value(1));  // Validate fileId in response
    }
}
