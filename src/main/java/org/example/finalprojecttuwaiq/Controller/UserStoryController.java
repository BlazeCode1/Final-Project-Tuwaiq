package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.UserStoryRequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Service.UserStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @GetMapping("/get")
    public ResponseEntity<List<UserStory>> getAllUserStories() {
        return ResponseEntity.ok(userStoryService.getAllUserStories());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserStory> getUserStoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(userStoryService.getUserStoryById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserStory(@AuthenticationPrincipal User user, @Valid @RequestBody UserStoryRequestDTO userStoryRequestDTO) {
        userStoryService.addUserStory(user.getId(), userStoryRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("UserStory added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserStory(@AuthenticationPrincipal User user,@PathVariable Integer id, @Valid @RequestBody UserStoryRequestDTO userStoryRequestDTO) {
        userStoryService.updateUserStory(user.getId(), id, userStoryRequestDTO);
        return ResponseEntity.ok(new ApiResponse("UserStory updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserStory(@AuthenticationPrincipal User user,@PathVariable Integer id) {
        userStoryService.deleteUserStory(user.getId(),id);
        return ResponseEntity.ok(new ApiResponse("UserStory deleted successfully"));
    }
    @PostMapping("/generate/{requirement_id}")
    public ResponseEntity<?> generateUserStories(@AuthenticationPrincipal User user,@PathVariable Integer requirement_id){
        userStoryService.extractUserStoryByRequirement(user.getId(),requirement_id);
        return ResponseEntity.ok(new ApiResponse("User Stories Generated!"));
    }

    @GetMapping("/draft/requirement/{requirement_id}")
    public ResponseEntity<?> getAllDraftUserStoriesWithRequirementId(@AuthenticationPrincipal User user,@PathVariable Integer requirement_id){
        return ResponseEntity.ok(userStoryService.getAllDraftsByRequirementId(user.getId(), requirement_id));
    }
    @GetMapping("/draft/get/{draft_id}")
    public ResponseEntity<?> getDraftById(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        return ResponseEntity.ok(userStoryService.getDraftById(user.getId(),draft_id));
    }
    @PostMapping("/draft/accept/{draft_id}")
    public ResponseEntity<?> acceptDraft(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        userStoryService.acceptDraft(user.getId(), draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft accepted!"));
    }
    @DeleteMapping("/draft/reject/{draft_id}")
    public ResponseEntity<?> rejectDraft(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        userStoryService.rejectDraft(user.getId(),draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft rejected!"));
    }

//
//    @PostMapping("/generate-by-project/{project_id}")
//    public ResponseEntity<?> generateUserStoriesForProject(@AuthenticationPrincipal @PathVariable Integer project_id){
//        userStoryService.extractUserStoriesForProject(project_id);
//        return ResponseEntity.ok(new ApiResponse("User Stories Generated!"));
//    }
}
