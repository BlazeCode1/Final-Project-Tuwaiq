package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping("/add/{BA_ID}")
    public ResponseEntity<ApiResponse> addProject(@PathVariable Integer BA_ID,@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        projectService.addProject(BA_ID,projectRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Project added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        projectService.updateProject(id, projectRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Project updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponse("Project deleted successfully"));
    }

    @PostMapping("/{projectId}/market-benchmark")
    public ResponseEntity<?> benchmark(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectService.marketBenchmarkPreview(projectId));
    }

    @PostMapping("/assign/stakeholder/{stakeholder_id}/{project_id}")
    public ResponseEntity<?> addStakeholderToProject(@PathVariable Integer stakeholder_id,@PathVariable Integer project_id){
        projectService.addStakeholderToProject(stakeholder_id,project_id);
        return ResponseEntity.ok(new ApiResponse("Assigned Stakeholder To Project"));
    }
    @PostMapping("/assign/ba/{ba_id}/{project_id}")
    public ResponseEntity<?> addBusinessAnalystToProject(@PathVariable Integer ba_id,@PathVariable Integer project_id){
        projectService.addBusinessAnalystToProject(ba_id,project_id);
        return ResponseEntity.ok(new ApiResponse("Assigned Business Analyst To Project"));
    }
}
