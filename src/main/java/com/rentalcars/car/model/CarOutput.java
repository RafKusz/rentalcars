package com.rentalcars.car.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CarOutput {

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
}