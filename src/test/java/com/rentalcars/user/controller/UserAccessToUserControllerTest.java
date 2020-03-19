package com.rentalcars.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.rentalcars.user.UserFixtures.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserAccessToUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public static final String USERS_URL = "/users/me";

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Getting an actual logged-in user returns status 'Ok' if it is valid")
    void getActualLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @DisplayName("Getting an actual logged-in user by not logged-in user returns status 'Unauthorized'")
    void getActualLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Updating an actual logged-in user returns status 'Created' if it is valid")
    void updateActualLoggedInUserReturnStatusCreated() throws Exception {
        mockMvc.perform(put(USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUpdateUserInput())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Updating a user by not logged-in user returns status 'Unauthorized'")
    void updateUserByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(put(USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUpdateUserInput())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Deleting a user by logged-in user returns status 'No Content' if it is valid")
    void deleteUserByLoggedInUserReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete(USERS_URL))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deleting a user by not logged-in user returns status 'Unauthorized'")
    void deleteUserWithExistedIdByLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(delete(USERS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Creating a user by not logged-in user returns status 'Created' if it is valid")
    void createUserByNotLoggedInUserAndReturnStatusOk() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserInput())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Creating a user by logged-in user returns status 'Forbidden'")
    void createUserByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserInput())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Creating a user by ADMIN returns status 'Forbidden'")
    void createUserByAdminAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserInput())))
                .andExpect(status().isForbidden());
    }
}