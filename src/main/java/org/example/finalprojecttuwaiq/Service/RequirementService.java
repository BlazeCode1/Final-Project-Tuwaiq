package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.RequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;

    public List<Requirement> getAllRequirements() {
        return requirementRepository.findAll();
    }

    public Requirement getRequirementById(Integer id) {
        return requirementRepository.findById(id).orElseThrow(() -> new ApiException("Requirement with id " + id + " not found"));
    }

    public void addRequirement(RequirementRequestDTO requirementRequestDTO) {
        Project project = projectRepository.findById(requirementRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + requirementRequestDTO.getProjectId() + " not found"));

        Requirement requirement = new Requirement();
        requirement.setTitle(requirementRequestDTO.getTitle());
        requirement.setDescription(requirementRequestDTO.getDescription());
        requirement.setType(requirementRequestDTO.getType());
        requirement.setPriority(requirementRequestDTO.getPriority());
        requirement.setStatus(requirementRequestDTO.getStatus());
        requirement.setSource(requirementRequestDTO.getSource());
        requirement.setRationale(requirementRequestDTO.getRationale());
        // Generate traceId using current time in milliseconds
        requirement.setTraceId(String.valueOf(System.currentTimeMillis()));
        requirement.setProject(project);
        requirementRepository.save(requirement);
    }

    public void updateRequirement(Integer id, RequirementRequestDTO requirementRequestDTO) {
        Requirement existingRequirement = requirementRepository.findById(id).orElseThrow(() -> new ApiException("Requirement with id " + id + " not found"));
        Project project = projectRepository.findById(requirementRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + requirementRequestDTO.getProjectId() + " not found"));

        existingRequirement.setTitle(requirementRequestDTO.getTitle());
        existingRequirement.setDescription(requirementRequestDTO.getDescription());
        existingRequirement.setType(requirementRequestDTO.getType());
        existingRequirement.setPriority(requirementRequestDTO.getPriority());
        existingRequirement.setStatus(requirementRequestDTO.getStatus());
        existingRequirement.setSource(requirementRequestDTO.getSource());
        existingRequirement.setRationale(requirementRequestDTO.getRationale());
        // traceId is not updated here as it's typically set on creation
        existingRequirement.setProject(project);
        requirementRepository.save(existingRequirement);
    }

    public void deleteRequirement(Integer id) {
        Requirement requirement = requirementRepository.findById(id).orElseThrow(() -> new ApiException("Requirement with id " + id + " not found"));
        requirementRepository.delete(requirement);
    }
}
