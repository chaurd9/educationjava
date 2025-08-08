import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private int money;
    private List<Product> products;

    public Person (String name, int money) {
        if (name.trim().equals("")) {
            System.out.println("Имя не может быть пустым.");
            System.exit(0);
        } else if (name.trim().length() < 3) {
            System.out.println("Имя не может быть короче 3 символов.");
            System.exit(0);
        } else if (money < 0) {
            System.out.println("Деньги не могут быть отрицательными.");
            System.exit(0);
        } else {
            this.name = name;
            this.money = money;
            this.products = new ArrayList<>();
        }
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void buy(Product product) {
        int price = product.getCost();

        if (money >= price) {
            money -= price;
            products.add(product);
            System.out.println(name + " купил " + product.getName());
        } else {
            System.out.println(name + " не может позволить себе " + product.getName());
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + money;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;
        Person other = (Person) obj;
        return this.name.equals(other.name) && this.money == other.money;
    }
}

