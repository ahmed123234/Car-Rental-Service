package com.example.carrentalservice.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

     Car findByCarId(Long id);

     @Transactional
     @Modifying
     @Query("UPDATE Car SET carCost = ?2 where carId = ?1")
     void updateCar(Long carId, Long carCost);


     @Transactional
     @Modifying
     @Query("UPDATE Car SET carCost = carCost * ?1")
     void updatePrices(double coefficient);
}
