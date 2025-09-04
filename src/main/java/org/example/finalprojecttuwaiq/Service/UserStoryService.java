package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.UserStoryRequestDTO;
import org.example.finalprojecttuwaiq.Model.*;
import org.example.finalprojecttuwaiq.Repository.DraftUserStoryRepository;
import org.example.finalprojecttuwaiq.Repository.RequirementRepository;
import org.example.finalprojecttuwaiq.Repository.UserStoryRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final DraftUserStoryRepository draftUserStoryRepository;
    private final RequirementRepository requirementRepository;
    private final ObjectMapper objectMapper;
    private final UserStoryMapper mapper;
    private final OpenAiChatModel ai;

    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    public UserStory getUserStoryById(Integer id) {
        return userStoryRepository.findById(id).orElseThrow(() -> new ApiException("UserStory with id " + id + " not found"));
    }

    public List<DraftUserStory> getAllDraftsByRequirementId(Integer requirement_id) {
        Requirement requirement = requirementRepository.getRequirementById(requirement_id);
        if (requirement == null)
            throw new ApiException("Requirement Not Found");
        return draftUserStoryRepository.findDraftUserStoriesByRequirement_id(requirement_id);
    }

    public void extractUserStories(Integer requirementId) {
        Requirement requirement = requirementRepository.findById(requirementId).orElseThrow(() -> new ApiException("Requirement with id " + requirementId + " not found"));
        if (requirement == null)
            throw new ApiException("Requirement Not Found");

        try {
            String requirementJson = objectMapper.writeValueAsString(requirement);
            String prompt = """
                    You are a Business Analyst AI assistant.
                    
                    Task:
                    - Convert the following requirement object into one or more user stories.
                    - Use all available fields from the requirement (title, description, type, priority, status, source, rationale, projectId).
                    - Output ONLY a valid JSON array.
                    - The first character of the output MUST be '[' and the last character MUST be ']'.
                    - Do NOT include markdown, code fences, comments, explanations, or any text outside the JSON array.
                    - Do Not Write ```json in front of the text, Write them as Json Objects directly
                    Each JSON object must map exactly to this DTO:
                    {
                      "asA": "...",
                      "iwant": "...",
                      "soThat": "...",
                      "priority": "Must|Should|Could|Wont|High|Medium|Low",
                      "acceptanceCriteria": "...",
                      "status": "Draft|Ready|InProgress|Done|Blocked",
                      "requirementId": %d
                    }
                    
                    Requirement Object:
                    %s
                    """.formatted(requirementId, requirementJson);

            DraftUserStory draftUserStory = new DraftUserStory(null, requirementId, ai.call(prompt));
            draftUserStoryRepository.save(draftUserStory);
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed To Parse Json :" + e.getMessage());
        }
    }

    public DraftUserStory getDraftById(Integer draft_id) {
        DraftUserStory draft = draftUserStoryRepository.findDraftUserStoryById(draft_id);
        if (draft == null)
            throw new ApiException("Draft Not Found");
        return draft;
    }


    public void acceptDraft(Integer draft_id) {
        DraftUserStory draftUserStory = draftUserStoryRepository.findDraftUserStoryById(draft_id);
        if (draftUserStory == null)
            throw new ApiException("Draft Not Found");
        try {
            List<UserStoryRequestDTO> request = objectMapper.readValue(draftUserStory.getAiResponse(), new TypeReference<>() {
                    }
            );
            List<UserStory> entities = request.stream()
                    .map(dto -> {
                        Requirement requirement = requirementRepository.getRequirementById(dto.getRequirementId());
                        if (requirement == null) {
                            throw new ApiException("Requirement Not Found for ID: " + dto.getRequirementId());
                        }
                        return mapper.toEntity(dto, requirement);
                    })
                    .toList();

            userStoryRepository.saveAll(entities);
            draftUserStoryRepository.delete(draftUserStory);

        } catch (Exception e) {
            throw new ApiException("Failed to parse AI JSON " + e.getMessage());
        }
    }

    //TODO: Reject Draft And Delete it from DB By Id
    public void rejectDraft(Integer draft_id) {
        DraftUserStory draftUserStory = draftUserStoryRepository.findDraftUserStoryById(draft_id);
        if (draftUserStory == null)
            throw new ApiException("Draft Not Found");
        draftUserStoryRepository.delete(draftUserStory);
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
