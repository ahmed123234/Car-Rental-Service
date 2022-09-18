package com.example.carrentalservice.models.handelers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
//@EqualsAndHashCode
@ToString
public class CarRequest {
    @NotBlank(message = "car class must not be blank!")
    private String carClass;

    @NotBlank(message = "car model must not be blank!")
    private String carModel;

    private Long carCost;

//    @UniqueElements(message = "car mark must be unique!")
    @NotBlank(message = "car mark must not be blank!")
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$")
    private String carMark;
}
