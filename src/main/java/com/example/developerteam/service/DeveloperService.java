package com.example.developerteam.service;

import com.example.developerteam.entity.Developer;
import com.example.developerteam.entity.DeveloperSkill;
import com.example.developerteam.repository.DeveloperRepository;
import com.example.developerteam.repository.DeveloperSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final DeveloperSkillRepository skillRepository;

    public DeveloperService(DeveloperRepository developerRepository,
                            DeveloperSkillRepository skillRepository) {
        this.developerRepository = developerRepository;
        this.skillRepository = skillRepository;
    }

    public Developer createDeveloper(Developer dev) {
        return developerRepository.save(dev);
    }

    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    public Optional<Developer> getById(Long id) {
        return developerRepository.findById(id);
    }

    public Developer updateDeveloper(Long id, Developer updated) {
        return developerRepository.findById(id).map(dev -> {
            dev.setName(updated.getName());
            dev.setEmail(updated.getEmail());
            dev.setPhone(updated.getPhone());
            dev.setHourlyRate(updated.getHourlyRate());
            dev.setSkillLevel(updated.getSkillLevel());
            dev.setIsAvailable(updated.getIsAvailable());
            return developerRepository.save(dev);
        }).orElseThrow(() -> new RuntimeException("Developer not found"));
    }

    public void deleteDeveloper(Long id) {
        developerRepository.deleteById(id);
    }

    @Transactional
    public void assignSkillToDeveloper(Long developerId, String skillName) {
        Developer dev = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        DeveloperSkill skill = skillRepository.findByName(skillName);
        if (skill == null) {
            // якщо навичка не існує, створимо її
            skill = new DeveloperSkill();
            skill.setName(skillName);
            skill = skillRepository.save(skill);
        }

        Set<DeveloperSkill> skills = dev.getSkills();
        if (skills == null) {
            skills = new HashSet<>();
        }
        skills.add(skill);
        dev.setSkills(skills);
        developerRepository.save(dev);
    }

    // метод для enum
    public List<Developer> findAvailableBySkillLevel(Developer.SkillLevel level) {
        return developerRepository.findByIsAvailableTrueAndSkillLevel(level);
    }

    // метод для String
    public List<Developer> findAvailableBySkillLevel(String level) {
        try {
            Developer.SkillLevel skillLevel = Developer.SkillLevel.valueOf(level.toUpperCase());
            return findAvailableBySkillLevel(skillLevel);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid skill level: " + level);
        }
    }
    }

