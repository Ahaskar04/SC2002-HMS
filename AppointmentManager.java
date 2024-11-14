import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AppointmentManager {
    private List<Appointment> appointments; // List to store all appointments

    // Constructor to initialize the appointment list
    public AppointmentManager() {
        this.appointments = new ArrayList<>();
    }

   public void addAppointment(Appointment appointment) {
        if (appointment.getDoctor() == null) {
            System.out.println("Error: Appointment is missing a doctor reference.");
        } else {
            appointments.add(appointment);
            System.out.println("Appointment added successfully.");
        }
    }

    // Method to remove an appointment from the list
    public void removeAppointment(Appointment appointment) {
        if (appointments.remove(appointment)) {
            System.out.println("Appointment removed successfully.");
        } else {
            System.out.println("Appointment not found.");
        }
    }

    // Method to find an appointment by date and time
    public Appointment findAppointment(Date appointmentDate, Time appointmentTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().equals(appointmentDate) &&
                    appointment.getAppointmentTime().equals(appointmentTime)) {
                return appointment; // Appointment found
            }
        }
        return null; // No matching appointment found
    }

    // Method to list all appointments
    public void listAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    //Method to retrieve all available appointments
    public List<Appointment> getAvailableAppointments() {
        List<Appointment> availableAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equalsIgnoreCase("available")) {
                availableAppointments.add(appointment);
            }
        }
        return availableAppointments;
    } 

    public boolean scheduleAppointment(Patient patient, Scanner scanner) {
        List<Appointment> availableAppointments = getAvailableAppointments();

        if (availableAppointments.isEmpty()) {
            System.out.println("No available appointments to schedule.");
            return false;
        }

        System.out.println("Available Appointment Slots:");
        for (int i = 0; i < availableAppointments.size(); i++) {
            Appointment appointment = availableAppointments.get(i);
            String doctorName = (appointment.getDoctor() != null) ? appointment.getDoctor().getName() : "Unknown";
            System.out.println((i + 1) + ". Date: " + appointment.getAppointmentDate() + 
                            ", Time: " + appointment.getAppointmentTime() + 
                            ", Doctor: " + doctorName);
        }

        System.out.print("Enter the number of the appointment slot you'd like to book (1-" + availableAppointments.size() + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice < 1 || choice > availableAppointments.size()) {
            System.out.println("Invalid selection. Please try again.");
            return false;
        }

        Appointment selectedAppointment = availableAppointments.get(choice - 1);
        selectedAppointment.setPatient(patient);  // Link the patient to the appointment
        selectedAppointment.updateStatus("requested");  // Mark as requested

        // Add the appointment to the patient's history
        patient.addAppointmentToHistory(selectedAppointment);

        System.out.println("Appointment requested successfully: " + selectedAppointment);
        return true;
    }


    public boolean rescheduleAppointment(Patient patient, Scanner scanner) {
        // Get the patient's current appointments
        List<Appointment> patientAppointments = getAppointmentsByPatient(patient);

        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments to reschedule.");
            return false;
        }

        // Display current appointments
        System.out.println("Your current appointments:");
        for (int i = 0; i < patientAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + patientAppointments.get(i));
        }

        System.out.print("Select the appointment to reschedule: ");
        int oldChoice = scanner.nextInt();
        scanner.nextLine();

        if (oldChoice < 1 || oldChoice > patientAppointments.size()) {
            System.out.println("Invalid selection.");
            return false;
        }

        Appointment oldAppointment = patientAppointments.get(oldChoice - 1);

        // Display available slots
        List<Appointment> availableSlots = getAvailableAppointments();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for rescheduling.");
            return false;
        }

        System.out.println("Available slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }

        System.out.print("Select a new slot: ");
        int newChoice = scanner.nextInt();
        scanner.nextLine();

        if (newChoice < 1 || newChoice > availableSlots.size()) {
            System.out.println("Invalid selection.");
            return false;
        }

        Appointment newAppointment = availableSlots.get(newChoice - 1);

        // Perform the rescheduling
        if (!newAppointment.getStatus().equalsIgnoreCase("available")) {
            System.out.println("Selected slot is not available.");
            return false;
        }

        // Update the old appointment
        oldAppointment.updateStatus("available");
        oldAppointment.setPatient(null);

        // Assign the new appointment
        newAppointment.updateStatus("requested");
        newAppointment.setPatient(patient);
        patient.addAppointmentToHistory(newAppointment);

        System.out.println("Appointment rescheduled successfully.");
        return true;
    }

    public boolean cancelAppointment(Patient patient, Scanner scanner) {
        // Get the patient's current appointments
        List<Appointment> patientAppointments = getAppointmentsByPatient(patient);

        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments to cancel.");
            return false;
        }

        // Display current appointments
        System.out.println("Your current appointments:");
        for (int i = 0; i < patientAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + patientAppointments.get(i));
        }

        System.out.print("Select the appointment to cancel: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice < 1 || choice > patientAppointments.size()) {
            System.out.println("Invalid selection.");
            return false;
        }

        Appointment appointmentToCancel = patientAppointments.get(choice - 1);

        // Update the appointment status
        appointmentToCancel.updateStatus("available");
        appointmentToCancel.setPatient(null); // Unlink the patient

        // Remove from patient's history
        patient.getAppointmentHistory().remove(appointmentToCancel);

        System.out.println("Appointment cancelled successfully: " + appointmentToCancel);
        return true;
    }

    public List<Appointment> getConfirmedAppointments(Patient patient) {
        List<Appointment> confirmedAppointments = new ArrayList<>();
        
        for (Appointment appointment : getAppointmentsByPatient(patient)) {
            if (appointment.getStatus().equalsIgnoreCase("confirmed")) {
                confirmedAppointments.add(appointment);
            }
        }
        
        return confirmedAppointments;
    }

    public List<Appointment> getCompletedAppointments(Patient patient) {
        List<Appointment> completedAppointments = new ArrayList<>();
        
        for (Appointment appointment : getAppointmentsByPatient(patient)) {
            if (appointment.getStatus().equalsIgnoreCase("completed")) {
                completedAppointments.add(appointment);
            }
        }
        
        return completedAppointments;
    }

    // Method to retrieve all pending appointments (e.g., for approval by doctors)
    public List<Appointment> getPendingAppointments() {
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equalsIgnoreCase("requested")) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;
    }

    // Method to retrieve appointments for a specific doctor
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctor)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    // Method to retrieve appointments for a specific patient
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patient)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }
}
