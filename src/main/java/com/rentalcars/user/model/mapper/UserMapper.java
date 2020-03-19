package com.rentalcars.user.model.mapper;

import com.rentalcars.contract.model.mapper.ContractMapper;
import com.rentalcars.user.model.User;
import com.rentalcars.user.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ContractMapper.class})
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "contracts", target = "contractDtos")
    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDtos(List<User> users);
}