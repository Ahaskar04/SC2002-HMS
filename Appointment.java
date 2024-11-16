import java.sql.Time;
import java.util.Date;
import java.util.Scanner;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status; // e.g., "confirmed", "cancelled", "completed"
    private String serviceType; // e.g., "Consultation", "Follow-up"
    private Prescription pres;
    private String notes; // Notes for the appointment outcome

    // Constructor
    public Appointment(Patient patient, Doctor doctor, Date appointmentDate, Time appointmentTime, String status) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.serviceType = ""; // Default to empty
        this.pres = new Prescription(); //Default to
        this.notes = ""; // Default to empty
    }

    Scanner sc=new Scanner(System.in);
    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Prescription getPrescription(){
        return pres;
    }

    public void setPrescription(String med, String Status, int q){
        pres.setMedicationName(med);
        pres.setStatus(Status);
        pres.setQuantity(q);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Method to update the status
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Appointment status updated to: " + newStatus);
    }

    @Override
    public String toString() {
        return "Appointment [Date: " + appointmentDate +
                ", Time: " + appointmentTime +
                ", Status: " + status +
                ", Service Type: " + serviceType +
                ", Notes: " + notes +
                ", Prescription: " + pres.toString() +
                ", Patient: " + (patient != null ? patient.getName() : "N/A") +
                ", Doctor: " + (doctor != null ? doctor.getName() : "N/A")+ "]";
    }

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
}
