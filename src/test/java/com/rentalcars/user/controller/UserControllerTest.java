package com.rentalcars.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.rentalcars.user.UserFixtures.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class UserControllerTest {

    private static final String USERS_URL = "/users";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Getting all users return status 'Ok'")
    void getAllUsersReturnAndStatusOk() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a user returns status 'Ok'")
    void getUserByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a user returns status 'Not Found' if id is not exist")
    void getUserByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getUserUrl(NOT_EXISTED_USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Creating a user returns status 'Ok' if it is valid")
    void createUserAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserDto())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Updating a user returns status 'Ok' if it is valid")
    void updateUserByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(put(getUserUrl(EXISTED_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserDto())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Updating a user returns status 'Not Found' if id is not existed")
    void updateUserByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(put(getUserUrl(NOT_EXISTED_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getUserDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deleting a user returns status 'Ok' if it is valid")
    void deleteUserAndReturnStatusOk() throws Exception {
        mockMvc.perform(delete(getUserUrl(EXISTED_USER_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deleting a user returns status 'Not Found' if id is not existed")
    void deleteUserByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getUserUrl(NOT_EXISTED_USER_ID)))
                .andExpect(status().isNotFound());
    }

    private String getUserUrl(Long id) {
        return String.format("/users/%s", id);
    }
}