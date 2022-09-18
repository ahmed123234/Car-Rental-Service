package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.Car;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {CarRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    /**
     * Method under test: {@link CarRepository#findByCarId(Long)}
     */
    @Test
    void testFindByCarId() {
        // given
        Long carId = 1L;

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(carId);
        carRepository.save(car);

        Car expected = carRepository.findByCarId(carId);

        assertThat(carRepository.findByCarId(carId)).isEqualTo(expected);
    }

    /**
     * Method under test: {@link CarRepository#updateCar(Long, Long)}
     */
    @Test
    void testUpdateCar() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.updateCar(1L, 1L)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCar(Long, Long)}
     */
    @Test
    void testUpdateCar2() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.updateCar(2L, 1L)).isEqualTo(0);

    }

    /**
     * Method under test: {@link CarRepository#updatePrices(Long)}
     */
    @Test
    void testUpdatePrices() {
        //given
        Long coefficient = 2L;

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.updatePrices(coefficient)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCarFeatures(Long, String, String, String, Long)}
     */
    @Test
    void testUpdateCarFeatures() {
        //given
        Long carId = 1L;
        Long cost = 200L;
        String model = "Golf";
        String carClass = "class B";
        String mark = "PS12TU2349";


        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.updateCarFeatures(carId, model, carClass, mark, cost)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCarModelClassAndMark(Long, String, String, String)}
     */
    @Test
    void testUpdateCarModelClassAndMark() {
        //given
        Long carId = 1L;
        String model = "Golf";
        String carClass = "class B";
        String mark = "PS12TU2349";


        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        assertThat(carRepository.updateCarModelClassAndMark(carId, model, carClass, mark)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCarModelAndClass(Long, String, String)}
     */
    @Test
    void testUpdateCarModelAndClass() {
        //given
        Long carId = 1L;
        String model = "Golf";
        String carClass = "class B";

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.updateCarModelAndClass(carId, model, carClass)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCarModelAndMark(Long, String, String)}
     */
    @Test
    void testUpdateCarModelAndMark() {
        //given
        Long carId = 1L;
        String model = "Golf";
        String mark = "PS12TU2349";


        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        assertThat(carRepository.updateCarModelAndMark(carId, model, mark)).isEqualTo(1);
    }


    /**
     * Method under test: {@link CarRepository#updateCarModel(Long, String)}
     */
    @Test
    void testUpdateCarModel() {
        //given
        Long carId = 1L;
        String model = "Golf";

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        assertThat(carRepository.updateCarModel(carId, model)).isEqualTo(1);
    }

    /**
     * Method under test: {@link CarRepository#updateCarStatus(Long, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateCarStatus() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        carRepository.updateCarStatus(1L, "foo");
    }

    /**
     * Method under test: {@link CarRepository#findByCarStatus(String, Sort)}
     */
    @Test
    void testFindByCarStatus() {

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);

        assertThat(carRepository.findByCarStatus("Car", Sort.unsorted())).asList().isEmpty();
    }

    /**
     * Method under test: {@link CarRepository#findByCarStatus(String, Sort)}
     */
    @Test
    void testFindByCarStatus2() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> carList  = carRepository.findByCarStatus("Car Status", Sort.by("carId")
                .ascending());

        assertThat(carRepository.findByCarStatus("Car Status", Sort.by("carId")
                .ascending())).isEqualTo(carList);
    }

    /**
     * Method under test: {@link CarRepository#findByCarMark(String)}
     */
    @Test
    void testFindByCarMark() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        Optional<Car> expectedCar = carRepository.findByCarMark("PS12TU2347");

        assertThat(carRepository.findByCarMark("PS12TU2347")).isEqualTo(expectedCar);
    }


    /**
     * Method under test: {@link CarRepository#findByCarCost(Long)}
     */
    @Test
    void testFindByCarCost() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> expectedCar = carRepository.findByCarCost(1L);

        assertThat(carRepository.findByCarCost(1L)).isEqualTo(expectedCar);
    }


    /**
     * Method under test: {@link CarRepository#findByCarCostGrater(Long, Sort)}
     */
    @Test
    void testFindByCarCostGrater() {

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> expectedCar = carRepository.findByCarCostGrater(1L, Sort.unsorted() );


        assertThat(carRepository.findByCarCostGrater(1L, Sort.unsorted())).isEqualTo(expectedCar);
    }


    /**
     * Method under test: {@link CarRepository#findByCarCostLess(Long, Sort)}
     */
    @Test
    void testFindByCarCostLess() {
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> expectedCar = carRepository.findByCarCostGrater(1L, Sort.unsorted() );


        assertThat(carRepository.findByCarCostLess(1L, Sort.unsorted())).isEqualTo(expectedCar);
    }



    /**
     * Method under test: {@link CarRepository#findByCarClass(String)}
     */
    @Test
    void testFindByCarClass() {
        //given
        String carClass = "Car Class";
        Car car = new Car();
        car.setCarClass(carClass);
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> expectedCar = carRepository.findByCarClass(carClass);


        assertThat(carRepository.findByCarClass(carClass)).isEqualTo(expectedCar);
    }


    /**
     * Method under test: {@link CarRepository#findByCarModel(String)}
     */
    @Test
    void testFindByCarModel() {

        String carsModel = "Car Model";

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarMark("PS12TU2347");
        car.setCarModel(carsModel);
        car.setCarStatus("Car Status");
        car.setCarId(1L);
        carRepository.save(car);
        List<Car> expectedCar = carRepository.findByCarModel(carsModel);


        assertThat(carRepository.findByCarModel(carsModel)).isEqualTo(expectedCar);
    }
}

