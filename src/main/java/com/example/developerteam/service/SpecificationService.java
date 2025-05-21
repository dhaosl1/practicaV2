package com.example.developerteam.service;

import com.example.developerteam.entity.Customer;
import com.example.developerteam.entity.Specification;
import com.example.developerteam.repository.CustomerRepository;
import com.example.developerteam.repository.SpecificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecificationService {

    private final SpecificationRepository specRepository;
    private final CustomerRepository customerRepository;

    public SpecificationService(SpecificationRepository specRepository,
                                CustomerRepository customerRepository) {
        this.specRepository = specRepository;
        this.customerRepository = customerRepository;
    }

    public Specification createSpecification(Long customerId, Specification spec) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        spec.setCustomer(customer);
        return specRepository.save(spec);
    }

    public List<Specification> getAllSpecifications() {
        return specRepository.findAll();
    }

    public Optional<Specification> getById(Long id) {
        return specRepository.findById(id);
    }

    public Specification updateSpecification(Long id, Specification updated) {
        return specRepository.findById(id).map(spec -> {
            spec.setTitle(updated.getTitle());
            spec.setDescription(updated.getDescription());
            spec.setRequirements(updated.getRequirements());
            return specRepository.save(spec);
        }).orElseThrow(() -> new RuntimeException("Specification not found"));
    }

    public void deleteSpecification(Long id) {
        specRepository.deleteById(id);
    }
}
