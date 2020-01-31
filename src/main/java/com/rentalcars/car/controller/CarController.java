package com.rentalcars.car.controller;

import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.model.CarOutput;
import com.rentalcars.car.service.CarService;
import com.rentalcars.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDto> getCars() {
        return carService.getCars();
    }

    @GetMapping("availableCars")
    public List<CarOutput> getAvailableCars(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam("finish") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishDate) {
        return carService.getAvailableCarsByRangeOfDates(startDate, finishDate);
    }

    @GetMapping("/{carId}")
    public CarDto getCar(@PathVariable("carId") Long id) throws CarNotFoundException {
        return carService.getCar(id);
    }

    @PostMapping
    public CarDto createCar(@Valid @RequestBody CarDto carDto) {
        return carService.createCar(carDto);
    }

    @PutMapping("/{carId}")
    public CarDto updateCar(@PathVariable("carId") Long id, @Valid @RequestBody CarDto carDto) throws CarNotFoundException {
        return carService.updateCar(carDto, id);
    }

    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable("carId") Long id) throws CarNotFoundException {
        carService.deleteCar(id);
    }
}