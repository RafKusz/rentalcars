package com.rentalcars.security.service;

import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.User;

public interface SecurityService {

    User getLoggedInUser() throws UserNotFoundException;

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}