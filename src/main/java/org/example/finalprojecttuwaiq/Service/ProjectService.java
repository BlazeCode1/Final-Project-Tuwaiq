package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BARepository baRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElseThrow(() -> new ApiException("Project with id " + id + " not found"));
    }

    public void addProject(Integer ba_id, ProjectRequestDTO projectRequestDTO) {
        BA businessAnalyst = baRepository.findBAById(ba_id);
        if(businessAnalyst == null)
            throw new ApiException("Business Analyst Not Found");
        if(!businessAnalyst.getUser().getRole().equalsIgnoreCase("BA"))
            throw new ApiException("Must Be Business Analyst to add A project");
        Project project = new Project();
        project.setName(projectRequestDTO.getName());
        project.setDescription(projectRequestDTO.getDescription());
        project.setStatus("Discovery");
        project.setCreatedAt(LocalDateTime.now());
        project.getBas().add(businessAnalyst);
        businessAnalyst.getProjects().add(project);
        projectRepository.save(project);
        baRepository.save(businessAnalyst);
    }

    public void updateProject(Integer id, ProjectRequestDTO projectRequestDTO) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new ApiException("Project with id " + id + " not found"));
        existingProject.setName(projectRequestDTO.getName());
        existingProject.setDescription(projectRequestDTO.getDescription());
        existingProject.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(existingProject);
    }

    public void deleteProject(Integer id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ApiException("Project with id " + id + " not found"));
        projectRepository.delete(project);
    }
}
