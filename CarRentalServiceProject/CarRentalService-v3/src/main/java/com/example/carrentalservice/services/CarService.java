package com.example.carrentalservice.services;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.repositories.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;


    public String addCar(Car car) {
        boolean carMarkExists = carRepository.findByCarMark(car.getCarMark())
                .isPresent();

            if (carMarkExists) {

                throw new ApiRequestException("car Mark already taken");
            }

        carRepository.save(car);
        return "Car added successfully";
    }

    public Car getCar(Long id) {
        return carRepository.findByCarId(id);
    }

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public String updateCarCost(Long carId, Long carCost) {
        carRepository.updateCar(carId, carCost);
        return "Car's cost updated Successfully";

    }

    public String updatePrices(double coefficient) {
        carRepository.updatePrices(coefficient);
        return "Cars' costs updated Successfully";

    }

    public String deleteCarById(Long carId) {
        carRepository.deleteById(carId);
        return "Car deleted Successfully";
    }

    public String updateCarFeatures(Long carId, String carModel,
                                  String carClass, String carMark, Long carCost) {
        carRepository.updateCarFeatures(carId, carModel, carClass, carMark, carCost);
        return "Car updated successfully";
    }

    public String updateCarFeatures(Long carId, String carModel,
                                    String carClass, String carMark) {
        carRepository.updateCarFeatures(carId, carModel, carClass, carMark);
        return "Car updated successfully";
    }

    public String updateCarFeatures(Long carId,  String carModel, String carClass) {
        carRepository.updateCarFeatures(carId, carModel, carClass);
        return "Car updated successfully";
    }

    public String updateCarStatus(Long carId, String status) {
        carRepository.updateCarStatus(carId, status);
        return "status updated successfully";
    }

    public List<Car> getAvailableCars(String status) {
       return carRepository.findByCarStatus(status);
    }

    public List<Car> getCarsByCost(Long cost) {
        return carRepository.findByCarCost(cost);
    }

    public List<Car> getCarsByClass(String carClass) {
        return carRepository.findByCarClass(carClass);
    }

    public List<Car> getCarsByModel(String model) {
        return carRepository.findByCarModel(model);
    }
}
