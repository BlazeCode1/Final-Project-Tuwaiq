package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Repository.DocumentRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;
    private final S3Service s3;
    private final OpenAiChatModel ai;
    private final PdfService pdf;


    private static final String bucket = "https://ba-copilot-documents-generation.s3.eu-central-1.amazonaws.com/";


    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Integer id) {
        return documentRepository.findById(id).orElseThrow(() -> new ApiException("Document with id " + id + " not found"));
    }



    public void deleteDocument(Integer id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ApiException("Document with id " + id + " not found"));
        documentRepository.delete(document);
    }


    //TODO: Generating Documents like BRD.
    public void generateBRD(Integer project_id) throws IOException {


        Project project = projectRepository.findProjectById(project_id);
        if (project == null)
            throw new ApiException("Project Not found");


        String fileName = "BRD-"+project.getName() + ".pdf";

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a Business Analyst. Generate a professional Business Requirements Document (BRD) ");
        prompt.append("for the following project. Use clear structure with headings, bullet points, and numbering.\n\n");

        prompt.append("## Project Information\n");
        prompt.append("Project Name: ").append(project.getName()).append("\n");
        prompt.append("Project Description: ").append(project.getDescription()).append("\n");
        prompt.append("Status: ").append(project.getStatus()).append("\n\n");

        prompt.append("## Requirements and User Stories\n");
        int reqIndex = 1;
        for (Requirement req : project.getRequirements()) {
            prompt.append(reqIndex++).append(". Requirement: ").append(req.getTitle()).append("\n");
            prompt.append("   - Description: ").append(req.getDescription()).append("\n");
            prompt.append("   - Priority: ").append(req.getPriority()).append("\n");

            if (req.getUserStories() != null && !req.getUserStories().isEmpty()) {
                prompt.append("   - User Stories:\n");
                for (UserStory us : req.getUserStories()) {
                    prompt.append("     * US").append(us.getId()).append(": ")
                            .append(us).append("\n");
                }
            }
            prompt.append("\n");
        }

        prompt.append("Format the output as a BRD with the following sections: ");
        prompt.append("Executive Summary, Business Objectives, Scope, Stakeholders, Requirements, Risks, Acceptance Criteria.\n");

        String fullHtml = """
                <html>
                <head>
                  <meta charset="UTF-8"/>
                  <style>
                    body { font-family: Arial, sans-serif; line-height: 1.5; }
                    h1, h2, h3 { color: #2c3e50; }
                    h1 { border-bottom: 2px solid #333; padding-bottom: 5px; }
                    ul { margin-left: 20px; }
                    .section { margin-top: 20px; }
                  </style>
                </head>
                <body>
                %s
                </body>
                </html>
                """.formatted(ai.call(prompt.toString()));

        //OUTPUT FILE
       String filepath= pdf.generatePdf(fileName, fullHtml);
       s3.uploadFile(fileName,filepath);
       Document document = new Document();

       document.setTitle(fileName);
       document.setType("BRD");
       document.setProject(project);
       document.setCreatedAt(LocalDateTime.now());
       document.setContentURI(bucket + fileName);
       documentRepository.save(document);
    }


    public void generateFRD(Integer project_id) throws IOException {
        Project project = projectRepository.findProjectById(project_id);
        if (project == null)
            throw new ApiException("Project Not found");

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a Systems/Functional Analyst. Generate a professional Functional Requirements Document (FRD) ");
        prompt.append("for the following project. Use clear structure with headings, bullet points, and numbering.\n\n");

        prompt.append("## Project Information\n");
        prompt.append("Project Name: ").append(project.getName()).append("\n");
        prompt.append("Project Description: ").append(project.getDescription()).append("\n");
        prompt.append("Status: ").append(project.getStatus()).append("\n\n");

        prompt.append("## Requirements and User Stories\n");
        int reqIndex = 1;
        for (Requirement req : project.getRequirements()) {
            prompt.append(reqIndex++).append(". Requirement: ").append(req.getTitle()).append("\n");
            prompt.append("   - Description: ").append(req.getDescription()).append("\n");
            prompt.append("   - Priority: ").append(req.getPriority()).append("\n");

            if (req.getUserStories() != null && !req.getUserStories().isEmpty()) {
                prompt.append("   - User Stories:\n");
                for (UserStory us : req.getUserStories()) {
                    prompt.append("     * US").append(us.getId()).append(": ")
                            .append(us).append("\n");
                }
            }
            prompt.append("\n");
        }

        prompt.append("Format the output as an FRD with the following sections: ");
        prompt.append("System Overview, Actors & Roles, Functional Scope, ");
        prompt.append("Use Cases (with Preconditions, Main Flow, Alternate Flows, Postconditions), ");
        prompt.append("Business Rules, Data Model, External Interfaces/APIs, ");
        prompt.append("Validations & Error Handling, Acceptance Criteria, Traceability Matrix.\n");

        String fullHtml = """
            <html>
            <head>
              <meta charset="UTF-8"/>
              <style>
                body { font-family: Arial, sans-serif; line-height: 1.5; }
                h1, h2, h3 { color: #2c3e50; }
                h1 { border-bottom: 2px solid #333; padding-bottom: 5px; }
                ul { margin-left: 20px; }
                .section { margin-top: 20px; }
              </style>
            </head>
            <body>
            %s
            </body>
            </html>
            """.formatted(ai.call(prompt.toString()));

        // OUTPUT FILE
        pdf.generatePdf("FRD-" + project.getName(), fullHtml);
    }

}
