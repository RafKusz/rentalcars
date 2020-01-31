package com.rentalcars.contract.service;

import com.rentalcars.car.model.Car;
import com.rentalcars.car.repository.CarRepository;
import com.rentalcars.contract.model.Contract;
import com.rentalcars.contract.model.ContractDto;
import com.rentalcars.contract.model.ContractInput;
import com.rentalcars.contract.repository.ContractRepository;
import com.rentalcars.exceptions.CarNotFoundException;
import com.rentalcars.exceptions.ContractNotFoundException;
import com.rentalcars.exceptions.ContractUnavailableException;
import com.rentalcars.exceptions.UserNotFoundException;
import com.rentalcars.user.model.User;
import com.rentalcars.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.rentalcars.car.service.CarServiceImpl.CAR_DOES_NOT_EXIST_MESSAGE;
import static com.rentalcars.contract.model.mapper.ContractInputMapper.CONTRACT_INPUT_MAPPER;
import static com.rentalcars.contract.model.mapper.ContractMapper.CONTRACT_MAPPER;
import static com.rentalcars.user.service.UserServiceImpl.USER_DOES_NOT_EXIST_MESSAGE;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    private static final String CONTRACT_NOT_FOUND_MESSAGE = "Contract not found with id: ";

    private ContractRepository contractRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, CarRepository carRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ContractDto> getAll() {
        List<Contract> contracts = contractRepository.findAll();
        log.info("Returned all contracts, actual number of contracts: {}", contracts.size());
        return CONTRACT_MAPPER.mapToContractDtos(contracts);
    }

    @Override
    public List<ContractDto> getAllContractsByUser(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + userId);
        }
        List<Contract> contracts = contractRepository.findAllByUserId(userId);
        log.info("Returned {} contracts for user's id {}", contracts.size(), userId);
        return CONTRACT_MAPPER.mapToContractDtos(contracts);
    }

    @Override
    public ContractDto getContract(Long id) throws ContractNotFoundException {
        Contract contractEntity = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException(CONTRACT_NOT_FOUND_MESSAGE + id));
        log.info("Returned the contract with id: {}", contractEntity.getId());
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    @Override
    public ContractDto createContract(ContractInput contractInput) throws UserNotFoundException, CarNotFoundException, ContractUnavailableException {
        checkIfDatesAreAvailable(contractInput.getDateOfRent(), contractInput.getDateOfReturn(), contractInput.getCarId());

        Contract contractEntity = CONTRACT_INPUT_MAPPER.mapToContractEntity(contractInput);
        contractEntity.setId(null);
        contractEntity.setUser(getUser(contractInput.getUserId()));
        contractEntity.setCar(getCar(contractInput.getCarId()));
        contractEntity.setCreateAt(LocalDateTime.now());

        contractRepository.save(contractEntity);
        log.info("The new contract was added with id: {}", contractEntity.getId());
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    @Override
    public void deleteContract(Long id) throws ContractNotFoundException {
        Contract contractEntity = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException(CONTRACT_NOT_FOUND_MESSAGE + id));
        log.info("The contract with id: {} was deleted", id);
        contractRepository.delete(contractEntity);
    }

    private User getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST_MESSAGE + id));
    }

    private Car getCar(Long id) throws CarNotFoundException {
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(CAR_DOES_NOT_EXIST_MESSAGE + id));
    }

    private void checkIfDatesAreAvailable(LocalDate dateOfRent, LocalDate dateOfReturn, Long carId) throws ContractUnavailableException {
        List<Contract> contractsListFromPeriodAndCarId = contractRepository.findContractsFromPeriodAndCarId(dateOfRent, dateOfReturn, carId);
        if (contractsListFromPeriodAndCarId.size() > 0) {
            throw new ContractUnavailableException(getMessageNotAvailableRangeOfDates(contractsListFromPeriodAndCarId, carId));
        }
    }

    private String getMessageNotAvailableRangeOfDates(List<Contract> contractList, Long carId) {
        StringBuilder message = new StringBuilder()
                .append("The car with id: ")
                .append(carId)
                .append(", is not available in this range of dates. Try another dates excluding:");
        for (Contract existedContract : contractList) {
            message.append(" from ")
                    .append(existedContract.getDateOfRent())
                    .append(" to ")
                    .append(existedContract.getDateOfReturn())
                    .append(",");
        }
        return message.deleteCharAt(message.length() - 1).toString();
    }
}