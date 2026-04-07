package com.placement.api.controller;

import com.placement.api.dto.FileUploadDTO;
import com.placement.api.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestParam("userId") Long userId) throws IOException {
        
        FileUploadDTO fileUpload = fileUploadService.uploadFile(userId, file, fileType);
        return new ResponseEntity<>(fileUpload, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileUploadDTO>> getUserFiles(@PathVariable Long userId) {
        List<FileUploadDTO> files = fileUploadService.getUserFiles(userId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/user/{userId}/type/{fileType}")
    public ResponseEntity<FileUploadDTO> getLatestFileByType(
            @PathVariable Long userId,
            @PathVariable String fileType) {
        FileUploadDTO file = fileUploadService.getLatestFileByType(userId, fileType);
        return ResponseEntity.ok(file);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        fileUploadService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
