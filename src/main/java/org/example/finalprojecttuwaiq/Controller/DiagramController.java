package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.Model.Diagram;
import org.example.finalprojecttuwaiq.Service.DiagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diagrams")
@RequiredArgsConstructor
public class DiagramController {

    private final DiagramService diagramService;

    @GetMapping("/get")
    public ResponseEntity<List<Diagram>> getAllDiagrams() {
        return ResponseEntity.ok(diagramService.getAllDiagrams());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Diagram> getDiagramById(@PathVariable Integer id) {
        return ResponseEntity.ok(diagramService.getDiagramById(id));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteDiagram(@PathVariable Integer id) {
        diagramService.deleteDiagram(id);
        return ResponseEntity.ok(new ApiResponse("Diagram deleted successfully"));
    }
}
