package com.rentalcars.contract.controller;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.service.ContractService;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ReplyStatus;
import com.rentalcars.exceptions.UserNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("contracts")
public class AdminAccessToContractController {

    private ContractService contractService;

    @Autowired
    public AdminAccessToContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting all contracts")
    @GetMapping
    public List<ContractDto> getAllContracts() {
        return contractService.getAll();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting all contracts by userId")
    @GetMapping("/users/{userId}")
    public List<ContractDto> findAllContractsByUserId(@PathVariable("userId") Long id) throws UserNotFoundException {
        return contractService.getAllContractsByUser(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting all future car reservations")
    @GetMapping("/future")
    public List<ContractDto> getAllFutureCarReservations() {
        return contractService.getFutureContracts();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = ContractDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting a contract by Id")
    @GetMapping("/{rentId}")
    public ContractDto getContract(@PathVariable("rentId") Long id) throws ContractNotFoundException {
        return contractService.getContract(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Deleting a contract by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{rentId}")
    public void deleteContract(@PathVariable("rentId") Long id) throws ContractNotFoundException {
        contractService.deleteContract(id);
    }
}