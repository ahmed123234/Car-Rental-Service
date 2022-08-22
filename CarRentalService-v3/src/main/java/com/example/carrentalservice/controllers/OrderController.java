package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.exception.ApiRequestException;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.RentOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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


    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "add")
    public RestResponse createOrder(Principal principal, @RequestBody RentOrderRequest rentOrderRequest,
                               @RequestParam Long [] carId) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) loginUser.getAuthorities();
        String role = authorities.get(0).getAuthority();
        System.out.println(role);
        System.out.println(loginUser.getAuthorities());

        if(role.equals("ADMIN") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied");
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

    // get orders for customer

    @GetMapping()
    public ResponseEntity<Object> getOrders(Principal principal) {
        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId =  appUserRepository.findByUsername(username).get().getUserId();

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) loginUser.getAuthorities();
        String role = authorities.get(0).getAuthority();

        if(role.equals("ADMIN") || role.equals(("MANAGER"))){
            throw new ApiRequestException("Access denied");
        }

        String message;
        //Optional<List<RentOrder>> =
        if (rentOrderService.getUSerOrders(userId).isEmpty()) {
            message = "Sorry, you have not any order yet.";
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
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
    }


    @GetMapping("{orderId}")
    public ResponseEntity<Object> getOrder(Principal principal, @PathVariable Long orderId) {
        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();

        String username = loginUser.getUsername();
        Long userId =  appUserRepository.findByUsername(username).get().getUserId();
        boolean isFound = rentOrderService.getUSerOrders(userId).contains(rentOrderService.getOrderById(orderId));

        String message;
        if (!isFound) {
            message = "The required order not found!";
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
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


}
