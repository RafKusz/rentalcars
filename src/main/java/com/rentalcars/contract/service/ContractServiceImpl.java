package com.rentalcars.contract.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.contract.repository.ContractRepository;
import com.rentalcars.user.model.User;
import com.rentalcars.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.rentalcars.car.service.CarServiceImpl.CAR_DOES_NOT_EXIST_MESSAGE;
import static com.rentalcars.contract.model.mapper.ContractMapper.*;
import static com.rentalcars.user.service.UserServiceImpl.USER_DOES_NOT_EXIST_MESSAGE;

@Service
public class ContractServiceImpl implements ContractService {

    public static final String CONTRACT_NOT_FOUND_MESSAGE = "Contract not found with id: ";

    private ContractRepository contractRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, UserRepository userRepository, CarRepository carRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Override
    public List<ContractDto> getAll() {
        return CONTRACT_MAPPER.mapToContractDtos(contractRepository.findAll());
    }

    @Override
    public ContractDto getContract(Long id) throws ContractNotFoundException {
        Contract contractEntity = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException(CONTRACT_NOT_FOUND_MESSAGE + id));
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    public ContractDto createContract(ContractDto contractDto) throws UserNotFoundException, CarNotFoundException {
        Long userId = contractDto.getUserDto().getId();
        Long carId = contractDto.getCarDto().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + userId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(CAR_DOES_NOT_EXIST_MESSAGE + carId));
        Contract contractEntity = CONTRACT_MAPPER.mapToContractEntity(contractDto);
        contractEntity.setId(null);
        contractEntity.setUser(user);
        contractEntity.setCar(car); //TODO: jeżeli mamy klucze obce, to musimy zapisać w ten sposób, bo inaczej wywali nam błąd przy samym '.save()'
        contractEntity.setCreateAt(LocalDateTime.now());
        contractRepository.save(contractEntity);
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    public void deleteContract(Long id) throws ContractNotFoundException {
        Contract contractEntity = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException(CONTRACT_NOT_FOUND_MESSAGE + id));
        contractRepository.delete(contractEntity);
    }
}
