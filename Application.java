import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Added for file reading

public class Application {

    public static void main(String[] args) {
        // Changes: Initialize users from CSV files
        List<User> staff = CsvManager.loadStaffData("Staff_List.csv"); // Load staff from CSV
        List<Patient> patients = CsvManager.loadPatientData("patient_list.csv"); // Load patients from CSV

        AppointmentManager manager = new AppointmentManager(); // Initialize AppointmentManager
        PasswordHandler passwordHandler = new PasswordHandler();
        List<ReplenishmentRequest> requests = new ArrayList<>(); // Initialize Replenishment Requests
        MedicalRecordManager recordManager = new MedicalRecordManager();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hospital Management System!");

        while (true) { // Keep running the application to allow multiple users to log in and out
            User currentUser = passwordHandler.authenticateUser(new Scanner(System.in), staff, patients);

            if (currentUser != null) {
                System.out.println("\nLogin successful! Welcome, " + currentUser.name + ".");
                displayRoleMenu(currentUser, scanner, manager, staff, patients, requests, recordManager);
            }
        }
    }

    private static void displayRoleMenu(User user, Scanner scanner, AppointmentManager manager, List<User> staff,
            List<Patient> patients, List<ReplenishmentRequest> requests, MedicalRecordManager recordManager) {
        boolean logout = false; // Track if the user chooses to logout
        while (!logout) {
            user.displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (user instanceof Administrator) {
                Administrator admin = (Administrator) user;
                handleAdminMenu(patients, admin, choice, scanner, manager, requests);
            } else if (user instanceof Doctor) {
                Doctor docUser = (Doctor) user;
                logout = handleDoctorMenu(docUser, choice, scanner, manager, patients, recordManager); // Capture logout status
            } else if (user instanceof Patient) {
                Patient patUser = (Patient) user;
                logout = handlePatientMenu(patUser, choice, null, scanner, manager, recordManager); // Capture logout status
            } else if (user instanceof Pharmacist) {
                Pharmacist pharmacist = (Pharmacist) user;
                handlePharmacistMenu(pharmacist, choice, scanner, patients, manager, requests);
            } else {
                System.out.println("Invalid role.");
            }
        }

        System.out.println("Logging out. Returning to main menu...");
    }

    private static boolean handleAdminMenu(List<Patient> patients, Administrator admin, int choice, Scanner scanner,
            AppointmentManager manager, List<ReplenishmentRequest> requests) {
        switch (choice) {
            case 1: // Manage Staff
                admin.manageStaff(scanner, "Staff_List.csv");
                break;

            case 2: // View Appointments
                // Find patient by ID
                Patient selectedPatient =CsvManager.find_patient();

                if (selectedPatient != null) {
                    System.out.println("Completed Appointments for Patient ID: " + selectedPatient.hospitalID);
                    List<Appointment> completedAppointments = manager.getCompletedAppointments(selectedPatient);

                    if (completedAppointments.isEmpty()) {
                        System.out.println("No completed appointments found for this patient.");
                    } else {
                        for (int i = 0; i < completedAppointments.size(); i++) {
                            System.out.println((i + 1) + ". " + completedAppointments.get(i));
                        }
                    }
                }

                break;

            case 3:// manage inventory
                admin.manageInventory(scanner);
                break;

            case 4: // Approve Replenishment Requests
                admin.approveReplenishmentRequests(requests);
                break;

            case 5: // Logout
                System.out.println("Logging out...");
                return true; // Signal successful logout

            default:
                System.out.println("Invalid option for Administrator.");
        }

        // Return false to indicate no logout occurred
        return false;
    }

    private static boolean handleDoctorMenu(Doctor doctor, int choice, Scanner scanner, AppointmentManager manager,
            List<Patient> patients, MedicalRecordManager recordManager) {
        switch (choice) {
            case 1: // View Patient Medical Records
                // Find patient from the list
                Patient patient1 = CsvManager.find_patient();
                doctor.viewPatientRecords(patient1);
                break;

            case 2: // Update Patient Medical Records
                // Find patient from the list
                Patient patient2 = CsvManager.find_patient();
                doctor.updatePatientRecord(patient2, recordManager);

                break;

            case 3: // View Personal Schedule
                List<Appointment> appointments = manager.getAppointmentsByDoctor(doctor);
                if (appointments.isEmpty()) {
                    System.out.println("No upcoming appointments.");
                } else {
                    for (Appointment appointment : appointments) {
                        System.out.println(appointment);
                    }
                }
                break;

            case 4: // Set Availability for Appointments
                manager.setAvailability(doctor,manager);
                break;

            case 5: // Accept or Decline Appointment Requests
                manager.ApptDec(doctor);

            case 6: // View Upcoming Appointments
                appointments = doctor.getUpcomingAppointments();
                if (appointments.isEmpty()) {
                    System.out.println("No upcoming appointments.");
                } else {
                    for (Appointment appointment : appointments) {
                        System.out.println(appointment);
                    }
                }
                break;

            case 7: // Record Appointment Outcome
                // Find patient from the list
                Patient patient3 = CsvManager.find_patient();
                if (patient3 == null) {
                    System.out.println("Invalid Patient ID.");
                    break;
                }

                List<Appointment> confirmedAppointments = manager.getConfirmedAppointments(patient3);

                if (confirmedAppointments.isEmpty()) {
                    System.out.println("No confirmed appointments found for this patient.");
                    break;
                }

                System.out.println("Confirmed Appointments:");
                for (int i = 0; i < confirmedAppointments.size(); i++) {
                    System.out.println((i + 1) + ". " + confirmedAppointments.get(i));
                }

                System.out.print(
                        "Select an appointment to record the outcome (1-" + confirmedAppointments.size() + "): ");
                int appointmentChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (appointmentChoice < 1 || appointmentChoice > confirmedAppointments.size()) {
                    System.out.println("Invalid appointment selection.");
                    break;
                }

                Appointment selectedAppointment = confirmedAppointments.get(appointmentChoice - 1);

                // Record the outcome using Doctor's method
                doctor.recordAppointmentOutcome(selectedAppointment);
                break;

            case 8: // Logout
                return true;

            default:
                System.out.println("Invalid option for Doctor.");
        }
        return false; // Continue in the Doctor menu
    }

    private static boolean handlePatientMenu(Patient patient, int choice, Doctor doctor, Scanner scanner,
            AppointmentManager manager, MedicalRecordManager recordManager) {
        switch (choice) {
            case 1: // View Medical Record
                patient.viewMedicalRecord(recordManager);
                break;

            case 2: // Update Contact Information
                System.out.println("Enter the new contact information: ");
                String newContactInfo = scanner.nextLine();
                patient.updateContactInfo(newContactInfo);
                break;

            case 3: // View Available Appointments
                patient.viewAvailableAppointments(manager);
                break;

            case 4: // Schedule Appointment
                if (!manager.scheduleAppointment(patient, scanner)) {
                    System.out.println("Failed to schedule an appointment. Please try again.");
                }
                break;

            case 5: // Reschedule Appointment
                if (!manager.rescheduleAppointment(patient, scanner)) {
                    System.out.println("Rescheduling failed. Please try again.");
                }
                break;

            case 6: // Cancel Appointment
                if (!manager.cancelAppointment(patient, scanner)) {
                    System.out.println("Cancellation failed. Please try again.");
                }
                break;

            case 7: // View Scheduled Appointments
                System.out.println("Your confirmed appointments:");
                List<Appointment> confirmedAppointments = manager.getConfirmedAppointments(patient);

                if (confirmedAppointments.isEmpty()) {
                    System.out.println("You have no confirmed appointments.");
                } else {
                    for (int i = 0; i < confirmedAppointments.size(); i++) {
                        System.out.println((i + 1) + ". " + confirmedAppointments.get(i));
                    }
                }
                break;

            case 8: // View Past Appointment Outcome Records
                System.out.println("Your past completed appointments:");
                List<Appointment> completedAppointments = manager.getCompletedAppointments(patient);

                if (completedAppointments.isEmpty()) {
                    System.out.println("You have no past appointment outcome records.");
                } else {
                    for (int i = 0; i < completedAppointments.size(); i++) {
                        System.out.println((i + 1) + ". " + completedAppointments.get(i));
                    }
                }
                break;

            case 9: // Logout
                return true; // Indicate logout
            default:
                System.out.println("Invalid option for Patient.");
        }
        return false; // Continue in Patient menu
    }

    private static void handlePharmacistMenu(Pharmacist pharmacist, int choice, Scanner scanner,
            List<Patient> patients, AppointmentManager manager, List<ReplenishmentRequest> requests) {
        // System.out.println("Enter Patient ID to view appointment outcomes:");
        // String patientId = scanner.nextLine();
                
        // Find patient from the list
        Patient selectedPatient = CsvManager.find_patient();

        if (selectedPatient == null) {
            System.out.println("Invalid Patient ID. Please try again.");
        }

        switch (choice) {
            case 1: // View Appointment Outcome
                selectedPatient.viewCompletedAppointments(manager);
                break;

            case 2: // Update Prescription Status
                pharmacist.handlePrescriptionStatusUpdate(manager, patients, scanner);
                break;

            case 3:// view medical inventory
                MedInvent.displayInventory();
                break;

            case 4: // Submit Replenishment Request
                System.out.print("Enter medication name: ");
                String medName = scanner.nextLine();
                System.out.print("Enter quantity to replenish: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                pharmacist.submitReplenishmentRequest(medName, quantity, requests);
                break;

            case 5:// logout
                System.out.println("Logging out... Goodbye!");
                // Optionally, break out of the current loop or end the session
                break;

            default:
                System.out.println("Invalid option for Pharmacist.");
        }
    }
}