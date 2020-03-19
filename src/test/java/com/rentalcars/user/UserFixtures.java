package com.rentalcars.user;

import com.rentalcars.security.role.Role;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.model.UserInput;

import static com.rentalcars.security.role.Role.USER;

public class UserFixtures {

    public static final Long EXISTED_USER_ID = 1L;
    public static final Long EXISTED_USER_ID_WITH_CONTRACT = 2L;
    public static final Long NOT_EXISTED_USER_ID = 5L;
    public static final String NAME = "Name";
    public static final String SURNAME = "Surname";
    public static final String EMAIL = "email@poczta.pl";
    public static final String PASSWORD = "password";
    public static final Role ROLE = USER;
    public static final String DESCRIPTION = "description";

    public static User getUser() {
        return User.builder()
                .id(EXISTED_USER_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(ROLE)
                .description(DESCRIPTION)
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(EXISTED_USER_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(ROLE)
                .description(DESCRIPTION)
                .build();
    }

    public static UserInput getUserInput() {
        return UserInput.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .passwordConfirm(PASSWORD)
                .description(DESCRIPTION)
                .build();
    }

    public static UserInput getUpdateUserInput() {
        return UserInput.builder()
                .name("new name")
                .surname("new surname")
                .email("newemail@new.pl")
                .password("new password")
                .passwordConfirm("new password")
                .description("new description")
                .build();
    }
}