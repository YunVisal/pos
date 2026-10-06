import java.math.BigDecimal;

public record SaleLine(String sku, int quantity, Money unitPrice) {
    public SaleLine {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1 unit");
        }
    }

    Money getTotal() {
        BigDecimal totalAmount = unitPrice.amount().multiply(new BigDecimal(quantity));
        return new Money(totalAmount, unitPrice.currency());
    }
}
