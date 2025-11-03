package com.sip3.backend.features.upload.controller;

import com.sip3.backend.features.upload.dto.UploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/uploads")
public class UploadController {

    @PostMapping
    public ResponseEntity<UploadResponse> upload(MultipartFile file) {
        String filename = file != null ? file.getOriginalFilename() : "file";
        String url = "https://example.com/uploads/" + Instant.now().toEpochMilli() + "/" + filename;
        return ResponseEntity.ok(new UploadResponse(url, filename));
    }
}
