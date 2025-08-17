public class DiscountProduct extends Product {
    private final int discount;
    private final String expiryDate;

    public DiscountProduct(String name, int cost, int discount, String expiryDate) {
        super(name, cost);

        validateDiscount(cost, discount);
        validateExpiry(expiryDate);

        this.discount = discount;
        this.expiryDate = expiryDate;
    }

    private void validateDiscount(int cost, int discount) {
        if (discount <= 0) {
            throw new IllegalArgumentException("Скидка не может быть отрицательной или равняться нулю.");
        }
        if (discount > cost) {
            throw new IllegalArgumentException("Скидка не может быть больше цены.");
        }
    }

    private void validateExpiry(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Срок действия скидки не может быть пустым.");
        }
    }

    @Override
    public int getCost() {
        return super.getCost() - discount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof DiscountProduct other)) return false;
        return discount == other.discount && expiryDate.equals(other.expiryDate);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + discount * 31 + expiryDate.hashCode();
    }
}