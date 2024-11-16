import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Doctor extends User {
    
    private List<Appointment> upcomingAppointments;

    Scanner scanner=new Scanner(System.in);

    // Constructor Method
    public Doctor(String doctorID, String name, String password, List<Appointment> upcomingAppointments) {
        super(doctorID, password, name);
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
       
        patient.viewMedicalRecord();

        System.out.println("Appointment history:");
        // // Display appointment history
        // if (patient.getAppointmentHistory().isEmpty()) {
        //     System.out.println("No appointments found.");
        // } else {
        //     for (Appointment appointment : patient.getAppointmentHistory()) {
        //         System.out.println("Date: " + appointment.getAppointmentDate() +
        //                 ", Time: " + appointment.getAppointmentTime() +
        //                 ", Status: " + appointment.getStatus() +
        //                 ", Doctor: " + appointment.getDoctor().getName());
        //     }
        // }
        patient.aptHistory(patient);

        // // Display medical records
        // System.out.println("\nMedical Records:");
        // if (patient.getMedicalRecords().isEmpty()) {
        //     System.out.println("No medical records found.");
        // } else {
        //     for (MedicalRecord record : patient.getMedicalRecords()) {
        //         System.out.println(record); // Calls MedicalRecord's toString()
        //     }
        // }
    }

    // Method to Update Patient Record
    public void updatePatientRecord() {
        Patient patient = CsvManager.find_patient();// Find patient from the list
        if (patient == null) {
            System.out.println("Invalid patient. Please provide a valid patient.");
            return;
        }
            System.out.println("Enter Diagnosis");
            String diagnosis = scanner.nextLine();
            System.out.println("Enter Treatment");
            String treatment = scanner.nextLine();
        // Add the new medical record to the patient's history
        String currentDate = java.time.LocalDate.now().toString(); // Assuming the record date is today
        patient.addMedicalRecord(diagnosis, treatment, currentDate);

        System.out.println("Patient record updated successfully.");
        System.out.println("Added new record: Diagnosis - " + diagnosis + ", Treatment - " + treatment + ", Date - "
                + currentDate);
    }

    // public void setAvailability( AppointmentManager manager) {
    //     System.out.println("Set availability as True(1) or False(0): ");
    //     int availabilityInput = scanner.nextInt();
    //     scanner.nextLine(); // Consume the leftover newline
    //     boolean isAvailable = availabilityInput == 1;

    //     try {
    //         System.out.print("Enter available date (dd/MM/yyyy): ");
    //         Date date = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());

    //         System.out.print("Enter available time (HH:mm): ");
    //         Time time = Time.valueOf(scanner.nextLine() + ":00");

    //         boolean slotExists = false;
    //         for (Appointment appointment : upcomingAppointments) {
    //             if (appointment.getAppointmentDate().equals(date) &&
    //                 appointment.getAppointmentTime().equals(time)) {
    //                 appointment.updateStatus(isAvailable ? "available" : "unavailable");
    //                 slotExists = true;
    //                 break;
    //             }
    //         }

    //         if (!slotExists && isAvailable) {
    //             Appointment newAppointment = new Appointment(null, this, date, time, "available");
    //             upcomingAppointments.add(newAppointment);
    //             manager.addAppointment(newAppointment);
    //             System.out.println("New appointment slot added.");
    //         } else if (!isAvailable) {
    //             System.out.println("Availability removed for the selected slot.");
    //         }
    //     } catch (ParseException | IllegalArgumentException e) {
    //         System.out.println("Invalid date or time format. Please try again.");
    //     }
    // }
    

    /**
     * @param manager
     * @param appointment
     */
    // public void acceptAppointment(AppointmentManager manager, Appointment appointment) {
    //     if (appointment == null || !appointment.getStatus().equalsIgnoreCase("requested")) {
    //         System.out.println("Invalid or non-requested appointment.");
    //         return;
    //     }

    //     // Update the status to confirmed via AppointmentManager
    //     appointment.updateStatus("confirmed");
    //     System.out.println("Appointment confirmed successfully.");
    // }

    // public void declineAppointment(AppointmentManager manager, Appointment appointment) {
    //     if (appointment == null || !appointment.getStatus().equalsIgnoreCase("requested")) {
    //         System.out.println("Invalid or non-requested appointment.");
    //         return;
    //     }

    //     // Update the status to declined via AppointmentManager
    //     appointment.updateStatus("declined");
    //     System.out.println("Appointment declined successfully.");
    // }

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
        if (!appointment.getStatus().equalsIgnoreCase("confirmed") &&
                !appointment.getStatus().equalsIgnoreCase("completed")) {
            System.out
                    .println("Cannot record outcome for this appointment. Current status: " + appointment.getStatus());
            return;
        }
         // Prompt for service type and notes
         System.out.print("Enter the service type: ");
         String serviceType = scanner.nextLine();

         System.out.print("Enter any additional notes: ");
         String notes = scanner.nextLine();

         System.out.println("Enter medicine name");
         String med = scanner.next();
         String stat = "pending";
         System.out.println("enter quantity of medicine");
         int q = scanner.nextInt();

        // Update appointment details with the outcome
        appointment.setServiceType(serviceType);
        appointment.setNotes(notes);
        appointment.setPrescription(med, stat, q);
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
