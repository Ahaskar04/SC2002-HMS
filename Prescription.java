public class Prescription {
    private String medicationName;
    private String status; // e.g., "pending", "dispensed"
    private int quantity;

    // Constructor
    public Prescription(String medicationName, String status, int quantity) {
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
    public String getStatus() {
        return status;
    }

    // Setter for Status
    public void setStatus(String status) {
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
    public void updateStatus(String status) {
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
