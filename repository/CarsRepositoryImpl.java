package repository;

import model.Car;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements CarsRepository {
    private final String ifile = "data/cars.txt";
    private final String ofile = "data/output.txt";

    public CarsRepositoryImpl() throws IOException {
    }

    @Override
    public List<Car> getAllCars() throws IOException {
        List<Car> cars = new ArrayList<>();
        File file = new File(ifile);
        if (!file.exists()) {
            System.err.println("Файл " + ifile + " не найден.");
            return cars;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 5) {
                cars.add(new Car(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Long.parseLong(parts[3].trim()),
                        Double.parseDouble(parts[4].trim())
                ));
            }
        }
        reader.close();
        return cars;
    }

    @Override
    public void saveAllCars(List<Car> cars) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(ofile));
        writer.println("Автомобили в базе:");
        writer.println("Number Model Color Mileage Cost");
        for (Car car : cars) {
            writer.println(car.getNumber() + " " + car.getModel() + " " + car.getColor() + " " + car.getMileage() + " " + car.getPrice());
        }
        writer.println("Номера автомобилей по цвету или пробегу: " + getNumbersByColorOrMileage(cars));
        writer.println("Уникальные модели: " + getUniqueCarsCount(cars) + " шт.");
        writer.println("Уникальные автомобили: " + getUniqueCarsCountauto(cars) + " шт.");
        writer.println("Цвет автомобиля с минимальной стоимостью: " + getMinpriceColor(cars));
        writer.println("Средняя стоимость модели Toyota: " + getAverageprice(cars, "Toyota"));
        writer.println("Средняя стоимость модели Volvo: " + getAverageprice(cars, "Volvo"));
        System.out.println("\nРезультаты сохранены в: " + ofile);
        writer.close();
    }

    @Override
    public String getNumbersByColorOrMileage(List<Car> cars) {
        String colorToFind = "Black";
        long mileageToFind = 0L;
        return cars.stream()
                .filter(c -> c.getColor().equalsIgnoreCase(colorToFind) || c.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.joining(" "));
    }

    @Override
    public long getUniqueCarsCount(List<Car> cars) {
        long n = 700000;
        long m = 800000;
        return cars.stream()
                .filter(c -> c.getPrice() >= n && c.getPrice() <= m)
                .map(Car::getModel)
                .distinct()
                .count();
    }

    @Override
    public long getUniqueCarsCountauto(List<Car> cars) {
        long n = 700000;
        long m = 800000;
        return cars.stream()
                .filter(c -> c.getPrice() >= n && c.getPrice() <= m)
                .count();
    }

    @Override
    public String getMinpriceColor(List<Car> cars) {
        return cars.stream()
                .min((c1, c2) -> Double.compare(c1.getPrice(), c2.getPrice()))
                .map(Car::getColor)
                .orElse("Не найден");
    }

    @Override
    public double getAverageprice(List<Car> cars, String model) {
        return cars.stream()
                .filter(c -> c.getModel().equalsIgnoreCase(model))
                .mapToDouble(Car::getPrice)
                .average()
                .orElse(0.0);
    }
}