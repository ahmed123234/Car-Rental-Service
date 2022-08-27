package com.example.carrentalservice.services.car;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.repositories.CarRepository;
import com.example.carrentalservice.services.car.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public String addCar(Car car) {
        boolean carMarkExists = carRepository.findByCarMark(car.getCarMark()).isPresent();

            if (carMarkExists) {
                throw new ApiRequestException("car Mark already taken");
            }

        carRepository.save(car);
        return "Car added successfully";
    }

    @Override
    public Car getCar(Long id) {
        return carRepository.findByCarId(id);
    }

    @Override
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    @Override
    public String updateCarCost(Long carId, Long carCost) {
        carRepository.updateCar(carId, carCost);
        return "Car's cost updated Successfully";
    }

    @Override
    public String updatePrices(double coefficient) {
        carRepository.updatePrices(coefficient);
        return "Cars' costs updated Successfully";
    }

    @Override
    public String deleteCarById(Long carId) {
        carRepository.deleteById(carId);
        return "Car deleted Successfully";
    }

    @Override
    public String updateCarFeatures(Long carId, String carModel,
                                  String carClass, String carMark, Long carCost) {
        carRepository.updateCarFeatures(carId, carModel, carClass, carMark, carCost);
        return "Car updated successfully";
    }

    @Override
    public String updateCarFeatures(Long carId, String carModel,
                                    String carClass, String carMark) {
        carRepository.updateCarFeatures(carId, carModel, carClass, carMark);
        return "Car updated successfully";
    }

    @Override
    public String updateCarFeatures(Long carId,  String carModel, String carClass) {
        carRepository.updateCarFeatures(carId, carModel, carClass);
        return "Car updated successfully";
    }

    @Override
    public String updateCarStatus(Long carId, String status) {
        carRepository.updateCarStatus(carId, status);
        return "status updated successfully";
    }

    @Override
    public List<Car> getAvailableCars(String status) {
       return carRepository.findByCarStatus(status);
    }

    @Override
    public List<Car> getCarsByCost(Long cost) {
        return carRepository.findByCarCost(cost);
    }

    @Override
    public List<Car> getCarsByClass(String carClass) {
        return carRepository.findByCarClass(carClass);
    }

    @Override
    public List<Car> getCarsByModel(String model) {
        return carRepository.findByCarModel(model);
    }
}
