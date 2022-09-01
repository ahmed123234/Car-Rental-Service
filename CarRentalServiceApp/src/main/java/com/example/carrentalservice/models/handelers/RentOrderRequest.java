package com.example.carrentalservice.models.handelers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
public class RentOrderRequest {

    private String orderDriver;
    private Date orderStartDate;
    private Date orderFinishDate;
}
