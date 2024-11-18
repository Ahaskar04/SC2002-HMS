import java.util.List;
import java.util.Objects;

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

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    /* Method to add a medical record
    public void addMedicalRecord(String diagnosis, String treatment, String date) {
        MedicalRecord record = new MedicalRecord(diagnosis, treatment, date);
        this.medicalRecords.add(record);
    } */

    //To view Medical Records
    public void viewMedicalRecord(MedicalRecordManager manager) {
        manager.viewMedicalRecord(this);
    }

    // Update Contact Information
    public void updateContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void addAppointmentToHistory(Appointment appointment) {
        if (!appointmentHistory.contains(appointment)) {
            appointmentHistory.add(appointment);
    }
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

    public void viewScheduledAppointments() {
        System.out.println("Scheduled Appointments:");

        boolean hasScheduledAppointments = false;

        for (Appointment appointment : appointmentHistory) {
            if (appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED) {
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

    public void aptHistory(Patient patient, AppointmentManager manager) {
        // Display appointment history from AppointmentManager
        List<Appointment> patientAppointments = manager.getAppointmentsByPatient(patient);

        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : patientAppointments) {
                System.out.println("Date: " + appointment.getAppointmentDate() +
                        ", Time: " + appointment.getAppointmentTime() +
                        ", Status: " + appointment.getStatus() +
                        ", Doctor: " + appointment.getDoctor().getName());
            }
        }
    }


    /* 
    this is added to check if the patient object we create elesehwere when finding with patient in csv manager is same or not

    To fix this issue, you should override the equals (and hashCode) method in the Patient class so that two Patient objects are considered equal if their hospitalID matches.
    */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient patient = (Patient) obj;
        return hospitalID.equals(patient.hospitalID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hospitalID);
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