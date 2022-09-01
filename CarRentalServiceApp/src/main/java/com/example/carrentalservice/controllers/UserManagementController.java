package com.example.carrentalservice.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.exception.ResponseErrorException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Slf4j
@RestController
//@Transactional
@RequestMapping("/api/v1/users")
public class UserManagementController {

   private final AppUserServiceImpl appUserServiceImpl;
   private final AppUserRepository appUserRepository;


    @GetMapping("/validate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse getUser(@RequestParam (value = "username") String username,
                                @RequestParam(value = "password")  String password) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.getUser(username, password));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    @GetMapping("/role/{userRole}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getByUserRole(@PathVariable String userRole) {
        return ResponseEntity.ok().body(appUserServiceImpl.getByUserRole(userRole));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PutMapping("/status/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse updateUserStatus(@RequestParam("username") String username, @RequestParam("status") String status) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", appUserServiceImpl.changeStatus(username, status));

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

    }

    @DeleteMapping("/account/delete")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse deleteUser(HttpServletRequest request) {

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String username = appUserServiceImpl.handleAuthorizationHeader(authorizationHeader);
                objectNode.put("message", appUserServiceImpl.deleteUser(username));
            } catch (Exception exception) {
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

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse deleteAppUser(@RequestParam ("id") Long userId) {
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

    @PutMapping("/password/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_MANAGER')")
//    @Transactional(rollbackFor = Exception.class)
    public RestResponse updatePassword(HttpServletRequest request, @RequestParam ("password") String password) {

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String username = appUserServiceImpl.handleAuthorizationHeader(authorizationHeader);
                objectNode.put("message", appUserServiceImpl.updateUserPassword(username, password));

            } catch (Exception exception) {
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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user = appUserRepository.findAppUserByUsername(username);

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(UserRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken",  accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception exception) {
               new ResponseErrorException().ResponseError(response, "error", FORBIDDEN, exception);
            }
        }else {
            throw new ApiRequestException("Refresh token is missing", BAD_REQUEST);
        }
    }

    @GetMapping("/active/find")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUser>> findAllActiveUsers() {
        List<AppUser> users = appUserRepository.findAllActiveUsers(JpaSort.unsafe("LENGTH(username)"));
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/disabled/find")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUser>> findAllDisabledUsers() {
        List<AppUser> users = appUserRepository.findAllDisabledUsers(JpaSort.unsafe("LENGTH(username)"));
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("page/find")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AppUser>> findAllByPagination() {
        Page<AppUser> users = appUserRepository.findAllByPagination(Pageable.ofSize(3));
        return ResponseEntity.ok().body(users);
    }


    @GetMapping("/roles/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse getUserRoles(@PathVariable(value = "name") String username) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        String [] roles = appUserServiceImpl.getUserRole(username);
        for (int i = 0 ; i < roles.length; i++) {
            objectNode.put("role # " + (i + 1), roles[i]);
        }

        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }


    @GetMapping("/orders/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public RestResponse getUserOrderCount(@PathVariable(value = "name") String username) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();

        objectNode.put("message", appUserServiceImpl.getUserOrderCount(username));
        return new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }
}
