package org.example.finalprojecttuwaiq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.finalprojecttuwaiq.Config.JwtAuthenticationFilter;
import org.example.finalprojecttuwaiq.Controller.UserController;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.JwtService;
import org.example.finalprojecttuwaiq.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User ali;
    private User faisal;

    @BeforeEach
    void setUp() {
        ali = new User(
                1,
                "Ali Abumansor",
                "ali.abumansor",
                "ali@gmail.com",
                "0501234567",
                "AliStrong#2024",
                "ADMIN",
                null, null, null, null
        );

        faisal = new User(
                2,
                "Faisal Almansour",
                "faisal.almansour",
                "faisal@gmail.com",
                "0557654321",
                "FaisalPass!99",
                "USER",
                null, null, null, null
        );
    }

    @Test
    void getAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(ali, faisal));

        mockMvc.perform(get("/api/v1/users/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("ali.abumansor"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("faisal.almansour"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_returnsAli() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(ali);

        mockMvc.perform(get("/api/v1/users/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ali Abumansor"))
                .andExpect(jsonPath("$.email").value("ali@gmail.com"));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void userRegisterTest() throws Exception {
        doNothing().when(userService).registerAdmin(any());

        String reqJson = """
          {
            "name": "Faisal Almansour",
            "username": "faisal.almansour",
            "email": "faisal@gmail.com",
            "phone": "0557654321",
            "password": "FaisalPass!99",
            "role": "ADMIN"
          }
          """;

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User added successfully"));

        verify(userService, times(1)).registerAdmin(any());
    }

    @Test
    void UserUpdateTest() throws Exception {
        doNothing().when(userService).updateUser(eq(1), any());

        String reqJson = """
          {
            "name": "Ali Abumansor",
            "username": "ali.abumansor",
            "email": "ali@gmail.com",
            "phone": "0509998888",
            "password": "AliStrong#2025",
            "role": "ADMIN"
          }
          """;

        mockMvc.perform(put("/api/v1/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"));

        verify(userService, times(1)).updateUser(eq(1), any());
    }

    @Test
    void UserDeleteTest() throws Exception {
        doNothing().when(userService).deleteUser(2);

        mockMvc.perform(delete("/api/v1/users/delete/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService, times(1)).deleteUser(2);
    }

    @Test
    void userRegister() throws Exception {
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
