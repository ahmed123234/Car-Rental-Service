package com.example.carrentalservice.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentOrder {

    @SequenceGenerator(
            name = "rent_order_sequence",
            sequenceName = "rent_order_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rent_order_sequence"
    )
    private Long orderId;

    @Column()
    private Long userId;

    @OneToMany()
    @JoinColumn(name = "order_id")
    private Set<RentOrderItem> orderItems;

    @Column(nullable = false)
    private int orderBill;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private String orderDriver;

    @Column(nullable = false)
    private Date orderStartDate;

    @Column(nullable = false)
    private Date orderFinishDate;

    public void addRentOrderItem(RentOrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new HashSet<>();
        }
        this.orderItems.add(orderItem);

    }

}
