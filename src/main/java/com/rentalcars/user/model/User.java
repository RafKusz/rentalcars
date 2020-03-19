package com.rentalcars.user.model;

import com.rentalcars.contract.model.Contract;
import com.rentalcars.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "user_rental")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @SequenceGenerator(name = "user_rental_seq", sequenceName = "user_rental_seq", initialValue = 15, allocationSize = 10)
    @GeneratedValue(strategy = SEQUENCE, generator = "user_rental_seq")
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "user")
    private List<Contract> contracts;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
}