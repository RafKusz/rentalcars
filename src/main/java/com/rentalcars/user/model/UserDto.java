package com.rentalcars.user.model;

import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

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

    private LocalDateTime createAt;

    private List<ContractDto> contractDtos;

    @Enumerated(EnumType.STRING)
    private Role role;
}