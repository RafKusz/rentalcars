package com.rentalcars.contract.model;

import com.rentalcars.car.model.Car;
import com.rentalcars.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    @Id
    @SequenceGenerator(name = "contract_seq", sequenceName = "contract_seq", initialValue = 10)
    @GeneratedValue(strategy = SEQUENCE, generator = "contract_seq")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_car", nullable = false)
    private Car car;

    @NotNull
    @FutureOrPresent
    private LocalDate dateOfRent;

    @NotNull
    @FutureOrPresent
    private LocalDate dateOfReturn;

    @NotNull
    private LocalDateTime createAt;
}