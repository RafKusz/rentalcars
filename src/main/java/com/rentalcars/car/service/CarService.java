package com.rentalcars.car.service;

import com.rentalcars.car.model.CarDto;
import com.rentalcars.exceptions.CarNotFoundException;

import java.util.List;

public interface CarService {

    List<CarDto> getCars();

    CarDto getCar(Long id) throws CarNotFoundException;

    CarDto createCar(CarDto carDto);

    CarDto updateCar(CarDto carDto, Long id) throws CarNotFoundException;

    void deleteCar(Long id) throws CarNotFoundException;
}
