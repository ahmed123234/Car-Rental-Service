package com.example.carrentalservice.car;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
    private String carClass;
    @Column(nullable = false)
    private String carModel;
    // car registration mark
    @Column(nullable = false)
    private String carMark;
    @Column(nullable = false)
    private Long carCost;

    public Car(String carClass, String carModel, Long carCost, String carMark) {
        this.carClass = carClass;
        this.carModel = carModel;
        this.carCost = carCost;
        this.carMark = carMark;
    }
}
