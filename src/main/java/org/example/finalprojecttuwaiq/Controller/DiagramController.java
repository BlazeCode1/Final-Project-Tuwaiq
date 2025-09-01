package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.DiagramRequestDTO;
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

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addDiagram(@Valid @RequestBody DiagramRequestDTO diagramRequestDTO) {
        diagramService.addDiagram(diagramRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Diagram added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateDiagram(@PathVariable Integer id, @Valid @RequestBody DiagramRequestDTO diagramRequestDTO) {
        diagramService.updateDiagram(id, diagramRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Diagram updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteDiagram(@PathVariable Integer id) {
        diagramService.deleteDiagram(id);
        return ResponseEntity.ok(new ApiResponse("Diagram deleted successfully"));
    }
}
