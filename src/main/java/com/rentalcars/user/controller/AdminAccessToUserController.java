package com.rentalcars.user.controller;

import com.rentalcars.exceptions.ReplyStatus;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class AdminAccessToUserController {

    private UserService userService;

    @Autowired
    AdminAccessToUserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting all users")
    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = UserDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Getting a user by Id")
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not found", response = ReplyStatus.class)})
    @ApiOperation(value = "Deleting a user by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }
}