package com.rentalcars.user;

import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;

public class UserFixtures {

    public static final Long EXISTED_ID = 1L;
    public static final Long NOT_EXISTED_ID = 5L;
    public static final String NAME = "Name";
    public static final String SURNAME = "Surname";
    public static final String EMAIL = "email@poczta.pl";
    public static final String DESCRIPTION = "description";
    public static final int EXPECTED_SIZE = 1;

    public static User getUser() {
        User user = User.builder()
                .id(EXISTED_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .description(DESCRIPTION)
                .build();
        return user;
    }

    public static UserDto getUserDto() {
        UserDto userDto = UserDto.builder()
                .id(EXISTED_ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .description(DESCRIPTION)
                .build();
        return userDto;
    }

    public static UserDto getUpdateUserDto() {
        UserDto updateUserDto = UserDto.builder()
                .id(EXISTED_ID)
                .name("new name")
                .surname("new surname")
                .email("newemail@new.pl")
                .description("new description")
                .build();
        return updateUserDto;
    }
}
