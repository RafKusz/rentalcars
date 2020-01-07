package com.rentalcars.contract.service;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ContractUnavailableException;
import com.rentalcars.exceptions.UserNotFoundException;

import java.util.List;

public interface ContractService {

    List<ContractDto> getAll();

    List<ContractDto> getAllContractsByUser(Long userId) throws UserNotFoundException;

    ContractDto getContract(Long id) throws ContractNotFoundException;

    ContractDto createContract(ContractDto contractDto) throws UserNotFoundException, CarNotFoundException, ContractUnavailableException;

    void deleteContract(Long id) throws ContractNotFoundException;

}
