import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public interface PasswordService {
    User authenticateUser(Scanner scanner, List<User> staff, List<Patient> patients);
    void updatePassword(String hospitalID, String newPassword) throws IOException;
    boolean login(String hospitalID, String inputPassword) throws IOException;
}
