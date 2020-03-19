package com.rentalcars.contract.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rentalcars.contract.model.annotation.DateValidator;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "carId", example = "1")
    @NotNull
    private Long carId;

    @ApiModelProperty(value = "dateOfRent", example = "11/11/2030")
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfRent;

    @ApiModelProperty(value = "dateOfReturn", example = "11/12/2030")
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfReturn;
}