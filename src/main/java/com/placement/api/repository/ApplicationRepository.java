package com.placement.api.repository;

import com.placement.api.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicant_Id(Long userId);
    List<Application> findByJob_Id(Long jobId);
    List<Application> findByJob_PostedBy_Id(Long userId);
}
