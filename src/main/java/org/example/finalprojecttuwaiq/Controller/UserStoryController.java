package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.UserStoryRequestDTO;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Service.UserStoryService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> addUserStory(@Valid @RequestBody UserStoryRequestDTO userStoryRequestDTO) {
        userStoryService.addUserStory(userStoryRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("UserStory added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUserStory(@PathVariable Integer id, @Valid @RequestBody UserStoryRequestDTO userStoryRequestDTO) {
        userStoryService.updateUserStory(id, userStoryRequestDTO);
        return ResponseEntity.ok(new ApiResponse("UserStory updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserStory(@PathVariable Integer id) {
        userStoryService.deleteUserStory(id);
        return ResponseEntity.ok(new ApiResponse("UserStory deleted successfully"));
    }
    @PostMapping("/generate/{requirement_id}")
    public ResponseEntity<?> generateUserStories(@PathVariable Integer requirement_id){
        userStoryService.extractUserStories(requirement_id);
        return ResponseEntity.ok(new ApiResponse("User Stories Generated!"));
    }

    @GetMapping("/draft/requirement/{requirement_id}")
    public ResponseEntity<?> getAllDraftUserStoriesWithRequirementId(@PathVariable Integer requirement_id){
        return ResponseEntity.ok(userStoryService.getAllDraftsByRequirementId(requirement_id));
    }
    @GetMapping("/draft/get/{draft_id}")
    public ResponseEntity<?> getDraftById(@PathVariable Integer draft_id){
        return ResponseEntity.ok(userStoryService.getDraftById(draft_id));
    }
    @PostMapping("/draft/accept/{draft_id}")
    public ResponseEntity<?> acceptDraft(@PathVariable Integer draft_id){
        userStoryService.acceptDraft(draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft accepted!"));
    }
    @DeleteMapping("/draft/reject/{draft_id}")
    public ResponseEntity<?> rejectDraft(@PathVariable Integer draft_id){
        userStoryService.rejectDraft(draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft rejected!"));
    }
}
