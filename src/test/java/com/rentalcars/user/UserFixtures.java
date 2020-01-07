package com.rentalcars.user;

import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;

public class UserFixtures {

    public static final Long EXISTED_ID = 1L;
    public static final Long EXISTED_USER_ID_WITH_CONTRACT = 2L;
    public static final Long NOT_EXISTED_ID = 5L;
    public static final String NAME = "Name";
    public static final String SURNAME = "Surname";
    public static final String EMAIL = "email@poczta.pl";
    public static final String DESCRIPTION = "description";

    public static User getUser() {
        return User.builder()
                .id(EXISTED_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .description(DESCRIPTION)
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(EXISTED_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .description(DESCRIPTION)
                .build();
    }

    public static UserDto getUpdateUserDto() {
        return UserDto.builder()
                .id(EXISTED_ID)
                .name("new name")
                .surname("new surname")
                .email("newemail@new.pl")
                .description("new description")
                .build();
    }
}
