package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.DiagramRequestDTO;
import org.example.finalprojecttuwaiq.Model.Diagram;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Repository.DiagramRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagramService {

    private final DiagramRepository diagramRepository;
    private final ProjectRepository projectRepository;

    public List<Diagram> getAllDiagrams() {
        return diagramRepository.findAll();
    }

    public Diagram getDiagramById(Integer id) {
        return diagramRepository.findById(id).orElseThrow(() -> new ApiException("Diagram with id " + id + " not found"));
    }

    public void addDiagram(DiagramRequestDTO diagramRequestDTO) {
        Project project = projectRepository.findById(diagramRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + diagramRequestDTO.getProjectId() + " not found"));

        Diagram diagram = new Diagram();
        diagram.setType(diagramRequestDTO.getType());
        diagram.setContentURI(diagramRequestDTO.getContentURI());
        diagram.setVersion(diagramRequestDTO.getVersion());
        diagram.setLastUpdated(LocalDateTime.now());
        diagram.setProject(project);
        diagramRepository.save(diagram);
    }

    public void updateDiagram(Integer id, DiagramRequestDTO diagramRequestDTO) {
        Diagram existingDiagram = diagramRepository.findById(id).orElseThrow(() -> new ApiException("Diagram with id " + id + " not found"));
        Project project = projectRepository.findById(diagramRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + diagramRequestDTO.getProjectId() + " not found"));

        existingDiagram.setType(diagramRequestDTO.getType());
        existingDiagram.setContentURI(diagramRequestDTO.getContentURI());
        existingDiagram.setVersion(diagramRequestDTO.getVersion());
        existingDiagram.setLastUpdated(LocalDateTime.now());
        existingDiagram.setProject(project);
        diagramRepository.save(existingDiagram);
    }

    public void deleteDiagram(Integer id) {
        Diagram diagram = diagramRepository.findById(id).orElseThrow(() -> new ApiException("Diagram with id " + id + " not found"));
        diagramRepository.delete(diagram);
    }
}
