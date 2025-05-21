package com.example.developerteam.service;

import com.example.developerteam.entity.Manager;
import com.example.developerteam.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getById(Long id) {
        return managerRepository.findById(id);
    }

    public Manager updateManager(Long id, Manager updated) {
        return managerRepository.findById(id).map(manager -> {
            manager.setName(updated.getName());
            manager.setEmail(updated.getEmail());
            manager.setPhone(updated.getPhone());
            return managerRepository.save(manager);
        }).orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    public void deleteManager(Long id) {
        managerRepository.deleteById(id);
    }
}
