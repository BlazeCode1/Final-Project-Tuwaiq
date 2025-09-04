package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Model.Diagram;
import org.example.finalprojecttuwaiq.Repository.DiagramRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

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


    public void deleteDiagram(Integer id) {
        Diagram diagram = diagramRepository.findById(id).orElseThrow(() -> new ApiException("Diagram with id " + id + " not found"));
        diagramRepository.delete(diagram);
    }

    //TODO: Generate Class, Sequence, Use Case, ERD Diagram.
}
