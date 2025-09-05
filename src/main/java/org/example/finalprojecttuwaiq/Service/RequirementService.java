package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Model.DraftRequirement;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Repository.DraftRequirementRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.RequirementRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;
    private final OpenAiChatModel ai;
    private final ObjectMapper objectMapper;
    private final RequirementMapper mapper;
    private final DraftRequirementRepository draftRequirementRepository;

    public List<Requirement> getAllRequirements() {
        return requirementRepository.findAll();
    }

    public Requirement getRequirementById(Integer id) {
        return requirementRepository.findById(id).orElseThrow(() -> new ApiException("Requirement with id " + id + " not found"));
    }

    //    public void addRequirement(RequirementRequestDTO requirementRequestDTO) {
//        Project project = projectRepository.findById(requirementRequestDTO.getProjectId())
//                .orElseThrow(() -> new ApiException("Project with ID " + requirementRequestDTO.getProjectId() + " not found"));
//
//        Requirement requirement = new Requirement();
//        requirement.setTitle(requirementRequestDTO.getTitle());
//        requirement.setDescription(requirementRequestDTO.getDescription());
//        requirement.setType(requirementRequestDTO.getType());
//        requirement.setPriority(requirementRequestDTO.getPriority());
//        requirement.setStatus(requirementRequestDTO.getStatus());
//        requirement.setSource(requirementRequestDTO.getSource());
//        requirement.setRationale(requirementRequestDTO.getRationale());
//        // Generate traceId using current time in milliseconds
//        requirement.setTraceId(String.valueOf(System.currentTimeMillis()));
//        requirement.setProject(project);
//        requirementRepository.save(requirement);
//    }
    public void extractRequirements(Integer projectId) {
        Project project = projectRepository.findProjectById(projectId);

        if (project == null)
            throw new ApiException("Project Not Found");


        String prompt = """
                You are a Business Analyst AI assistant.
                
                Task:
                - Analyze the following project description.
                - Extract requirements and output ONLY a valid JSON array (no text, no markdown, no explanations).
                - Each JSON object must map exactly to this DTO:
                
                {
                  "title": "...",
                  "description": "...",
                  "type": "Business|Functional|NonFunctional|Regulatory|Constraint",
                  "priority": "Must|Should|Could|Wont",
                  "status": "Draft|Analyzed|Validated|Approved|Deprecated",
                  "source": "...",
                  "rationale": "...",
                  "projectId": %d
                }
                
                Rules:
                - The output must be a single JSON array, e.g. [ { ... }, { ... } ].
                - Do NOT wrap the response in ``` or any other formatting.
                - Do NOT add comments or explanations.
                
                Project Description:
                %s
                """.formatted(projectId, project.getDescription());
         DraftRequirement draftRequirement = new DraftRequirement(null,projectId,ai.call(prompt));
         draftRequirementRepository.save(draftRequirement);
    }

    //TODO: Accept Draft And Map it to requirements By Id
    public void acceptDraft(Integer draft_id){
        DraftRequirement draftRequirement = draftRequirementRepository.findDraftRequirementById(draft_id);

        if(draftRequirement == null)
            throw new ApiException("Draft Not Found");
        Project projectMain = projectRepository.findProjectById(draftRequirement.getProject_id());
        if(projectMain == null)
            throw new ApiException("Project Not found");
        try {
        List<RequirementRequestDTO> request = objectMapper.readValue(draftRequirement.getAiResponse(),new TypeReference<>() {}
        );
            List<Requirement> entities = request.stream()
                    .map(dto -> {
                        Project project = projectRepository.findProjectById(dto.getProjectId());
                        if (project == null) {
                            throw new ApiException("Project Not Found for ID: " + dto.getProjectId());
                        }
                        return mapper.toEntity(dto, project);
                    })
                    .toList();

            requirementRepository.saveAll(entities);
            projectMain.setStatus("Analysis");
            projectRepository.save(projectMain);
        }catch (Exception e){
            throw new ApiException("Failed to parse AI JSON " + e.getMessage());
        }
    }



    public List<Requirement> getRequirementsByProjectId(Integer project_id) {

        Project project = projectRepository.findProjectById(project_id);
        if(project == null)
            throw new ApiException("Project not found");
    return requirementRepository.getRequirementsByProject(project);
    }


    public void updateRequirement(Integer id, RequirementRequestDTO requirementRequestDTO) {
        Requirement existingRequirement = requirementRepository.findById(id).orElseThrow(() -> new ApiException("Requirement with id " + id + " not found"));
        Project project = projectRepository.findById(requirementRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + requirementRequestDTO.getProjectId() + " not found"));

        existingRequirement.setTitle(requirementRequestDTO.getTitle());
        existingRequirement.setDescription(requirementRequestDTO.getDescription());
        existingRequirement.setType(requirementRequestDTO.getType());
        existingRequirement.setPriority(requirementRequestDTO.getPriority());
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
