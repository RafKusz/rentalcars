package com.rentalcars.user.service;

import com.rentalcars.exceptions.EmailIsAlreadyExistException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.security.service.SecurityService;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.model.UserInput;
import com.rentalcars.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.rentalcars.security.role.Role.USER;
import static com.rentalcars.user.model.mapper.UserInputMapper.USER_INPUT_MAPPER;
import static com.rentalcars.user.model.mapper.UserMapper.MAPPER;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String USER_DOES_NOT_EXIST_MESSAGE = "User does not exist with id: ";
    private static final String USER_EXIST_WITH_THIS_EMAIL_MESSAGE = "User is already exist with this email: ";

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private SecurityService securityService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SecurityService securityService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityService = securityService;
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        log.info("Returned all users, actual number of users: {}", users.size());
        return MAPPER.mapToUserDtos(users);
    }

    public UserDto getUser(Long id) throws UserNotFoundException {
        User user = findUserById(id);
        log.info("Returned the user with id: {}", user.getId());
        return MAPPER.mapToUserDto(user);
    }

    public UserDto getLoggedInUser() throws UserNotFoundException {
        User user = securityService.getLoggedInUser();
        log.info("Returned the user with id: {}", user.getId());
        return MAPPER.mapToUserDto(user);
    }

    public UserDto createUser(UserInput userInput) throws EmailIsAlreadyExistException {
        checkEmailIsExist(userInput.getEmail());
        User user = prepareUser(userInput);
        userRepository.save(user);
        log.info("The new user was added with id: {}", user.getId());
        return MAPPER.mapToUserDto(user);
    }

    public UserDto updateLoggedInUser(UserInput userInput) throws UserNotFoundException, EmailIsAlreadyExistException {
        User user = securityService.getLoggedInUser();
        if ((userRepository.existsByEmail(userInput.getEmail())) && (!(userInput.getEmail().equals(user.getEmail())))) {
            log.info("Given email address: {} is already exists", userInput.getEmail());
            throw new EmailIsAlreadyExistException(USER_EXIST_WITH_THIS_EMAIL_MESSAGE + userInput.getEmail());
        }
        user.setName(userInput.getName());
        user.setSurname(userInput.getSurname());
        user.setEmail(userInput.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
        user.setRole(USER);
        user.setDescription(userInput.getDescription());
        userRepository.save(user);
        log.info("The user with id: {} was updated", user.getId());
        return MAPPER.mapToUserDto(user);
    }

    @Override
    public void deleteLoggedInUser() throws UserNotFoundException {
        User user = securityService.getLoggedInUser();
        log.info("The user with id: {} was deleted", user.getId());
        userRepository.delete(user);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        User user = findUserById(id);
        log.info("The user with id: {} was deleted", id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + id));
    }

    private void checkEmailIsExist(String email) throws EmailIsAlreadyExistException {
        if (userRepository.existsByEmail(email)) {
            log.info("Given email address: {} is already exists", email);
            throw new EmailIsAlreadyExistException(USER_EXIST_WITH_THIS_EMAIL_MESSAGE + email);
        }
    }

    private User prepareUser(UserInput userInput) {
        User user = USER_INPUT_MAPPER.mapToUserEntity(userInput);
        user.setId(null);
        user.setRole(USER);
        user.setCreateAt(LocalDateTime.now());
        user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
        return user;
    }
}