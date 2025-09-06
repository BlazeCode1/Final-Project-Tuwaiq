package org.example.finalprojecttuwaiq.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.StakeholderRepository;
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
    private final StakeholderRepository stakeholderRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElseThrow(() -> new ApiException("Project with id " + id + " not found"));
    }

    public void addProject(Integer ba_id, ProjectRequestDTO projectRequestDTO) {
        BA businessAnalyst = baRepository.findBAById(ba_id);
        if (businessAnalyst == null)
            throw new ApiException("Business Analyst Not Found");
        if (!businessAnalyst.getUser().getRole().equalsIgnoreCase("BA"))
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
    public void addStakeholderToProject(Integer stakeholder_id,Integer project_id){
        Stakeholder stakeholder = stakeholderRepository.findStakeholderById(stakeholder_id);
        if (stakeholder == null)
            throw new ApiException("Stakeholder Not Found");
        Project project = projectRepository.findProjectById(project_id);
        if (project == null)
            throw new ApiException("Project Not Found");

        project.getStakeholders().add(stakeholder);
        stakeholder.getProjects().add(project);
        projectRepository.save(project);
        stakeholderRepository.save(stakeholder);
    }
    public void addBusinessAnalystToProject(Integer ba_id,Integer project_id){
        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA Not Found");
        Project project = projectRepository.findProjectById(project_id);
        if (project == null)
            throw new ApiException("Project Not Found");

        project.getBas().add(ba);
        ba.getProjects().add(project);
        projectRepository.save(project);
        baRepository.save(ba);
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


        String prompt = """
                You are a market analyst AI.
                
                TASK:
                - Identify up to %d direct competitors for the project below in Saudi Arabia (KSA).
                - Use web knowledge and include real websites plus at least 2 source URLs per competitor.
                - Return ONLY a valid JSON object (no markdown, no extra text).
                - The first character MUST be '{' and the last character MUST be '}'.
                
                JSON SHAPE:
                { 
                  "project": { 
                    "id": %d, 
                    "name": "...", 
                    "description": "..." 
                  }, 
                  "competitors": [ 
                    { 
                      "name": "...", 
                      "website": "https://...", 
                      "similarity": 0-100, 
                      "reasoning": "...", 
                      "key_features": ["..."], 
                      "overlaps": ["..."], 
                      "gaps": ["..."], 
                      "price_tier": "LOW|MID|HIGH|null", 
                      "sources": ["https://...","https://..."] 
                    } 
                  ], 
                  "summary": { 
                    "gaps_union": ["..."], 
                    "differentiators_union": ["..."], 
                    "action_items": ["..."] 
                  } 
                }
                
                PROJECT:
                - Name: %s
                - Description: %s
                """.formatted(3, project.getId(), project.getName(), project.getDescription());



        String raw = ai.call(prompt);
        int s = raw.indexOf('{');
        int e = raw.lastIndexOf('}');
        if (s < 0 || e <= s) throw new ApiException("Invalid AI JSON");
        String json = raw.substring(s, e + 1).trim();

        try {
            return objectMapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new ApiException("Invalid AI JSON");
        }
    }

    public Map<String, Object> recommendTools(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project with id " + projectId + " not found"));

        try {
            String projectJson = objectMapper.writeValueAsString(project);

            String prompt = """
            You are a pragmatic software stack recommender.

            Task:
            - Based on the following project object, return ONE JSON object with recommended tools/stack to implement it.
            - Output ONLY a valid JSON object. No markdown, no code fences, no comments, no extra text.
            - The FIRST character MUST be '{' and the LAST character MUST be '}'.

            Keep keys concise (omit sections you cannot infer):
            {
              "name": string,
              "assumptions": [string],
              "target": { "platform": "web"|"mobile"|"backend", "scale": "small"|"medium"|"large" },
              "architecture": "monolith"|"modular monolith"|"microservices",
              "frontend": { "framework": string, "ui": [string], "state": [string], "forms": [string], "build": [string], "testing": [string] },
              "backend": { "language": string, "framework": [string], "api": ["REST"|"GraphQL"], "realtime": [string], "testing": [string] },
              "database": { "primary": [string], "search": [string], "cache": [string], "analytics": [string] },
              "infra": { "cloud": [string], "runtime": [string], "container": [string], "orchestration": [string], "IaC": [string], "cicd": [string] },
              "observability": { "logging": [string], "metrics": [string], "tracing": [string] },
              "security": { "auth": [string], "secrets": [string], "vuln_scan": [string] },
              "ai": { "models": [string], "vector_db": [string], "embedding_model": [string], "orchestration": [string] },
              "notes": [string]
            }

            Project Object:
            %s
            """.formatted(projectJson);

            String raw = ai.call(prompt);
            return objectMapper.readValue(raw, new TypeReference<Map<String, Object>>() {});
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new ApiException("Failed to parse JSON: " + e.getMessage());
        }
    }

}
