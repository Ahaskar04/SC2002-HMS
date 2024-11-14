import java.util.HashMap;
import java.util.Map;

public class MedInvent {
    // Inner class to hold stock information for each medicine
    private static class StockInfo {
        int currentStock;
        int lowStockLevel;

        StockInfo(int currentStock, int lowStockLevel) {
            this.currentStock = currentStock;
            this.lowStockLevel = lowStockLevel;
        }
    }

    // Map to store medicine names and their stock information
    private static Map<String, StockInfo> inventory = new HashMap<>();

    // Initialize the inventory with medicine names, current stock, and low stock
    // level
    static {
        inventory.put("paracetamol", new StockInfo(135, 30));
        inventory.put("ibuprofen", new StockInfo(56, 17));
        inventory.put("amoxicillin", new StockInfo(122, 11));
        inventory.put("cetirizine", new StockInfo(119, 5));
        inventory.put("metformin", new StockInfo(73, 21));
        inventory.put("aspirin", new StockInfo(148, 10));
        inventory.put("atorvastatin", new StockInfo(73, 21));
        inventory.put("azithromycin", new StockInfo(60, 30));
        inventory.put("omeprazole", new StockInfo(141, 9));
        inventory.put("losartan", new StockInfo(93, 29));
    }

    // Method to display the current inventory
    public static void displayInventory() {
        System.out.println("Medicine Inventory:");
        System.out.printf("%-15s %-15s %-15s%n", "Medicine Name", "Current Stock", "Low Stock Level"); // Header with
                                                                                                       // fixed width
        for (Map.Entry<String, StockInfo> entry : inventory.entrySet()) {
            System.out.printf("%-15s %-15d %-15d%n",
                    entry.getKey(),
                    entry.getValue().currentStock,
                    entry.getValue().lowStockLevel);
        }
    }

    // Method to update the stock based on medicine name and prescribed quantity
    public static void updateInventory(String medicineName, int prescribedQuantity) {
        if (inventory.containsKey(medicineName.toLowerCase())) {
            StockInfo stockInfo = inventory.get(medicineName.toLowerCase());
            if (stockInfo.currentStock >= prescribedQuantity) {
                stockInfo.currentStock -= prescribedQuantity;
                System.out.println(medicineName + " stock updated. Remaining stock: " + stockInfo.currentStock);

                // Check if stock falls below the low stock level
                if (stockInfo.currentStock < stockInfo.lowStockLevel) {
                    System.out.println(
                            "Warning: " + medicineName + " stock is below the low stock level. Consider restocking.");
                }
            } else {
                System.out.println("Insufficient stock for " + medicineName + ". Only " + stockInfo.currentStock
                        + " units available.");
            }
        } else {
            System.out.println("Medicine not found in inventory: " + medicineName);
        }
    }

    // Method to directly set the stock of a specific medicine
    public static void setInventory(String medicineName, int newQuantity) {
        if (inventory.containsKey(medicineName.toLowerCase())) {
            inventory.get(medicineName.toLowerCase()).currentStock = newQuantity;
            System.out.println(medicineName + " stock set to " + newQuantity + " units.");
        } else {
            System.out.println("Medicine not found in inventory: " + medicineName);
        }
    }

    // Method to restock a specific medicine
    public static void restockMedicine(String medicineName, int quantityToAdd) {
        if (inventory.containsKey(medicineName.toLowerCase())) {
            StockInfo stockInfo = inventory.get(medicineName.toLowerCase());
            stockInfo.currentStock += quantityToAdd;
            System.out.println(medicineName + " restocked. New stock: " + stockInfo.currentStock);
        } else {
            System.out.println("Medicine not found in inventory: " + medicineName);
        }
    }

    public static boolean isMedicineAvailable(String medicineName) {
        return inventory.containsKey(medicineName);
    }

    public static int getCurrentStock(String medicineName) {
        StockInfo stockInfo = inventory.getOrDefault(medicineName.toLowerCase(), new StockInfo(0, 0));
        return stockInfo.currentStock; // Return the current stock value
    }

    public static void addMedicine(String medicineName, int initialStock, int lowStockAlert) {
        inventory.put(medicineName, new StockInfo(initialStock, lowStockAlert));
    }

    public static void removeMedicine(String medicineName) {
        inventory.remove(medicineName);
    }

    public static void updateLowStockAlert(String medicineName, int newAlertLevel) {
        if (inventory.containsKey(medicineName)) {
            inventory.get(medicineName).lowStockLevel = newAlertLevel;
        }
    }
}
