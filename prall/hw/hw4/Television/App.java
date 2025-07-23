package Television;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Television> tvList = new ArrayList<>();
        tvList.add(new Television("BBK", 32));
        tvList.add(new Television("Xiaomi TV", 42));
        tvList.add(new Television("Samsung", 50));

        Television selectedTV = null;
        boolean okSelect = false;

        while(!okSelect) {
            System.out.println("\n\033[1mСписок телевизоров:\033[0m");
            for (int i = 0; i < tvList.size(); i++) {
                Television tv = tvList.get(i);
                System.out.printf("%d. %s (%.1f\")\n", i + 1, tv.getModel(), tv.getDiagonal());
            }

            System.out.println("\n\033[1mВыберите способ выбора телевизора:\033[0m");
            System.out.println("1. Выбрать по номеру из списка");
            System.out.println("2. Ввести название телевизора");
            System.out.println("3. Ввести диагональ телевизора");

            System.out.print("\n\033[1mВаш выбор (1-3): \033[0m");
            int tvMethod = scanner.nextInt();
            scanner.nextLine();

            switch (tvMethod) {
                case 1:
                    System.out.print("\n\033[1mВыберите телевизор (1-" + tvList.size() + "): \033[0m");
                    int tvChoice = scanner.nextInt() - 1;

                    if (tvChoice >= 0 && tvChoice < tvList.size()) {
                        selectedTV = tvList.get(tvChoice);
                        okSelect = true;
                    } else {
                        System.out.println("Ошибка! Неверный выбор.");
                    } break;
                case 2:
                    System.out.print("\n\033[1mВведите название телевизора: \033[0m");
                    String nameInput = scanner.nextLine().trim();
                    for (Television tv : tvList) {
                        if (tv.getModel().equalsIgnoreCase(nameInput)) {
                            selectedTV = tv;
                            okSelect = true;
                            break;
                        }
                    }
                    if (selectedTV == null) {
                        System.out.println("Телевизор с таким названием не найден.");
                    }
                    break;
                case 3:
                    System.out.print("\n\033[1mВведите диагональ телевизора: \033[0m");
                    double diagInput = scanner.nextDouble();
                    for (Television tv : tvList) {
                        if (tv.getDiagonal() == diagInput) {
                            selectedTV = tv;
                            okSelect = true;
                            break;
                        }
                    }
                    if (selectedTV == null) {
                        System.out.println("Телевизор с такой диагональю не найден.");
                    }
                default:
                    System.out.println("Ошибка! Неверный способ выбора.");
                    break;
            }
        }

        while (true) {
            if (selectedTV != null) {
                System.out.println("\n\n\033[1mПульт управления от " + selectedTV.getModel() + " (" + selectedTV.getDiagonal() + "\")\033[0m");
            }
            System.out.println("= = = = = = = = = = = = = = =");
            System.out.println("1. Вкл/Выкл TV");
            System.out.println("2. Включить случайный канал");
            System.out.println("3. Включить конкретный канал");
            System.out.println("4. Включить следующий канал");
            System.out.println("5. Включить предыдущий канал");
            System.out.println("6. Установить громкость");
            System.out.println("7. Добавить уровень громкости (+15 ед.)");
            System.out.println("8. Убавить уровень громкости (-15 ед.)");
            System.out.println("9. Сведения о статусе TV");
            System.out.println("0. \033[1;3mЗавершить программу\033[0m");
            System.out.println("= = = = = = = = = = = = = = =");
            System.out.print("\n\033[1mВведите цифру: \033[0m");

            int choiceuser = scanner.nextInt();

            switch (choiceuser) {
                case 1: selectedTV.power(); break;
                case 2: selectedTV.powerOnRandomChannel(); break;
                case 3:
                    if (selectedTV.isPowerOn()) {
                        System.out.print("Введите номер канала (1-20): ");
                        int ChannelNumber = scanner.nextInt();
                        selectedTV.setChannel(ChannelNumber);
                    } else {
                        System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
                    } break;
                case 4: selectedTV.nextChannel(); break;
                case 5: selectedTV.prevChannel(); break;
                case 6:
                    if (selectedTV.isPowerOn()) {
                        System.out.print("Введите уровень громкости (1-100): ");
                        int VolumeNumber = scanner.nextInt();
                        selectedTV.setVolume(VolumeNumber);
                    } else {
                        System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
                    } break;
                case 7: selectedTV.upVolume(); break;
                case 8: selectedTV.downVolume(); break;
                case 9: selectedTV.tvStatus(); break;
                case 0:
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ошибка! Попробуйте повторить действие.");
            }
        }
    }
}
