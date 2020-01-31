package com.rentalcars.contract.controller;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.model.ContractInput;
import com.rentalcars.contract.service.ContractService;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ContractUnavailableException;
import com.rentalcars.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("contracts")
public class ContractController {

    private ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public List<ContractDto> getAllContracts() {
        return contractService.getAll();
    }

    @GetMapping("/users/{userId}")
    public List<ContractDto> findAllContractsByUserId(@PathVariable("userId") Long id) throws UserNotFoundException {
        return contractService.getAllContractsByUser(id);
    }

    @GetMapping("/{rentId}")
    public ContractDto getContract(@PathVariable("rentId") Long id) throws ContractNotFoundException {
        return contractService.getContract(id);
    }

    @PostMapping
    public ContractDto createContract(@Valid @RequestBody ContractInput contractInput) throws UserNotFoundException, CarNotFoundException, ContractUnavailableException {
        return contractService.createContract(contractInput);
    }

    @DeleteMapping("/{rentId}")
    public void deleteContract(@PathVariable("rentId") Long id) throws ContractNotFoundException {
        contractService.deleteContract(id);
    }
}