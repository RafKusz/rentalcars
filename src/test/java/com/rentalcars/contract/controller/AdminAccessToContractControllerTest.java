package com.rentalcars.contract.controller;

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

import static com.rentalcars.contract.ContractFixtures.EXISTED_CONTRACT_ID;
import static com.rentalcars.contract.ContractFixtures.NOT_EXISTED_CONTRACT_ID;
import static com.rentalcars.user.UserFixtures.EXISTED_USER_ID_WITH_CONTRACT;
import static com.rentalcars.user.UserFixtures.NOT_EXISTED_USER_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
class AdminAccessToContractControllerTest {

    private static final String CONTRACTS_URL = "/contracts";

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting all contracts by ADMIN returns status 'Ok'")
    void getContractsByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting all contracts by logged-in user returns status 'Forbidden'")
    void getContractsByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting all contracts by not logged-in user returns status 'Unauthorized'")
    void getContractsByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting future contracts by ADMIN returns status 'Ok'")
    void getFutureContractsByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL + "/future"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting future contracts by logged-in user returns status 'Forbidden'")
    void getFutureContractsByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL + "/future"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting future contracts by not logged-in user returns status 'Unauthorized'")
    void getFutureContractsByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(CONTRACTS_URL + "/future"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a contract with existed id by ADMIN returns status 'Ok'")
    void getContractWithExistedIdByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractUrl(EXISTED_CONTRACT_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(EXISTED_CONTRACT_ID));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting a contract with existed contract id by logged-in user returns status 'Forbidden'")
    void getContractWithExistedContractIdByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(getContractUrl(EXISTED_CONTRACT_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting a contract with existed id by not logged-in user returns status 'Unauthorized'")
    void getContractWithExistedIdByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(getContractUrl(EXISTED_CONTRACT_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a contract with existed user id by ADMIN returns status 'Ok'")
    void getContractsWithExistedUserIdByAdminAndReturnStatusOk() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(EXISTED_USER_ID_WITH_CONTRACT)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id").value(Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Getting a contract with existed user id by logged-in user returns status 'Forbidden'")
    void getContractsWithExistedUserIdByLoggedInUserAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(EXISTED_USER_ID_WITH_CONTRACT)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Getting a contract with existed user id by not logged-in user returns status 'Unauthorized'")
    void getContractsWithExistedUserIdByNotLoggedInUserAndReturnStatusUnauthorized() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(EXISTED_USER_ID_WITH_CONTRACT)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a contract by ADMIN returns status 'Not Found' if user does not exist")
    void getContractsWithNotExistedUserIdByAdminAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getContractsUrlByUserId(NOT_EXISTED_USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Getting a contract by ADMIN returns status 'Not Found' if id does not exist")
    void getContractWithNotExitedIdByAdminAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(get(getContractUrl(NOT_EXISTED_CONTRACT_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a contract by ADMIN returns status 'No Content' if it is valid")
    void deleteContractWithExistedIdByAdminAndReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete(getContractUrl(EXISTED_CONTRACT_ID)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a contract by ADMIN returns status 'Not Found' if id is not existed")
    void deleteContractWithNotExistedIdByAdminAndReturnStatusNotFound() throws Exception {
        mockMvc.perform(delete(getContractUrl(NOT_EXISTED_CONTRACT_ID)))
                .andExpect(status().isNotFound());
    }

    private String getContractUrl(Long id) {
        return "/contracts/" + id;
    }

    private String getContractsUrlByUserId(Long userId) {
        return "/contracts/users/" + userId;
    }
}