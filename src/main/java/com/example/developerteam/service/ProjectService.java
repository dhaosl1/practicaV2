package com.example.developerteam.service;

import com.example.developerteam.entity.*;
import com.example.developerteam.entity.Project.Status;
import com.example.developerteam.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SpecificationRepository specificationRepository;
    private final ManagerRepository managerRepository;
    private final DeveloperRepository developerRepository;
    private final DeveloperAssignmentRepository assignmentRepository;

    public ProjectService(ProjectRepository projectRepository,
                          SpecificationRepository specificationRepository,
                          ManagerRepository managerRepository,
                          DeveloperRepository developerRepository,
                          DeveloperAssignmentRepository assignmentRepository) {
        this.projectRepository = projectRepository;
        this.specificationRepository = specificationRepository;
        this.managerRepository = managerRepository;
        this.developerRepository = developerRepository;
        this.assignmentRepository = assignmentRepository;
    }


     // 1) Менеджер створює проєкт на базі існуючого ТЗ
     // 2) Автоматично призначає усіх вільних розробників з відповідним skillLevel
     // 3) Обчислює попередню вартість (estimatedHours * сума ставок призначених розробників)

    @Transactional
    public Project createProject(Long specId, Long managerId, Project projectData) {
        Specification spec = specificationRepository.findById(specId)
                .orElseThrow(() -> new RuntimeException("Specification not found"));
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        projectData.setSpecification(spec);
        projectData.setManager(manager);
        projectData.setStatus(Status.NEW);

        // Зберігаємо проєкт
        Project savedProject = projectRepository.save(projectData);

        //система автоматично призначає вільних розробників з відповідним рівнем skillLevel
        String requiredLevel = savedProject.getSpecification().getRequirements();
        Developer.SkillLevel level;
        try {
            level = Developer.SkillLevel.valueOf(requiredLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid skill level: " + requiredLevel);
        }

        List<Developer> available = developerRepository
                .findByIsAvailableTrueAndSkillLevel(level);

        if (!available.isEmpty()) {
            for (Developer dev : available) {
                // створити запис у DeveloperAssignment
                DeveloperAssignment da = new DeveloperAssignment();
                da.setProject(savedProject);
                da.setDeveloper(dev);
                da.setRole("developer");
                assignmentRepository.save(da);

                // позначити як зайнятого
                dev.setIsAvailable(false);
                developerRepository.save(dev);
            }
        }

        return savedProject;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getById(Long id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(Long id, Project updated) {
        return projectRepository.findById(id).map(proj -> {
            proj.setName(updated.getName());
            proj.setDescription(updated.getDescription());
            proj.setStatus(updated.getStatus());
            proj.setEstimatedHours(updated.getEstimatedHours());
            proj.setStartDate(updated.getStartDate());
            proj.setEndDate(updated.getEndDate());
            return projectRepository.save(proj);
        }).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }


     // Обчислити попередню вартість:
     // estimatedHours * (сума hourlyRate всіх призначених розробників)

    public BigDecimal calculatePreliminaryCost(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        BigDecimal sumRates = project.getDeveloperAssignments().stream()
                .map(da -> da.getDeveloper().getHourlyRate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (project.getEstimatedHours() == null) {
            return BigDecimal.ZERO;
        }
        return project.getEstimatedHours().multiply(sumRates);
    }


     // Коли треба вручну призначити конкретного розробника

    @Transactional
    public DeveloperAssignment assignDeveloperToProject(Long projectId, Long developerId, String role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Developer dev = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        if (!dev.getIsAvailable()) {
            throw new RuntimeException("Developer is not available");
        }

        DeveloperAssignment da = new DeveloperAssignment();
        da.setProject(project);
        da.setDeveloper(dev);
        da.setRole(role);
        assignmentRepository.save(da);

        dev.setIsAvailable(false);
        developerRepository.save(dev);

        return da;
    }
}
