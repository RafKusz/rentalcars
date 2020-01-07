package com.rentalcars.user.service;

import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.rentalcars.user.UserFixtures.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
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

        UserDto userDto = userService.getUser(EXISTED_ID);

        assertNotNull(userDto);
        assertEquals(EXISTED_ID, userDto.getId());
    }

    @Test
    @DisplayName("Getting a user throw exception if id is not exist")
    public void throwExceptionIfUserIdDoesNotExisted() {
        Mockito.when(userRepository.findById(NOT_EXISTED_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(NOT_EXISTED_ID));
    }

    @Test
    @DisplayName("Creating a user creates new user if it is valid")
    public void createNewUserIfItIsValid() {
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> getUserFromMock((User) i.getArguments()[0]));

        UserDto userDto = userService.createUser(getUserDto());

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getCreateAt());
        assertEquals(NAME, userDto.getName());
        assertEquals(SURNAME, userDto.getSurname());
        assertEquals(EMAIL, userDto.getEmail());
        assertEquals(DESCRIPTION, userDto.getDescription());
    }

    private Object getUserFromMock(User user) {
        user.setId(1L);
        return user;
    }

    @Test
    @DisplayName("Updating a user update a user if it is valid")
    public void updateUserIfItIsValid() throws Exception {
        Mockito.when(userRepository.findById(EXISTED_ID)).thenReturn(ofNullable(getUser()));

        UserDto userDto = userService.updateUser(getUpdateUserDto(), EXISTED_ID);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotEquals(NAME, userDto.getName());
        assertNotEquals(SURNAME, userDto.getSurname());
        assertNotEquals(EMAIL, userDto.getEmail());
        assertNotEquals(DESCRIPTION, userDto.getDescription());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deleting a user do not throw exception if a user to delete is valid")
    public void doNotThrowExceptionIfUserToDeleteIsValid() throws Exception {
        Mockito.when(userRepository.findById(EXISTED_ID)).thenReturn(ofNullable(getUser()));
        Mockito.doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUser(EXISTED_ID);
    }
}