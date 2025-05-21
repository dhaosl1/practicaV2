package com.example.developerteam.controller;

import com.example.developerteam.entity.Manager;
import com.example.developerteam.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<Manager> create(@RequestBody Manager manager) {
        Manager saved = managerService.createManager(manager);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Manager>> getAll() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getById(@PathVariable Long id) {
        return managerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manager> update(
            @PathVariable Long id,
            @RequestBody Manager manager) {
        Manager updated = managerService.updateManager(id, manager);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }
}
