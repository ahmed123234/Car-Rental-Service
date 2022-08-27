package com.example.carrentalservice.services.order;

import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.entities.RentOrderItem;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.repositories.RentOrderItemRepository;
import com.example.carrentalservice.repositories.RentOrderRepository;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RentOrderServiceImpl implements RentOrderService {

    private final AppUserServiceImpl appUserServiceImpl;
    private final CarServiceImpl carServiceImpl;
    private final RentOrderRepository rentOrderRepository;
    private final RentOrderItemRepository rentOrderItemRepository;
    @Override
    public void addOrder(RentOrder order, List<Long> carsId) {

        Long orderId = rentOrderRepository.save(order).getOrderId();

        for (Long carId : carsId) {
            if(carServiceImpl.getCar(carId).getCarStatus().equals("available")) {
                carServiceImpl.updateCarStatus(carId, "rented");
                RentOrderItem item = new RentOrderItem(orderId, carId);
                rentOrderItemRepository.save(item);
            }
        }
    }

    @Override
    public String createOrder (Principal principal, RentOrderRequest rentOrderRequest, Long [] carId) {

        UserDetails loginUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String message;
        List<Long> carsId = new ArrayList<>();
        Collections.addAll(carsId, carId);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderStatus("requested");
        rentOrder.setOrderDriver(rentOrderRequest.getOrderDriver());
        rentOrder.setOrderStartDate(rentOrderRequest.getOrderStartDate());
        rentOrder.setOrderFinishDate(rentOrderRequest.getOrderFinishDate());
        rentOrder.setUserId(appUserServiceImpl.getUserId(loginUser.getUsername()));

        long days = ChronoUnit.DAYS.between(rentOrder.getOrderStartDate().toLocalDate(),
                rentOrder.getOrderFinishDate().toLocalDate());

        if (days >= 1) {
            long bill = 0;

            // calculate the price
            for (Long car : carsId) {
                if(carServiceImpl.getCar(car).getCarStatus().equals("available")) {
                    bill = bill + (carServiceImpl.getCar(car).getCarCost()) * days;
                }
            }
            if (bill!=0 && rentOrder.getOrderDriver().equals("yes")) {
                bill = bill + (days * 35);
            }
            if (bill == 0)
                return "The selected Cars is not available right now, please try again later";

            rentOrder.setOrderBill((int) bill);
            addOrder(rentOrder, carsId);
            message = "Order created successfully";
        }else
            message = "Invalid rent duration";

        return message;
    }

    @Override
    public List<RentOrder> getUSerOrders(Long userId) {

        return rentOrderRepository.findAllByUserId(userId);
    }

    @Override
    public RentOrder getOrderById(Long orderId) {
        return rentOrderRepository.findByOrderId(orderId);
    }

    @Override
    public List<RentOrder> getAllOrders() {
        return rentOrderRepository.findAll();
    }

    //update order status requested or canceled
    @Override
    public String updateOrderStatus(Long orderId, String status) {

        rentOrderRepository.updateOrderStatus(orderId, status);
        return "the selected order updated successfully.";
    }
    @Override
    public List<RentOrder> getOrdersByStatus(String status) {
       return rentOrderRepository.findByOrderStatus(status);
    }
}
