import java.io.*;
import java.util.*;

public class InventoryManager {
    private List<InventoryItem> inventory;
    private String logFilePath = "Data/Inventory/inventory_log.txt";
    private String inventoryFilePath = "Data/Inventory/inventory.txt";
    private String usageReportFilePath = "Data/Inventory/usage_report.txt";

    public InventoryManager() {
        inventory = new ArrayList<>();
        loadInventoryFromFile();
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public InventoryItem findItemByName(String name) {
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Add a new item to the inventory
    public String addItem(String name, String category, int quantity) {
        if (name == null || name.isEmpty() || category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Item name and category must be provided.");
        }
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                updateItem(name, quantity, true);
                return "Item already exists. Updating the existing item.";
            }
        }
        InventoryItem newItem = new InventoryItem(name, category, quantity);
        inventory.add(newItem);
        logTransaction("Added item: " + name + ", Category: " + category + ", Quantity: " + quantity);
        sortInventoryByQuantity();
        saveInventoryToFile();
        return "Item added and saved to inventory.";
    }

    // Update the quantity of an existing item
    public String updateItem(String name, int quantityChange, boolean isAdding) {
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                int newQuantity = isAdding ? item.getQuantity() + quantityChange : item.getQuantity() - quantityChange;
                if (newQuantity < 0) {
                    return "Error: You do not have sufficient quantity to use.";
                }
                item.setQuantity(newQuantity);
                logTransaction((isAdding ? "Added " : "Used ") + quantityChange + " of item: " + name + ", New Quantity: " + newQuantity);
                sortInventoryByQuantity();
                saveInventoryToFile();
                return "Item updated.";
            }
        }
        return "Item not found.";
    }

    // Delete an item from the inventory
    public String deleteItem(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name must be provided.");
        }
        Iterator<InventoryItem> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            InventoryItem item = iterator.next();
            if (item.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                logTransaction("Deleted item: " + name);
                sortInventoryByQuantity();
                saveInventoryToFile();
                return "Item deleted successfully.";
            }
        }
        return "Item not found in inventory.";
    }

    // Save inventory to a text file with headings
    public void saveInventoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFilePath))) {
            writer.write("Item,Category,Quantity");
            writer.newLine();
            for (InventoryItem item : inventory) {
                writer.write(item.getName() + "," + item.getCategory() + "," + item.getQuantity());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load inventory from a text file
    public void loadInventoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFilePath))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String category = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    inventory.add(new InventoryItem( name, category, quantity));
                }
            }
            sortInventoryByQuantity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log transactions
    private void logTransaction(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(new Date() + ": " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate usage report
    public void generateUsageReport() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(usageReportFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // Display inventory items in the console
    public void displayInventory() {
        System.out.printf("%-20s %-20s %-10s\n", "ID","Item", "Category", "Quantity");
        System.out.println("----------------------------------------------------------------------");
        for (InventoryItem item : inventory) {
            System.out.printf("%-20s %-20s %-10d\n", item.getId(), item.getName(), item.getCategory(), item.getQuantity());
        }
    }

    // Sort inventory by quantity
    private void sortInventoryByQuantity() {
        inventory.sort(Comparator.comparingInt(InventoryItem::getQuantity));
    }
}
