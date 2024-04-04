import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Payment {
    private double netPayment;
    private double nhifAmount;
    private LocalDateTime lastUpdatedDateTime;

    //Constructor that initializes the Payment object
    public Payment(double netPayment, double nhifAmount) {
        this.netPayment = netPayment;
        this.nhifAmount = nhifAmount;
        this.lastUpdatedDateTime = LocalDateTime.now();
    }

    public double getNetPayment() {
        return netPayment;
    }

    public double getNhifAmount() {
        return nhifAmount;
    }

    //Automatic Timestamp that is added after patients details
    public LocalDateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public String getLastUpdatedDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return lastUpdatedDateTime.format(formatter);
    }

    // Method to format payment details
    public String formatPaymentDetails() {
        return String.format("Net Payment: %.2f, NHIF Amount: %.2f, Date: %s", netPayment, nhifAmount, getLastUpdatedDateFormatted());
    }
}
