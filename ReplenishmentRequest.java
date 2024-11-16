public class ReplenishmentRequest {
    private String medicationName;
    private int quantity;
    private String requester;// Pharmacist ID or Name
    private String status; // "Pending", "Approved", "Rejected"

    public ReplenishmentRequest(String medicationName, int quantity, String requester) {
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.requester = requester;
        this.status = "Pending";
    }

    public String getMedicationName() {
        return medicationName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRequester() {
        return requester;
    }

    public String getStatus() {
        return status;
    }

    public void approveRequest() {
        this.status = "Approved";
    }

    public void rejectRequest() {
        this.status = "Rejected";
    }

    @Override
    public String toString() {
        return "Medication: " + medicationName + ", Quantity: " + quantity +
                ", Requester: " + requester + ", Status: " + status;
    }
}
