package com.example.developerteam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "developer_assignment")
@Getter
@Setter
@NoArgsConstructor
public class DeveloperAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private Developer developer;

    @Column(nullable = false, updatable = false)
    private Instant assignedAt = Instant.now();

    @Column(length = 100)
    private String role; // наприклад: "backend", "frontend" тощо
}
