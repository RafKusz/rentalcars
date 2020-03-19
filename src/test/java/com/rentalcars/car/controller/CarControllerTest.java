package com.rentalcars.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.rentalcars.car.CarFixtures.*;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RENT;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RETURN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class CarControllerTest {

    private static final String CARS_URL = "/cars";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting all cars by 'ADMIN' returns status 'Ok'")
    void getCarsByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CARS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting all cars by logged-in user returns status 'Forbidden'")
    void getCarsByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(CARS_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting all cars by not logged-in user returns status 'Unauthorized'")
    void getCarsByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(CARS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Getting all available cars by not logged-in user returns status 'Ok'")
    void getAvailableCarsByNotLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(getAvailableCarsUrl()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting all available cars by logged-in user returns status 'Ok'")
    void getAvailableCarsByLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(getAvailableCarsUrl()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting all available cars by 'ADMIN' returns status 'Ok'")
    void getAvailableCarsByAdminReturnStatusOk() throws Exception {
        mockMvc.perform(get(getAvailableCarsUrl()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Getting a car with existed id by not logged-in user returns status 'Ok'")
    void getCarWithExistedIdByNotLoggedInUserAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_CAR_ID));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting a car with existed id by logged-in user returns status 'Ok'")
    void getCarWithExistedIdByLoggedInUserAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_CAR_ID));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a car with existed id by 'ADMIN' returns status 'Ok'")
    void getCarWithExistedIdByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_CAR_ID));
    }

    @Test
    @DisplayName("Getting a car with not existed id returns status 'Not Found' if id is not exist")
    void getCarWithNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getCarUrl(NOT_EXISTED_CAR_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Creating a car by 'ADMIN' returns status 'Created' if it is valid")
    void createCarAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Creating a car by logged-in user returns status 'Forbidden'")
    void createCarAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Creating a car by not logged-in user returns status 'Unauthorized'")
    void createCarByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Updating a car by 'ADMIN' returns status 'Created' if it is valid")
    void updateCarWithExistedIdAndReturnStatusCreated() throws Exception {
        mockMvc.perform(put(getCarUrl(EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Updating a car by logged-in user returns status 'Forbidden'")
    void updateCarWithExistedIdAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(put(getCarUrl(EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Updating a car by not logged-in user returns status 'Unauthorized'")
    void updateCarWithExistedIdByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(put(getCarUrl(EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Updating a car by 'ADMIN' returns status 'Not Found' if id is not existed")
    void updateCarWithNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(put(getCarUrl(NOT_EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a car with existed id by 'ADMIN' returns status 'No Content' if it is valid")
    void deleteCarWithExistedIdAndReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Deleting a car with existed id by logged-in user returns status 'Forbidden'")
    void deleteCarWithExistedIdAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(delete(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deleting a car with existed id by not logged-in user returns status 'Unauthorized'")
    void deleteCarWithExistedIdByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(delete(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a car by 'ADMIN' returns status 'Not Found' if id is not existed")
    void deleteCarWithNoExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getCarUrl(NOT_EXISTED_CAR_ID)))
                .andExpect(status().isNotFound());
    }

    private String getCarUrl(Long id) {
        return String.format("/cars/%s", id);
    }

    private String getAvailableCarsUrl() {
        return new StringBuilder()
                .append("/cars/availableCars?start=")
                .append(DATE_OF_RENT)
                .append("&finish=")
                .append(DATE_OF_RETURN)
                .toString();
    }
}