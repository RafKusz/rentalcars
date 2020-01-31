package com.rentalcars.contract;

import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.model.ContractInput;
import lombok.Builder;

import java.time.LocalDate;

import static com.rentalcars.car.CarFixtures.*;
import static com.rentalcars.user.UserFixtures.*;

@Builder
public class ContractFixtures {

    public static final Long EXISTED_CONTRACT_ID = 1L;
    public static final Long NOT_EXISTED_CONTRACT_ID = 5L;
    public static final LocalDate DATE_OF_RENT = LocalDate.of(LocalDate.now().getYear() + 1, 8, 15);
    public static final LocalDate DATE_OF_RETURN = LocalDate.of(LocalDate.now().getYear() + 1, 8, 20);

    public static Contract getRentContract() {
        return Contract.builder()
                .id(EXISTED_CONTRACT_ID)
                .car(getCar())
                .user(getUser())
                .dateOfRent(DATE_OF_RENT)
                .dateOfReturn(DATE_OF_RETURN)
                .build();
    }

    public static ContractDto getRentContractDto() {
        return ContractDto.builder()
                .id(EXISTED_CONTRACT_ID)
                .carDto(getCarDto())
                .userDto(getUserDto())
                .dateOfRent(DATE_OF_RENT)
                .dateOfReturn(DATE_OF_RETURN)
                .build();
    }

    public static ContractInput getRentContractInput() {
        return ContractInput.builder()
                .carId(EXISTED_CAR_ID)
                .userId(EXISTED_USER_ID)
                .dateOfRent(DATE_OF_RENT)
                .dateOfReturn(DATE_OF_RETURN)
                .build();
    }
}