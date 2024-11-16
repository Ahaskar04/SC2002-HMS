import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; // Added for file reading
import java.util.List;
import java.util.Scanner; // Added for file reading


public class CsvManager {

    private static Scanner  scanner=new Scanner(System.in);
    private static List<Patient> patients = loadPatientData("patient_list.csv"); // Load patients from CSV
    private static List<User> staff = loadStaffData("Staff_List.csv"); // Load staff from CSV

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

    public static Patient find_patient()
    {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("Patient ID cannot be empty.");
            return null;
        }
        return patients.stream()
                .filter(p -> p.getHospitalID().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Patient not found.");
                    return null;
                });
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

    public  static User find_staff()
    {
        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("Staff ID cannot be empty.");
            return null;
        }
        return staff.stream()
                .filter(user -> user.getHospitalID().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Staff not found.");
                    return null;
                });
    }



    


}