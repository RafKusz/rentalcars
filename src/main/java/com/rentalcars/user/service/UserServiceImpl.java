package com.rentalcars.user.service;

import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import com.rentalcars.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.rentalcars.user.model.mapper.UserMapper.MAPPER;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_DOES_NOT_EXIST_MESSAGE = "User does not exist with id: ";

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsers() {
        return MAPPER.mapToUserDtos(userRepository.findAll());
    }

    public UserDto getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + id));
        return MAPPER.mapToUserDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        User user = MAPPER.mapToUser(userDto);
        user.setId(null);
        user.setCreateAt(LocalDateTime.now());
        userRepository.save(user);
        return MAPPER.mapToUserDto(user);
    }

    public UserDto updateUser(UserDto userDto, Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + id));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setDescription(userDto.getDescription());
        userRepository.save(user);
        return MAPPER.mapToUserDto(user);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + id));
        userRepository.delete(user);
    }
}