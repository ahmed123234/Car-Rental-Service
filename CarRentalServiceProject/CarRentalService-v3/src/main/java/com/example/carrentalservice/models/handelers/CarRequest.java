package com.example.carrentalservice.models.handelers;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CarRequest {

    private String carClass;
    private String carModel;
    private Long carCost;
    private String carMark;
}
