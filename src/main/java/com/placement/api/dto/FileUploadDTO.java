package com.placement.api.dto;

public class FileUploadDTO {
    private Long id;
    private Long userId;
    private String originalFileName;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String uploadedAt;

    public FileUploadDTO() {}

    public FileUploadDTO(Long id, Long userId, String originalFileName, String fileName, String fileUrl, String fileType, Long fileSize, String uploadedAt) {
        this.id = id;
        this.userId = userId;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
}
