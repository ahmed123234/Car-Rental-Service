package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.handelers.CarRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarServiceImpl carServiceImpl;

    // allow the admin to add a car
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<RestResponse> addCar(@Valid @RequestBody @NotNull CarRequest carRequest) {

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


    @GetMapping(value = "/get", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<Car> getCar(@RequestParam Long carId) {
        return ResponseEntity.ok().body(carServiceImpl.getCar(carId));
    }


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<List<Car>> getCars() {
        return ResponseEntity.ok().body(carServiceImpl.getCars());
    }


    @PutMapping(value = "/cost/update", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public @ResponseBody RestResponse updateCarCost(@RequestParam("carId") Long carId, @RequestParam("carCost") Long carCost) {
        String message = carServiceImpl.updateCarCost(carId, carCost);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping(value = "/costs/update", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public @ResponseBody RestResponse updatePrices(@RequestParam ("coefficient") Long coefficient) {
        String message = carServiceImpl.updatePrices(coefficient);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse deleteCarById(@RequestParam("carId") Long carId) {
        String message = carServiceImpl.deleteCarById(carId);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }


    @PutMapping(value = "/update/all", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarInfo(@RequestParam (value = "id") Long carId,
                                                    @Valid @RequestParam (value = "model") String carModel,
                                                    @Valid @RequestParam (value = "class", required = false) String carClass,
                                                    @Valid @RequestParam (value = "mark", required = false) String carMark,
                                                    @RequestParam (value = "cost", required = false) Long carCost) {
        String message;
        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark, carCost);


        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping(value = "/update/v1", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarModelClassAndMark(@RequestParam (value = "id") Long carId,
                                                                 @Valid @RequestParam (value = "model") String carModel,
                                                                 @Valid @RequestParam (value = "class", required = false) String carClass,
                                                                 @Valid @RequestParam (value = "mark", required = false) String carMark) {
        String message;
        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass, carMark);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping(value = "/update/v2", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarModelAndClass(@RequestParam (value = "id") Long carId,
                                                             @Valid @RequestParam (value = "model") String carModel,
                                                             @Valid @RequestParam (value = "class", required = false) String carClass) {
        String message;
        message = carServiceImpl.updateCarFeatures(carId, carModel, carClass);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping(value = "/update/v3", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarMark(@RequestParam (value = "id") Long carId,
                                                     @Valid @RequestParam (value = "model") String carModel,
                                                     @Valid @RequestParam (value = "mark", required = false) String carMark) {
        String message;
        message = carServiceImpl.updateCarMark(carId, carModel, carMark);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }



    @PutMapping(value = "/update/v4", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarModel(@RequestParam (value = "id") Long carId,
                                                     @Valid @RequestParam (value = "model") String carModel){
        String message;
        message = carServiceImpl.updateCarFeatures(carId, carModel);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @PutMapping(value = "/update/status", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody RestResponse updateCarStatus (@RequestParam Long carId,
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


    @GetMapping(value = "/available", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<List<Car>> getAvailableCars() {

        return ResponseEntity.ok().body(carServiceImpl.getAvailableCars("available"));
    }

    @GetMapping(value = "/cost/get", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<List<Car>> getCarsByCost(@RequestParam("cost") Long cost, @RequestParam("operand") String operation) {

         return ResponseEntity.ok().body(carServiceImpl.getCarsByCost(cost, operation));
    }


    @GetMapping(value = "/class/get", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<List<Car>> getCarsByClass(@Valid @RequestParam ("class") String carClass) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByClass(carClass));
    }

    @GetMapping(value = "/model/get", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER','ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<List<Car>> getCarsByModel(@Valid @RequestParam ("model") String model) {
        return ResponseEntity.ok().body(carServiceImpl.getCarsByModel(model));
    }

}
