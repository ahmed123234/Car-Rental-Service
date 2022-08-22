package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.handelers.CarRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cars/api/v1")
//@Transactional
public class CarController {

    private final CarService carService;

    @PostMapping("/add")
   // @Transactional(rollbackFor = Exception.class)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse addCar(@RequestBody CarRequest carRequest) {

//        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();
//        if(Objects.equals(loginUser.getAuthorities(), "CUSTOMER")){
//            throw new ApiRequestException("Sorry, you don't have authorization");
//        }

        String message = carService.addCar(new Car(
                    carRequest.getCarClass(),
                    carRequest.getCarModel(),
                    carRequest.getCarCost(),
                    carRequest.getCarMark(),
                    "available"
            ));

//            return new ResponseEntity(
//                    new Car(carRequest.getCarClass(),
//                            carRequest.getCarModel(),
//                            carRequest.getCarCost(),
//                            carRequest.getCarMark(),
//                            "available"),
//                   // message,
//                    HttpStatus.OK
//            );
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @ResponseBody
    @GetMapping("{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Car getCar(@PathVariable Long carId) {
        return carService.getCar(carId);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Car> getCars() {
        return carService.getCars();

    }


    @PutMapping("update-cost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updateCarCost(@RequestBody Long carId, Long carCost) {

       String message = carService.updateCarCost(carId, carCost);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping("update-prices{coefficient}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updatePrices(@PathVariable double coefficient) {

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse deleteCarById(@PathVariable Long carId) {

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RestResponse updateCarInfo(@PathVariable Long carId,
                                @RequestBody  String carModel,
                                @RequestBody (required = false) String carClass,
                                @RequestBody (required = false) String carMark,
                                @RequestBody (required = false) Long carCost) {
        String message = "";

        if (carClass!=null && carMark!=null &&carCost!=0)
          message = carService.updateCarFeatures(carId, carModel, carClass, carMark, carCost);

        else if (carClass!=null && carMark!=null && carCost==0)
            message = carService.updateCarFeatures(carId, carModel, carClass, carMark);

        else if (carClass!=null && carMark==null && carCost==0)
            message = carService.updateCarFeatures(carId, carModel ,carClass);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @PutMapping("/update-status")
    public RestResponse updateCarStatus (@RequestParam Long carId,
                                   @RequestParam String status) {

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
}
