package repository;

import model.Car;
import java.io.IOException;
import java.util.List;

public interface CarsRepository {
    List<Car> getAllCars() throws IOException;
    void saveAllCars(List<Car> cars) throws IOException;
    String getNumbersByColorOrMileage(List<Car> cars);
    long getUniqueCarsCount(List<Car> cars);
    long getUniqueCarsCountauto(List<Car> cars);
    String getMinpriceColor(List<Car> cars);
    double getAverageprice(List<Car> cars, String model);
}