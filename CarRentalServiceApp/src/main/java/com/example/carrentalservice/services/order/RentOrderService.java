package com.example.carrentalservice.services.order;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.handelers.RentOrderRequest;

import java.util.List;

public interface RentOrderService {

    void addOrder(RentOrder order, List<Long> carsId);

    String createOrder (String username, RentOrderRequest rentOrderRequest, Long [] carId);

    List<RentOrder> getUSerOrders(Long userId);

    RentOrder getOrderById(Long orderId);

    List<RentOrder> getAllOrders();

    String updateOrderStatus(Long orderId, String status);

    List<RentOrder> getOrdersByStatus(String status);

    List<Car> getOrderItems(Long orderId);
}
