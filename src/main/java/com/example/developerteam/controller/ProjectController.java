package com.example.developerteam.controller;

import com.example.developerteam.entity.Project;
import com.example.developerteam.entity.DeveloperAssignment;
import com.example.developerteam.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Створити проєкт на основі існуючого specId і managerId.
     * Попередня вартість рахується автоматично.
     * У тілі запиту JSON із полями name, description, estimatedHours тощо.
     */
    @PostMapping("/spec/{specId}/manager/{managerId}")
    public ResponseEntity<Project> createProject(
            @PathVariable Long specId,
            @PathVariable Long managerId,
            @RequestBody Project project) {
        Project saved = projectService.createProject(specId, managerId, project);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        return projectService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(
            @PathVariable Long id,
            @RequestBody Project project) {
        Project updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/preliminaryCost")
    public ResponseEntity<BigDecimal> getPreliminaryCost(@PathVariable Long id) {
        BigDecimal cost = projectService.calculatePreliminaryCost(id);
        return ResponseEntity.ok(cost);
    }

    @PostMapping("/{projectId}/assign/{developerId}")
    public ResponseEntity<DeveloperAssignment> assignDeveloper(
            @PathVariable Long projectId,
            @PathVariable Long developerId,
            @RequestParam(required = false, defaultValue = "developer") String role) {
        DeveloperAssignment da = projectService.assignDeveloperToProject(projectId, developerId, role);
        return ResponseEntity.ok(da);
    }
}
