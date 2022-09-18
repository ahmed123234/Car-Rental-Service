package com.example.carrentalservice.models.handelers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RestResponse {

    private ObjectNode data;
    private HttpStatus status;
    private ZonedDateTime zonedDateTime;

}
