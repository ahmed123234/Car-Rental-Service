package com.example.carrentalservice.services.car;

import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.repositories.CarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CarServiceImpl.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Autowired
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carService = new CarServiceImpl(carRepository);
        List<Car> cars = new ArrayList<>();
        cars.add(new Car(1L,
                "class A",
                "Kia",
                "123-453-909",
                200L,
                "available"));

        cars.add(new Car(2L,
                "class B",
                "Kia",
                "123-453-989",
                100L,
                "available"));

        carRepository.saveAll(cars);
    }

    @AfterEach
    void tearDown() {
        carRepository.deleteAll();
    }

    /**
     * Method under test: {@link CarServiceImpl#addCar(Car)}
     */
    @Test
    void canAddCar() {
        //given
        Car actualCar = new Car();
        actualCar.setCarClass("class A");
        actualCar.setCarCost(109L);
        actualCar.setCarMark("123-456-909");
        actualCar.setCarModel("Kia");
        actualCar.setCarStatus("available");

        //when
        carService.addCar(actualCar);

        //then
        ArgumentCaptor<Car> carArgumentCaptor =
                ArgumentCaptor.forClass(Car.class);

        verify(carRepository).save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();

        assertThat(capturedCar).isEqualTo(actualCar);
    }

    /**
     * Method under test: {@link CarServiceImpl#addCar(Car)}
     */
    @Test
    void canAddCarWillThrowWhenMarkExit() {

        //given
        String mark = "123-453-909";

        Car actualCar = new Car();
        actualCar.setCarClass("class A");
        actualCar.setCarCost(109L);
        actualCar.setCarMark(mark);
        actualCar.setCarModel("Kia");
        actualCar.setCarStatus("available");

        Optional<Car> car = Optional.of(actualCar);

        //when
        //then
        given(carRepository.findByCarMark(mark)).willReturn(car);
        assertThatThrownBy(() -> carService.addCar(actualCar))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("car Mark already taken");
    }

    /**
     * Method under test: {@link CarServiceImpl#getCar(Long)}
     */
    @Test
    void canGetCarById() {
        //given
        Long carId = 1L;
        Car car = new Car(1L,
                "class B",
                "Kia",
                "123-453-989",
                100L,
                "available");

        //when
        ArgumentCaptor<Long> carArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(carRepository.findByCarId(carArgumentCaptor.capture())).thenReturn(car);

        //then

        assertThat(carService.getCar(carId)).isEqualTo(car);
    }

    /**
     * Method under test: {@link CarServiceImpl#getCars()}
     */
    @Test
    void canGetAllCars() {
        //when
        carService.getCars();

        //then
        verify(carRepository).findAll();
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarCost(Long, Long)}
     */
    @Test
    void CanTestIfCarCostUpdated() {
        //given
        Long carId = 1L;
        Long cost = 300L;

        //when
        carService.updateCarCost(carId, cost);

        ArgumentCaptor<Long> carIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        ArgumentCaptor<Long> costArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        //when
        when(carRepository.updateCar(carIdArgumentCaptor.capture(), costArgumentCaptor.capture())).thenReturn(0);

        //then
        assertThat(carService.updateCarCost(carId, cost)).isEqualTo("No car affected");


    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarCost(Long, Long)}
     */
    @Test
    void CanUpdateCarCostSuccess() {
        //given
        Long carId = 1L;
        Long cost = 300L;

        //when
        when(carRepository.updateCar(carId, cost)).thenReturn(1);

        //then
        assertThat(carService.updateCarCost(carId, cost)).isEqualTo("Car's cost updated Successfully");

    }

    /**
     * Method under test: {@link CarServiceImpl#updatePrices(Long)}
     */
    @Test
    void canUpdatePrices() {
        //given
        Long coefficient = 2L;

        //when
        ArgumentCaptor<Long> carArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(carRepository.updatePrices(carArgumentCaptor.capture())).thenReturn(0);

        //then
        assertThat(carService.updatePrices(coefficient)).isEqualTo("No car affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updatePrices(Long)}
     */
    @Test
    void CanTestIfCarsPricesUpdated() {

        //given
        Long coefficient = 2L;

        //when
        ArgumentCaptor<Long> carArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(carRepository.updatePrices(carArgumentCaptor.capture())).thenReturn(2);

        //then
        assertThat(carService.updatePrices(coefficient)).isEqualTo("Cars' costs updated Successfully");

    }

    /**
     * Method under test: {@link CarServiceImpl#deleteCarById(Long)}
     */
    @Test
    void deleteCarById() {
        //given
        Long carId = 1L;

        Car actualCar = new Car(2L,
                "class B",
                "Kia",
                "123-453-989",
                100L,
                "available");


        carRepository.save(actualCar);


        //when
        carService.deleteCarById(carId);

        //then
        ArgumentCaptor<Long> carArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(carRepository).deleteById(carArgumentCaptor.capture());
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarMark(Long, String, String)}
     */
    @Test
    void updateCarMark() {
        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";

        //when
        carService.updateCarMark(carId, model, mark);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> modelArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> markArgumentCaptor = ArgumentCaptor.forClass(String.class);


        verify(carRepository).updateCarModelAndMark(idArgumentCaptor.capture(),
                modelArgumentCaptor.capture(),
                markArgumentCaptor.capture());
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarMark(Long, String, String)}
     */
    @Test
    void testIfUpdateCarMarkSuccess() {
        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";

        //when
        carService.updateCarMark(carId, model, mark);

        //when
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> modelArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> markArgumentCaptor = ArgumentCaptor.forClass(String.class);


        when(carRepository.updateCarModelAndMark(idArgumentCaptor.capture(),
                modelArgumentCaptor.capture(),
                markArgumentCaptor.capture())).thenReturn(1);

        //then
        assertThat(carService.updateCarMark(carId, model, mark)).isEqualTo("Car updated successfully");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String, String, Long)}
     */
    @Test
    void canUpdateCarModelMarkClassAndCost() {

        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";
        String carClass = "class D";
        Long cost = 100L;

        //when
        when(carRepository.updateCarFeatures(carId, model, carClass, mark, cost)).thenReturn(0);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass, mark, cost)).isEqualTo("No car affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String, String, Long)}
     */
    @Test
    void canTestIFUpdateCarModelMarkClassAndCostSuccess() {

        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";
        String carClass = "class D";
        Long cost = 100L;

        //when
        when(carRepository.updateCarFeatures(carId, model, carClass, mark, cost)).thenReturn(1);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass, mark, cost))
                .isEqualTo("Car updated successfully");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String, String)}
     */
    @Test
    void testUpdateCarModelClassAndMark() {
        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";
        String carClass = "class D";

        //when
        when(carRepository.updateCarModelClassAndMark(carId, model, carClass, mark)).thenReturn(0);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass, mark)).isEqualTo("No car affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String, String)}
     */
    @Test
    void canTestIFUpdateCarModelMarkAndClassSuccess() {

        //given
        Long carId = 1L;
        String model = "Fiat";
        String mark = "123-453-989";
        String carClass = "class D";

        //when
        when(carRepository.updateCarModelClassAndMark(carId, model, carClass, mark)).thenReturn(1);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass, mark))
                .isEqualTo("Car updated successfully");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String)}
     */
    @Test
    void testUpdateCarModelAndClass() {
        //given
        Long carId = 1L;
        String model = "Fiat";
        String carClass = "class D";

        //when
        when(carRepository.updateCarModelAndClass(carId, model, carClass)).thenReturn(0);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass)).isEqualTo("No cars affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String, String)}
     */
    @Test
    void canTestIFUpdateCarModelAndClassSuccess() {

        //given
        Long carId = 1L;
        String model = "Fiat";
        String carClass = "class D";

        //when
        when(carRepository.updateCarModelAndClass(carId, model, carClass)).thenReturn(1);

        //then
        assertThat(carService.updateCarFeatures(carId, model, carClass))
                .isEqualTo("Car updated successfully");
    }


    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String)}
     */
    @Test
    void testUpdateCarModel() {
        //given
        Long carId = 1L;
        String model = "Fiat";

        //when
        when(carRepository.updateCarModel(carId, model)).thenReturn(0);

        //then
        assertThat(carService.updateCarFeatures(carId, model))
                .isEqualTo("No cars affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarFeatures(Long, String)}
     */
    @Test
    void canTestIFUpdateCarModelSuccess() {

        //given
        Long carId = 1L;
        String model = "Fiat";

        //when
        when(carRepository.updateCarModel(carId, model)).thenReturn(1);

        //then
        assertThat(carService.updateCarFeatures(carId, model))
                .isEqualTo("Car updated successfully");
    }


    /**
     * Method under test: {@link CarServiceImpl#updateCarStatus(Long, String)}
     */
    @Test
    void updateCarStatus() {
        //given
        Long carId = 1L;
        String status = "rented";

        //when
        when(carRepository.updateCarStatus(carId, status)).thenReturn(0);

        //then
        assertThat(carService.updateCarStatus(carId, status))
                .isEqualTo("No car affected");
    }

    /**
     * Method under test: {@link CarServiceImpl#updateCarStatus(Long, String)}
     */
    @Test
    void canTestIFUpdateCarStatusSuccess() {

        //given
        Long carId = 1L;
        String status = "rented";

        //when
        when(carRepository.updateCarStatus(carId, status)).thenReturn(1);

        //then
        assertThat(carService.updateCarStatus(carId, status))
                .isEqualTo("status updated successfully");
    }

    /**
     * Method under test: {@link CarServiceImpl#getAvailableCars(String)}
     */
    @Test
    void testGetAvailableCars() {
        //given
        String status = "available";

        Car actualCar = new Car(2L,
                "class B",
                "Kia",
                "123-453-989",
                100L,
                "available");


        List<Car> cars = new ArrayList<>();
        cars.add(actualCar);
        //when
        when(carRepository.findByCarStatus(status, Sort.by(Sort.Order.asc("carCost"))))
                .thenReturn(cars);

        //then
        assertThat(carService.getAvailableCars(status)).isEqualTo(cars);

    }

    /**
     * Method under test: {@link CarServiceImpl#getCarsByCost(Long, String)}
     */
    @Test
    void canGetCarsByCostIfEqual() {
        //given
        Long cost = 100L;
        String operation = "=";

        //when
        carService.getCarsByCost(cost, operation);

        //then
        verify(carRepository).findByCarCost(cost);
    }

    /**
     * Method under test: {@link CarServiceImpl#getCarsByCost(Long, String)}
     */
    @Test
    void canGetCarsByCostIfGrater() {
        //given
        Long cost = 100L;
        String operation = ">";

        //when
        carService.getCarsByCost(cost, operation);

        //then
        verify(carRepository).findByCarCostGrater(cost, Sort.by(Sort.Direction.DESC, "carCost"));
    }

    /**
     * Method under test: {@link CarServiceImpl#getCarsByCost(Long, String)}
     */
    @Test
    void canGetCarsByCostIfLess() {
        //given
        Long cost = 100L;
        String operation = "<";

        //when
        carService.getCarsByCost(cost, operation);

        //then
        verify(carRepository).findByCarCostLess(cost, Sort.by(Sort.Direction.ASC, "carCost"));
    }


    /**
     * Method under test: {@link CarServiceImpl#getCarsByClass(String)}
     */
    @Test
    void testGetCarsByClass() {
        //given
        String carClass = "class D";

        //when
        carService.getCarsByClass(carClass);

        //then
        verify(carRepository).findByCarClass(carClass);
    }

    /**
     * Method under test: {@link CarServiceImpl#getCarsByModel(String)}
     */
    @Test
    void testGetCarsByModel() {
        //given
        String model = "Kia";

        //when
        carService.getCarsByModel(model);

        //then
        verify(carRepository).findByCarModel(model);
    }
}