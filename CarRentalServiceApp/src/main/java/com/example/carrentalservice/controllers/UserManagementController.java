package com.example.carrentalservice.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import com.example.carrentalservice.models.handelers.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Slf4j
@RestController
@Transactional
@RequestMapping("/api/v1/users")
public class UserManagementController {

   private final AppUserServiceImpl appUserServiceImpl;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;
   private final AppUserRepository appUserRepository;


    @GetMapping("/validate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse getUser(@RequestBody String username, String password) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.getUser(username,password));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getByUserRole(@PathVariable("role") UserRole userRole) {
        return ResponseEntity.ok().body(appUserServiceImpl.getByUserRole(userRole));
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        if (appUserServiceImpl.getUsers()== null) {
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message", "no one");

            return ResponseEntity.ok().body(new RestResponse(
                    objectNode,
                    HttpStatus.OK,
                    ZonedDateTime.now(ZoneId.of("Z"))
            ));
        }
        return ResponseEntity.ok().body(appUserServiceImpl.getUsers());
    }

    @PutMapping("/update/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse updateUserStatus(@RequestBody Long userId, boolean status) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.changeStatus(userId, status));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @DeleteMapping("/account/delete")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse deleteUser(Principal principal) {
        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String username = loginUser.getUsername();
        Long loginUserId =  appUserRepository.findByUsername(username).get().getUserId();

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.deleteUser(loginUserId));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse deleteAppUser(Long userId) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.deleteUser(userId));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse addAppUser(@RequestBody RegistrationRequest registrationRequest) {
        appUserServiceImpl.addUser(registrationRequest);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", "User added successfully");

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @PutMapping("/update/password{password}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public RestResponse updatePassword(Principal principal, @PathVariable("password")String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.updateUserPassword(principal,  encodedPassword));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<AppUser> user = appUserRepository.findByUsername(username);

                String accessToken = JWT.create()
                        .withSubject(user.get().getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.get().getRoles().stream().map(UserRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken",  accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                // response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
