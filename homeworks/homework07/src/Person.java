import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private int money;
    private List<Product> products;

    public Person(String name, int money) {
        validName(name);
        validMoney(money);
        this.name = name;
        this.money = money;
        this.products = new ArrayList<>();
    }

    private void validName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        if (name.trim().length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов.");
        }
    }

    private void validMoney(int money) {
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными.");
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
            System.out.println("\n" + name + " купил " + product.getName());
            System.out.println("У покупателя " + name + " осталось " + money + " уе.\n");
        } else {
            System.out.println("\n" + name + " не может позволить себе " + product.getName() + "\n");
            System.out.println("У покупателя " + name + " осталось " + money + " уе.\n");
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
