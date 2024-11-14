import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends User {
    // private String doctorID;
    // private String name;
    // private String password;
    private List<Appointment> upcomingAppointments;

    // Constructor Method
    public Doctor(String doctorID, String name, String password, List<Appointment> upcomingAppointments) {
        super(doctorID, password, name);
        // this.doctorID = doctorID;
        // this.name = name;
        this.upcomingAppointments = new ArrayList<>(upcomingAppointments);
    }

    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    // Method to get available appointments
    public List<Appointment> getAvailableAppointments() {
        List<Appointment> availableAppointments = new ArrayList<>();
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getStatus().equalsIgnoreCase("available")) {
                availableAppointments.add(appointment);
            }
        }
        return availableAppointments;
    }

    // Method to view Patient Records
    public void viewPatientRecords(Patient patient) {
        if (patient == null) {
            System.out.println("Invalid patient. Please provide a valid patient.");
            return;
        }

        // Display patient personal information
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Contact Info: " + patient.getContactInfo());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("\nAppointment History:");

        // Display appointment history
        if (patient.getAppointmentHistory().isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : patient.getAppointmentHistory()) {
                System.out.println("Date: " + appointment.getAppointmentDate() +
                        ", Time: " + appointment.getAppointmentTime() +
                        ", Status: " + appointment.getStatus() +
                        ", Doctor: " + appointment.getDoctor().getName());
            }
        }

        // Display medical records
        System.out.println("\nMedical Records:");
        if (patient.getMedicalRecords().isEmpty()) {
            System.out.println("No medical records found.");
        } else {
            for (MedicalRecord record : patient.getMedicalRecords()) {
                System.out.println(record); // Calls MedicalRecord's toString()
            }
        }
    }

    // Method to Update Patient Record
    public void updatePatientRecord(Patient patient, String diagnosis, String treatment) {
        if (patient == null) {
            System.out.println("Invalid patient. Please provide a valid patient.");
            return;
        }

        // Add the new medical record to the patient's history
        String currentDate = java.time.LocalDate.now().toString(); // Assuming the record date is today
        patient.addMedicalRecord(diagnosis, treatment, currentDate);

        System.out.println("Patient record updated successfully.");
        System.out.println("Added new record: Diagnosis - " + diagnosis + ", Treatment - " + treatment + ", Date - "
                + currentDate);
    }

    public void setAvailability(Date date, Time time, boolean isAvailable, AppointmentManager manager) {
        boolean slotExists = false;
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getAppointmentDate().equals(date) && appointment.getAppointmentTime().equals(time)) {
                appointment.updateStatus(isAvailable ? "available" : "unavailable");
                slotExists = true;
                break;
            }
        }

        if (!slotExists && isAvailable) {
            Appointment newAppointment = new Appointment(null, this, date, time, "available");
            upcomingAppointments.add(newAppointment);
            manager.addAppointment(newAppointment);
        }
    }

    /**
     * @param manager
     * @param appointment
     */
    public void acceptAppointment(AppointmentManager manager, Appointment appointment) {
        if (appointment == null || !appointment.getStatus().equalsIgnoreCase("requested")) {
            System.out.println("Invalid or non-requested appointment.");
            return;
        }

        // Update the status to confirmed via AppointmentManager
        appointment.updateStatus("confirmed");
        System.out.println("Appointment confirmed successfully.");
    }

    public void declineAppointment(AppointmentManager manager, Appointment appointment) {
        if (appointment == null || !appointment.getStatus().equalsIgnoreCase("requested")) {
            System.out.println("Invalid or non-requested appointment.");
            return;
        }

        // Update the status to declined via AppointmentManager
        appointment.updateStatus("declined");
        System.out.println("Appointment declined successfully.");
    }

    public void recordAppointmentOutcome(Appointment appointment, String serviceType, String Medicine, String Status,
            int Quantity, String notes) {
        if (appointment == null) {
            System.out.println("Invalid appointment. Please provide a valid appointment.");
            return;
        }

        // Check if the appointment exists in the doctor's upcoming appointments
        if (!upcomingAppointments.contains(appointment)) {
            System.out.println("Appointment not found in your upcoming appointments.");
            return;
        }

        // Ensure the appointment is in a status that allows recording an outcome
        if (!appointment.getStatus().equalsIgnoreCase("confirmed") &&
                !appointment.getStatus().equalsIgnoreCase("completed")) {
            System.out
                    .println("Cannot record outcome for this appointment. Current status: " + appointment.getStatus());
            return;
        }

        // Update appointment details with the outcome
        appointment.setServiceType(serviceType);
        appointment.setNotes(notes);
        appointment.setPrescription(Medicine, Status, Quantity);
        appointment.updateStatus("completed"); // Mark the appointment as completed

        System.out.println("Appointment outcome recorded successfully for the appointment on " +
                appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime() +
                ". Service Type: " + serviceType + ", Notes: " + notes);
    }

    @Override
    public String toString() {
        return "Doctor ID: " + this.getHospitalID() + ", Name: " + name;
    }

    @Override
    public void displayMenu() {
        System.out.println("\nDoctor Menu:");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
    }

}
