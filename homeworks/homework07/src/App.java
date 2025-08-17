import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> persons = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        System.out.println("Введите количество покупателей: ");
        int peopleAmount = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < peopleAmount; i++) {
            System.out.println("Введите Имя покупателя №" + (i + 1) + " и сумму денег в его кошельке (формат: Имя=Сумма): ");
            String[] peopleDB = scanner.nextLine().trim().split("=");
            if (peopleDB.length != 2) {
                System.out.println("Ошибка! Формат: Имя пользователя=Сумма (например: Борис=10)");
                i--;
                continue;
            } else {
                int money = Integer.parseInt(peopleDB[1].trim());
                Person person = new Person(peopleDB[0].trim(), money);
                persons.add(person);
            }
        }

        System.out.println("Введите количество продуктов:");
        int productAmount = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < productAmount; i++) {
            System.out.println("Введите данные продукта №" + (i+1));
            System.out.println("\nФормат");
            System.out.println("Обычный: Название=Цена");
            System.out.println("\u001B[3mНапример: Яблоко=100\u001B[23m\n");
            System.out.println("Скидочный: Название=Цена=Скидка(СУММА)=Срок(ГГГГ-ММ-ДД)");
            System.out.println("\u001B[3mНапример: Яблоко=100=50=2026-08-5\u001B[23m\n");
            String[] productDB = scanner.nextLine().trim().split("=");

            if (productDB.length == 2) {
                String name = productDB[0].trim();
                String priceInput = productDB[1].trim();

                if (!priceInput.matches("\\d+")) {
                    System.out.println("Ошибка! Цена должна быть целым положительным числом.");
                    System.exit(0);
                }

                int price = Integer.parseInt(priceInput);

                if (name.isEmpty()) {
                    System.out.println("Имя продукта не может быть пустым.");
                    System.exit(0);
                } else if (name.length() < 3) {
                    System.out.println("Имя продукта не может быть короче 3 символов.");
                    System.exit(0);
                } else if (name.matches("\\d+")) {
                    System.out.println("Имя продукта не может состоять только из цифр.");
                    System.exit(0);
                } else if (price <= 0) {
                    System.out.println("Цена продукта должна быть положительной.");
                    System.exit(0);
                }

                Product product = new Product(name, price);
                products.add(product);

            } else if (productDB.length == 4) {
                String name = productDB[0].trim();
                String priceInput = productDB[1].trim();
                String discountInput = productDB[2].trim();
                String expiry = productDB[3].trim();

                if (!priceInput.matches("\\d+") || !discountInput.matches("\\d+")) {
                    System.out.println("Ошибка! Цена и скидка должны быть целыми положительными числами.");
                    System.exit(0);
                }

                int price = Integer.parseInt(priceInput);
                int discount = Integer.parseInt(discountInput);

                if (name.isEmpty()) {
                    System.out.println("Имя продукта не может быть пустым.");
                    System.exit(0);
                } else if (name.length() < 3) {
                    System.out.println("Имя продукта не может быть короче 3 символов.");
                    System.exit(0);
                } else if (name.matches("\\d+")) {
                    System.out.println("Имя продукта не может состоять только из цифр.");
                    System.exit(0);
                } else if (price <= 0) {
                    System.out.println("Цена продукта должна быть положительной.");
                    System.exit(0);
                } else if (discount < 0) {
                    System.out.println("Скидка не может быть отрицательной.");
                    System.exit(0);
                } else if (discount > price) {
                    System.out.println("Скидка не может быть больше цены.");
                    System.exit(0);
                } else if (expiry.isEmpty()) {
                    System.out.println("Срок действия скидки не может быть пустым.");
                    System.exit(0);
                }

                Product product = new DiscountProduct(name, price, discount, expiry);
                products.add(product);

            } else {
                System.out.println("Ошибка! Неверный формат. Допустимые форматы:\n"
                        + "Обычный продукт: Название=Цена\n"
                        + "Скидочный продукт: Название=Цена=Скидка=Срок");
                i--;
            }
        }
        System.out.print("Доступные продукты: ");
        for (int i = 0; i < products.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(products.get(i).getName());
        }
        System.out.println();
        boolean buying = true;
        while (buying) {
            for (Person person : persons) {
                System.out.println(person.getName() + ", введите название продукта для покупки (или END для завершения):");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("END")) {
                    buying = false;
                    break;
                }

                Product selected = null;
                for (Product NameProduct : products) {
                    if (NameProduct.getName().equalsIgnoreCase(input)) {
                        selected = NameProduct;
                        break;
                    }
                }

                if (selected != null) {
                    person.buy(selected);
                } else {
                    System.out.println("Продукт отсутствует в списке заявленных.");
                }
            }
        }

        System.out.println();

        for (Person person : persons) {
            if (person.getProducts().isEmpty()) {
                System.out.println(person.getName() + " - Ничего не куплено");
            } else {
                StringBuilder estr = new StringBuilder();
                for (Product product : person.getProducts()) {
                    if (estr.length() > 0) {
                        estr.append(", ");
                    }
                    estr.append(product.getName());
                }
                System.out.println(person.getName() + " - " + estr.toString());
            }
        }
        scanner.close();
    }
}