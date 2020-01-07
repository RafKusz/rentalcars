package com.rentalcars.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.user.UserFixtures;
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

import static com.rentalcars.contract.ContractFixtures.*;
import static com.rentalcars.user.UserFixtures.EXISTED_USER_ID_WITH_CONTRACT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class ContractControllerTest {

    private static final String CONTRACTS_URL = "/contracts";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Getting all contracts return status 'Ok'")
    void getContractsAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a contract returns status 'Ok'")
    void getContractByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a contract by existed user returns status 'Ok'")
    void getContractsByExistedUserIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(EXISTED_USER_ID_WITH_CONTRACT)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting a contract returns status 'Not Found' if user do not exist")
    void getContractsByNotExistedUserIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(UserFixtures.NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Getting a contract returns status 'Not Found' if id is not exist")
    void getContractByNotExitedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getContractUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Creating a contract returns status 'Ok' if it is valid")
    void createContractAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(CONTRACTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getRentContractDto())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Creating a contract on unavailable range of dates returns status 'Conflict'")
    void createContractOnUnavailableRangeOfDatesAndReturnStatusConflict() throws Exception {
        mockMvc.perform(post(CONTRACTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getContractDtoWithUnavailableRangeOfDates())))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Creating a contract with not existed user returns status 'Not Found'")
    void createContractWithNotExistedUserReturnStatusNotFound() throws Exception {
        mockMvc.perform(post(CONTRACTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getContractDtoWithNotExistedUser())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Creating a contract with not existed car returns status 'Not Found'")
    void createContractWithNotExistedCarReturnStatusNotFound() throws Exception {
        mockMvc.perform(post(CONTRACTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getContractDtoWithNotExistedCar())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deleting a contract returns status 'Ok' if it is valid")
    void deleteContractByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(delete(getContractUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deleting a contract returns status 'Not Found' if id is not existed")
    void deleteContractByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getContractUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    private String getContractUrl(Long id) {
        return "/contracts/" + id;
    }

    private String getContractsUrlByUserId(Long userId) {
        return "/contracts/users/" + userId;
    }

    private ContractDto getContractDtoWithUnavailableRangeOfDates() {
        ContractDto contractDto = getRentContractDto();
        contractDto.setDateOfRent(LocalDate.of(2020, 11, 11));
        contractDto.setDateOfReturn(LocalDate.of(2020, 12, 11));
        return contractDto;
    }

    private ContractDto getContractDtoWithNotExistedUser() {
        ContractDto contractDto = getRentContractDto();
        contractDto.getUserDto().setId(NOT_EXISTED_ID);
        return contractDto;
    }

    private ContractDto getContractDtoWithNotExistedCar() {
        ContractDto contractDto = getRentContractDto();
        contractDto.getCarDto().setId(NOT_EXISTED_ID);
        return contractDto;
    }
}