package com.example.developerteam.repository;

import com.example.developerteam.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.developerteam.entity.Developer.SkillLevel;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    List<Developer> findByIsAvailableTrueAndSkillLevel(SkillLevel skillLevel);
}