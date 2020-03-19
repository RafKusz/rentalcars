package com.rentalcars.car.controller;

import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.model.CarOutput;
import com.rentalcars.car.service.CarService;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ReplyStatus;
import com.rentalcars.exceptions.UnavailableRangeOfDatesException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting all cars")
    @GetMapping
    public List<CarDto> getCars() {
        return carService.getCars();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class),
            @ApiResponse(code = 409, message = "Conflict", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting available cars by range of dates")
    @GetMapping("/availableCars")
    public List<CarOutput> getAvailableCars(@RequestParam("start") @ApiParam(value = "write start date in format: yyyy-MM-dd")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam("finish") @ApiParam(value = "write finish date in format: yyyy-MM-dd")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishDate) throws UnavailableRangeOfDatesException {
        return carService.getAvailableCarsByRangeOfDates(startDate, finishDate);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = CarOutput.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting a car by Id")
    @GetMapping("/{carId}")
    public CarOutput getCar(@PathVariable("carId") Long id) throws CarNotFoundException {
        return carService.getCar(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = CarDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class)})
    @ApiOperation(value = "Creating a new car")
    @PostMapping
    @ResponseStatus(CREATED)
    public CarDto createCar(@Valid @RequestBody CarDto carDto) {
        return carService.createCar(carDto);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = CarDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Updating a car by Id")
    @ResponseStatus(CREATED)
    @PutMapping("/{carId}")
    public CarDto updateCar(@PathVariable("carId") Long id, @Valid @RequestBody CarDto carDto) throws CarNotFoundException {
        return carService.updateCar(carDto, id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Deleting a car by Id")
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable("carId") Long id) throws CarNotFoundException {
        carService.deleteCar(id);
    }
}