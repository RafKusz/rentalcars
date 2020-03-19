package com.rentalcars.user.service;

import com.rentalcars.exceptions.EmailIsAlreadyExistException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.model.UserInput;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(Long id) throws UserNotFoundException;

    UserDto getLoggedInUser() throws UserNotFoundException;

    UserDto createUser(UserInput userInput) throws EmailIsAlreadyExistException;

    UserDto updateLoggedInUser(UserInput userInput) throws UserNotFoundException, EmailIsAlreadyExistException;

    void deleteUser(Long id) throws UserNotFoundException;

    void deleteLoggedInUser() throws UserNotFoundException;
}