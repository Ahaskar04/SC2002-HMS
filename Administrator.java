import java.io.*;
import java.util.*;

public class Administrator extends User {
    private List<Staff> staffList;
    private List<Appointment> appointments;
    private Map<String, Integer> medicationInventory;

    // Constructor
    public Administrator(String hospitalID, String password, String name) {
        super(hospitalID, password, name);
        this.staffList = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.medicationInventory = new HashMap<>();
    }

    // Load staff from CSV file
    public static List<Staff> loadStaffFromCSV(String filePath) {
        List<Staff> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    staffList.add(new Staff(details[0].trim(), details[1].trim(), details[2].trim(),
                            details[3].trim(), Integer.parseInt(details[4].trim())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    // get function

    // Save staff list to CSV file
    public static void saveStaffToCSV(List<Staff> staffList, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Staff ID,Name,Role,Gender,Age");
            bw.newLine();
            for (Staff staff : staffList) {
                bw.write(staff.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to view all appointments
    public void viewAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("List of Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    // Manage staff
    public void manageStaff(Scanner scanner, String filePath) {
        List<Staff> staffList = loadStaffFromCSV(filePath);
        System.out.println("Hospital Staff:");
        staffList.forEach(System.out::println);

        System.out.println("\nSelect an action:");
        System.out.println("1. Add Staff");
        System.out.println("2. Update Staff");
        System.out.println("3. Remove Staff");
        System.out.println("4. Sort Staff");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                // Add Staff
                System.out.println("Enter Staff ID:");
                String id = scanner.nextLine();
                System.out.println("Enter Staff Name:");
                String name = scanner.nextLine();
                System.out.println("Enter Role:");
                String role = scanner.nextLine();
                System.out.println("Enter Gender:");
                String gender = scanner.nextLine();
                System.out.println("Enter Age:");
                int age = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Staff newStaff = new Staff(id.trim(), name.trim(), role.trim(), gender.trim(), age);
                staffList.add(newStaff);
                System.out.println("Staff added successfully.");
                break;

            case 2:
                // Update Staff
                System.out.print("Enter Staff ID to update: ");
                String updateID = scanner.nextLine();
                Optional<Staff> staffToUpdate = staffList.stream()
                        .filter(staff -> staff.getStaffID().equals(updateID))
                        .findFirst();
                if (staffToUpdate.isPresent()) {
                    Staff existingStaff = staffToUpdate.get();
                    System.out.println("Existing Details: " + existingStaff);

                    System.out.println("Enter New Name:");
                    String newName = scanner.nextLine();
                    System.out.println("Enter New Role:");
                    String newRole = scanner.nextLine();
                    System.out.println("Enter New Gender:");
                    String newGender = scanner.nextLine();
                    System.out.println("Enter New Age:");
                    int newAge = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    existingStaff.setName(newName.trim());
                    existingStaff.setRole(newRole.trim());
                    existingStaff.setGender(newGender.trim());
                    existingStaff.setAge(newAge);
                    System.out.println("Staff updated successfully.");
                } else {
                    System.out.println("Staff ID not found.");
                }
                break;

            case 3:
                // Remove Staff
                System.out.print("Enter Staff ID to remove: ");
                String removeID = scanner.nextLine();
                if (staffList.removeIf(staff -> staff.getStaffID().equals(removeID))) {
                    System.out.println("Staff removed successfully.");
                } else {
                    System.out.println("Staff ID not found.");
                }
                break;

            case 4:
                // Filter Staff
                System.out.println("Choose filtering criteria:");
                System.out.println("1. Filter by Role");
                System.out.println("2. Filter by Age");
                System.out.println("3. Filter by Gender");
                System.out.print("Enter your choice: ");
                int filterChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                List<Staff> filteredStaff = new ArrayList<>(staffList); // Start with the full list

                switch (filterChoice) {
                    case 1: // Filter by Role
                        System.out.println("Choose Role to Filter:");
                        System.out.println("1. Administrator");
                        System.out.println("2. Doctor");
                        System.out.println("3. Patient");
                        System.out.println("4. Pharmacist");
                        System.out.print("Enter your choice: ");
                        int roleChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String roleFilter;
                        switch (roleChoice) {
                            case 1:
                                roleFilter = "Administrator";
                                break;
                            case 2:
                                roleFilter = "Doctor";
                                break;
                            case 3:
                                roleFilter = "Patient";
                                break;
                            case 4:
                                roleFilter = "Pharmacist";
                                break;
                            default:
                                System.out.println("Invalid role choice.");
                                return; // Exit method if invalid choice
                        }

                        // Now use roleFilter in your filtering logic
                        filteredStaff = filteredStaff.stream()
                                .filter(staff -> staff.getRole().equalsIgnoreCase(roleFilter))
                                .toList();

                        break;

                    case 2: // Filter by Age
                        System.out.print("Enter the age to filter: ");
                        int ageFilter = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        filteredStaff = filteredStaff.stream()
                                .filter(staff -> staff.getAge() == ageFilter)
                                .toList();
                        break;

                    case 3: // Filter by Gender
                        System.out.println("Choose Gender to Filter:");
                        System.out.println("1. Male");
                        System.out.println("2. Female");
                        System.out.println("3. Hijra Behenchod");
                        System.out.print("Enter your choice: ");
                        int genderChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String genderFilter;
                        switch (genderChoice) {
                            case 1:
                                genderFilter = "Male";
                                break;
                            case 2:
                                genderFilter = "Female";
                                break;
                            case 3:
                                genderFilter = "Hijra";
                                break;
                            default:
                                System.out.println("Invalid gender choice.");
                                return; // Exit the method if an invalid choice is provided
                        }

                        filteredStaff = filteredStaff.stream()
                                .filter(staff -> staff.getGender().equalsIgnoreCase(genderFilter))
                                .toList();
                        break;

                    default:
                        System.out.println("Invalid filtering choice.");
                        return; // Exit method if invalid choice
                }

                // Display the filtered staff list
                if (filteredStaff.isEmpty()) {
                    System.out.println("No staff found matching the selected criteria.");
                } else {
                    System.out.println("Filtered Staff List:");
                    filteredStaff.forEach(System.out::println);
                }

            default:
                System.out.println("Invalid choice.");
        }

        // Save updated staff list to CSV
        saveStaffToCSV(staffList, filePath);
        System.out.println("Staff list updated.");
    }

    // Method to manage inventory (add, update, remove)
    public void manageInventory(Scanner scanner) {
        System.out.println("Medicine Inventory:");
        MedInvent.displayInventory(); // Show current inventory

        System.out.println("\nSelect an action:");
        System.out.println("1. Add New Medicine");
        System.out.println("2. Update Stock Level");
        System.out.println("3. Remove Medicine");
        System.out.println("4. Update Low Stock Level Alert");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1: // Add New Medicine
                System.out.print("Enter Medicine Name: ");
                String newMedName = scanner.nextLine().toLowerCase();

                if (MedInvent.isMedicineAvailable(newMedName)) {
                    System.out.println("Medicine already exists in the inventory.");
                    return;
                }

                System.out.print("Enter Initial Stock: ");
                int initialStock = scanner.nextInt();
                System.out.print("Enter Low Stock Level Alert: ");
                int lowStockAlert = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                MedInvent.addMedicine(newMedName, initialStock, lowStockAlert);
                System.out.println("New medicine added: " + newMedName);
                break;

            case 2: // Update Stock Level
                System.out.print("Enter Medicine Name to update stock: ");
                String medNameToUpdate = scanner.nextLine().toLowerCase();

                if (!MedInvent.isMedicineAvailable(medNameToUpdate)) {
                    System.out.println("Medicine not found in inventory.");
                    return;
                }

                System.out.print("Enter New Stock Quantity: ");
                int newStock = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                MedInvent.setInventory(medNameToUpdate, newStock);
                System.out.println("Stock level updated for " + medNameToUpdate);
                break;

            case 3: // Remove Medicine
                System.out.print("Enter Medicine Name to remove: ");
                String medNameToRemove = scanner.nextLine().toLowerCase();

                if (!MedInvent.isMedicineAvailable(medNameToRemove)) {
                    System.out.println("Medicine not found in inventory.");
                    return;
                }

                MedInvent.removeMedicine(medNameToRemove);
                System.out.println("Medicine removed: " + medNameToRemove);
                break;

            case 4: // Update Low Stock Level Alert
                System.out.print("Enter Medicine Name to update low stock alert: ");
                String medNameToAlert = scanner.nextLine().toLowerCase();

                if (!MedInvent.isMedicineAvailable(medNameToAlert)) {
                    System.out.println("Medicine not found in inventory.");
                    return;
                }

                System.out.print("Enter New Low Stock Level Alert: ");
                int newLowStockAlert = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                MedInvent.updateLowStockAlert(medNameToAlert, newLowStockAlert);
                System.out.println("Low stock level alert updated for " + medNameToAlert);
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // Method to approve replenishment request
    public void approveReplenishmentRequests(List<ReplenishmentRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("No pending replenishment requests.");
            return;
        }

        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a request to approve/reject (1-" + requests.size() + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice < 1 || choice > requests.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ReplenishmentRequest selectedRequest = requests.get(choice - 1);

        System.out.print("Approve or Reject? (approve/reject): ");
        String decision = scanner.nextLine();

        if (decision.equalsIgnoreCase("approve")) {
            selectedRequest.approveRequest();
            MedInvent.restockMedicine(selectedRequest.getMedicationName(), selectedRequest.getQuantity());
            System.out.println("Request approved and inventory updated.");
        } else if (decision.equalsIgnoreCase("reject")) {
            selectedRequest.rejectRequest();
            System.out.println("Request rejected.");
        } else {
            System.out.println("Invalid decision.");
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("Administrator Menu:");
        System.out.println("1. Manage Staff");
        System.out.println("2. View Appointments");
        System.out.println("3. Manage Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
    }
}
