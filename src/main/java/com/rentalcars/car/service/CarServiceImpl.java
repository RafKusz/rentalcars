package com.rentalcars.car.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.model.CarOutput;
import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.UnavailableRangeOfDatesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.rentalcars.car.model.mapper.CarMapper.MAPPER;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    public static final String CAR_DOES_NOT_EXIST_MESSAGE = "Car does not exist with id: ";
    private static final String UNAVAILABLE_RANGE_OF_DATES_MESSAGE = "Finish date must be after than start date";

    private CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDto> getCars() {
        List<Car> cars = carRepository.findAll();
        log.info("Returned all cars, actual number of car: {}", cars.size());
        return MAPPER.mapToCarDtos(cars);
    }

    @Override
    public List<CarOutput> getAvailableCarsByRangeOfDates(LocalDate startDate, LocalDate finishDate) throws UnavailableRangeOfDatesException {
        if (startDate.isAfter(finishDate)) {
            throw new UnavailableRangeOfDatesException(UNAVAILABLE_RANGE_OF_DATES_MESSAGE);
        }
        List<Car> cars = carRepository.findAvailableCarsByDate(startDate, finishDate);
        log.info("Returned all available cars, actual number of car: {}", cars.size());
        return MAPPER.mapToCarOutputs(cars);
    }

    @Override
    public CarOutput getCar(Long id) throws CarNotFoundException {
        Car car = findCarById(id);
        log.info("Returned the car with id: {}", car.getId());
        return MAPPER.mapToCarOutput(car);
    }

    @Override
    public CarDto createCar(CarDto carDto) {
        Car car = MAPPER.mapToCar(carDto);
        car.setId(null);
        car.setCreateAt(LocalDateTime.now());
        carRepository.save(car);
        log.info("The new car was added with id: {}", car.getId());
        return MAPPER.mapToDto(car);
    }

    @Override
    public CarDto updateCar(CarDto carDto, Long id) throws CarNotFoundException {
        Car car = findCarById(id);
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setProductionYear(carDto.getProductionYear());
        car.setPriceOfRent(carDto.getPriceOfRent());
        car.setDescription(carDto.getDescription());
        carRepository.save(car);
        log.info("The car with id: {} was updated, new values: {}", id, carDto);
        return MAPPER.mapToDto(car);
    }

    @Override
    public void deleteCar(Long id) throws CarNotFoundException {
        Car car = findCarById(id);
        carRepository.delete(car);
        log.info("The car with id: {} was deleted", id);
    }

    private Car findCarById(Long id) throws CarNotFoundException {
        return carRepository.findById(id).orElseThrow(() ->
                new CarNotFoundException(CAR_DOES_NOT_EXIST_MESSAGE + id));
    }
}