package com.placement.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ApplicationStatus status; // PENDING, ACCEPTED, REJECTED, etc.

    @Column(name = "applied_date")
    private Date appliedDate = new Date();

    private String resume;
}
