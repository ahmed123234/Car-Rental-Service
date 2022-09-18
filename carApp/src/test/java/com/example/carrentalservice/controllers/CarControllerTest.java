package com.example.carrentalservice.controllers;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.handelers.CarRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.CarRepository;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CarController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest
class CarControllerTest {
    @Autowired
    private CarController carController;

    @MockBean
    private CarServiceImpl carServiceImpl;

    @Autowired
    MockMvc mockMvc;


    /**
     * Method under test: {@link CarController#addCar(CarRequest)}
     */
    @Test
    void testAddCar2() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.save(any())).thenReturn(car);
        when(carRepository.findByCarMark(anyString())).thenReturn(Optional.empty());
        CarController carController = new CarController(new CarServiceImpl(carRepository));
        ResponseEntity<RestResponse> actualAddCarResult = carController
                .addCar(new CarRequest("Car Class", "Car Model", 1L, "Car Mark"));
        assertTrue(actualAddCarResult.hasBody());
        assertTrue(actualAddCarResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAddCarResult.getStatusCode());
        RestResponse body = actualAddCarResult.getBody();
        assert body != null;
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(1, body.getData().size());
        verify(carRepository).save(any());
        verify(carRepository).findByCarMark(anyString());
    }

    /**
     * Method under test: {@link CarController#addCar(CarRequest)}
     */
    @Test
    void testAddCar3() {
        CarServiceImpl carServiceImpl = mock(CarServiceImpl.class);
        when(carServiceImpl.addCar(any())).thenReturn("Add Car");
        CarController carController = new CarController(carServiceImpl);
        ResponseEntity<RestResponse> actualAddCarResult = carController
                .addCar(new CarRequest("Car Class", "Car Model", 1L, "Car Mark"));
        assertTrue(actualAddCarResult.hasBody());
        assertTrue(actualAddCarResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAddCarResult.getStatusCode());
        RestResponse body = actualAddCarResult.getBody();
        assert body != null;
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(1, body.getData().size());
        verify(carServiceImpl).addCar(any());
    }


    /**
     * Method under test: {@link CarController#getCar(Long)}
     */
    @Test
    void testGetCar1() throws Exception {
        Mockito.when(carServiceImpl.updateCarFeatures(anyLong(), anyString()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.getCar(anyLong());
        Mockito.verify(carServiceImpl).getCar(anyLong());

    }


    /**
     * Method under test: {@link CarController#getCars()}
     */
    @Test
    void testGetCars1() throws Exception {
        Mockito.when(carServiceImpl.getCars()).thenReturn(Collections.EMPTY_LIST);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.getCars();
        Mockito.verify(carServiceImpl).getCars();

    }


    /**
     * Method under test: {@link CarController#getCars()}
     */
    @Test
    void testGetCars3() throws Exception {
        Car car = new Car();
        car.setCarClass("?");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("?");
        car.setCarModel("?");
        car.setCarStatus("?");

        Car car1 = new Car();
        car1.setCarClass("?");
        car1.setCarCost(1L);
        car1.setCarId(123L);
        car1.setCarMark("?");
        car1.setCarModel("?");
        car1.setCarStatus("?");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car1);
        carList.add(car);
        when(carServiceImpl.getCars()).thenReturn(carList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"carId\":123,\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"},{\"carId\":123,"
                                        + "\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"}]"));
    }

    /**
     * Method under test: {@link CarController#updateCarCost(Long, Long)}
     */
    @Test
    void testUpdateCarCost() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updateCar(anyLong(), anyLong())).thenReturn(1);
        RestResponse actualUpdateCarCostResult = (new CarController(new CarServiceImpl(carRepository)))
                .updateCarCost(123L, 1L);
        assertEquals(HttpStatus.OK, actualUpdateCarCostResult.getStatus());
        ObjectNode data = actualUpdateCarCostResult.getData();
        assertEquals("{\r\n  \"message\" : \"Car's cost updated Successfully\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updateCar(anyLong(), anyLong());
    }

    /**
     * Method under test: {@link CarController#updateCarCost(Long, Long)}
     */
    @Test
    void testUpdateCarCost2() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updateCar(anyLong(), anyLong())).thenReturn(0);
        RestResponse actualUpdateCarCostResult = (new CarController(new CarServiceImpl(carRepository)))
                .updateCarCost(123L, 1L);
        assertEquals(HttpStatus.OK, actualUpdateCarCostResult.getStatus());
        ObjectNode data = actualUpdateCarCostResult.getData();
        assertEquals("{\r\n  \"message\" : \"No car affected\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updateCar(anyLong(), anyLong());
    }


    /**
     * Method under test: {@link CarController#updateCarCost(Long, Long)}
     */
    @Test
    void testUpdateCarCost4() {
        CarServiceImpl carServiceImpl = mock(CarServiceImpl.class);
        when(carServiceImpl.updateCarCost(anyLong(), anyLong())).thenReturn("2020-03-01");
        RestResponse actualUpdateCarCostResult = (new CarController(carServiceImpl)).updateCarCost(123L, 1L);
        assertEquals(HttpStatus.OK, actualUpdateCarCostResult.getStatus());
        ObjectNode data = actualUpdateCarCostResult.getData();
        assertEquals("{\r\n  \"message\" : \"2020-03-01\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carServiceImpl).updateCarCost(anyLong(), anyLong());
    }

    /**
     * Method under test: {@link CarController#updatePrices(Long)}
     */
    @Test
    void testUpdatePrices() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updatePrices(anyLong())).thenReturn(1);
        RestResponse actualUpdatePricesResult = (new CarController(new CarServiceImpl(carRepository))).updatePrices(1L);
        assertEquals(HttpStatus.OK, actualUpdatePricesResult.getStatus());
        ObjectNode data = actualUpdatePricesResult.getData();
        assertEquals("{\r\n  \"message\" : \"Cars' costs updated Successfully\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updatePrices(anyLong());
    }

    /**
     * Method under test: {@link CarController#updatePrices(Long)}
     */
    @Test
    void testUpdatePrices2() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updatePrices(anyLong())).thenReturn(0);
        RestResponse actualUpdatePricesResult = (new CarController(new CarServiceImpl(carRepository))).updatePrices(1L);
        assertEquals(HttpStatus.OK, actualUpdatePricesResult.getStatus());
        ObjectNode data = actualUpdatePricesResult.getData();
        assertEquals("{\r\n  \"message\" : \"No car affected\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updatePrices(anyLong());
    }


    /**
     * Method under test: {@link CarController#updatePrices(Long)}
     */
    @Test
    void testUpdatePrices4() {
        CarServiceImpl carServiceImpl = mock(CarServiceImpl.class);
        when(carServiceImpl.updatePrices(anyLong())).thenReturn("2020-03-01");
        RestResponse actualUpdatePricesResult = (new CarController(carServiceImpl)).updatePrices(1L);
        assertEquals(HttpStatus.OK, actualUpdatePricesResult.getStatus());
        ObjectNode data = actualUpdatePricesResult.getData();
        assertEquals("{\r\n  \"message\" : \"2020-03-01\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carServiceImpl).updatePrices(anyLong());
    }

    /**
     * Method under test: {@link CarController#deleteCarById(Long)}
     */
    @Test
    void testDeleteCarById() {
        CarRepository carRepository = mock(CarRepository.class);
        doNothing().when(carRepository).deleteById(anyLong());
        RestResponse actualDeleteCarByIdResult = (new CarController(new CarServiceImpl(carRepository)))
                .deleteCarById(123L);
        assertEquals(HttpStatus.OK, actualDeleteCarByIdResult.getStatus());
        ObjectNode data = actualDeleteCarByIdResult.getData();
        assertEquals("{\r\n  \"message\" : \"Car deleted Successfully\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).deleteById(anyLong());
    }


    /**
     * Method under test: {@link CarController#deleteCarById(Long)}
     */
    @Test
    void testDeleteCarById3() {
        CarServiceImpl carServiceImpl = mock(CarServiceImpl.class);
        when(carServiceImpl.deleteCarById(anyLong())).thenReturn("42");
        RestResponse actualDeleteCarByIdResult = (new CarController(carServiceImpl)).deleteCarById(123L);
        assertEquals(HttpStatus.OK, actualDeleteCarByIdResult.getStatus());
        ObjectNode data = actualDeleteCarByIdResult.getData();
        assertEquals("{\r\n  \"message\" : \"42\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carServiceImpl).deleteCarById(anyLong());
    }

    /**
     * Method under test: {@link CarController#updateCarStatus(Long, String)}
     */
    @Test
    void testUpdateCarStatus() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updateCarStatus(anyLong(), anyString())).thenReturn(1);
        RestResponse actualUpdateCarStatusResult = (new CarController(new CarServiceImpl(carRepository)))
                .updateCarStatus(123L, "Status");
        assertEquals(HttpStatus.OK, actualUpdateCarStatusResult.getStatus());
        ObjectNode data = actualUpdateCarStatusResult.getData();
        assertEquals("{\r\n  \"message\" : \"status updated successfully\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updateCarStatus(anyLong(), anyString());
    }

    /**
     * Method under test: {@link CarController#updateCarStatus(Long, String)}
     */
    @Test
    void testUpdateCarStatus2() {
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.updateCarStatus(anyLong(), anyString())).thenReturn(0);
        RestResponse actualUpdateCarStatusResult = (new CarController(new CarServiceImpl(carRepository)))
                .updateCarStatus(123L, "Status");
        assertEquals(HttpStatus.OK, actualUpdateCarStatusResult.getStatus());
        ObjectNode data = actualUpdateCarStatusResult.getData();
        assertEquals("{\r\n  \"message\" : \"No car affected\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carRepository).updateCarStatus(anyLong(), anyString());
    }

    /**
     * Method under test: {@link CarController#updateCarStatus(Long, String)}
     */
    @Test
    void testUpdateCarStatus4() {
        CarServiceImpl carServiceImpl = mock(CarServiceImpl.class);
        when(carServiceImpl.updateCarStatus(anyLong(), anyString())).thenReturn("2020-03-01");
        RestResponse actualUpdateCarStatusResult = (new CarController(carServiceImpl)).updateCarStatus(123L, "Status");
        assertEquals(HttpStatus.OK, actualUpdateCarStatusResult.getStatus());
        ObjectNode data = actualUpdateCarStatusResult.getData();
        assertEquals("{\r\n  \"message\" : \"2020-03-01\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(carServiceImpl).updateCarStatus(anyLong(), anyString());
    }

    /**
     * Method under test: {@link CarController#getAvailableCars()}
     */
    @Test
    void testGetAvailableCars() throws Exception {
        when(carServiceImpl.getAvailableCars(anyString())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/available");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CarController#getAvailableCars()}
     */
    @Test
    void testGetAvailableCars2() throws Exception {
        Car car = new Car();
        car.setCarClass("?");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("?");
        car.setCarModel("?");
        car.setCarStatus("?");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carServiceImpl.getAvailableCars(anyString())).thenReturn(carList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/available");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"carId\":123,\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"}]"));
    }

    /**
     * Method under test: {@link CarController#getAvailableCars()}
     */
    @Test
    void testGetAvailableCars3() throws Exception {
        Car car = new Car();
        car.setCarClass("?");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("?");
        car.setCarModel("?");
        car.setCarStatus("?");

        Car car1 = new Car();
        car1.setCarClass("?");
        car1.setCarCost(1L);
        car1.setCarId(123L);
        car1.setCarMark("?");
        car1.setCarModel("?");
        car1.setCarStatus("?");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car1);
        carList.add(car);
        when(carServiceImpl.getAvailableCars(anyString())).thenReturn(carList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/available");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"carId\":123,\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"},{\"carId\":123,"
                                        + "\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"}]"));
    }

    /**
     * Method under test: {@link CarController#getCarsByModel(String)}
     */
    @Test
    void testGetCarsByModel() throws Exception {
        when(carServiceImpl.getCarsByModel(anyString())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/model/get")
                .param("model", "foo");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link CarController#getCarsByModel(String)}
     */
    @Test
    void testGetCarsByModel2() throws Exception {
        Car car = new Car();
        car.setCarClass("?");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("?");
        car.setCarModel("?");
        car.setCarStatus("?");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carServiceImpl.getCarsByModel(anyString())).thenReturn(carList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/model/get")
                .param("model", "foo");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"carId\":123,\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"}]"));
    }

    /**
     * Method under test: {@link CarController#getCarsByModel(String)}
     */
    @Test
    void testGetCarsByModel3() throws Exception {
        Car car = new Car();
        car.setCarClass("?");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("?");
        car.setCarModel("?");
        car.setCarStatus("?");

        Car car1 = new Car();
        car1.setCarClass("?");
        car1.setCarCost(1L);
        car1.setCarId(123L);
        car1.setCarMark("?");
        car1.setCarModel("?");
        car1.setCarStatus("?");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car1);
        carList.add(car);
        when(carServiceImpl.getCarsByModel(anyString())).thenReturn(carList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/model/get")
                .param("model", "foo");
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"carId\":123,\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"},{\"carId\":123,"
                                        + "\"carClass\":\"?\",\"carModel\":\"?\",\"carMark\":\"?\",\"carCost\":1,\"carStatus\":\"?\"}]"));
    }

    /**
     * Method under test: {@link CarController#getCarsByClass(String)}
     */
    @Test
    void testGetCarsByClass() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/class/get")
                .param("carClass", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testGetCarsByClass2() throws Exception {
        Mockito.when(carServiceImpl.getCarsByClass(anyString())).thenReturn(Collections.EMPTY_LIST);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/cost/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.getCarsByClass(anyString());
        Mockito.verify(carServiceImpl).getCarsByClass(anyString());

    }


    /**
     * Method under test: {@link CarController#getCarsByCost(Long, String)}
     */
    @Test
    void testGetCarsByCost() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/cars/cost/get");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("cost", String.valueOf(1L))
                .param("operation", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }


    @Test
    void testGetCarsByCost2() throws Exception {
        Mockito.when(carServiceImpl.getCarsByCost(any(), anyString())).thenReturn(Collections.EMPTY_LIST);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/cost/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.getCarsByCost(any(), anyString());
        Mockito.verify(carServiceImpl).getCarsByCost(any(), anyString());

    }

    /**
     * Method under test: {@link CarController#updateCarInfo(Long, String, String, String, Long)}
     */
    @Test
    void testUpdateCarInfo() throws Exception {
        MockHttpServletRequestBuilder putResult = MockMvcRequestBuilders.put("/api/v1/cars/update/all");
        MockHttpServletRequestBuilder requestBuilder = putResult.param("carId", String.valueOf(1L))
                .param("carModel", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }


    @Test
    void testUpdateCarInfo2() throws Exception {
        Mockito.when(carServiceImpl.updateCarFeatures(anyLong(), anyString(), anyString(), anyString(), anyLong()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/all").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.updateCarInfo(anyLong(), anyString(), anyString(), anyString(), anyLong());
        Mockito.verify(carServiceImpl).updateCarFeatures(anyLong(), anyString(), anyString(), anyString(), anyLong());

    }

    @Test
    void testUpdateCarModelClassAndMark() throws Exception {
        Mockito.when(carServiceImpl.updateCarFeatures(anyLong(), anyString(), anyString(), anyString()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/v1").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.updateCarModelClassAndMark(anyLong(), anyString(), anyString(), anyString());
        Mockito.verify(carServiceImpl).updateCarFeatures(anyLong(), anyString(), anyString(), anyString());

    }


    @Test
    void testUpdateCarModelAndClass() throws Exception {
        Mockito.when(carServiceImpl.updateCarFeatures(anyLong(), anyString(), anyString()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/v2").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.updateCarModelAndClass(anyLong(), anyString(), anyString());
        Mockito.verify(carServiceImpl).updateCarFeatures(anyLong(), anyString(), anyString());

    }


    @Test
    void testUpdateCarMark() throws Exception {
        Mockito.when(carServiceImpl.updateCarMark(anyLong(), anyString(), anyString()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/v3").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.updateCarMark(anyLong(), anyString(), anyString());
        Mockito.verify(carServiceImpl).updateCarMark(anyLong(), anyString(), anyString());
    }

    @Test
    void testUpdateCarModel() throws Exception {
        Mockito.when(carServiceImpl.updateCarFeatures(anyLong(), anyString()))
                .thenReturn("car updated successfully");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/cars/update/v4").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        carController.updateCarModel(anyLong(), anyString());
        Mockito.verify(carServiceImpl).updateCarFeatures(anyLong(), anyString());

    }
}

