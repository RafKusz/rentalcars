package com.rentalcars.contract.service;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.model.ContractInput;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ContractUnavailableException;
import com.rentalcars.exceptions.UserNotFoundException;

import java.util.List;

public interface ContractService {

    List<ContractDto> getAll();

    List<ContractDto> getAllContractsByUser(Long userId) throws UserNotFoundException;

    List<ContractDto> getAllContractsByLoggedInUser() throws UserNotFoundException;

    List<ContractDto> getFutureContracts();

    List<ContractDto> getFutureContractsByLoggedInUser() throws UserNotFoundException;

    ContractDto getContract(Long id) throws ContractNotFoundException;

    ContractDto getMyContract(Long id) throws ContractNotFoundException, UserNotFoundException, ContractUnavailableException;

    ContractDto createContract(ContractInput contractInput) throws ContractUnavailableException, UserNotFoundException, CarNotFoundException;

    void deleteContract(Long id) throws ContractNotFoundException;

    void deleteContractForLoggedInUser(Long id) throws UserNotFoundException, ContractNotFoundException, ContractUnavailableException;
}