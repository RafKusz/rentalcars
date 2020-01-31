package com.rentalcars.car.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.model.CarOutput;
import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.exceptions.CarNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.rentalcars.car.CarFixtures.*;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RENT;
import static com.rentalcars.contract.ContractFixtures.DATE_OF_RETURN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    private CarServiceImpl carService;

    @BeforeEach
    public void setUp() {
        carService = new CarServiceImpl(carRepository);
    }

    @Test
    @DisplayName("Getting a car returns a car if id is exist")
    public void returnCarIfIdExisted() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));

        CarDto carDto = carService.getCar(EXISTED_CAR_ID);

        assertNotNull(carDto);
        assertEquals(EXISTED_CAR_ID, carDto.getId());
    }

    @Test
    @DisplayName("Getting a car throw exception if id is not exist")
    public void throwExceptionIfCarIdDoesNotExisted() {
        Mockito.when(carRepository.findById(NOT_EXISTED_CAR_ID)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.getCar(NOT_EXISTED_CAR_ID));
    }

    @Test
    @DisplayName("Getting all cars return list of cars")
    public void returnListOfCars() {
        Mockito.when(carRepository.findAll()).thenReturn(Collections.singletonList(getCar()));

        List<CarDto> carDtoList = carService.getCars();

        assertNotNull(carDtoList);
        assertEquals(1, carDtoList.size());
    }

    @Test
    @DisplayName("Getting all available cars in not occupied range of dates return list of cars")
    public void returnListOfAvailableCarsIfRangeOfDatesIsNotOccupied() {
        Mockito.when(carRepository.findAvailableCarsByDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(getCar()));

        List<CarOutput> carOutputList = carService.getAvailableCarsByRangeOfDates(DATE_OF_RENT, DATE_OF_RETURN);

        assertNotNull(carOutputList);
        assertEquals(1, carOutputList.size());
    }

    @Test
    @DisplayName("Creating a car creates new car if it is valid")
    public void createCarIfItIsValid() {
        Mockito.when(carRepository.save(any(Car.class))).thenAnswer(i -> getCarFromMock((Car) i.getArguments()[0]));

        CarDto carDto = carService.createCar(getCarDto());

        assertNotNull(carDto);
        assertNotNull(carDto.getId());
        assertNotNull(carDto.getCreateAt());
        assertEquals(EXISTED_CAR_ID, carDto.getId());
        assertEquals(BRAND, carDto.getBrand());
        assertEquals(MODEL, carDto.getModel());
        assertEquals(PRODUCTION_YEAR, carDto.getProductionYear());
        assertEquals(PRICE_OF_RENT, carDto.getPriceOfRent());
        assertEquals(DESCRIPTION, carDto.getDescription());
    }

    private Car getCarFromMock(Car car) {
        car.setId(1L);
        return car;
    }

    @Test
    @DisplayName("Updating a car update a car if it is valid")
    public void updateCarIfIdExistedAndItIsValid() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));

        CarDto carDto = carService.updateCar(getUpdateCarDto(), EXISTED_CAR_ID);

        assertNotNull(carDto);
        assertNotNull(carDto.getId());
        Mockito.verify(carRepository).save(any(Car.class));
        assertEquals("new brand", carDto.getBrand());
        assertEquals("new model", carDto.getModel());
        assertEquals(PRODUCTION_YEAR, carDto.getProductionYear());
        assertEquals(PRICE_OF_RENT, carDto.getPriceOfRent());
        assertEquals("new description", carDto.getDescription());
    }

    @Test
    @DisplayName("Deleting a car do not throw exception if a car to delete is valid")
    public void doNotThrowExceptionIfCarToDeleteIsValid() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));
        Mockito.doNothing().when(carRepository).delete(any(Car.class));

        carService.deleteCar(EXISTED_CAR_ID);
    }
}