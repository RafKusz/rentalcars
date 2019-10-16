package com.rentalcars.contract.service;

import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.repository.ContractRepository;
import com.rentalcars.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rentalcars.car.service.CarFixtures.getCar;
import static com.rentalcars.contract.model.mapper.ContractFixtures.*;
import static com.rentalcars.user.UserFixtures.getUser;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@RunWith(MockitoJUnitRunner.class)
public class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    ContractServiceImpl contractService;

    @Before
    public void setUp() {
        contractService = new ContractServiceImpl(contractRepository, userRepository, carRepository);
    }

    @Test
    public void returnListOfContracts() throws Exception {
        Mockito.when(contractRepository.findAll()).thenReturn(singletonList(getRentContract()));

        List<ContractDto> dtoList = contractService.getAll();

        assertNotNull(dtoList);
        assertEquals(SIZE, dtoList.size());
    }

    @Test
    public void returnContractIfIdIsExisted() throws Exception {
        Mockito.when(contractRepository.findById(anyLong())).thenReturn(ofNullable(getRentContract()));

        ContractDto contractDto = contractService.getContract(EXISTED_ID);

        assertNotNull(contractDto);
        assertNotNull(contractDto.getId());
        assertEquals(CAR_DTO, contractDto.getCarDto());
        assertEquals(USER_DTO, contractDto.getUserDto());
        assertEquals(DATE_OF_RENT, contractDto.getDateOfRent());
        assertEquals(DATE_OF_RETURN, contractDto.getDateOfReturn());
    }

    @Test
    public void createContractIfItIsValid() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(ofNullable(getUser()));
        Mockito.when(carRepository.findById(anyLong())).thenReturn(ofNullable(getCar()));
        Mockito.when(contractRepository.save(any(Contract.class))).thenAnswer(i -> getContractFromTheMock((Contract) i.getArguments()[0]));

        ContractDto contractDto = contractService.createContract(getRentContractDto());

        assertNotNull(contractDto);
        assertNotNull(contractDto.getId());
        assertNotNull(contractDto.getCreateAt());
        assertEquals(CAR_DTO, contractDto.getCarDto());
        assertEquals(USER_DTO, contractDto.getUserDto());
        assertEquals(DATE_OF_RENT, contractDto.getDateOfRent());
        assertEquals(DATE_OF_RETURN, contractDto.getDateOfReturn());
    }

    Object getContractFromTheMock(Contract contract) {
        contract.setId(1L);
        return contract;
    }

    @Test
    public void doNotThrowExceptionIfContractToDeleteIsValid() throws Exception {
        Mockito.when(contractRepository.findById(anyLong())).thenReturn(ofNullable(getRentContract()));
        Mockito.doNothing().when(contractRepository).delete(any(Contract.class));

        contractService.deleteContract(EXISTED_ID);
    }
}