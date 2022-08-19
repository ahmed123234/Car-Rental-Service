package com.example.carrentalservice.car;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public String addCar(Car car) {
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

}
