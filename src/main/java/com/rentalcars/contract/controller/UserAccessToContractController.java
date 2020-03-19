package com.rentalcars.contract.controller;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.model.ContractInput;
import com.rentalcars.contract.service.ContractService;
import com.rentalcars.exceptions.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("contracts/my")
public class UserAccessToContractController {

    private ContractService contractService;

    @Autowired
    public UserAccessToContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting my all contracts")
    @GetMapping
    public List<ContractDto> findAllMyContracts() throws UserNotFoundException {
        return contractService.getAllContractsByLoggedInUser();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting my all future car reservations")
    @GetMapping("/future")
    public List<ContractDto> findAllMyFutureContracts() throws UserNotFoundException {
        return contractService.getFutureContractsByLoggedInUser();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = ContractDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class),
            @ApiResponse(code = 409, message = "Conflict", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting my contract by Id")
    @GetMapping("/{rentId}")
    public ContractDto findMyContract(@PathVariable("rentId") Long id) throws UserNotFoundException, ContractNotFoundException, ContractUnavailableException {
        return contractService.getMyContract(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ContractDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class),
            @ApiResponse(code = 409, message = "Conflict", response = ReplyStatus.class)})
    @ApiOperation(value = "Creating a new contract")
    @PostMapping
    @ResponseStatus(CREATED)
    public ContractDto createMyContract(@Valid @RequestBody ContractInput contractInput) throws ContractUnavailableException, CarNotFoundException, UserNotFoundException {
        return contractService.createContract(contractInput);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Deleting my contract by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{rentId}")
    public void deleteMyContract(@PathVariable("rentId") Long id) throws UserNotFoundException, ContractNotFoundException, ContractUnavailableException {
        contractService.deleteContractForLoggedInUser(id);
    }
}