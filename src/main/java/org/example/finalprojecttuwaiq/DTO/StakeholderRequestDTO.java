package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  StakeholderRequestDTO {
    @NotEmpty(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    @NotEmpty(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be blank")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @NotEmpty(message = "Password hash cannot be blank")
    private String password;



    @NotEmpty(message = "Organization cannot be blank")
    @Size(max = 100, message = "Organization cannot exceed 100 characters")
    private String organization;

    @NotEmpty(message = "Position cannot be empty")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    @Pattern(regexp = "^(Sponsor|Reviewer)$")
    private String role_in_project;

}
