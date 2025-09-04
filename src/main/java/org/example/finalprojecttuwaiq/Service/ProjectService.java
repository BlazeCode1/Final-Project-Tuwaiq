package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BARepository baRepository;
    private final OpenAiChatModel ai;
    private final ObjectMapper objectMapper;

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

    public Map<String, Object> marketBenchmarkPreview(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project with id " + projectId + " not found"));

        int limit = 3;
        String name = project.getName();
        String desc = project.getDescription();
        //STRING
        String prompt =
                "You are a market analyst AI.\n\n" +
                        "TASK:\n" +
                        "- Identify up to " + limit + " direct competitors for the project below in Saudi Arabia (KSA).\n" +
                        "- Use web knowledge and include real websites plus at least 2 source URLs per competitor.\n" +
                        "- Return ONLY a valid JSON object (no markdown, no extra text).\n" +
                        "- The first character MUST be '{' and the last character MUST be '}'.\n\n" +
                        "JSON SHAPE:\n" +
                        "{ \"project\": { \"id\": " + project.getId() + ", \"name\": \"...\", \"description\": \"...\" }, " +
                        "\"competitors\": [{ \"name\": \"...\", \"website\": \"https://...\", \"similarity\": 0-100, " +
                        "\"reasoning\": \"...\", \"key_features\": [\"...\"], \"overlaps\": [\"...\"], \"gaps\": [\"...\"], " +
                        "\"price_tier\": \"LOW|MID|HIGH|null\", \"sources\": [\"https://...\",\"https://...\"] }], " +
                        "\"summary\": { \"gaps_union\": [\"...\"], \"differentiators_union\": [\"...\"], \"action_items\": [\"...\"] } }\n\n" +
                        "PROJECT:\n" +
                        "- Name: " + name + "\n" +
                        "- Description: " + desc + "\n";

        String raw = ai.call(prompt);
        int s = raw.indexOf('{');
        int e = raw.lastIndexOf('}');
        if (s < 0 || e <= s) throw new ApiException("Invalid AI JSON");
        String json = raw.substring(s, e + 1).trim();

        try {
            return objectMapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new ApiException("Invalid AI JSON");
        }
    }
}
