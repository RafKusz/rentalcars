package com.rentalcars.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.rentalcars.contract.model.mapper.ContractFixtures.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class ContractControllerTest {

    public static final String CONTRACTS_URL = "/contracts";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getContractsAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL))
                .andExpect(status().isOk());
    }

    @Test
    void getContractByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void getContractByNotExitedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getContractUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createContractAndReturnStatusOk() throws Exception {
        mockMvc.perform(post(CONTRACTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getRentContractDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteContractByExistedIdAndReturnStatusOk() throws Exception {
        mockMvc.perform(delete(getContractUrl(EXISTED_ID)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteContractByNotExistedIdAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getContractUrl(NOT_EXISTED_ID)))
                .andExpect(status().isNotFound());
    }

    String getContractUrl(Long id) {
        String contractUrl = String.format("/contracts/%s", id);
        return contractUrl;
    }
}