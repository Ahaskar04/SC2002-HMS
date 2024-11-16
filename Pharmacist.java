//import Prescription.PrescriptionStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Pharmacist extends User {
    private List<Prescription> prescriptions;
    // private Map<String, Integer> medicationInventory;

    // Constructor
    public Pharmacist(String hospitalID, String password, String name) {
        super(hospitalID, password, name);
        this.prescriptions = new ArrayList<>();
        // this.medicationInventory = new HashMap<>();
    }

    // Method to view completed appointments for a specific patient
    public void viewCompletedAppointmentsForPatient(AppointmentManager manager, Patient patient) {
        // Get completed appointments for the patient
        List<Appointment> completedAppointments = manager.getCompletedAppointments(patient);

        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments found for this patient.");
        } else {
            System.out.println("Completed Appointments and Prescription Details:");
            for (int i = 0; i < completedAppointments.size(); i++) {
                Appointment appointment = completedAppointments.get(i);
                System.out.println((i + 1) + ". " + appointment);

                Prescription prescription = appointment.getPrescription();
                if (prescription != null) {
                    System.out.println("   Medication Name: " + prescription.getMedicationName());
                    System.out.println("   Quantity: " + prescription.getQuantity());
                    System.out.println("   Status: " + prescription.getStatus());
                } else {
                    System.out.println("   No prescription available for this appointment.");
                }
            }
        }
    }

    // Method to update prescription status
    public void handlePrescriptionStatusUpdate(AppointmentManager manager, List<Patient> patients, Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId2 = scanner.nextLine();

        // Find patient from the list
        Patient patient2 = patients.stream()
                .filter(p -> p.getHospitalID().equals(patientId2))
                .findFirst()
                .orElse(null);

        if (patient2 == null) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        // Get the list of completed appointments for the patient
        List<Appointment> completedAppointments2 = manager.getCompletedAppointments(patient2);

        if (completedAppointments2.isEmpty()) {
            System.out.println("No completed appointments found for this patient.");
            return;
        }

        // Display completed appointments to the pharmacist
        System.out.println("Completed Appointments:");
        for (int i = 0; i < completedAppointments2.size(); i++) {
            System.out.println((i + 1) + ". " + completedAppointments2.get(i));
        }

        // Select the appointment to update
        System.out.print(
                "Select an appointment to update the prescription (1-" + completedAppointments2.size() + "): ");
        int appointmentChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (appointmentChoice < 1 || appointmentChoice > completedAppointments2.size()) {
            System.out.println("Invalid appointment selection.");
            return;
        }

        Appointment selectedAppointment = completedAppointments2.get(appointmentChoice - 1);
        Prescription prescription = selectedAppointment.getPrescription();

        if (prescription == null) {
            System.out.println("No prescription available for this appointment.");
            return;
        }

        //updating prescription status
        System.out.print("Enter new status (0: Dispensed, 1: Not Dispensed): ");
        int choice = scanner.nextInt();
        Prescription.PrescriptionStatus status;

        if (choice == 0) {
            status = Prescription.PrescriptionStatus.DISPENSED;
        } else if (choice == 1) {
            status = Prescription.PrescriptionStatus.PENDING;
        } else {
            System.out.println("Invalid choice");
            return; // Exit method if the choice is invalid
        }

        // Update inventory only if status is DISPENSED
        if (prescription.getStatus() == Prescription.PrescriptionStatus.DISPENSED) {
            MedInvent.updateInventory(prescription.getMedicationName(), prescription.getQuantity());
        }

        updatePrescriptionStatus(prescription, status);
        System.out.println("Prescription updated successfully:");
        System.out.println(prescription);

    }

    // Existing updatePrescriptionStatus method
    public void updatePrescriptionStatus(Prescription prescription, Prescription.PrescriptionStatus status) {
        if (prescription == null) {
            System.out.println("Invalid prescription. Please provide a valid prescription.");
            return;
        }

        prescription.updateStatus(status);
        System.out.println("Prescription for " + prescription.getMedicationName() +
                " has been updated to status: " + status);
    }

    // Method to submit replenishment request
    public void submitReplenishmentRequest(String medicationName, int quantity, List<ReplenishmentRequest> requests) {
        if (medicationName == null || !MedInvent.isMedicineAvailable(medicationName.toLowerCase()) || quantity <= 0) {
            System.out.println("Invalid medication name or quantity. Please provide valid details.");
            return;
        }

        ReplenishmentRequest request = new ReplenishmentRequest(medicationName.toLowerCase(), quantity, this.name);
        requests.add(request);

        System.out.println("Replenishment request submitted for " + quantity + " units of " + medicationName + ".");
    }

    @Override
    public String toString() {
        return "Pharmacist Name: " + name + ", ID: " + hospitalID;
    }

    // Abstract method from User class
    @Override
    public void displayMenu() {
        System.out.println("1. View Appointment Outcome");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View medical inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
    }
}
