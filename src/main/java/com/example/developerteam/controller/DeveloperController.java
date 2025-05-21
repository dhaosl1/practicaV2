package com.example.developerteam.controller;

import com.example.developerteam.entity.Developer;
import com.example.developerteam.service.DeveloperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperService devService;

    public DeveloperController(DeveloperService devService) {
        this.devService = devService;
    }

    @PostMapping
    public ResponseEntity<Developer> create(@RequestBody Developer dev) {
        Developer saved = devService.createDeveloper(dev);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Developer>> getAll() {
        return ResponseEntity.ok(devService.getAllDevelopers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Developer> getById(@PathVariable Long id) {
        return devService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Developer> update(
            @PathVariable Long id,
            @RequestBody Developer dev) {
        Developer updated = devService.updateDeveloper(id, dev);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        devService.deleteDeveloper(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/skills")
    public ResponseEntity<String> assignSkill(
            @PathVariable Long id,
            @RequestParam String skillName) {
        devService.assignSkillToDeveloper(id, skillName);
        return ResponseEntity.ok("Skill assigned");
    }

    @GetMapping("/available")
    public ResponseEntity<List<Developer>> getAvailableByLevel(@RequestParam String level) {
        return ResponseEntity.ok(devService.findAvailableBySkillLevel(level));
    }
}
