package com.example.developerteam.repository;

import com.example.developerteam.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
}
