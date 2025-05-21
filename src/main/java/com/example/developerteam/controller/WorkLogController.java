package com.example.developerteam.controller;

import com.example.developerteam.entity.WorkLog;
import com.example.developerteam.service.WorkLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worklogs")
public class WorkLogController {

    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @PostMapping("/project/{projectId}/developer/{developerId}")
    public ResponseEntity<WorkLog> logHours(
            @PathVariable Long projectId,
            @PathVariable Long developerId,
            @RequestBody WorkLog workLog) {
        WorkLog saved = workLogService.logHours(projectId, developerId, workLog);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<WorkLog>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(workLogService.getWorkLogsByProject(projectId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        workLogService.deleteWorkLog(id);
        return ResponseEntity.noContent().build();
    }
}
