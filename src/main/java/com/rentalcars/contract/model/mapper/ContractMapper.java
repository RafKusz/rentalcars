package com.rentalcars.contract.model.mapper;

import com.rentalcars.car.model.mapper.CarMapper;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.user.model.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CarMapper.class, UserMapper.class})
public interface ContractMapper {

    ContractMapper CONTRACT_MAPPER = Mappers.getMapper(ContractMapper.class);

    @Mappings({
            @Mapping(source = "carDto", target = "car"),
            @Mapping(target = "car.contracts", ignore = true),
            @Mapping(source = "userDto", target = "user"),
            @Mapping(target = "user.contracts", ignore = true)})
    Contract mapToContractEntity(ContractDto contractDto);

    @Mappings({
            @Mapping(source = "car", target = "carDto"),
            @Mapping(target = "carDto.contractDtos", ignore = true),
            @Mapping(source = "user", target = "userDto")})
            @Mapping(target = "userDto.contractDtos", ignore = true)
    ContractDto mapToContractDto(Contract contractEntity);

    List<Contract> mapToContracts(List<ContractDto> contractDtos);

    List<ContractDto> mapToContractDtos(List<Contract> contractEntities);
}
