package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.DocumentRequestDTO;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Repository.DocumentRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Integer id) {
        return documentRepository.findById(id).orElseThrow(() -> new ApiException("Document with id " + id + " not found"));
    }

    public void addDocument(DocumentRequestDTO documentRequestDTO) {
        Project project = projectRepository.findById(documentRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + documentRequestDTO.getProjectId() + " not found"));

        Document document = new Document();
        document.setType(documentRequestDTO.getType());
        document.setTitle(documentRequestDTO.getTitle());
        document.setContentURI(documentRequestDTO.getContentURI());
        document.setVersion(documentRequestDTO.getVersion());
        document.setLastUpdated(LocalDateTime.now());
        document.setProject(project);
        documentRepository.save(document);
    }

    public void updateDocument(Integer id, DocumentRequestDTO documentRequestDTO) {
        Document existingDocument = documentRepository.findById(id).orElseThrow(() -> new ApiException("Document with id " + id + " not found"));
        Project project = projectRepository.findById(documentRequestDTO.getProjectId())
                .orElseThrow(() -> new ApiException("Project with ID " + documentRequestDTO.getProjectId() + " not found"));

        existingDocument.setType(documentRequestDTO.getType());
        existingDocument.setTitle(documentRequestDTO.getTitle());
        existingDocument.setContentURI(documentRequestDTO.getContentURI());
        existingDocument.setVersion(documentRequestDTO.getVersion());
        existingDocument.setLastUpdated(LocalDateTime.now());
        existingDocument.setProject(project);
        documentRepository.save(existingDocument);
    }

    public void deleteDocument(Integer id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ApiException("Document with id " + id + " not found"));
        documentRepository.delete(document);
    }
}
