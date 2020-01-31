package com.rentalcars.car.repository;

import com.rentalcars.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.id NOT IN " +
            "(SELECT cc.id FROM Car cc INNER JOIN Contract con ON cc.id = con.car " +
            "WHERE con.dateOfRent BETWEEN :startRent AND :finishRent " +
            "OR con.dateOfReturn BETWEEN :startRent AND :finishRent " +
            "OR con.dateOfRent < :startRent AND con.dateOfReturn > :finishRent)")
    List<Car> findAvailableCarsByDate(@Param("startRent") LocalDate startRent,
                                      @Param("finishRent") LocalDate finishRent);
}