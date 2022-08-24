package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.AppUserService;
import com.example.carrentalservice.services.RentOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@AllArgsConstructor
@RestController
@Transactional
@RequestMapping("/orders/api/v1")
public class OrderController {

    private final RentOrderService rentOrderService;
    private final AppUserRepository appUserRepository;

    private  final AppUserService appUserService;


    // create new order for customer
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "add")
    public RestResponse createOrder(Principal principal, @RequestBody RentOrderRequest rentOrderRequest,
                               @RequestParam Long [] carId) {

        String role = appUserService.getUserRole(principal);

        if(role.equals("ADMIN") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        String message  = rentOrderService.createOrder(principal, rentOrderRequest,carId);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    // get all orders depending on the user's role

    @GetMapping()
    public ResponseEntity<Object> getOrders(Principal principal) {

        String message;
        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId =  appUserRepository.findByUsername(username).get().getUserId();

        String role = appUserService.getUserRole(principal);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        if (role.equals("CUSTOMER")) {

            if (rentOrderService.getUSerOrders(userId).isEmpty()) {
                message = "Sorry, you have not any order yet.";
                objectNode.put("message", message);

                return new ResponseEntity<>(new RestResponse(
                        objectNode,
                        HttpStatus.OK,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    rentOrderService.getUSerOrders(userId),
                    HttpStatus.OK
            );
        } else if (role.equals("MANAGER") || role.equals("ADMIN")) {

            if (rentOrderService.getAllOrders().isEmpty()) {
                message = "There is no orders yet.";
                objectNode.put("message", message);

                return new ResponseEntity<>(new RestResponse(
                        objectNode,
                        HttpStatus.OK,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    rentOrderService.getAllOrders(),
                    HttpStatus.OK
            );
        }
        else {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }
    }


    // get a specific order by the login customer
    @GetMapping("{orderId}")
    public ResponseEntity<Object> getOrder(Principal principal, @PathVariable Long orderId) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId =  appUserRepository.findByUsername(username).get().getUserId();

        String role = appUserService.getUserRole(principal);
        String message;
        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        if (role.equals("CUSTOMER")) {

            boolean isFound = rentOrderService.getUSerOrders(userId).contains(rentOrderService.getOrderById(orderId));

            if (!isFound) {
                message = "The required order not found!";
                objectNode.put("message", message);

                return new ResponseEntity<>(new RestResponse(
                        objectNode,
                        HttpStatus.NO_CONTENT,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    rentOrderService.getOrderById(orderId),
                    HttpStatus.OK
            );
        }

        else if (role.equals("MANAGER") || role.equals("ADMIN")) {

            if (rentOrderService.getOrderById(orderId) == null) {
                message = "There is no orders yet.";
                objectNode.put("message", message);

                return new ResponseEntity<>(new RestResponse(
                        objectNode,
                        HttpStatus.OK,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    rentOrderService.getOrderById(orderId),
                    HttpStatus.OK
            );
        }
        else {
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public RestResponse updateOrderStatus(@RequestParam Long orderId, @RequestParam String status, Principal principal) {

        String role = appUserService.getUserRole(principal);

        if (role.equals("CUSTOMER")){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        String message  = rentOrderService.updateOrderStatus(orderId, status);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    // get all orders with status requested or canceled
    @GetMapping("status")
    public ResponseEntity<Object> getByOrderStatus(@RequestParam String status, Principal principal) {

        List<RentOrder> orders;

        String role = appUserService.getUserRole(principal);

        if (role.equals("ADMIN") || role.equals(("CUSTOMER"))){
            throw new ApiRequestException("Access denied", HttpStatus.FORBIDDEN);
        }

        orders =  rentOrderService.getOrdersByStatus(status);

        if (orders.isEmpty()) {
            String message = "There are no orders with status " + status + "yet.";
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", message);

            return new ResponseEntity<>(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                orders,
                HttpStatus.OK
        );
    }
}
