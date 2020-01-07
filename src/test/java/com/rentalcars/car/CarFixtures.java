package com.rentalcars.car;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import lombok.Builder;

@Builder
public class CarFixtures {

    public static final Long EXISTED_ID = 1L;
    public static final Long NOT_EXISTED_ID = 5L;
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final Integer PRODUCTION_YEAR = 2015;
    public static final Double PRICE_OF_RENT = 100.0;
    public static final boolean AVAILABLE = true;
    public static final String DESCRIPTION = "description";

    public static Car getCar() {
        return Car.builder()
                .id(EXISTED_ID)
                .brand(BRAND)
                .model(MODEL)
                .productionYear(PRODUCTION_YEAR)
                .priceOfRent(PRICE_OF_RENT)
                .available(AVAILABLE)
                .description(DESCRIPTION)
                .build();
    }

    public static CarDto getCarDto() {
        return CarDto.builder()
                .id(EXISTED_ID)
                .brand(BRAND)
                .model(MODEL)
                .productionYear(PRODUCTION_YEAR)
                .priceOfRent(PRICE_OF_RENT)
                .available(AVAILABLE)
                .description(DESCRIPTION)
                .build();
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
