package com.example.carrentalservice.models.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @SequenceGenerator(
            name = "car_sequence",
            sequenceName = "car_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "car_sequence"
    )
    private Long carId;
    @Column(nullable = false)
    @NotBlank(message = "car class must not be blank!")
    private String carClass;
    @Column(nullable = false)
    @NotBlank(message = "car model must not be blank!")
    private String carModel;
    // car registration mark
    @Column(nullable = false, unique = true)
    @NotBlank(message = "car mark must not be blank!")
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$") // \\d means  : [0-9]
    private String carMark;
    @Column(nullable = false)
    private Long carCost;
    private  String carStatus;

    public Car(String carClass, String carModel, Long carCost,
               String carMark, String carStatus) {
        this.carClass = carClass;
        this.carModel = carModel;
        this.carCost = carCost;
        this.carMark = carMark;
        this.carStatus = carStatus;
    }
}
