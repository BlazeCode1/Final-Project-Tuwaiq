package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.DocumentRequestDTO;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/get")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addDocument(@Valid @RequestBody DocumentRequestDTO documentRequestDTO) {
        documentService.addDocument(documentRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Document added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateDocument(@PathVariable Integer id, @Valid @RequestBody DocumentRequestDTO documentRequestDTO) {
        documentService.updateDocument(id, documentRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Document updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteDocument(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(new ApiResponse("Document deleted successfully"));
    }

    @PostMapping("/generate/brd/{project_id}")
    public ResponseEntity<?> generateBRD(@PathVariable Integer project_id) throws IOException {
        documentService.generateBRD(project_id);
        return ResponseEntity.ok(new ApiResponse("Generated BRD Successfully"));
    }
}
