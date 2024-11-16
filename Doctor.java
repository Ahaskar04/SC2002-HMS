import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            if (appointment.getStatus() == Appointment.AppointmentStatus.AVAILABLE) {
                availableAppointments.add(appointment);
            }
        }
        return availableAppointments;
    }

    // Method to view Patient Records
    public void viewPatientRecords(Patient patient) {
        MedicalRecordManager recordManager = new MedicalRecordManager();
        patient.viewMedicalRecord(recordManager);
        System.out.println("Appointment history:");
        patient.aptHistory(patient);
    }

    // Method to Update Patient Record
    public void updatePatientRecord(Patient patient, MedicalRecordManager manager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Diagnosis");
        String diagnosis = scanner.nextLine();
        System.out.println("Enter Treatment");
        String treatment = scanner.nextLine();

        // Add the new medical record to the patient's history
        String currentDate = java.time.LocalDate.now().toString(); // Assuming the record date is today
        manager.addMedicalRecord(patient, diagnosis, treatment, currentDate);

        System.out.println("Patient record updated successfully.");
        System.out.println("Added new record: Diagnosis - " + diagnosis + ", Treatment - " + treatment + ", Date - "
                + currentDate);
    }

    /*public void setAvailability(Date date, Time time, boolean isAvailable, AppointmentManager manager) {
        boolean slotExists = false;
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getAppointmentDate().equals(date) && appointment.getAppointmentTime().equals(time)) {
                appointment.updateStatus(isAvailable ? Appointment.AppointmentStatus.AVAILABLE : Appointment.AppointmentStatus.UNAVAILABLE);
                slotExists = true;
                break;
            }
        }

        if (!slotExists && isAvailable) {
            Appointment newAppointment = new Appointment(null, this, date, time, Appointment.AppointmentStatus.AVAILABLE);
            upcomingAppointments.add(newAppointment);
            manager.addAppointment(newAppointment);
        }
    } */

    /**
     * @param manager
     * @param appointment
     */
    public void acceptAppointment(AppointmentManager manager, Appointment appointment) {
        if (appointment == null || !(appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED)) {
            System.out.println("Invalid or non-requested appointment.");
            return;
        }

        // Update the status to confirmed via AppointmentManager
        appointment.updateStatus(Appointment.AppointmentStatus.CONFIRMED);
        System.out.println("Appointment confirmed successfully.");
    }

    public void declineAppointment(AppointmentManager manager, Appointment appointment) {
        if (appointment == null || !(appointment.getStatus() == Appointment.AppointmentStatus.REQUESTED)) {
            System.out.println("Invalid or non-requested appointment.");
            return;
        }

        // Update the status to declined via AppointmentManager
        appointment.updateStatus(Appointment.AppointmentStatus.DECLINED);
        System.out.println("Appointment declined successfully.");
    }

    public void recordAppointmentOutcome(Appointment appointment) {
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
        if (!(appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED) &&
                !(appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED)) {
            System.out
                    .println("Cannot record outcome for this appointment. Current status: " + appointment.getStatus());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        // Prompt for service type and notes
         System.out.print("Enter the service type: ");
         String serviceType = scanner.nextLine();

         System.out.print("Enter any additional notes: ");
         String notes = scanner.nextLine();

         System.out.println("Enter medicine name");
         String med = scanner.next();
         Prescription.PrescriptionStatus stat = Prescription.PrescriptionStatus.PENDING;
         System.out.println("enter quantity of medicine");
         int q = scanner.nextInt();

        // Update appointment details with the outcome
        appointment.setServiceType(serviceType);
        appointment.setNotes(notes);
        appointment.setPrescription(med, stat, q);
        appointment.updateStatus(Appointment.AppointmentStatus.COMPLETED); // Mark the appointment as completed

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
