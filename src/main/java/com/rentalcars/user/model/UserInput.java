package com.rentalcars.user.model;

import com.rentalcars.user.model.annotation.PasswordMatcher;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatcher
public class UserInput {

    @ApiModelProperty(value = "name", example = "example name")
    @NotBlank
    @Size(max = 50)
    private String name;

    @ApiModelProperty(value = "surname", example = "example surname")
    @NotBlank
    @Size(max = 50)
    private String surname;

    @ApiModelProperty(value = "email", example = "example@email.com")
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(value = "password", example = "password")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "passwordConfirm", example = "password")
    @NotBlank
    private String passwordConfirm;

    @ApiModelProperty(value = "description", example = "example description")
    @Size(max = 500)
    private String description;
}
