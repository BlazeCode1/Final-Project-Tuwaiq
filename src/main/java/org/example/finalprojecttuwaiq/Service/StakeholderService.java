package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.StakeholderRequestDTO;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.StakeholderRepository;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;
    private final UserRepository userRepository;

    public List<Stakeholder> getAllStakeholders() {
        return stakeholderRepository.findAll();
    }

    public Stakeholder getStakeholderById(Integer id) {
        return stakeholderRepository.findById(id).orElseThrow(() -> new ApiException("Stakeholder with id " + id + " not found"));
    }

    public void addStakeholder(StakeholderRequestDTO stakeholderRequestDTO) {
        User user = new User();
        user.setName(stakeholderRequestDTO.getName());
        user.setEmail(stakeholderRequestDTO.getEmail());
        user.setPhone(stakeholderRequestDTO.getPhone());
        user.setPasswordHash(stakeholderRequestDTO.getPasswordHash());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("STAKEHOLDER");
        Stakeholder stakeholder = new Stakeholder();
        stakeholder.setOrganization(stakeholderRequestDTO.getOrganization());
        stakeholder.setRole_in_project(stakeholderRequestDTO.getRole_in_project());
        stakeholder.setUser(user);
        user.setStakeholder(stakeholder);
        userRepository.save(user);
        stakeholderRepository.save(stakeholder);
    }

    public void updateStakeholder(Integer id, StakeholderRequestDTO stakeholderRequestDTO) {
        Stakeholder existingStakeholder = stakeholderRepository.findById(id).orElseThrow(() -> new ApiException("Stakeholder with id " + id + " not found"));
        User user = existingStakeholder.getUser();
        if (user == null) {
            throw new ApiException("Associated user not found for stakeholder with id " + id);
        }
        user.setName(stakeholderRequestDTO.getName());
        user.setEmail(stakeholderRequestDTO.getEmail());
        user.setPhone(stakeholderRequestDTO.getPhone());
        user.setPasswordHash(stakeholderRequestDTO.getPasswordHash());
        userRepository.save(user);

        existingStakeholder.setOrganization(stakeholderRequestDTO.getOrganization());
        existingStakeholder.setRole_in_project(stakeholderRequestDTO.getRole_in_project());
        stakeholderRepository.save(existingStakeholder);
    }

    public void deleteStakeholder(Integer id) {
        Stakeholder stakeholder = stakeholderRepository.findById(id).orElseThrow(() -> new ApiException("Stakeholder with id " + id + " not found"));
        userRepository.delete(stakeholder.getUser()); // Delete associated user
        stakeholderRepository.delete(stakeholder);
    }
}
