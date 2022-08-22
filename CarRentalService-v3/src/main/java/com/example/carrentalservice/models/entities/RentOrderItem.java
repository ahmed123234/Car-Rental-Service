package com.example.carrentalservice.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class RentOrderItem /*implements Serializable*/ {
    @SequenceGenerator(
            name = "order_item_sequence",
            sequenceName = "order_item_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_item_sequence"
    )
    private Long OrderItemId;

    @Column(name = "order_id")
    private Long OrderId;

    @Column()
    private Long carId;

    public RentOrderItem( Long orderId, Long carId) {
        OrderId = orderId;
        this.carId = carId;
    }

    //    @ManyToOne()
//    @JoinColumn(name = "order_id")
//    private RentOrder order;
}
