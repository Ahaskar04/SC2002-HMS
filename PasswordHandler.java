import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PasswordHandler implements PasswordService {

    private static List<User> staff = CsvManager.loadStaffData("Staff_List.csv"); // Load staff from CSV
    private static List<Patient> patients = CsvManager.loadPatientData("patient_list.csv"); // Load patients from CSV

     // Updated Method: Now accepts dynamic lists of users
    public User authenticateUser(Scanner scanner, List<User> staff, List<Patient> patients) {
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
            
            PasswordUtils utils = new PasswordUtils();
            String hashedPassword = utils.hashPassword(password);
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
                currentUser.setPassword(utils.hashPassword(newPassword));
                currentUser.setFirstLogin(false);
    
                updateStaffList(staff, "Staff_Pass.csv", utils);
                System.out.println("Password updated successfully. Please login again.");
                return null;  // Restart login for updated credentials
            }
        }
        return currentUser;
    }    

    protected void updateStaffList(List<User> staff, String filePath, PasswordUtils utils) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Name,Role,Password,FirstLogin\n"); // Write header
            for (User user : staff) {
                writer.write(user.hospitalID + "," + user.name + "," + user.getClass().getSimpleName() + ","
                            + utils.hashPassword(user.getPassword()) + "," + user.isFirstLogin() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    public void updatePassword(String hospitalID, String newPassword) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Staff_Pass.csv"));
        StringBuilder sb = new StringBuilder();
        String line;
        PasswordUtils utils = new PasswordUtils();
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equals(hospitalID)) {
                parts[3] = utils.hashPassword(newPassword); // Update password to hashed value
                parts[4] = "false"; // Set FirstLogin to false
            }
            sb.append(String.join(",", parts)).append("\n");
        }
        br.close();
    
        BufferedWriter bw = new BufferedWriter(new FileWriter("Staff_Pass.csv"));
        bw.write(sb.toString());
        bw.close();
    }

    public boolean login(String hospitalID, String inputPassword) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Staff_Pass.csv"));
        String line;
        PasswordUtils utils = new PasswordUtils();
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
    
                return utils.hashPassword(inputPassword).equals(storedHashedPassword);
            }
        }
        br.close(); // Close the BufferedReader only
        return false; // If no match found
    }

}