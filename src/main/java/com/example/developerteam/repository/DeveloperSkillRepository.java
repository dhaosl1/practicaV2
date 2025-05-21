package com.example.developerteam.repository;

import com.example.developerteam.entity.DeveloperSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperSkillRepository extends JpaRepository<DeveloperSkill, Long> {
    DeveloperSkill findByName(String name);
}
