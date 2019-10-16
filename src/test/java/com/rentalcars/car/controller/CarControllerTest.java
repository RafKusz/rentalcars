package com.rentalcars.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.rentalcars.car.service.CarFixtures.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class CarControllerTest {

    public static final String CARS_URL = "/cars";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getCarsAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CARS_URL))
                .andExpect(status().isOk());
    }

    @Test
    void getCarByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getCarUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void getCarByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getCarUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCarAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(CARS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isOk());
    }

    @Test
    void updateCarWithExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(put(getCarUrl(EXISTED_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isOk());
    }

    @Test
    void updateCarWithNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(put(getCarUrl(NOT_EXISTED_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getCarDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCarWithExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(delete(getCarUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCarWithNoExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getCarUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    String getCarUrl(Long id) {
        String url = String.format("/cars/%s", id);
        return url;
    }
}