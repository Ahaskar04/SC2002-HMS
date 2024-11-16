public class Prescription {
    public enum PrescriptionStatus {
        PENDING, // Status before the prescription is filled
        DISPENSED; // Status after the prescription is filled
    }

    private String medicationName;
    private PrescriptionStatus status; // e.g., "pending", "dispensed"
    private int quantity;

    // Constructor
    public Prescription(String medicationName, PrescriptionStatus status, int quantity) {
        this.medicationName = medicationName;
        this.status = status;
        this.quantity = quantity;
    }
    public Prescription(){}//default constructor
    
    // Getter for Medication Name
    public String getMedicationName() {
        return medicationName;
    }

    // Setter for Medication Name
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    // Getter for Status
    public PrescriptionStatus getStatus() {
        return status;
    }

    // Setter for Status
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    // Getter for Quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for Quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to update the status
    public void updateStatus(PrescriptionStatus status) {
        this.status = status;
    }

    /*public Prescription getPrescription(){
        return this;
    }

    public void setPrescription(String med, String Status, int q){
        this.medicationName = med;
        this.status = status;
        this.quantity = q;
    }*/

    @Override
    public String toString() {
        return "Prescription [Medication: " + medicationName + ", Status: " + status + ", Quantity: " + quantity + "]";
    }
}
