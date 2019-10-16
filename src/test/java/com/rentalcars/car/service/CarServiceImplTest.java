package com.rentalcars.car.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.exceptions.CarNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.rentalcars.car.service.CarFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    private CarServiceImpl carService;

    @Before
    public void setUp() {
        carService = new CarServiceImpl(carRepository);
    }

    @Test
    public void returnCarIfIdExisted() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));

        CarDto carDto = carService.getCar(EXISTED_ID);

        assertNotNull(carDto);
        assertEquals(EXISTED_ID, carDto.getId());
    }

    @Test
    public void throwExceptionIfCarIdDoesNotExisted() throws Exception {
        Mockito.when(carRepository.findById(NOT_EXISTED_ID)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.getCar(NOT_EXISTED_ID));
    }

    @Test
    public void returnListOfCars() throws Exception {
        Mockito.when(carRepository.findAll()).thenReturn(Collections.singletonList(getCar()));

        List<CarDto> carDtoList = carService.getCars();

        assertNotNull(carDtoList);
        assertEquals(EXPECTED_SIZE, carDtoList.size());
    }

    @Test
    public void createCarIfItIsValid() throws Exception {
        Mockito.when(carRepository.save(any(Car.class))).thenAnswer(i -> getCarFromMock((Car) i.getArguments()[0]));

        CarDto carDto = carService.createCar(getCarDto());

        assertNotNull(carDto);
        assertNotNull(carDto.getId());
        assertNotNull(carDto.getCreateAt());
        assertEquals(EXISTED_ID, carDto.getId());
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
    public void updateCarIfIdExistedAndItIsValid() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));

        CarDto carDto = carService.updateCar(getUpdateCarDto(), EXISTED_ID);

        assertNotNull(carDto);
        assertNotNull(carDto.getId());
        Mockito.verify(carRepository).save(any(Car.class));
        assertEquals("new brand", carDto.getBrand());
        assertEquals("new model", carDto.getModel());
        assertEquals(PRODUCTION_YEAR, carDto.getProductionYear());
        assertEquals(PRICE_OF_RENT, carDto.getPriceOfRent());
        assertEquals(false, carDto.getAvailable());
        assertEquals("new description", carDto.getDescription());
    }

    @Test
    public void doNotThrowExceptionIfCarToDeleteIsValid() throws Exception {
        Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getCar()));
        Mockito.doNothing().when(carRepository).delete(any(Car.class));

        carService.deleteCar(EXISTED_ID);
    }
}