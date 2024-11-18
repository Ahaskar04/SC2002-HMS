import java.util.HashMap;
import java.util.Map;

public class Inventory {
    // Singleton instance
    private static Inventory instance;

    // Medication stock
    private Map<String, Integer> medicationStock;

    // Private constructor to prevent instantiation
    private Inventory() {
        medicationStock = new HashMap<>();
    }

    // Method to get the single instance of Inventory
    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    // Method to check stock of a specific medication
    public int checkStock(String medicationName) {
        return medicationStock.getOrDefault(medicationName, 0);
    }

    // Method to update the stock of a specific medication
    public void updateStock(String medicationName, int quantity) {
        if (quantity < 0) {
            System.out.println("Cannot update stock with a negative quantity.");
            return;
        }
        medicationStock.put(medicationName, medicationStock.getOrDefault(medicationName, 0) + quantity);
        System.out.println(
                "Stock updated for " + medicationName + ": Current stock is " + medicationStock.get(medicationName));
    }

    @Override
    public String toString() {
        return "Inventory: " + medicationStock.toString();
    }
}
