package com.rentalcars.contract.model.mapper;

import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContractInputMapper {

    ContractInputMapper CONTRACT_INPUT_MAPPER = Mappers.getMapper(ContractInputMapper.class);

    @Mappings({
            @Mapping(target = "car", ignore = true),
            @Mapping(target = "user", ignore = true)})
    Contract mapToContractEntity(ContractInput contractInput);
}