package com.example.developerteam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "specification")
@Getter
@Setter
@NoArgsConstructor
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToOne(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Project project;
}
