package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
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


    @PostMapping("/generate/frd/{project_id}")
    public ResponseEntity<?> generateFRD(@PathVariable Integer project_id) throws IOException {
        documentService.generateFRD(project_id);
        return ResponseEntity.ok(new ApiResponse("Generated FRD Successfully"));
    }
}
