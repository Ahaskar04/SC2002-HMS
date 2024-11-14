import java.util.List;

public class Patient extends User {
    private String dateOfBirth;
    private String gender;
    private String contactInfo;
    private String bloodType;
    private List<Appointment> appointmentHistory;
    private List<MedicalRecord> medicalRecords;

    // Constructor
    public Patient(String patientID, String dateOfBirth, String gender, String contactInfo, String bloodType,
            String name, String password, List<Appointment> appointmentHistory, List<MedicalRecord> medicalRecords) {
        super(patientID, password, name); // Calls the User constructor
        // this.patientID = patientID; // Correctly set the patientID
        // this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.medicalRecords = medicalRecords; // Use the provided list, avoid redundant ArrayList creation
        this.appointmentHistory = appointmentHistory;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<Appointment> getAppointmentHistory() {
        return appointmentHistory;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    // Method to add a medical record
    public void addMedicalRecord(String diagnosis, String treatment, String date) {
        MedicalRecord record = new MedicalRecord(diagnosis, treatment, date);
        this.medicalRecords.add(record);
    }

    // To view Medical Records
    public void viewMedicalRecord() {
        System.out.println("Patient ID: " + hospitalID);
        System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Gender: " + gender);
        System.out.println("Contact Information: " + contactInfo);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("\nMedical History:");
        if (medicalRecords.isEmpty()) {
            System.out.println("No past medical records found.");
        } else {
            for (MedicalRecord record : medicalRecords) {
                System.out.println(record); // Calls the toString() method of MedicalRecord
            }
        }
    }

    // Update Contact Information
    public void updateContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void addAppointmentToHistory(Appointment appointment) {
        appointmentHistory.add(appointment);
    }

    public void viewAvailableAppointments(AppointmentManager manager) {
        List<Appointment> availableAppointments = manager.getAvailableAppointments();
        System.out.println("Available Appointment Slots:");

        if (availableAppointments.isEmpty()) {
            System.out.println("No available appointments.");
        } else {
            for (int i = 0; i < availableAppointments.size(); i++) {
                Appointment appointment = availableAppointments.get(i);
                String doctorName = (appointment.getDoctor() != null) ? appointment.getDoctor().getName() : "Unknown";
                System.out.println((i + 1) + ". Date: " + appointment.getAppointmentDate() +
                        ", Time: " + appointment.getAppointmentTime() +
                        ", Doctor: " + doctorName);
            }
        }
    }

    /*
     * public void scheduleAppointment(Scanner scanner, AppointmentManager manager)
     * {
     * List<Appointment> availableAppointments = manager.getAvailableAppointments();
     * 
     * if (availableAppointments.isEmpty()) {
     * System.out.println("No available appointments to schedule.");
     * return;
     * }
     * 
     * System.out.println("Available Appointment Slots:");
     * for (int i = 0; i < availableAppointments.size(); i++) {
     * Appointment appointment = availableAppointments.get(i);
     * String doctorName = (appointment.getDoctor() != null) ?
     * appointment.getDoctor().getName() : "Unknown";
     * System.out.println((i + 1) + ". Date: " + appointment.getAppointmentDate() +
     * ", Time: " + appointment.getAppointmentTime() +
     * ", Doctor: " + doctorName);
     * }
     * 
     * System.out.
     * print("Enter the number of the appointment slot you'd like to book (1-" +
     * availableAppointments.size() + "): ");
     * 
     * int choice = scanner.nextInt();
     * scanner.nextLine(); // Consume newline
     * 
     * if (choice < 1 || choice > availableAppointments.size()) {
     * System.out.println("Invalid selection. Please try again.");
     * return;
     * }
     * 
     * Appointment selectedAppointment = availableAppointments.get(choice - 1);
     * selectedAppointment.setPatient(this); // Link the patient to the appointment
     * selectedAppointment.updateStatus("requested"); // Mark as requested
     * 
     * // Add the appointment to the patient's history
     * appointmentHistory.add(selectedAppointment);
     * 
     * System.out.println("Appointment requested successfully: " +
     * selectedAppointment);
     * }
     */

    /*
     * public void rescheduleAppointment(Appointment appointment, Date newDate, Time
     * newTime) {
     * // Get the doctor associated with the current appointment
     * Doctor doctor = appointment.getDoctor();
     * 
     * // Check if the new date and time are available for the same doctor
     * List<Appointment> doctorAppointments = doctor.getAvailableAppointments();
     * boolean isRescheduled = false;
     * 
     * for (Appointment availableAppointment : doctorAppointments) {
     * if (availableAppointment.getAppointmentDate().equals(newDate) &&
     * availableAppointment.getAppointmentTime().equals(newTime) &&
     * availableAppointment.getStatus().equalsIgnoreCase("available")) {
     * 
     * // Cancel the current appointment by updating its status
     * appointment.updateStatus("available");
     * 
     * // Update the new appointment status to confirmed
     * availableAppointment.updateStatus("requested");
     * 
     * // Add the new appointment to the appointment history
     * appointmentHistory.add(availableAppointment);
     * 
     * System.out.println(
     * "Appointment rescheduled to " + newDate + " at " + newTime + " with Dr. " +
     * doctor.getName());
     * isRescheduled = true;
     * break;
     * }
     * }
     * 
     * if (!isRescheduled) {
     * System.out.println("The selected slot on " + newDate + " at " + newTime
     * + " is unavailable. Please choose a different time.");
     * }
     * }
     */

    /*
     * public void cancelAppointment(Appointment appointment) {
     * if (appointment == null) {
     * System.out.
     * println("Invalid appointment. Please provide a valid appointment to cancel."
     * );
     * return;
     * }
     * 
     * // Check if the appointment exists in the patient's history
     * if (!appointmentHistory.contains(appointment)) {
     * System.out.println("Appointment not found in your history.");
     * return;
     * }
     * 
     * // Update the appointment status to cancelled
     * appointment.updateStatus("cancelled");
     * 
     * // Remove the appointment from the history
     * appointmentHistory.remove(appointment);
     * 
     * System.out.println(
     * "Appointment on " + appointment.getAppointmentDate() + " at " +
     * appointment.getAppointmentTime() +
     * " with Dr. " + appointment.getDoctor().getName() + " has been cancelled.");
     * }
     */

    public void viewScheduledAppointments() {
        System.out.println("Scheduled Appointments:");

        boolean hasScheduledAppointments = false;

        for (Appointment appointment : appointmentHistory) {
            if (appointment.getStatus().equalsIgnoreCase("confirmed")) {
                System.out.println("Date: " + appointment.getAppointmentDate() +
                        ", Time: " + appointment.getAppointmentTime() +
                        ", Doctor: " + appointment.getDoctor().getName());
                hasScheduledAppointments = true;
            }
        }

        if (!hasScheduledAppointments) {
            System.out.println("No scheduled appointments found.");
        }
    }

    public void viewCompletedAppointments(AppointmentManager manager) {
        // Get completed appointments for this patient
        List<Appointment> completedAppointments = manager.getCompletedAppointments(this);

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

    // Abstract User Method
    @Override
    public void displayMenu() {
        System.out.println("Patient Menu:");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Contact Information");
        System.out.println("3. View Available Appointments");
        System.out.println("4. Schedule Appointment");
        System.out.println("5. Reschedule Appointment");
        System.out.println("6. Cancel Appointment");
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Exit");
    }

}