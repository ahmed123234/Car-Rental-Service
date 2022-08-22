package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = ?2, c.carClass = ?3," +
             " c.carMark = ?4," +
             " c.carCost =  ?5 " +
             "WHERE c.carId = ?1")
     void updateCarFeatures(Long carId, String carModel,
                            String carClass, String carMark, Long carId1);


     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = ?2, c.carClass = ?3," +
             " c.carMark = ?4" +
             "WHERE c.carId = ?1")
     void updateCarFeatures(Long carId, String carModel, String carClass, String carMark);

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = ?2," +
             " c.carClass = ?3" +
             "WHERE c.carId = ?1")
     void updateCarFeatures(Long carId, String carModel, String carClass);

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET c.carStatus = ?2 WHERE c.carId = ?1")
     void updateCarStatus(Long carId, String status);

     List<Car> findByCarStatus(String status);
}
