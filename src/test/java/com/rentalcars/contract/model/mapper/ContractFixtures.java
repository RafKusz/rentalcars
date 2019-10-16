package com.rentalcars.contract.model.mapper;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import lombok.Builder;

import java.time.LocalDate;

import static com.rentalcars.car.service.CarFixtures.getCar;
import static com.rentalcars.car.service.CarFixtures.getCarDto;
import static com.rentalcars.user.UserFixtures.getUser;
import static com.rentalcars.user.UserFixtures.getUserDto;

@Builder
public class ContractFixtures {

    public static final Long EXISTED_ID = 1L;
    public static final Long NOT_EXISTED_ID = 5L;
    public static final LocalDate DATE_OF_RENT = LocalDate.of(2020, 8, 15);
    public static final LocalDate DATE_OF_RETURN = LocalDate.of(2020, 8, 20);
    public static final Car CAR = getCar();
    public static final User USER = getUser();
    public static final CarDto CAR_DTO = getCarDto();
    public static final UserDto USER_DTO = getUserDto();
    public static final int SIZE = 1;

    public static Contract getRentContract() {
        Contract contract = Contract.builder()
                .id(EXISTED_ID)
                .car(CAR)
                .user(USER)
                .dateOfRent(DATE_OF_RENT)
                .dateOfReturn(DATE_OF_RETURN)
                .build();
        return contract;
    }

    public static ContractDto getRentContractDto() {
        ContractDto contractDto = ContractDto.builder()
                .id(EXISTED_ID)
                .carDto(CAR_DTO)
                .userDto(USER_DTO)
                .dateOfRent(DATE_OF_RENT)
                .dateOfReturn(DATE_OF_RETURN)
                .build();
        return contractDto;
    }

}
