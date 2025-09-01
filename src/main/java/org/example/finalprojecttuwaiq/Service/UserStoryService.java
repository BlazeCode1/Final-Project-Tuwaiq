package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.UserStoryRequestDTO;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Repository.RequirementRepository;
import org.example.finalprojecttuwaiq.Repository.UserStoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final RequirementRepository requirementRepository;

    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    public UserStory getUserStoryById(Integer id) {
        return userStoryRepository.findById(id).orElseThrow(() -> new ApiException("UserStory with id " + id + " not found"));
    }

    public void addUserStory(UserStoryRequestDTO userStoryRequestDTO) {
        Requirement requirement = requirementRepository.findById(userStoryRequestDTO.getRequirementId())
                .orElseThrow(() -> new ApiException("Requirement with ID " + userStoryRequestDTO.getRequirementId() + " not found"));

        UserStory userStory = new UserStory();
        userStory.setAsA(userStoryRequestDTO.getAsA());
        userStory.setIWant(userStoryRequestDTO.getIWant());
        userStory.setSoThat(userStoryRequestDTO.getSoThat());
        userStory.setPriority(userStoryRequestDTO.getPriority());
        userStory.setAcceptanceCriteria(userStoryRequestDTO.getAcceptanceCriteria());
        userStory.setStatus(userStoryRequestDTO.getStatus());
        userStory.setRequirement(requirement);
        userStoryRepository.save(userStory);
    }

    public void updateUserStory(Integer id, UserStoryRequestDTO userStoryRequestDTO) {
        UserStory existingUserStory = userStoryRepository.findById(id).orElseThrow(() -> new ApiException("UserStory with id " + id + " not found"));
        Requirement requirement = requirementRepository.findById(userStoryRequestDTO.getRequirementId())
                .orElseThrow(() -> new ApiException("Requirement with ID " + userStoryRequestDTO.getRequirementId() + " not found"));

        existingUserStory.setAsA(userStoryRequestDTO.getAsA());
        existingUserStory.setIWant(userStoryRequestDTO.getIWant());
        existingUserStory.setSoThat(userStoryRequestDTO.getSoThat());
        existingUserStory.setPriority(userStoryRequestDTO.getPriority());
        existingUserStory.setAcceptanceCriteria(userStoryRequestDTO.getAcceptanceCriteria());
        existingUserStory.setStatus(userStoryRequestDTO.getStatus());
        existingUserStory.setRequirement(requirement);
        userStoryRepository.save(existingUserStory);
    }

    public void deleteUserStory(Integer id) {
        UserStory userStory = userStoryRepository.findById(id).orElseThrow(() -> new ApiException("UserStory with id " + id + " not found"));
        userStoryRepository.delete(userStory);
    }
}
