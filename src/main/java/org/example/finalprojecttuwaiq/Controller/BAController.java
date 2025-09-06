package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.BARequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.BAService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ba")
@RequiredArgsConstructor
public class BAController {

    private final BAService baService;
    @PostMapping("/register")
    public ResponseEntity<?> registerBa(@Valid @RequestBody BARequestDTO baRequestDTO) {
        baService.registerBa(baRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("BA Registered successfully"));
    }
    @GetMapping("/projects")
    public ResponseEntity<?> getProjectsByBusinessAnalystId(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(baService.findProjectsByBaId(user.getId()));
    }


    @GetMapping("/get")
    public ResponseEntity<?> getAllBAs() {
        return ResponseEntity.ok(baService.getAllBAs());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBAById(@PathVariable Integer id) {
        return ResponseEntity.ok(baService.getBAById(id));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBA(@PathVariable Integer id, @Valid @RequestBody BARequestDTO baRequestDTO) {
        baService.updateBA(id, baRequestDTO);
        return ResponseEntity.ok(new ApiResponse("BA updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBA(@PathVariable Integer id) {
        baService.deleteBA(id);
        return ResponseEntity.ok(new ApiResponse("BA deleted successfully"));
    }

}
