package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.BARequestDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BAService {

    private final BARepository baRepository;
    private final UserRepository userRepository;

    public List<BA> getAllBAs() {
        return baRepository.findAll();
    }

    public BA getBAById(Integer id) {
        return baRepository.findById(id).orElseThrow(() -> new ApiException("BA with id " + id + " not found"));
    }

    public void registerBa(BARequestDTO baRequestDTO) {
        User user = new User();
        user.setName(baRequestDTO.getName());
        user.setUsername(baRequestDTO.getUsername());
        user.setEmail(baRequestDTO.getEmail());
        user.setPhone(baRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(baRequestDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("BA");

        BA ba = new BA();
        ba.setDomainExpertise(baRequestDTO.getDomainExpertise());
        ba.setUser(user);
        user.setBa(ba);
        userRepository.save(user);
        baRepository.save(ba);
    }

    public void updateBA(Integer id, BARequestDTO baRequestDTO) {
        BA existingBA = baRepository.findById(id).orElseThrow(() -> new ApiException("BA with id " + id + " not found"));
        User user = existingBA.getUser();
        if (user == null) {
            throw new ApiException("Associated user not found for BA with id " + id);
        }
        user.setName(baRequestDTO.getName());
        user.setEmail(baRequestDTO.getEmail());
        user.setPhone(baRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(baRequestDTO.getPassword()));
        userRepository.save(user);

        existingBA.setDomainExpertise(baRequestDTO.getDomainExpertise());
        baRepository.save(existingBA);
    }

    public void deleteBA(Integer id) {
        BA ba = baRepository.findById(id).orElseThrow(() -> new ApiException("BA with id " + id + " not found"));
        userRepository.delete(ba.getUser()); // Delete associated user
        baRepository.delete(ba);
    }

    public List<Project> findProjectsByBaId(Integer ba_id){
        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");
        if (!ba.getUser().getRole().equalsIgnoreCase("BA"))
            throw new ApiException("Unauthorized");
        return baRepository.findProjectsByBaID(ba_id);
    }
}
