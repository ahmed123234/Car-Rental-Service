package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.RentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentOrderRepository extends JpaRepository<RentOrder, Long> {


    List<RentOrder> findAllByUserId(Long userId);

    RentOrder findByOrderId(Long orderId);

}
