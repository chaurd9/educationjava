public class Product {
    private String name;
    private int cost;

    public Product (String name, int cost) {
        if (name.trim().equals("")) {
            System.out.println("Имя не может быть пустым.");
            System.exit(0);
        } else if (name.trim().length() < 3) {
            System.out.println("Имя не может быть короче 3 символов.");
            System.exit(0);
        } else if (cost < 0) {
            System.out.println("Деньги не могут быть отрицательными.");
            System.exit(0);
        } else if (name.trim().matches("\\d+")) {
            System.out.println("Имя не должно содержать только цифры.");
            System.exit(0);
        } else {
            this.name = name;
            this.cost = cost;
        }
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String toString() {
        return name;
    }

    public int hashCode() {
        return name.hashCode() + cost;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.name.equals(other.name) && this.cost == other.cost;
    }

}