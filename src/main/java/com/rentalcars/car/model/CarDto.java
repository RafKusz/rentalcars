package com.rentalcars.car.model;

import com.rentalcars.contract.model.ContractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    private String brand;

    @NotBlank
    @Size(max = 50)
    private String model;

    @NotNull
    private Integer productionYear;

    @NotNull
    @Min(0)
    private Double priceOfRent;

    @Size(max = 500)
    private String description;

    private LocalDateTime createAt;

    private List<ContractDto> contractDtos;
}