package com.example.carrentalservice.controllers;

import com.example.carrentalservice.CarRentalServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {CarRentalServiceApplication.class, CarController.class}
)
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.yml")
class CarControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void addCar() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/cars/add").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void getCar() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void getCars() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updateCarCost() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/cost/update").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updatePrices() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/costs/update").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void deleteCarById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/cars/delete").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updateCarInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/update/all").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updateCarModelClassAndMark() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/update/v1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updateCarModelAndClass() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/v2").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void updateCarMark() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/update/v3").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void testUpdateCarModel() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/update/v4").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void testUpdateCarStatus() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/cars/update/status").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());

    }

    @Test
    void getAvailableCars() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/available").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void getCarsByCost() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/cost/get").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void getCarsByClass() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/class/get").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }

    @Test
    void getCarsByModel() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/model/get").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println(mvcResult.getResponse());
    }
}