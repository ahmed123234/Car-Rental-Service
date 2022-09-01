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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Car> getCar(@PathVariable Long carId) {
        return ResponseEntity.ok().body(carServiceImpl.getCar(carId));
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<Car>> getCars() {
        return ResponseEntity.ok().body(carServiceImpl.getCars());
    }


    @PutMapping("/cost/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public RestResponse updateCarCost(@RequestParam("carId") Long carId, @RequestParam("carCost") Long carCost) {
        String message = carServiceImpl.updateCarCost(carId, carCost);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping("/costs/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public RestResponse updatePrices(@RequestParam ("coefficient") Long coefficient) {
        String message = carServiceImpl.updatePrices(coefficient);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse deleteCarById(@RequestParam("carId") Long carId) {
        String message = carServiceImpl.deleteCarById(carId);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse updateCarInfo(@RequestParam (value = "id") Long carId,
                                      @RequestParam (value = "model") String carModel,
                                      @RequestParam (value = "class", required = false) String carClass,
                                      @RequestParam (value = "mark", required = false) String carMark,
                                      @RequestParam (value = "cost", required = false) Long carCost) {
        String message;

            if (carClass != null) {
                if (carMark != null) {
                    if (carCost != null)
                        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark, carCost);
                    else
                        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark);

                }
                else {
                    if (carCost != null) {
                        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass);
                        carServiceImpl.updateCarCost(carId, carCost);
                    }
                    else
                        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass);
                }
            }else {
                if (carMark != null) {
                    if (carCost != null) {
                        message = carServiceImpl.updateCarFeatures(carId, carModel, carMark);
                        carServiceImpl.updateCarCost(carId, carCost);
                    } else
                        message = carServiceImpl.updateCarMark(carId, carModel, carMark);
                } else {
                    if (carCost != null) {
                        message = carServiceImpl.updateCarFeatures(carId, carModel);
                        carServiceImpl.updateCarCost(carId, carCost);
                    }
                    else
                        message = carServiceImpl.updateCarFeatures(carId, carModel);
                }
            }

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @PutMapping("/update/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public ResponseEntity<List<Car>> getAvailableCars() {

        return ResponseEntity.ok().body(carServiceImpl.getAvailableCars("available"));
    }

    @GetMapping("/cost/get")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public ResponseEntity<List<Car>> getCarsByCost(@RequestParam("cost") Long cost, @RequestParam("operand") String operation) {

         return ResponseEntity.ok().body(carServiceImpl.getCarsByCost(cost, operation));
    }


    @GetMapping("/class/get")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public ResponseEntity<List<Car>> getCarsByClass(@RequestParam ("class") String carClass) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByClass(carClass));
    }

    @GetMapping("/model/get")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public ResponseEntity<List<Car>> getCarsByModel(@RequestParam ("model") String model) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByModel(model));
    }

}
