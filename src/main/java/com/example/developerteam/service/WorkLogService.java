package com.example.developerteam.service;

import com.example.developerteam.entity.Developer;
import com.example.developerteam.entity.Project;
import com.example.developerteam.entity.WorkLog;
import com.example.developerteam.repository.DeveloperRepository;
import com.example.developerteam.repository.ProjectRepository;
import com.example.developerteam.repository.WorkLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;

    public WorkLogService(WorkLogRepository workLogRepository,
                          ProjectRepository projectRepository,
                          DeveloperRepository developerRepository) {
        this.workLogRepository = workLogRepository;
        this.projectRepository = projectRepository;
        this.developerRepository = developerRepository;
    }

    public WorkLog logHours(Long projectId, Long developerId, WorkLog workLogData) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Developer dev = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        workLogData.setProject(project);
        workLogData.setDeveloper(dev);

        return workLogRepository.save(workLogData);
    }

    public List<WorkLog> getWorkLogsByProject(Long projectId) {
        return workLogRepository.findByProjectProjectId(projectId);
    }

    public void deleteWorkLog(Long id) {
        workLogRepository.deleteById(id);
    }
}
