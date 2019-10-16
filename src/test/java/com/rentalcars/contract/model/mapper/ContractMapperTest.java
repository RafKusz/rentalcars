package com.rentalcars.contract.model.mapper;

import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.rentalcars.contract.model.mapper.ContractFixtures.*;
import static com.rentalcars.contract.model.mapper.ContractMapper.CONTRACT_MAPPER;

class ContractMapperTest {


    @Test
    void returnNullIfDtoIsNull() {
        Contract contract = CONTRACT_MAPPER.mapToContractEntity(null);

        Assertions.assertNull(contract);
    }

    @Test
    void returnNullIfContractIsNull() {
        ContractDto contractDto = CONTRACT_MAPPER.mapToContractDto(null);

        Assertions.assertNull(contractDto);
    }

    @Test
    void returnContractIfDtoIsNotNull() {

        Contract contract = CONTRACT_MAPPER.mapToContractEntity(getRentContractDto());

        Assertions.assertNotNull(contract);
        Assertions.assertNotNull(contract.getId());
        Assertions.assertEquals(CAR, contract.getCar());
        Assertions.assertEquals(USER, contract.getUser());
        Assertions.assertEquals(DATE_OF_RENT, contract.getDateOfRent());
        Assertions.assertEquals(DATE_OF_RETURN, contract.getDateOfReturn());
    }

    @Test
    void returnContractDtoIfEntityIsNotNull() {

        ContractDto contractDto = CONTRACT_MAPPER.mapToContractDto(getRentContract());

        Assertions.assertNotNull(contractDto);
        Assertions.assertNotNull(contractDto.getId());
        Assertions.assertEquals(CAR_DTO, contractDto.getCarDto());
        Assertions.assertEquals(USER_DTO, contractDto.getUserDto());
        Assertions.assertEquals(DATE_OF_RENT, contractDto.getDateOfRent());
        Assertions.assertEquals(DATE_OF_RETURN, contractDto.getDateOfReturn());
    }

    @Test
    void returnListOfContractsIfItIsValid() {
        List<ContractDto> givenList = Collections.singletonList(getRentContractDto());

        List<Contract> contracts = CONTRACT_MAPPER.mapToContracts(givenList);

        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(SIZE, contracts.size());
    }

    @Test
    void returnListOfDtosIfItIsValid() {
        List<Contract> givenList = Collections.singletonList(getRentContract());

        List<ContractDto> contractDtos = CONTRACT_MAPPER.mapToContractDtos(givenList);

        Assertions.assertNotNull(contractDtos);
        Assertions.assertEquals(SIZE, contractDtos.size());
    }
}




