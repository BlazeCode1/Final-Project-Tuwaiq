package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BARequestDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @NotBlank(message = "Password hash cannot be blank")
    private String passwordHash;


    @NotBlank(message = "Domain expertise cannot be blank")
    @Size(max = 255, message = "Domain expertise cannot exceed 255 characters")
    private String domainExpertise;
}
