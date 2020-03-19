package com.rentalcars.car.model;

import com.rentalcars.contract.model.ContractDto;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "brand", example = "example brand")
    @NotBlank
    @Size(max = 50)
    private String brand;

    @ApiModelProperty(value = "model", example = "example model")
    @NotBlank
    @Size(max = 50)
    private String model;

    @ApiModelProperty(value = "productionYear", example = "2010")
    @NotNull
    private Integer productionYear;

    @ApiModelProperty(value = "priceOfRent", example = "100")
    @NotNull
    @Min(0)
    private Double priceOfRent;

    @ApiModelProperty(value = "description", example = "example description")
    @Size(max = 500)
    private String description;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createAt;

    @ApiModelProperty(hidden = true)
    private List<ContractDto> contractDtos;
}