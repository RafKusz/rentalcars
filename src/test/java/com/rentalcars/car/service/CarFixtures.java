package com.rentalcars.car.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import lombok.Builder;

@Builder
public class CarFixtures {

    public static final long EXISTED_ID = 1L;
    public static final long NOT_EXISTED_ID = 5L;
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final int PRODUCTION_YEAR = 2015;
    public static final double PRICE_OF_RENT = 100.0;
    public static final boolean AVAILABLE = true;
    public static final String DESCRIPTION = "description";
    public static final int EXPECTED_SIZE = 1;

    public static Car getCar() {
        Car car = Car.builder()
                .id(EXISTED_ID)
                .brand(BRAND)
                .model(MODEL)
                .productionYear(PRODUCTION_YEAR)
                .priceOfRent(PRICE_OF_RENT)
                .available(AVAILABLE)
                .description(DESCRIPTION)
                .build();
        return car;
    }

    public static CarDto getCarDto() {
        CarDto carDto = CarDto.builder()
                .id(EXISTED_ID)
                .brand(BRAND)
                .model(MODEL)
                .productionYear(PRODUCTION_YEAR)
                .priceOfRent(PRICE_OF_RENT)
                .available(AVAILABLE)
                .description(DESCRIPTION)
                .build();
        return carDto;
    }

    public static CarDto getUpdateCarDto() {
        CarDto carDto = CarDto.builder()
                .id(EXISTED_ID)
                .brand("new brand")
                .model("new model")
                .productionYear(PRODUCTION_YEAR)
                .priceOfRent(PRICE_OF_RENT)
                .available(false)
                .description("new description")
                .build();
        return carDto;

    }
}
