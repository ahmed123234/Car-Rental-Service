package com.example.carrentalservice.models.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DailyRentOrder {
    private Long sum;
    private Long count;
    private java.sql.Date start_date;
}
