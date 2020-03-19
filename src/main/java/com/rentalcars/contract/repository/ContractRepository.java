package com.rentalcars.contract.repository;

import com.rentalcars.contract.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findAllByUserId(Long userId);

    @Query("SELECT c FROM Contract c WHERE (c.dateOfRent BETWEEN :startRent AND :finishRent " +
            "OR c.dateOfReturn BETWEEN :startRent AND :finishRent " +
            "OR c.dateOfRent < :startRent AND c.dateOfReturn > :finishRent) " +
            "AND c.car.id = :carId")
    List<Contract> findContractsFromPeriodAndCarId(@Param("startRent") LocalDate startRent,
                                                   @Param("finishRent") LocalDate finishRent,
                                                   @Param("carId") Long carId);

    @Query("SELECT c FROM Contract c WHERE c.dateOfRent > now()" +
            "ORDER BY c.dateOfRent ASC")
    List<Contract> findAllFutureContracts();

    @Query("SELECT c FROM Contract c WHERE c.dateOfRent > now() " +
            "AND c.user.id = :userId " +
            "ORDER BY c.dateOfRent ASC")
    List<Contract> findAllFutureContractsByUserId(@Param("userId") Long userId);
}