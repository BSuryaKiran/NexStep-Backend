package com.placement.api.repository;

import com.placement.api.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByActiveTrue();
    List<Job> findByCompany(String company);
    List<Job> findByPostedBy_Id(Long userId);
}
