package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/generate/brd/{project_id}")
    public ResponseEntity<?> generateBRD(@AuthenticationPrincipal User user,@PathVariable Integer project_id) throws IOException {
        documentService.generateBRD(user.getId(), project_id);
        return ResponseEntity.ok(new ApiResponse("Generated BRD Successfully"));
    }


    @PostMapping("/generate/frd/{project_id}")
    public ResponseEntity<?> generateFRD(@AuthenticationPrincipal User user, @PathVariable Integer project_id) throws IOException {
        documentService.generateFRD(user.getId(),project_id);
        return ResponseEntity.ok(new ApiResponse("Generated FRD Successfully"));
    }

    @PostMapping("/generate/feasibility/{project_id}")
    public ResponseEntity<?> generateFeasibilityStudy(@AuthenticationPrincipal User user, @PathVariable Integer project_id) throws IOException {
        documentService.generateFeasibilityStudy(user.getId(),project_id);
        return ResponseEntity.ok(new ApiResponse("Generated feasibility study Successfully"));
    }


    @GetMapping("/get")
    public ResponseEntity<?> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(new ApiResponse("Document deleted successfully"));
    }


}
