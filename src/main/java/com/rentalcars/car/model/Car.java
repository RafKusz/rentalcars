package com.rentalcars.car.model;

import com.rentalcars.contract.model.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @SequenceGenerator(name = "car_seq", sequenceName = "car_seq", initialValue = 10, allocationSize = 20)
    @GeneratedValue(strategy = SEQUENCE, generator = "car_seq")
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

    @NotNull
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "car")
    private List<Contract> contracts;
}