package com.example.developerteam.controller;

import com.example.developerteam.entity.Specification;
import com.example.developerteam.service.SpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {

    private final SpecificationService specService;

    public SpecificationController(SpecificationService specService) {
        this.specService = specService;
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Specification> create(
            @PathVariable Long customerId,
            @RequestBody Specification spec) {
        Specification saved = specService.createSpecification(customerId, spec);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Specification>> getAll() {
        return ResponseEntity.ok(specService.getAllSpecifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specification> getById(@PathVariable Long id) {
        return specService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specification> update(
            @PathVariable Long id,
            @RequestBody Specification spec) {
        Specification updated = specService.updateSpecification(id, spec);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        specService.deleteSpecification(id);
        return ResponseEntity.noContent().build();
    }
}
