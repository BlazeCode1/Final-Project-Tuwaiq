package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@AuthenticationPrincipal User user, @PathVariable Integer id, @Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        projectService.updateProject(user.getId(), id, projectRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Project updated successfully"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@AuthenticationPrincipal User user, @Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        projectService.addProject(user.getId(), projectRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Project added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        projectService.deleteProject(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse("Project deleted successfully"));
    }

    @PostMapping("/{projectId}/market-benchmark")
    public ResponseEntity<?> benchmark(@AuthenticationPrincipal User user, @PathVariable Integer projectId) {
        return ResponseEntity.ok(projectService.marketBenchmarkPreview(user.getId(), projectId));
    }

    @PostMapping("/assign/stakeholder/{stakeholder_id}/{project_id}")
    public ResponseEntity<?> addStakeholderToProject(@AuthenticationPrincipal User user, @PathVariable Integer stakeholder_id, @PathVariable Integer project_id) {
        projectService.addStakeholderToProject(user.getId(), stakeholder_id, project_id);
        return ResponseEntity.ok(new ApiResponse("Assigned Stakeholder To Project"));
    }

    @PostMapping("/assign/ba/{ba_id}/{project_id}")
    public ResponseEntity<?> addBusinessAnalystToProject(@AuthenticationPrincipal User user, @PathVariable Integer ba_id, @PathVariable Integer project_id) {
        projectService.addBusinessAnalystToProject(user.getId(), ba_id, project_id);
        return ResponseEntity.ok(new ApiResponse("Assigned Business Analyst To Project"));
    }

    @PutMapping("/exit/ba/{project_id}")
    public ResponseEntity<?> exitProjectGroupForBA(@AuthenticationPrincipal User user, @PathVariable Integer project_id) {
        projectService.exitProjectGroupForBA(user.getId(), project_id);
        return ResponseEntity.ok(new ApiResponse("you left the project successfully"));
    }

    @PutMapping("/exit/stakeholder/{project_id}")
    public ResponseEntity<?> exitProjectGroupForStakeHolder(@AuthenticationPrincipal User user, @PathVariable Integer project_id) {
        projectService.exitProjectGroupForStakeHolder(user.getId(), project_id);
        return ResponseEntity.ok(new ApiResponse("you left the project successfully"));
    }

    @PutMapping("/reassign/owner/{project_id}/{nextOwnerId}")
    public ResponseEntity<?> reassignProjectOwner(@AuthenticationPrincipal User user,
                                                  @PathVariable Integer project_id,
                                                  @PathVariable Integer nextOwnerId) {
        projectService.reassignProjectOwner(user.getId(), project_id, nextOwnerId);
        return ResponseEntity.ok(new ApiResponse("project owner reassigned successfully"));
    }

    @PostMapping("/recommend-tools/{projectId}")
    public ResponseEntity<?> recommendTools(@AuthenticationPrincipal User user, @PathVariable Integer projectId) {
        return ResponseEntity.ok().body(projectService.recommendTools(user.getId(), projectId));
    }
}
