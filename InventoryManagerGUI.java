import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryManagerGUI extends JFrame {
    private InventoryManager inventoryManager;
    private JTextArea displayArea;
    private JPanel displayPanel;

    public InventoryManagerGUI() {
        inventoryManager = new InventoryManager();
        setTitle("Inventory Manager");
        setSize(800, 600);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon image = new ImageIcon("logo.png");
        setIconImage(image.getImage());
        setLayout(new BorderLayout());

        // Create input panel with GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Creating the buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(new AddButtonListener());
        inputPanel.add(addButton, gbc);

        gbc.gridy = 1;
        JButton updateButton = new JButton("Update Item");
        updateButton.addActionListener(new UpdateButtonListener());
        inputPanel.add(updateButton, gbc);

        gbc.gridy = 2;
        JButton deleteButton = new JButton("Delete Item");
        deleteButton.addActionListener(new DeleteButtonListener());
        inputPanel.add(deleteButton, gbc);

        gbc.gridy = 3;
        JButton displayButton = new JButton("Display Inventory");
        displayButton.addActionListener(new DisplayButtonListener());
        inputPanel.add(displayButton, gbc);

        gbc.gridy = 4;
        JButton viewLogButton = new JButton("View Log");
        viewLogButton.addActionListener(new ViewLogButtonListener());
        inputPanel.add(viewLogButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        //  display area
        displayPanel = new JPanel(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);
    }

    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearDisplayArea();
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField nameField = new JTextField();
            JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Groceries", "Sanitation"});
            JTextField quantityField = new JTextField();

            panel.add(new JLabel("Item Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Category:"));
            panel.add(categoryComboBox);
            panel.add(new JLabel("Quantity:"));
            panel.add(quantityField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    displayArea.setText("Error: Item must have a name to be added.");
                    return;
                }
                String category = (String) categoryComboBox.getSelectedItem();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException ex) {
                    displayArea.setText("Error: Incorrect format, quantity must be a number.");
                    return;
                }
                String message = inventoryManager.addItem(name, category, quantity);
                displayArea.setText(message);
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearDisplayArea();
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JTextField nameField = new JTextField();
            JTextField quantityField = new JTextField();
            JRadioButton addRadioButton = new JRadioButton("Add");
            JRadioButton useRadioButton = new JRadioButton("Use");
            ButtonGroup updateGroup = new ButtonGroup();
            updateGroup.add(addRadioButton);
            updateGroup.add(useRadioButton);

            panel.add(new JLabel("Item name:"));
            panel.add(nameField);
            panel.add(new JLabel("Quantity:"));
            panel.add(quantityField);
            panel.add(addRadioButton);
            panel.add(useRadioButton);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name  = nameField.getText();
                if (name.isEmpty()) {
                    displayArea.setText("Error: Item must have a name to be updated.");
                    return;
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException ex) {
                    displayArea.setText("Error: Incorrect format, quantity must be a number.");
                    return;
                }
                if (!addRadioButton.isSelected() && !useRadioButton.isSelected()) {
                    displayArea.setText("Error: You must select Add or Use to update the item.");
                    return;
                }
                boolean isAdding = addRadioButton.isSelected();
                String message = inventoryManager.updateItem(name, quantity, isAdding);
                displayArea.setText(message);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearDisplayArea();
            String name = JOptionPane.showInputDialog("Enter item name to delete:");
            if (name != null && !name.isEmpty()) {
                String message = inventoryManager.deleteItem(name);
                displayArea.setText(message);
            }
        }
    }

    private class DisplayButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayInventory();
        }
    }

    private class ViewLogButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            viewLog();
        }
    }

    private void displayInventory() {
        clearDisplayArea();
        String[] columnNames = {"ID", "Item", "Category", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (InventoryItem item : inventoryManager.getInventory()) {
            Object[] row = {item.getId(), item.getName(), item.getCategory(), item.getQuantity()};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        displayPanel.removeAll();
        displayPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private void viewLog() {
        clearDisplayArea();
        StringBuilder logText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("Data/Inventory/inventory_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logText.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayArea.setText(logText.toString());
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private void clearDisplayArea() {
        displayArea.setText("");
        displayPanel.removeAll();
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        displayPanel.revalidate();
        displayPanel.repaint();
    }
}
