package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.order.RentOrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("api/v1/orders")
public class OrderController {

    private final RentOrderServiceImpl rentOrderServiceImpl;
    private final AppUserRepository appUserRepository;

    // create new order for customer
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping(path = "/add")
    public RestResponse createOrder(Principal principal, @RequestBody RentOrderRequest rentOrderRequest,
                                    @RequestParam Long [] carId) {

        String message  = rentOrderServiceImpl.createOrder(principal, rentOrderRequest,carId);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", message);

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    // get all orders depending on the user's role
    // customer

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> getUserOrders(Principal principal) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId;
        ResponseEntity response = null;

        if (appUserRepository.findByUsername(username).isPresent()) {
            userId = appUserRepository.findByUsername(username).get().getUserId();

            if (rentOrderServiceImpl.getUSerOrders(userId).isEmpty()) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                objectNode.put("message", "Sorry, you have not any order yet.");
                response = ResponseEntity.ok().body(new RestResponse(
                        objectNode,
                        HttpStatus.OK,
                        ZonedDateTime.now(ZoneId.of("Z"))
                ));
            }else
                response = ResponseEntity.ok().body(rentOrderServiceImpl.getUSerOrders(userId));
        }
        return response;
    }

    // admin

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getOrders() {
        if (rentOrderServiceImpl.getAllOrders().isEmpty()) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "There is no orders yet." );
            return ResponseEntity.ok().body(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ));
        }
        return ResponseEntity.ok().body(rentOrderServiceImpl.getAllOrders());
    }


    // get a specific order by the login customer
    @GetMapping("/customer/get{orderId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> getUserOrder(Principal principal, @PathVariable Long orderId) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId =  appUserRepository.findByUsername(username).get().getUserId();

        String message;
        ObjectNode objectNode = new ObjectMapper().createObjectNode();


        boolean isFound = rentOrderServiceImpl.getUSerOrders(userId).contains(rentOrderServiceImpl.getOrderById(orderId));

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
        return ResponseEntity.ok().body(rentOrderServiceImpl.getOrderById(orderId));
    }

    @GetMapping("/admin/get{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        if (rentOrderServiceImpl.getOrderById(orderId) == null) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "There is no orders yet." );
            return ResponseEntity.ok().body(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ));
        }
        return ResponseEntity.ok().body(rentOrderServiceImpl.getOrderById(orderId));
    }


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        return ResponseEntity.ok().body(rentOrderServiceImpl.updateOrderStatus(orderId, status));
    }

    // get all orders with status requested or canceled
    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getByOrderStatus(@RequestParam String status) {

        List<RentOrder> orders;

        orders =  rentOrderServiceImpl.getOrdersByStatus(status);

        if (orders.isEmpty()) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "There are no orders with status " + status + "yet.");
            return ResponseEntity.ok().body(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ));
        }
        return ResponseEntity.ok().body(orders);
    }
}
