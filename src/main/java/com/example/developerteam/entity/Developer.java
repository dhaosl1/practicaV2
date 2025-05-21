package com.example.developerteam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "developer")
@Getter
@Setter
@NoArgsConstructor
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel skillLevel = SkillLevel.JUNIOR;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeveloperAssignment> developerAssignments;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkLog> workLogs;

    @ManyToMany
    @JoinTable(
        name = "developer_skill_assignment",
        joinColumns = @JoinColumn(name = "developer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<DeveloperSkill> skills;

    public enum SkillLevel {
        JUNIOR,
        MIDDLE,
        SENIOR
    }
}
