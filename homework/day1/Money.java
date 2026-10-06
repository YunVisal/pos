import java.math.BigDecimal;
import java.util.Locale;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount can not be null.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount can not be negative value.");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency can not be null.");
        }

        if (!currency.toLowerCase(Locale.ROOT).equals("usd") && !currency.toLowerCase(Locale.ROOT).equals("khr")) {
            throw new IllegalArgumentException("Currency should be either KHR or USD");
        }
    }
}
