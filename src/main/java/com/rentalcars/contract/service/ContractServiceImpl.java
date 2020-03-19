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
import com.rentalcars.security.service.SecurityService;
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
    private static final String CANNOT_GET_CONTRACT_MESSAGE = "Cannot get Contract which is not yours";
    private static final String CANNOT_DELETE_CONTRACT_MESSAGE = "Cannot delete Contract which is not yours";

    private ContractRepository contractRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private SecurityService securityService;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, CarRepository carRepository,
                               UserRepository userRepository, SecurityService securityService) {
        this.contractRepository = contractRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
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
    public List<ContractDto> getAllContractsByLoggedInUser() throws UserNotFoundException {
        List<Contract> contracts = contractRepository.findAllByUserId(securityService.getLoggedInUser().getId());
        log.info("Returned {} contracts for logged-in user", contracts.size());
        return CONTRACT_MAPPER.mapToContractDtos(contracts);
    }

    @Override
    public List<ContractDto> getFutureContracts() {
        List<Contract> contracts = contractRepository.findAllFutureContracts();
        log.info("Returned all {} future contracts", contracts.size());
        return CONTRACT_MAPPER.mapToContractDtos(contracts);
    }

    @Override
    public List<ContractDto> getFutureContractsByLoggedInUser() throws UserNotFoundException {
        List<Contract> contracts = contractRepository.findAllFutureContractsByUserId(securityService.getLoggedInUser().getId());
        log.info("Returned all {} future contracts for logged-in user", contracts.size());
        return CONTRACT_MAPPER.mapToContractDtos(contracts);
    }

    @Override
    public ContractDto getContract(Long id) throws ContractNotFoundException {
        Contract contractEntity = findContractById(id);
        log.info("Returned the contract with id: {}", contractEntity.getId());
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    @Override
    public ContractDto getMyContract(Long id) throws ContractNotFoundException, UserNotFoundException, ContractUnavailableException {
        Long userId = securityService.getLoggedInUser().getId();
        Contract contractEntity = findContractById(id);
        if (!userId.equals(contractEntity.getUser().getId())) {
            throw new ContractUnavailableException(CANNOT_GET_CONTRACT_MESSAGE);
        }
        return CONTRACT_MAPPER.mapToContractDto(contractEntity);
    }

    @Override
    public ContractDto createContract(ContractInput contractInput) throws ContractUnavailableException, UserNotFoundException, CarNotFoundException {
        Contract contract = prepareContract(contractInput);
        contractRepository.save(contract);
        log.info("The new contract was added with id: {}", contract.getId());
        return CONTRACT_MAPPER.mapToContractDto(contract);
    }

    @Override
    public void deleteContract(Long id) throws ContractNotFoundException {
        Contract contract = findContractById(id);
        log.info("The contract with id: {} was deleted", id);
        contractRepository.delete(contract);
    }

    @Override
    public void deleteContractForLoggedInUser(Long contractId) throws UserNotFoundException, ContractNotFoundException, ContractUnavailableException {
        Long userId = securityService.getLoggedInUser().getId();
        Contract contract = findContractById(contractId);
        if (!userId.equals(contract.getUser().getId())) {
            throw new ContractUnavailableException(CANNOT_DELETE_CONTRACT_MESSAGE);
        }
        contractRepository.delete(contract);
    }

    private Contract findContractById(Long id) throws ContractNotFoundException {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException(CONTRACT_NOT_FOUND_MESSAGE + id));
    }

    private Contract prepareContract(ContractInput contractInput) throws CarNotFoundException, ContractUnavailableException, UserNotFoundException {
        checkIfDatesAreAvailable(contractInput.getDateOfRent(), contractInput.getDateOfReturn(), contractInput.getCarId());
        Contract contract = CONTRACT_INPUT_MAPPER.mapToContractEntity(contractInput);
        contract.setId(null);
        contract.setCar(findCarById(contractInput.getCarId()));
        contract.setUser(securityService.getLoggedInUser());
        contract.setCreateAt(LocalDateTime.now());
        return contract;
    }

    private Car findCarById(Long id) throws CarNotFoundException {
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