package com.example.developerteam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})//тут
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @JsonIgnoreProperties({ "projects", "specifications" })//тут
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_id", nullable = false, unique = true)
    private Specification specification;

    @JsonIgnoreProperties("projects")//тут
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;

    @Column(precision = 8, scale = 2)
    private BigDecimal estimatedHours = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private LocalDate startDate;
    private LocalDate endDate;

    @JsonIgnore//тут
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeveloperAssignment> developerAssignments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkLog> workLogs;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;

    public enum Status {
        NEW,
        IN_PROGRESS,
        COMPLETED,
        CLOSED
    }
}
