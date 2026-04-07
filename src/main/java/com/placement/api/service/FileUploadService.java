package com.placement.api.service;

import com.placement.api.dto.FileUploadDTO;
import com.placement.api.entity.FileUpload;
import com.placement.api.entity.User;
import com.placement.api.exception.BadRequestException;
import com.placement.api.exception.ResourceNotFoundException;
import com.placement.api.repository.FileUploadRepository;
import com.placement.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

    @Value("${file.upload.url:http://localhost:8080}")
    private String uploadUrl;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_TYPES = {"pdf", "doc", "docx"};

    public FileUploadDTO uploadFile(Long userId, MultipartFile file, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 5MB");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate file type
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        
        if (!isAllowedFileType(fileExtension)) {
            throw new BadRequestException("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_TYPES));
        }

        // Create uploads directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = userId + "_" + fileType + "_" + timestamp + "." + fileExtension;
        String filePath = uploadPath + File.separator + fileName;

        // Save file
        file.transferTo(new File(filePath));

        // Create FileUpload record
        FileUpload fileUpload = new FileUpload();
        fileUpload.setUser(user);
        fileUpload.setOriginalFileName(originalFileName);
        fileUpload.setFileName(fileName);
        fileUpload.setFileUrl(uploadUrl + "/uploads/" + fileName);
        fileUpload.setFileType(fileType);
        fileUpload.setFileSize(file.getSize());

        fileUpload = fileUploadRepository.save(fileUpload);

        return convertToDTO(fileUpload);
    }

    public List<FileUploadDTO> getUserFiles(Long userId) {
        List<FileUpload> files = fileUploadRepository.findByUserId(userId);
        return files.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FileUploadDTO getLatestFileByType(Long userId, String fileType) {
        return fileUploadRepository.findFirstByUserIdAndFileTypeOrderByUploadedAtDesc(userId, fileType)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No file found for type: " + fileType));
    }

    public void deleteFile(Long fileId) throws IOException {
        FileUpload fileUpload = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        // Delete physical file
        String filePath = uploadPath + File.separator + fileUpload.getFileName();
        Files.deleteIfExists(Paths.get(filePath));

        // Delete database record
        fileUploadRepository.deleteById(fileId);
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    private boolean isAllowedFileType(String extension) {
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private FileUploadDTO convertToDTO(FileUpload fileUpload) {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setId(fileUpload.getId());
        dto.setUserId(fileUpload.getUser().getId());
        dto.setOriginalFileName(fileUpload.getOriginalFileName());
        dto.setFileName(fileUpload.getFileName());
        dto.setFileUrl(fileUpload.getFileUrl());
        dto.setFileType(fileUpload.getFileType());
        dto.setFileSize(fileUpload.getFileSize());
        dto.setUploadedAt(fileUpload.getUploadedAt().toString());
        return dto;
    }
}
