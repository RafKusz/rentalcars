package com.rentalcars.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentalcars.contract.model.ContractInput;
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

import java.time.LocalDate;

import static com.rentalcars.car.CarFixtures.NOT_EXISTED_CAR_ID;
import static com.rentalcars.contract.ContractFixtures.EXISTED_CONTRACT_ID;
import static com.rentalcars.contract.ContractFixtures.getRentContractInput;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class UserAccessToContractControllerTest {

    private static final String CONTRACT_URL = "/contracts/my";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Getting all contracts by logged-in user returns status 'Ok'")
    void getAllContractsByLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Getting all contracts by not logged-in user returns status 'Unauthorized'")
    void getAllContractsByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(CONTRACT_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Getting future contracts by logged-in user returns status 'Ok'")
    void getFutureContractsByLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACT_URL + "/future"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Getting future contracts by not logged-in user returns status 'Unauthorized'")
    void getFutureContractsByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(CONTRACT_URL + "/future"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Getting a contract by logged-in user returns status 'Ok' if id is existed")
    void getContractsWithExistedIdByLoggedInUserReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractUrlWithExistedId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_CONTRACT_ID));
    }

    @Test
    @WithMockUser(username = "kowalski@poczta.pl", authorities = "USER")
    @DisplayName("Getting a contract with existed id by different logged-in user returns status 'Conflict'")
    void getContractsWithExistedIdByDifferentLoggedInUserReturnStatusConflict() throws Exception {
        mockMvc.perform(get(getContractUrlWithExistedId()))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Getting contract with existed id by not logged-in user returns status 'Unauthorized'")
    void getContractWithNotExistedIdByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(getContractUrlWithExistedId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Creating a contract by logged-in user returns status 'Created' if it is valid")
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    void createContractByLoggedInUserReturnStatusCreated() throws Exception {
        mockMvc.perform(post(CONTRACT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getRentContractInput())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Creating a contract by not logged-in user returns status 'Unauthorized'")
    void createContractByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(post(CONTRACT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getRentContractInput())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Creating a contract on unavailable range of dates by logged-in user returns status 'Conflict'")
    void createContractOnUnavailableRangeOfDatesByLoggedInUserAndReturnStatusConflict() throws Exception {
        mockMvc.perform(post(CONTRACT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getContractInputWithUnavailableRangeOfDates())))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    @DisplayName("Creating a contract with not existed car by logged-in user returns status 'Not Found'")
    void createContractWithNotExistedCarReturnStatusNotFound() throws Exception {
        mockMvc.perform(post(CONTRACT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getContractInputWithNotExistedCar())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deleting a contract with existed id by logged-in user returns status 'No Content'")
    @WithMockUser(username = "nowak@poczta.pl", authorities = "USER")
    void deleteContractWithExistedIdByLoggedInUserReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete(getContractUrlWithExistedId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deleting a contract with existed id by logged-in different user returns status 'Conflict'")
    @WithMockUser(username = "kowalski@poczta.pl", authorities = "USER")
    void deleteContractWithExistedIdByLoggedInDifferentUserReturnStatusConflict() throws Exception {
        mockMvc.perform(delete(getContractUrlWithExistedId()))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deleting a contract with existed id by not logged-in user returns status 'Unauthorized'")
    void deleteContractWithExistedIdByNotLoggedInUserReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(delete(getContractUrlWithExistedId()))
                .andExpect(status().isUnauthorized());
    }

    private String getContractUrlWithExistedId() {
        return "/contracts/my/" + EXISTED_CONTRACT_ID;
    }

    private ContractInput getContractInputWithUnavailableRangeOfDates() {
        ContractInput contractInput = getRentContractInput();
        contractInput.setDateOfRent(LocalDate.of(2090, 11, 11));
        contractInput.setDateOfReturn(LocalDate.of(2090, 12, 11));
        return contractInput;
    }

    private ContractInput getContractInputWithNotExistedCar() {
        ContractInput contractInput = getRentContractInput();
        contractInput.setCarId(NOT_EXISTED_CAR_ID);
        return contractInput;
    }
}