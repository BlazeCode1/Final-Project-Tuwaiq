package org.example.finalprojecttuwaiq.Service;

import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.DTO.UserStoryRequestDTO;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class UserStoryMapper {
    public UserStory toEntity(UserStoryRequestDTO dto, Requirement requirement) {
        UserStory entity = new UserStory();

        entity.setAsA(dto.getAsA());
        entity.setIWant(dto.getIWant());
        entity.setSoThat(dto.getSoThat());
        entity.setPriority(dto.getPriority());
        entity.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        entity.setStatus(dto.getStatus());
        entity.setRequirement(requirement);

        return entity;
    }
}