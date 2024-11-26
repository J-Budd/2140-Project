import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class InventoryMain {
    private static final String[] CATEGORIES = {"Sanitation", "Groceries"};

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InventoryManagerGUI gui = new InventoryManagerGUI();
                gui.setVisible(true);
            }
        });
    }

    private static void addItem(InventoryManager manager, Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.println("Select item category:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }
        int categoryIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline
        String message = manager.addItem(name, CATEGORIES[categoryIndex], quantity);
        System.out.println(message);
    }

    private static void updateItem(InventoryManager manager, Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter quantity to add: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        manager.updateItem(name, quantity, true);
        promptSave(manager, scanner);
    }

    private static void deleteItem(InventoryManager manager, Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        manager.deleteItem(name);
        promptSave(manager, scanner);
    }

    private static void promptSave(InventoryManager manager, Scanner scanner) {
        System.out.println("Do you want to save the changes? (yes/no)");
        String saveChoice = scanner.next();
        if (saveChoice.equalsIgnoreCase("yes")) {
            manager.saveInventoryToFile();
            System.out.println("Inventory saved to file.");
        } else {
            System.out.println("Changes not saved.");
        }
    }
}