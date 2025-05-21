package com.example.developerteam.repository;

import com.example.developerteam.entity.DeveloperAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperAssignmentRepository extends JpaRepository<DeveloperAssignment, Long> {
}
