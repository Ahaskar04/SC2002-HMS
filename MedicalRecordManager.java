import java.util.List;

public class MedicalRecordManager {

    // Method to view a patient's medical record
    public void viewMedicalRecord(Patient patient) {
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Contact Information: " + patient.getContactInfo());
        System.out.println("Blood Type: " + patient.getBloodType());

        List<MedicalRecord> records = patient.getMedicalRecords();
        System.out.println("\nMedical History:");
        if (records.isEmpty()) {
            System.out.println("No past medical records found.");
        } else {
            for (MedicalRecord record : records) {
                System.out.println(record); // Assuming MedicalRecord has a proper toString() method
            }
        }
    }

    // Method to add a medical record to a patient's history
    public void addMedicalRecord(Patient patient, String diagnosis, String treatment, String date) {
        MedicalRecord record = new MedicalRecord(diagnosis, treatment, date);
        
        List<MedicalRecord> updatedRecords = patient.getMedicalRecords();
        updatedRecords.add(record);
        
        // Explicitly set the updated list back to the patient
        patient.setMedicalRecords(updatedRecords);

        System.out.println("Medical record added successfully.");
    }

}