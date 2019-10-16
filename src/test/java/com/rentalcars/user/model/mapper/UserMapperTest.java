package com.rentalcars.user.model.mapper;

import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.rentalcars.user.UserFixtures.*;
import static com.rentalcars.user.model.mapper.UserMapper.MAPPER;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void returnDtoIfUserEntityIsNotNull() {
        User user = getUser();

        UserDto userDto = MAPPER.mapToUserDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getSurname(), userDto.getSurname());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getDescription(), userDto.getDescription());
    }

    @Test
    void returnNullIfEntityIsNull() {
        User user = MAPPER.mapToUser(null);

        assertNull(user);
    }

    @Test
    void returnUserIfDtoIsNotNull() {
        UserDto userDto = getUserDto();

        User user = MAPPER.mapToUser(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getSurname(), user.getSurname());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getDescription(), user.getDescription());
    }

    @Test
    void returnNullIfDtoIsNull() {
        UserDto userDto = MAPPER.mapToUserDto(null);

        assertNull(userDto);
    }

    @Test
    void returnListOfUsersIfItIsValid() {
        List<UserDto> userDtos = Collections.singletonList(getUserDto());

        List<User> users = MAPPER.maptoUsers(userDtos);
        User returnedUser = users.get(0);

        assertNotNull(users);
        assertNotNull(returnedUser);
        assertEquals(EXPECTED_SIZE, users.size());
        assertEquals(EXISTED_ID, returnedUser.getId());
        assertEquals(NAME, returnedUser.getName());
        assertEquals(SURNAME, returnedUser.getSurname());
        assertEquals(EMAIL, returnedUser.getEmail());
        assertEquals(DESCRIPTION, returnedUser.getDescription());
    }


    @Test
    void returnListOfUserDtosIfItIsValid() {
        List<User> users = Collections.singletonList(getUser());

        List<UserDto> userDtos = MAPPER.mapToUserDtos(users);

        assertEquals(EXPECTED_SIZE, userDtos.size());
    }
}