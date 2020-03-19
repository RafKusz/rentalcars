package com.rentalcars.user.controller;

import com.rentalcars.exceptions.EmailIsAlreadyExistException;
import com.rentalcars.exceptions.ReplyStatus;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.model.UserInput;
import com.rentalcars.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserAccessToUserController {

    private UserService userService;

    @Autowired
    public UserAccessToUserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = UserDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting my profile")
    @GetMapping("/me")
    public UserDto getMyProfile() throws UserNotFoundException {
        return userService.getLoggedInUser();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class),
            @ApiResponse(code = 409, message = "Conflict", response = ReplyStatus.class)})
    @ApiOperation(value = "Updating my profile")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/me")
    public UserDto updateMyProfile(@Valid @RequestBody UserInput userInput) throws UserNotFoundException, EmailIsAlreadyExistException {
        return userService.updateLoggedInUser(userInput);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Deleting my profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    public void deleteMyProfile() throws UserNotFoundException {
        userService.deleteLoggedInUser();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ReplyStatus.class),
            @ApiResponse(code = 409, message = "Conflict", response = ReplyStatus.class)})
    @ApiOperation(value = "Creating a new user")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserInput userInput) throws EmailIsAlreadyExistException {
        return userService.createUser(userInput);
    }
}