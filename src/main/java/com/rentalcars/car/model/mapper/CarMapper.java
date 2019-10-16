package com.rentalcars.car.model.mapper;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.model.CarDto;
import com.rentalcars.contract.model.mapper.ContractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ContractMapper.class})
public interface CarMapper {

    CarMapper MAPPER = Mappers.getMapper(CarMapper.class);

    @Mapping(source = "contractDtos", target = "contracts")
    Car mapToCar(CarDto carDto);

    @Mapping(source = "contracts", target = "contractDtos")
    CarDto mapToDto(Car car);

    List<Car> mapToCars(List<CarDto> carDtos);

    List<CarDto> mapToCarDtos(List<Car> cars);
}
