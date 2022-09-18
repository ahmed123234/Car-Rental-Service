package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.RentOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentOrderItemRepository extends JpaRepository<RentOrderItem, Long> {

}
