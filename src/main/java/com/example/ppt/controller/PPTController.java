package com.example.ppt.controller;

import com.example.ppt.dto.UploadResponse;
import com.example.ppt.service.PPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1")
public class PPTController {

    private final PPTService pptService;

    @Autowired
    public PPTController(PPTService pptService) {
        this.pptService = pptService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadPPT(@RequestParam("file") MultipartFile file) throws IOException {
        // Check if file has .ppt extension
        if (file == null || !Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".ppt")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UploadResponse("Only .ppt files are allowed.", -1));
        }
        UploadResponse response =  pptService.saveFileMetadata(file);
        return ResponseEntity.ok(response);

    }
}
