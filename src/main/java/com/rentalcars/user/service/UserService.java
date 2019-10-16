package com.rentalcars.user.service;

import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(Long id) throws UserNotFoundException;

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long id) throws UserNotFoundException;

    void deleteUser(Long id) throws UserNotFoundException;
}
