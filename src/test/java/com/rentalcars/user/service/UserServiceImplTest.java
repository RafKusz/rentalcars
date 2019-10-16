package com.rentalcars.user.service;

import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.rentalcars.user.UserFixtures.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void returnListOfUsers() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(singletonList(getUser()));

        List<UserDto> userDtoList = userService.getUsers();

        assertNotNull(userDtoList);
        assertEquals(EXPECTED_SIZE, userDtoList.size());
    }

    @Test
    public void returnUserIfIdIsExisted() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(ofNullable(getUser()));

        UserDto userDto = userService.getUser(EXISTED_ID);

        assertNotNull(userDto);
        assertEquals(EXISTED_ID, userDto.getId());
    }

    @Test
    public void throwExceptionIfUserIdDoesNotExisted() throws Exception {
        Mockito.when(userRepository.findById(NOT_EXISTED_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(NOT_EXISTED_ID));
    }

    @Test
    public void createNewUserIfItIsValid() throws Exception {
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> getUserFromMock((User) i.getArguments()[0]));
//        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> {
//            User user = (User) i.getArguments()[0];
//            user.setId(1L);
//            return user;
//        });

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
    public void doNotThrowExceptionIfUserToDeleteIsValid() throws Exception {
        Mockito.when(userRepository.findById(EXISTED_ID)).thenReturn(ofNullable(getUser()));
        Mockito.doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUser(EXISTED_ID);
    }


}