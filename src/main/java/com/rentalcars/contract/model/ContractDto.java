package com.rentalcars.contract.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {

    private Long id;

    @NotNull
    private UserDto userDto;

    @NotNull
    private CarDto carDto;

    @NotNull
    @FutureOrPresent
    @JsonFormat
    private LocalDate dateOfRent;

    @NotNull
    @FutureOrPresent
    @JsonFormat
    private LocalDate dateOfReturn;

    private LocalDateTime createAt;
}