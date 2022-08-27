package com.example.carrentalservice.services.car;

import com.example.carrentalservice.models.entities.Car;

import java.util.List;

public interface CarService {

    String addCar(Car car);

    Car getCar(Long id);

    List<Car> getCars();

    String updateCarCost(Long carId, Long carCost);

    String updatePrices(double coefficient);

    String deleteCarById(Long carId);

    String updateCarFeatures(Long carId, String carModel, String carClass, String carMark, Long carCost);

    String updateCarFeatures(Long carId, String carModel, String carClass, String carMark);

    String updateCarFeatures(Long carId,  String carModel, String carClass);

    String updateCarStatus(Long carId, String status);

    List<Car> getAvailableCars(String status);

    List<Car> getCarsByCost(Long cost);

    List<Car> getCarsByClass(String carClass);

    List<Car> getCarsByModel(String model);
}
