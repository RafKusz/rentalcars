package com.rentalcars.user.controller;

import org.hamcrest.Matchers;
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

import static com.rentalcars.user.UserFixtures.EXISTED_USER_ID;
import static com.rentalcars.user.UserFixtures.NOT_EXISTED_USER_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AdminAccessToUserControllerTest {

    private static final String USERS_URL = "/users";

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting all users by ADMIN returns status 'Ok'")
    void getAllUsersByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(3)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting all users by logged-in user returns status 'Forbidden'")
    void getAllUsersByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting all users by not logged-in user returns status 'Unauthorized'")
    void getAllUsersByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a user with existed id by ADMIN returns status 'Ok'")
    void getUserWithExistedIdByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_USER_ID));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting a user with existed id by logged-in user returns status 'Forbidden'")
    void getUserWithExistedIdByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting a user with existed id by not logged-in user returns status 'Unauthorized'")
    void getUserWithExistedIdByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a user by ADMIN returns status 'Not Found' if id is not exist")
    void getUserWithNotExistedIdByAdminAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getUserUrl(NOT_EXISTED_USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a user by ADMIN returns status 'No Content' if it is valid")
    void deleteUserByAdminAndReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Deleting a contract with existed id by logged-in user returns status 'Forbidden'")
    void deleteUserByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(delete(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deleting a contract with existed id by not logged-in user returns status 'Unauthorized'")
    void deleteUserByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(delete(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a user by ADMIN returns status 'Not Found' if id is not existed")
    void deleteUserWithNotExistedIdByAdminAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getUserUrl(NOT_EXISTED_USER_ID)))
                .andExpect(status().isNotFound());
    }

    private String getUserUrl(Long id) {
        return String.format("/users/%s", id);
    }
}