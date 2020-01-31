package com.rentalcars.contract.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rentalcars.contract.model.annotation.DateValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DateValidator
public class ContractInput {

    @NotNull
    private Long userId;

    @NotNull
    private Long carId;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRent;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfReturn;
}