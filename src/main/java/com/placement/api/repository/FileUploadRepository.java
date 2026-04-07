package com.placement.api.repository;

import com.placement.api.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    List<FileUpload> findByUserId(Long userId);
    List<FileUpload> findByUserIdAndFileType(Long userId, String fileType);
    Optional<FileUpload> findFirstByUserIdAndFileTypeOrderByUploadedAtDesc(Long userId, String fileType);
}
