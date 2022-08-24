package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.handelers.CarRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.services.AppUserService;
import com.example.carrentalservice.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cars/api/v1")
@Transactional
public class CarController {

    private final CarService carService;

    private final AppUserService appUserService;


    // allow the admin to add a car
    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse addCar(Principal principal, @RequestBody CarRequest carRequest) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        String message = carService.addCar(new Car(
                    carRequest.getCarClass(),
                    carRequest.getCarModel(),
                    carRequest.getCarCost(),
                    carRequest.getCarMark(),
                    "available"
            ));

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @GetMapping("{carId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Car getCar(Principal principal, @PathVariable Long carId) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        return carService.getCar(carId);
    }


    @GetMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Car> getCars(Principal principal) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }
        return carService.getCars();

    }


    @PutMapping("update/cost")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updateCarCost(Principal principal, @RequestBody Long carId, Long carCost) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

       String message = carService.updateCarCost(carId, carCost);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping("update/prices{coefficient}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updatePrices(Principal principal, @PathVariable double coefficient) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        String message = carService.updatePrices(coefficient);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @DeleteMapping("delete{carId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse deleteCarById(Principal principal, @PathVariable Long carId) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        String message = carService.deleteCarById(carId);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @PutMapping("/update{carId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updateCarInfo(Principal principal, @PathVariable Long carId,
                                @RequestBody  String carModel,
                                @RequestBody (required = false) String carClass,
                                @RequestBody (required = false) String carMark,
                                @RequestBody (required = false) Long carCost) {
        String message = "";

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }
        else {

            if (carClass != null && carMark != null && carCost != 0)
                message = carService.updateCarFeatures(carId, carModel, carClass, carMark, carCost);

            else if (carClass != null && carMark != null && carCost == 0)
                message = carService.updateCarFeatures(carId, carModel, carClass, carMark);

            else if (carClass != null && carMark == null && carCost == 0)
                message = carService.updateCarFeatures(carId, carModel, carClass);

            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", message);

            return new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            );
        }
    }


    @PutMapping("/update/status")
    public RestResponse updateCarStatus (Principal principal, @RequestParam Long carId,
                                   @RequestParam String status) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("CUSTOMER") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

       String message = carService.updateCarStatus(carId, status);

       ObjectNode objectNode = new ObjectMapper().createObjectNode();
       objectNode.put("message", message);

       return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
       );
    }


    @GetMapping("available")
    public List<Car> getAvailableCars() {
       return carService.getAvailableCars("available");
    }

    @GetMapping("{cost}")
    public List<Car> getCarsByCost(@PathVariable Long cost) {
        return carService.getCarsByCost(cost);
    }


    @GetMapping("{class}")
    public List<Car> getCarsByClass(@PathVariable ("class") String carClass) {
        return carService.getCarsByClass(carClass);
    }

    @GetMapping("{model}")
    public List<Car> getCarsByModel(@PathVariable ("model") String model) {
        return carService.getCarsByModel(model);
    }

}
