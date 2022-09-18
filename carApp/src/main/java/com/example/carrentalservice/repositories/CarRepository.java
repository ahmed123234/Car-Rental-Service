package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.Car;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

     Car findByCarId(Long id);

     @Transactional
     @Modifying
     @Query("UPDATE Car SET carCost = :carCost where carId = :carId")
     int updateCar(Long carId, Long carCost);


     @Transactional
     @Modifying
     @Query("UPDATE Car SET carCost = carCost * :coefficient")
     int updatePrices(Long coefficient);

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = :model, c.carClass = :class," +
             " c.carMark = :mark," +
             " c.carCost =  :cost " +
             "WHERE c.carId = :id")
     int updateCarFeatures(@Param("id") Long carId,
                           @Param("model") String carModel,
                           @Param("class") String carClass,
                           @Param("mark") String carMark,
                           @Param("cost") Long carCost);


     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = :model, c.carClass = :class, c.carMark = :mark WHERE c.carId = :id")
     int updateCarModelClassAndMark(@Param("id") Long carId,
                                    @Param("model") String carModel,
                                    @Param("class") String carClass,
                                    @Param("mark") String carMark);

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = :model, c.carClass = :class WHERE c.carId = :id")
     int updateCarModelAndClass(@Param("id") Long carId,
                                @Param("model") String carModel,
                                @Param("class") String carClass);

    @Transactional
    @Modifying
    @Query("UPDATE Car c SET  c.carModel = ?2, c.carMark = ?3 WHERE c.carId = ?1")
    int updateCarModelAndMark(Long carId, String carModel, String carMark);


    @Transactional
     @Modifying
     @Query("UPDATE Car c SET  c.carModel = ?2 WHERE c.carId = ?1")
     int updateCarModel(Long carId, String carModel);

     @Transactional
     @Modifying
     @Query("UPDATE Car c SET c.carStatus = ?2 WHERE c.carId = ?1")
     int updateCarStatus(Long carId, String status);

     List<Car> findByCarStatus(String status, Sort sort);

    Optional<Car> findByCarMark(String carMark);

    List<Car> findByCarCost(Long cost);
    @Query("SELECT c from Car c where c.carCost >= ?1")
    List<Car> findByCarCostGrater(Long cost, Sort sort);

    @Query("SELECT c from Car c where c.carCost <= ?1")
    List<Car> findByCarCostLess(Long cost, Sort sort);

    List<Car> findByCarClass(String carClass);

    List<Car> findByCarModel(String model);

}
