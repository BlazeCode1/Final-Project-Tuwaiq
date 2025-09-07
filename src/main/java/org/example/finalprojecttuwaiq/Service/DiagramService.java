package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Diagram;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.DiagramRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagramService {

    private final DiagramRepository diagramRepository;
    private final ProjectRepository projectRepository;
    private final BARepository baRepository;
    private final OpenAiChatModel ai;
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

    public void generateClassDiagram(Integer ba_id, Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project with id " + projectId + " not found"));

        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");

        if (!ba.getIsSubscribed()){
            throw new ApiException("Unauthorized, you are not subscribed");
        }

        if (!project.getBas().contains(ba))
            throw new ApiException("Not Authorized");

        try {

            String desc = project.getDescription();
            if (desc == null || desc.isBlank()) desc = "Project Name: " + (project.getName() == null ? "" : project.getName());

            String prompt = """
            You are a Mermaid *class diagram* generator.

            Task:
            - From the following project description, produce ONE concise class diagram that captures the core domain.

            Output:
            - Output ONLY valid Mermaid code. No markdown fences, no comments, no extra text.
            - The FIRST word MUST be 'classDiagram'.

            Guidelines:
            - 3–8 core classes with PascalCase names (no spaces/special chars in identifiers).
            - Include only key fields when clearly implied (keep it brief).
            - Use relations where appropriate:
              * composition:   *--
              * aggregation:   o--
              * inheritance:   <|--
              * association:   --
            - Prefer associations if uncertain. Avoid methods unless obviously required.

            Project Description:
            %s
            """.formatted(desc);

            // Return raw Mermaid code (should start with 'classDiagram')



            Diagram diagram = new Diagram();
            diagram.setType("Class");
            diagram.setMermaidText(ai.call(prompt));
            diagram.setProject(project);
            diagramRepository.save(diagram);
        } catch (Exception e) {
            throw new ApiException("Failed to generate class diagram: " + e.getMessage());
        }
    }

    public void generateSequenceDiagram(Integer ba_id, Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project with id " + projectId + " not found"));

        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");

        if (!ba.getIsSubscribed()){
            throw new ApiException("Unauthorized, you are not subscribed");
        }

        if (!project.getBas().contains(ba))
            throw new ApiException("Not Authorized");

        try {
            // minimal source text: prefer description; fallback to name
            String desc = project.getDescription();
            if (desc == null || desc.isBlank()) desc = "Project Name: " + (project.getName() == null ? "" : project.getName());

            String prompt = """
            You are a Mermaid sequence diagram generator.

            Task:
            - From the following project description, produce ONE concise sequence diagram that captures the primary end-to-end interaction.

            Output:
            - Output ONLY valid Mermaid code. No markdown fences, no comments, no extra text.
            - The FIRST word MUST be 'sequenceDiagram'.

            Guidelines:
            - Declare 3–6 participants with short readable names (e.g., User, WebApp, API, Auth, DB, Payment).
            - Begin with explicit participant lines (e.g., `participant U as User`).
            - Show 6–12 messages max using `->>` for requests and `-->>` for responses.
            - Prefer clear action labels (e.g., POST /login, Validate token, Fetch records).
            - Include ONE `alt` or `opt` block if a failure/alternative path is implied.
            - Keep it technology-agnostic unless the description specifies otherwise.

            Project Description:
            %s
            """.formatted(desc);

            // Return raw Mermaid code (should start with 'sequenceDiagram')

            Diagram diagram = new Diagram();
            diagram.setType("Class");
            diagram.setMermaidText(ai.call(prompt));
            diagram.setProject(project);
            diagramRepository.save(diagram);
        } catch (Exception e) {
            throw new ApiException("Failed to generate sequence diagram: " + e.getMessage());
        }
    }

    public void generateErDiagram(Integer ba_id, Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project with id " + projectId + " not found"));

        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");

        if (!ba.getIsSubscribed()){
            throw new ApiException("Unauthorized, you are not subscribed");
        }

        if (!project.getBas().contains(ba))
            throw new ApiException("Not Authorized");


        try {
            String desc = project.getDescription();
            if (desc == null || desc.isBlank()) {
                desc = "Project Name: " + (project.getName() == null ? "" : project.getName());
            }

            String prompt = """
            You are a Mermaid ER diagram generator.

            Task:
            - From the following project description, produce ONE concise entity-relationship diagram capturing the core data model.

            Output:
            - Output ONLY valid Mermaid code. No markdown fences, no comments, no extra text.
            - The FIRST word MUST be 'erDiagram'.

            Guidelines:
            - Define 3–8 entities with PascalCase identifiers (no spaces/special chars).
            - Use relationships with proper cardinalities:
              * one-to-one:      ||--||
              * one-to-many:     ||--|{
              * zero-to-many:    |o--o{  (use o for optional)
            - Prefer clear relationship labels (`: owns`, `: contains`, `: references`).
            - For each entity, list a few key attributes in a block:
              EntityName {
                <type> id PK
                <type> <field>
                <type> <field> FK   // when applicable
              }
            - Keep it brief and domain-accurate; omit uncertain details.

            Project Description:
            %s
            """.formatted(desc);

            // Return raw Mermaid code (should start with 'erDiagram')

            Diagram diagram = new Diagram();
            diagram.setType("ERD");
            diagram.setMermaidText(ai.call(prompt));
            diagram.setProject(project);
            diagramRepository.save(diagram);
        } catch (Exception e) {
            throw new ApiException("Failed to generate ER diagram: " + e.getMessage());
        }
    }

}
