package com.rentalcars.user.service;

import com.rentalcars.exceptions.EmailIsAlreadyExistException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.security.service.SecurityService;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.rentalcars.user.UserFixtures.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, bCryptPasswordEncoder, securityService);
    }

    @Test
    @DisplayName("Getting all users return list of users")
    public void returnListOfUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(singletonList(getUser()));

        List<UserDto> userDtoList = userService.getUsers();

        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
    }

    @Test
    @DisplayName("Getting a user returns a user if id is exist")
    public void returnUserIfIdIsExisted() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(ofNullable(getUser()));

        UserDto userDto = userService.getUser(EXISTED_USER_ID);

        assertNotNull(userDto);
        assertEquals(EXISTED_USER_ID, userDto.getId());
    }

    @Test
    @DisplayName("Getting a user throw exception if id is not exist")
    public void throwExceptionIfUserIdDoesNotExisted() {
        Mockito.when(userRepository.findById(NOT_EXISTED_USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(NOT_EXISTED_USER_ID));
    }

    @Test
    @DisplayName("Getting a logged-in user returns a user if it is valid")
    public void returnLoggedInUserIfItIsValid() throws UserNotFoundException {
        Mockito.when(securityService.getLoggedInUser()).thenReturn(getUser());

        UserDto userDto = userService.getLoggedInUser();

        assertNotNull(userDto);
        assertEquals(EXISTED_USER_ID, userDto.getId());
    }

    @Test
    @DisplayName("Creating a user creates new user if it is valid")
    public void createNewUserIfItIsValid() throws EmailIsAlreadyExistException {
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> getUserFromMock((User) i.getArguments()[0]));
        Mockito.when(bCryptPasswordEncoder.encode(anyString())).thenCallRealMethod();

        UserDto userDto = userService.createUser(getUserInput());

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getCreateAt());
        assertNotNull(userDto.getPassword());
        assertEquals(NAME, userDto.getName());
        assertEquals(SURNAME, userDto.getSurname());
        assertEquals(EMAIL, userDto.getEmail());
        assertEquals(ROLE, userDto.getRole());
        assertEquals(DESCRIPTION, userDto.getDescription());
    }

    private Object getUserFromMock(User user) {
        user.setId(1L);
        return user;
    }

    @Test
    @DisplayName("Updating a logged-in user, update a user if it is valid")
    public void updateLoggedInUserIfItIsValid() throws Exception {
        Mockito.when(securityService.getLoggedInUser()).thenReturn(getUser());
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(bCryptPasswordEncoder.encode(anyString())).thenCallRealMethod();

        UserDto userDto = userService.updateLoggedInUser(getUpdateUserInput());

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getPassword());
        assertNotEquals(NAME, userDto.getName());
        assertNotEquals(SURNAME, userDto.getSurname());
        assertNotEquals(EMAIL, userDto.getEmail());
        assertNotEquals(DESCRIPTION, userDto.getDescription());
        assertEquals(ROLE, userDto.getRole());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deleting a user do not throw exception if a user to delete is valid")
    public void doNotThrowExceptionIfUserToDeleteIsValid() throws Exception {
        Mockito.when(userRepository.findById(EXISTED_USER_ID)).thenReturn(ofNullable(getUser()));
        Mockito.doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUser(EXISTED_USER_ID);
    }

    @Test
    @DisplayName("Deleting a logged-in user do not throw exception if a user to delete is valid")
    public void doNotThrowExceptionIfLoggedInUserToDeleteIsValid() throws Exception {
        Mockito.when(securityService.getLoggedInUser()).thenReturn(getUser());
        Mockito.doNothing().when(userRepository).delete(any(User.class));

        userService.deleteLoggedInUser();
    }
}