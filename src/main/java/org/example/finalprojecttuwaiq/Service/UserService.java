package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.UserRequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ApiException("User with id " + id + " not found"));
    }

    public void addUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone(userRequestDTO.getPhone());
        user.setPasswordHash(userRequestDTO.getPasswordHash());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updateUser(Integer id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ApiException("User with id " + id + " not found"));
        existingUser.setName(userRequestDTO.getName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPhone(userRequestDTO.getPhone());
        existingUser.setPasswordHash(userRequestDTO.getPasswordHash());
        userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException("User with id " + id + " not found"));
        userRepository.delete(user);
    }
}
