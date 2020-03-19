package com.rentalcars.car.service;

import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.model.CarOutput;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.UnavailableRangeOfDatesException;

import java.time.LocalDate;
import java.util.List;

public interface CarService {

    List<CarDto> getCars();

    List<CarOutput> getAvailableCarsByRangeOfDates(LocalDate startDate, LocalDate finishDate) throws UnavailableRangeOfDatesException;

    CarOutput getCar(Long id) throws CarNotFoundException;

    CarDto createCar(CarDto carDto);

    CarDto updateCar(CarDto carDto, Long id) throws CarNotFoundException;

    void deleteCar(Long id) throws CarNotFoundException;
}