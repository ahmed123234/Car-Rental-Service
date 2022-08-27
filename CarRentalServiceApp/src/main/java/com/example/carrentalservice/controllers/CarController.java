package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.handelers.CarRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/cars")
@Transactional
public class CarController {
    private final CarServiceImpl carServiceImpl;

    // allow the admin to add a car
    @PostMapping("/add")
    public ResponseEntity<RestResponse> addCar(@RequestBody CarRequest carRequest) {

        String message = carServiceImpl.addCar(new Car(
                    carRequest.getCarClass(),
                    carRequest.getCarModel(),
                    carRequest.getCarCost(),
                    carRequest.getCarMark(),
                    "available"
            ));

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return ResponseEntity.ok().body(new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        ));
    }


    @GetMapping("{carId}")
    public ResponseEntity<Car> getCar(@PathVariable Long carId) {
        return ResponseEntity.ok().body(carServiceImpl.getCar(carId));
    }


    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        return ResponseEntity.ok().body(carServiceImpl.getCars());
    }


    @PutMapping("update/cost")
    public RestResponse updateCarCost(@RequestBody Long carId, Long carCost) {
        String message = carServiceImpl.updateCarCost(carId, carCost);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping("update/prices{coefficient}")
    public RestResponse updatePrices(@PathVariable double coefficient) {
        String message = carServiceImpl.updatePrices(coefficient);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @DeleteMapping("delete{carId}")
    public RestResponse deleteCarById(@PathVariable Long carId) {
        String message = carServiceImpl.deleteCarById(carId);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @PutMapping("/update{carId}")
    public RestResponse updateCarInfo(@PathVariable Long carId,
                                                @RequestBody  String carModel,
                                                @RequestBody (required = false) String carClass,
                                                @RequestBody (required = false) String carMark,
                                                @RequestBody (required = false) Long carCost) {
        String message = "";

            if (carClass != null && carMark != null && carCost != 0)
                message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark, carCost);

            else if (carClass != null && carMark != null && carCost == 0)
                message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark);

            else if (carClass != null && carMark == null && carCost == 0)
                message = carServiceImpl.updateCarFeatures(carId, carModel, carClass);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @PutMapping("/update/status")
    public RestResponse updateCarStatus (@RequestParam Long carId,
                                   @RequestParam String status) {
        String message = carServiceImpl.updateCarStatus(carId, status);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {

        return ResponseEntity.ok().body(carServiceImpl.getAvailableCars("available"));
    }

    @GetMapping("/get{cost}")
    public ResponseEntity<List<Car>> getCarsByCost(@PathVariable Long cost) {

        return ResponseEntity.ok().body(carServiceImpl.getCarsByCost(cost));
    }


    @GetMapping("/get{class}")
    public ResponseEntity<List<Car>> getCarsByClass(@PathVariable ("class") String carClass) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByClass(carClass));
    }

    @GetMapping("/get{model}")
    public ResponseEntity<List<Car>> getCarsByModel(@PathVariable ("model") String model) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByModel(model));
    }

}
