package com.rentalcars.user.model.mapper;

import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserInput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInputMapper {

    UserInputMapper USER_INPUT_MAPPER = Mappers.getMapper(UserInputMapper.class);

    User mapToUserEntity(UserInput userInput);
}