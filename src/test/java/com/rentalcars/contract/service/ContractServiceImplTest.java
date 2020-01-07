package com.rentalcars.contract.service;

import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.repository.ContractRepository;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ContractUnavailableException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.UserFixtures;
import com.rentalcars.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.rentalcars.car.CarFixtures.getCar;
import static com.rentalcars.car.CarFixtures.getCarDto;
import static com.rentalcars.contract.ContractFixtures.EXISTED_ID;
import static com.rentalcars.contract.ContractFixtures.NOT_EXISTED_ID;
import static com.rentalcars.contract.ContractFixtures.*;
import static com.rentalcars.user.UserFixtures.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    private ContractServiceImpl contractService;

    @BeforeEach
    public void setUp() {
        contractService = new ContractServiceImpl(contractRepository, carRepository, userRepository);
    }

    @Test
    @DisplayName("Getting all contracts return list of contracts")
    public void returnListOfContracts() {
        Mockito.when(contractRepository.findAll()).thenReturn(singletonList(getRentContract()));

        List<ContractDto> dtoList = contractService.getAll();

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    @DisplayName("Getting all contracts by user id return list of contracts")
    public void returnContractListByUserIfUserIdIsExisted() throws Exception {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(contractRepository.findAllByUserId(anyLong())).thenReturn(singletonList(getRentContract()));

        List<ContractDto> dtoList = contractService.getAllContractsByUser(EXISTED_USER_ID_WITH_CONTRACT);

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
    }

    @Test
    @DisplayName("Getting all contracts by user id throw exception if user id does not exist")
    public void throwExceptionIfUserDoesNotExists() {
        Mockito.when(userRepository.existsById(UserFixtures.NOT_EXISTED_ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> contractService.getAllContractsByUser(UserFixtures.NOT_EXISTED_ID));
    }

    @Test
    @DisplayName("Getting a contract returns a contract if id is exist")
    public void returnContractIfIdIsExisted() throws Exception {
        Mockito.when(contractRepository.findById(anyLong())).thenReturn(ofNullable(getRentContract()));

        ContractDto contractDto = contractService.getContract(EXISTED_ID);

        assertNotNull(contractDto);
        assertNotNull(contractDto.getId());
        assertEquals(getCarDto(), contractDto.getCarDto());
        assertEquals(getUserDto(), contractDto.getUserDto());
        assertEquals(DATE_OF_RENT, contractDto.getDateOfRent());
        assertEquals(DATE_OF_RETURN, contractDto.getDateOfReturn());
    }

    @Test
    @DisplayName("Getting a contract throw exception if id is not exist")
    public void throwExceptionIfIdIsNotExisted() {
        Mockito.when(contractRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, () -> contractService.getContract(NOT_EXISTED_ID));
    }

    @Test
    @DisplayName("Creating a contract creates new contract if it is valid")
    public void createContractIfItIsValid() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(ofNullable(getUser()));
        Mockito.when(carRepository.findById(anyLong())).thenReturn(ofNullable(getCar()));
        Mockito.when(contractRepository.findContractsFromPeriodAndCarId(any(LocalDate.class), any(LocalDate.class), anyLong())).thenReturn(emptyList());
        Mockito.when(contractRepository.save(any(Contract.class))).thenAnswer(i -> getContractFromTheMock((Contract) i.getArguments()[0]));

        ContractDto contractDto = contractService.createContract(getRentContractDto());

        assertNotNull(contractDto);
        assertNotNull(contractDto.getId());
        assertNotNull(contractDto.getCreateAt());
        assertEquals(getCarDto(), contractDto.getCarDto());
        assertEquals(getUserDto(), contractDto.getUserDto());
        assertEquals(DATE_OF_RENT, contractDto.getDateOfRent());
        assertEquals(DATE_OF_RETURN, contractDto.getDateOfReturn());
    }

    Object getContractFromTheMock(Contract contract) {
        contract.setId(1L);
        return contract;
    }

    @Test
    @DisplayName("Creating a contract with the same range of dates like existing contract, throw exception")
    public void throwExceptionIfNewContractHasRangeOfDatesLikeExistingContract() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(ofNullable(getUser()));
        Mockito.when(carRepository.findById(anyLong())).thenReturn(ofNullable(getCar()));
        Mockito.when(contractRepository.findContractsFromPeriodAndCarId(any(LocalDate.class), any(LocalDate.class), anyLong()))
                .thenReturn(Collections.singletonList(getRentContract()));

        assertThrows(ContractUnavailableException.class, () -> contractService.createContract(getRentContractDto()));
    }

    @Test
    @DisplayName("Deleting a contract do not throw exception if a contract to delete is valid")
    public void doNotThrowExceptionIfContractToDeleteIsValid() throws Exception {
        Mockito.when(contractRepository.findById(anyLong())).thenReturn(ofNullable(getRentContract()));
        Mockito.doNothing().when(contractRepository).delete(any(Contract.class));

        contractService.deleteContract(EXISTED_ID);
    }
}