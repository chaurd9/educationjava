package test;

import model.Car;
import repository.CarsRepository;
import repository.CarsRepositoryImpl;
import java.util.List;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CarsRepository repository = new CarsRepositoryImpl();
        List<Car> cars = repository.getAllCars();

        System.out.println("Автомобили в базе:");
        System.out.println("Number Model Color Mileage Cost");
        for (Car car : cars) {
            System.out.println(car.getNumber() + " " + car.getModel() + " " + car.getColor() + " " + car.getMileage() + " " + car.getPrice());
        }

        System.out.println("Номера автомобилей по цвету или пробегу: " + repository.getNumbersByColorOrMileage(cars));
        System.out.println("Уникальные модели: " + repository.getUniqueCarsCount(cars) + " шт.");
        System.out.println("Уникальные автомобили: " + repository.getUniqueCarsCountauto(cars) + " шт.");
        System.out.println("Цвет автомобиля с минимальной стоимостью: " + repository.getMinpriceColor(cars));
        System.out.println("Средняя стоимость модели Toyota: " + repository.getAverageprice(cars, "Toyota"));
        System.out.println("Средняя стоимость модели Volvo: " + repository.getAverageprice(cars, "Volvo"));

        repository.saveAllCars(cars);
    }
}