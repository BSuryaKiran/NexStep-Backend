package com.placement.api.dto;

import lombok.Data;

@Data
public class ApplicationRequest {
    private Long jobId;
    private Long userId;
    private String resumeUrl;
}
