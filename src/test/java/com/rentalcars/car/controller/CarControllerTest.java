package com.rentalcars.car.controller;

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

import java.time.LocalDate;

import static com.rentalcars.car.CarFixtures.*;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RENT;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RETURN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Getting all cars return status 'Ok'")
    void getCarsAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CARS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting all available cars in not occupied range of dates return status 'Ok'")
    void getAvailableCarsReturnStatusOk() throws Exception {
        mockMvc.perform(get(getAvailableCarsUrl(DATE_OF_RENT, DATE_OF_RETURN)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a car returns status 'Ok'")
    void getCarByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a car returns status 'Not Found' if id is not exist")
    void getCarByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getCarUrl(NOT_EXISTED_CAR_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Creating a car returns status 'Ok' if it is valid")
    void createCarAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Updating a car returns status 'Ok' if it is valid")
    void updateCarWithExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(put(getCarUrl(EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Updating a car returns status 'Not Found' if id is not existed")
    void updateCarWithNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(put(getCarUrl(NOT_EXISTED_CAR_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deleting a car returns status 'Ok' if it is valid")
    void deleteCarWithExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(delete(getCarUrl(EXISTED_CAR_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deleting a car returns status 'Not Found' if id is not existed")
    void deleteCarWithNoExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getCarUrl(NOT_EXISTED_CAR_ID)))
                .andExpect(status().isNotFound());
    }

    private String getCarUrl(Long id) {
        return String.format("/cars/%s", id);
    }

    private String getAvailableCarsUrl(LocalDate startDate, LocalDate finishDate) {
        return new StringBuilder()
                .append("/cars/availableCars?start=")
                .append(startDate)
                .append("&finish=")
                .append(finishDate)
                .toString();
    }
}