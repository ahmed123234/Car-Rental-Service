package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.RentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RentOrderRepository extends JpaRepository<RentOrder, Long> {


    List<RentOrder> findAllByUserId(Long userId);

    RentOrder findByOrderId(Long orderId);

    @Transactional
    @Modifying
    @Query("UPDATE RentOrder o SET o.orderStatus = ?2 WHERE o.orderId = ?1")
    void updateOrderStatus(Long orderId, String status);

    List<RentOrder> findByOrderStatus(String status);
}
