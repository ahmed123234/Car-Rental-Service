package com.example.carrentalservice.controllers;

import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.order.RentOrderServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("api/v1/orders")
public class OrderController {

    private final RentOrderServiceImpl rentOrderServiceImpl;

    private final AppUserServiceImpl appUserServiceImpl;
    private final AppUserRepository appUserRepository;

    // create new order for customer
//    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(path = "/add")
    public @ResponseBody RestResponse createOrder(@NotNull HttpServletRequest request, @RequestBody RentOrderRequest rentOrderRequest,
                                    @RequestParam (value = "cars") Long [] carId) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {

                String username = appUserServiceImpl.handleAuthorizationHeader(authorizationHeader);
                String message  = rentOrderServiceImpl.createOrder(username, rentOrderRequest, carId);
                objectNode.put("message", message);

            } catch (Exception exception){
                log.error("error {}", exception.getMessage());
            }
        }else {
        throw new ApiRequestException("Access token is missing", BAD_REQUEST);
    }

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    // get all orders depending on the user's role
    // customer

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<?> getUserOrders(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Long userId;
        ResponseEntity<?> response = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {

                String username = appUserServiceImpl.handleAuthorizationHeader(authorizationHeader);
                if (appUserRepository.findByUsername(username).isPresent()) {
                    userId = appUserRepository.findByUsername(username).get().getUserId();

                    if (rentOrderServiceImpl.getUserOrders(userId).isEmpty()) {
                        ObjectNode objectNode = new ObjectMapper().createObjectNode();
                        objectNode.put("message", "Sorry, you have not any order yet.");
                        response = ResponseEntity.ok().body(new RestResponse(
                                objectNode,
                                HttpStatus.OK,
                                ZonedDateTime.now(ZoneId.of("Z"))
                        ));
                    } else
                        response = ResponseEntity.ok().body(rentOrderServiceImpl.getUserOrders(userId));
                }
            } catch (Exception exception) {
                log.error("error {}", exception.getMessage());
            }
        }

        return response;
    }

    // admin

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<?> getOrders() {
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
    @GetMapping("/customer/get")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public @ResponseBody ResponseEntity<?> getUserOrder(HttpServletRequest request, @RequestParam (value = "id") Long orderId) {
        String message;
        String username;
         boolean isFound = false;
        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {

                username = appUserServiceImpl.handleAuthorizationHeader(authorizationHeader);
                Long userId = appUserRepository.findAppUserByUsername(username).getUserId();
                isFound = rentOrderServiceImpl.getUserOrders(userId).contains(rentOrderServiceImpl.getOrderById(orderId));
            } catch (Exception exception) {
                log.error("error {}", exception.getMessage());
            }
        }

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

    @GetMapping("/admin/get{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<?> getOrder(@PathVariable (value = "id") Long orderId) {
        if (rentOrderServiceImpl.getOrderById(orderId) == null) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "There is no order with the required orderId: " + orderId );
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
    public @ResponseBody ResponseEntity<?> updateOrderStatus(@RequestParam (value = "id") Long orderId,
                                               @RequestParam (value = "status") String status) {

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", rentOrderServiceImpl.updateOrderStatus(orderId, status) );
        return ResponseEntity.ok().body(new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        ));
    }

    // get all orders with status requested or canceled
    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<?> getByOrderStatus(@RequestParam (value = "status") String status) {

        List<RentOrder> orders;

        orders =  rentOrderServiceImpl.getOrdersByStatus(status);

        if (orders.isEmpty()) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "There are no orders with status " + status + " yet.");
            return ResponseEntity.ok().body(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ));
        }
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/items/get")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<?> getOrderItems(@RequestParam (value = "id") Long orderId) {

       return ResponseEntity.ok().body(rentOrderServiceImpl.getOrderItems(orderId));
    }
}
