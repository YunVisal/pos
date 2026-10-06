import java.math.BigDecimal;

public class main {
    public static void main(String[] args) {
        Money riceUnitPrice = new Money(new BigDecimal("2000"), "KHR");
        SaleLine rice = new SaleLine("ABC123", 40, riceUnitPrice);
        Cash cash = new Cash(riceUnitPrice);
        System.out.println(printRecieptLabel(cash));
    }

    static String printRecieptLabel(PaymentMethod method) {
        return switch (method) {
            case Cash cash -> String.format("Cash %s %s", cash.tendered().amount(), cash.tendered().currency());
            case Card card -> String.format("Card ****%s", card.last4Digits());
            case KhqrQr khqrQr -> String.format("KHQR ref %s", khqrQr.transactionRef());
            case Vocher vocher -> String.format("Vocher code %s", vocher.code());
        };
    }
}
