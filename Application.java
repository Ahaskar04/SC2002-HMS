import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader; // Added for file reading
import java.io.FileWriter;
import java.io.IOException; // Added for file reading
import java.sql.Time; // Added for handling file reading errors
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        // Changes: Initialize users from CSV files
        List<User> staff = loadStaffData("Staff_List.csv"); // Load staff from CSV
        List<Patient> patients = loadPatientData("patient_list.csv"); // Load patients from CSV

        AppointmentManager manager = new AppointmentManager(); // Initialize AppointmentManager
        List<ReplenishmentRequest> requests = new ArrayList<>(); // Initialize Replenishment Requests

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hospital Management System!");

        while (true) { // Keep running the application to allow multiple users to log in and out
            User currentUser = authenticateUser(scanner, staff, patients);

            if (currentUser != null) {
                System.out.println("\nLogin successful! Welcome, " + currentUser.name + ".");
                displayRoleMenu(currentUser, scanner, manager, staff, patients, requests);
            }
        }
    }

    // New Method: Load staff from CSV
    public static List<User> loadStaffData(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String id = details[0].trim();
                String name = details[1].trim();
                String role = details[2].trim();
                
                
                

                switch (role.toLowerCase()) {
                    case "administrator":
                        users.add(new Administrator(id, "password", name));
                        break;
                    case "doctor":
                        // users.add(new Doctor(id, name, "docPass", new ArrayList<>()));
                        Doctor doctor = new Doctor(id, name, "password", new ArrayList<>());
                        users.add(doctor);
                        break;
                    case "pharmacist":
                        users.add(new Pharmacist(id, "password", name));
                        break;
                    default:
                        System.out.println("Unknown role: " + role);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // New Method: Load patients from CSV
    public static List<Patient> loadPatientData(String filePath) {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 6) {
                    System.out.println("Invalid data row, skipping: " + line);
                    continue;
                }

                String id = details[0].trim();
                String name = details[1].trim();
                String dob = details[2].trim();
                String gender = details[3].trim();
                String bloodType = details[4].trim();
                String contactInfo = details[5].trim();

                patients.add(new Patient(id, dob, gender, contactInfo, bloodType, name, "password", new ArrayList<>(),
                        new ArrayList<>()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Updated Method: Now accepts dynamic lists of users
    private static User authenticateUser(Scanner scanner, List<User> staff, List<Patient> patients) {
        User currentUser = null;
    
        while (currentUser == null) {
            System.out.println("Please select your role to log in:");
            System.out.println("1. Administrator");
            System.out.println("2. Doctor");
            System.out.println("3. Patient");
            System.out.println("4. Pharmacist");
            System.out.print("Enter your choice: ");
            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            System.out.print("Enter ID: ");
            String hospitalID = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
    
            String hashedPassword = PasswordUtils.hashPassword(password);
    
            switch (roleChoice) {
                case 1:
                case 2:
                case 4:
                    currentUser = staff.stream()
                            .filter(user -> user.getHospitalID().equals(hospitalID)
                                    && user.getPassword().equals(hashedPassword))
                            .findFirst()
                            .orElse(null);
                    break;
                case 3:
                    currentUser = patients.stream()
                            .filter(patient -> patient.getHospitalID().equals(hospitalID)
                                    && patient.getPassword().equals(hashedPassword))
                            .findFirst()
                            .orElse(null);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
    
            if (currentUser == null) {
                System.out.println("Invalid credentials. Please try again.");
            } else if (currentUser.isFirstLogin()) {
                System.out.println("This is your first login. Please change your password.");
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                currentUser.setPassword(PasswordUtils.hashPassword(newPassword));
                currentUser.setFirstLogin(false);
    
                updateStaffList(staff, "Staff_Pass.csv");
                System.out.println("Password updated successfully. Please login again.");
                return null;  // Restart login for updated credentials
            }
        }
        return currentUser;
    }    

    private static void updateStaffList(List<User> staff, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Name,Role,Password,FirstLogin\n"); // Write header
            for (User user : staff) {
                writer.write(user.hospitalID + "," + user.name + "," + user.getClass().getSimpleName() + ","
                            + PasswordUtils.hashPassword(user.getPassword()) + "," + user.isFirstLogin() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    public static void updatePassword(String hospitalID, String newPassword) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Staff_Pass.csv"));
        StringBuilder sb = new StringBuilder();
        String line;
    
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(hospitalID)) {
                parts[3] = PasswordUtils.hashPassword(newPassword); // Update password to hashed value
                parts[4] = "false"; // Set FirstLogin to false
            }
            sb.append(String.join(",", parts)).append("\n");
        }
        br.close();
    
        BufferedWriter bw = new BufferedWriter(new FileWriter("Staff_Pass.csv"));
        bw.write(sb.toString());
        bw.close();
    }
    
    
    public static boolean login(String hospitalID, String inputPassword) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Staff_Pass.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(hospitalID)) {
                String storedHashedPassword = parts[3]; // Assuming index 3 contains the hashed password
                boolean firstLogin = Boolean.parseBoolean(parts[4]);
    
                if (firstLogin) {
                    System.out.println("First-time login detected. Please change your password.");
                    Scanner scanner = new Scanner(System.in);  // Do not close this Scanner
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    updatePassword(hospitalID, newPassword);
                    System.out.println("Password updated successfully. Please login again.");
                    return false;  // Force user to log in again
                }
    
                return PasswordUtils.hashPassword(inputPassword).equals(storedHashedPassword);
            }
        }
        br.close(); // Close the BufferedReader only
        return false; // If no match found
    }
    
    
   


    private static void displayRoleMenu(User user, Scanner scanner, AppointmentManager manager, List<User> staff,
            List<Patient> patients, List<ReplenishmentRequest> requests) {
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
                logout = handleDoctorMenu(docUser, choice, scanner, manager, patients); // Capture logout status
            } else if (user instanceof Patient) {
                Patient patUser = (Patient) user;
                logout = handlePatientMenu(patUser, choice, null, scanner, manager); // Capture logout status
            } else if (user instanceof Pharmacist) {
                Pharmacist pharmacist = (Pharmacist) user;
                handlePharmacistMenu(pharmacist, choice, scanner, patients, manager, requests);
            } else {
                System.out.println("Invalid role.");
            }

            if (!logout) { // If user hasn't logged out, ask if they want to continue
                System.out.println("\nDo you want to continue? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("no")) {
                    logout = true;
                }
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
                System.out.print("Enter Patient ID to view completed appointments: ");
                String patientId = scanner.nextLine();

                // Find patient by ID
                Patient selectedPatient = patients.stream()
                        .filter(p -> p.getHospitalID().equals(patientId))
                        .findFirst()
                        .orElse(null);

                if (selectedPatient == null) {
                    System.out.println("Invalid Patient ID. Please try again.");
                } else {
                    System.out.println("Completed Appointments for Patient ID: " + patientId);
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
            List<Patient> patients) {
        switch (choice) {
            case 1: // View Patient Medical Records
                System.out.print("Enter Patient ID to view records: ");
                String patientID1 = scanner.nextLine();

                // Find patient from the list
                Patient patient1 = patients.stream()
                        .filter(p -> p.getHospitalID().equals(patientID1))
                        .findFirst()
                        .orElse(null);

                doctor.viewPatientRecords(patient1);
                break;

            case 2: // Update Patient Medical Records
                System.out.print("Enter Patient ID to view records: ");
                String patientID2 = scanner.nextLine();

                // Find patient from the list
                Patient patient2 = patients.stream()
                        .filter(p -> p.getHospitalID().equals(patientID2))
                        .findFirst()
                        .orElse(null);

                System.out.println("Enter Diagnosis");
                String diagnosis = scanner.nextLine();
                System.out.println("Enter Treatment");
                String treatment = scanner.nextLine();
                doctor.updatePatientRecord(patient2, diagnosis, treatment);

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
                System.out.println("Set availability as True(1) or False(0): ");
                int availabilityInput = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                boolean availability = availabilityInput == 1;

                try {
                    System.out.print("Enter available date (dd/MM/yyyy): ");
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
                    System.out.print("Enter available time (HH:mm): ");
                    Time time = Time.valueOf(scanner.nextLine() + ":00");

                    doctor.setAvailability(date, time, availability, manager);
                } catch (ParseException | IllegalArgumentException e) {
                    System.out.println("Invalid date or time format. Please try again.");
                }
                break;

            case 5: // Accept or Decline Appointment Requests
                List<Appointment> pendingAppointments = manager.getPendingAppointments();
                List<Appointment> doctorPending = pendingAppointments.stream()
                        .filter(app -> app.getDoctor().equals(doctor))
                        .toList();

                if (doctorPending.isEmpty()) {
                    System.out.println("No pending appointment requests.");
                } else {
                    for (int i = 0; i < doctorPending.size(); i++) {
                        System.out.println((i + 1) + ". " + doctorPending.get(i));
                    }
                    System.out.print("Select an appointment to process (1-" + doctorPending.size() + "): ");
                    int selection = scanner.nextInt();
                    scanner.nextLine();

                    if (selection >= 1 && selection <= doctorPending.size()) {
                        Appointment selectedAppointment = doctorPending.get(selection - 1);
                        System.out.print("Accept or Decline (accept/decline): ");
                        String decision = scanner.nextLine();
                        if (decision.equalsIgnoreCase("accept")) {
                            doctor.acceptAppointment(manager, selectedAppointment);

                        } else if (decision.equalsIgnoreCase("decline")) {
                            doctor.declineAppointment(manager, selectedAppointment);
                        } else {
                            System.out.println("Invalid input.");
                        }
                    } else {
                        System.out.println("Invalid selection.");
                    }
                }
                break;

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
                System.out.println("Enter Patient ID for the appointment:");
                String patientID3 = scanner.nextLine();

                // Find patient from the list
                Patient patient3 = patients.stream()
                        .filter(p -> p.getHospitalID().equals(patientID3))
                        .findFirst()
                        .orElse(null);

                if (patient3 == null) {
                    System.out.println("Invalid Patient ID.");
                    break;
                }

                // Display confirmed appointments for the selected patient
                List<Appointment> confirmedAppointments = patient3.getAppointmentHistory().stream()
                        .filter(app -> app.getStatus().equalsIgnoreCase("confirmed"))
                        .toList();

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

                // Record the outcome using Doctor's method
                doctor.recordAppointmentOutcome(selectedAppointment, serviceType, med, stat, q, notes);
                break;

            case 8: // Logout
                return true;

            default:
                System.out.println("Invalid option for Doctor.");
        }
        return false; // Continue in the Doctor menu
    }

    private static boolean handlePatientMenu(Patient patient, int choice, Doctor doctor, Scanner scanner,
            AppointmentManager manager) {
        switch (choice) {
            case 1: // View Medical Record
                patient.viewMedicalRecord();
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
        System.out.println("Enter Patient ID to view appointment outcomes:");
        String patientId = scanner.nextLine();

        // Find patient from the list
        Patient selectedPatient = patients.stream()
                .filter(p -> p.getHospitalID().equals(patientId))
                .findFirst()
                .orElse(null);

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
