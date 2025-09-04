package org.example.finalprojecttuwaiq.Service;

import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.springframework.stereotype.Component;

@Component
public class RequirementMapper {
    public Requirement toEntity(RequirementRequestDTO dto, Project project) {
        Requirement entity = new Requirement();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setPriority(dto.getPriority());
        entity.setSource(dto.getSource());
        entity.setRationale(dto.getRationale());
        entity.setProject(project);
        return entity;
    }
}
