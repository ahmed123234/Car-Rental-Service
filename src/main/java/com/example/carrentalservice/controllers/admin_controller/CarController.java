package com.example.carrentalservice.controllers.admin_controller;

import com.example.carrentalservice.car.Car;
import com.example.carrentalservice.car.CarRequest;
import com.example.carrentalservice.car.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/cars")
public class CarController {

    private final CarService carService;

    @PostMapping("/add")
    public String addCar(@RequestBody CarRequest carRequest) {

        return carService.addCar(new Car(
                carRequest.getCarClass(),
                carRequest.getCarModel(),
                carRequest.getCarCost(),
                carRequest.getCarMark()
        ));
    }

    @GetMapping("{carId}")
    public Car getCar(@PathVariable Long carId) {
        return carService.getCar(carId);
    }

    @GetMapping
    List<Car> getCars() {
        return carService.getCars();
    }

    @PutMapping("update-cost")
    public String updateCarCost(@RequestBody Long carId, Long carCost) {
       return carService.updateCarCost(carId, carCost);
    }
    @PutMapping("update-prices{coefficient}")
    public String updatePrices(@PathVariable double coefficient) {
       return carService.updatePrices(coefficient);
    }

    @DeleteMapping("delete{carId}")
    public String deleteCarById(@PathVariable Long carId) {
        return carService.deleteCarById(carId);
    }
}
