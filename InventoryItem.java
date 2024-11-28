public class InventoryItem {
    private static int idCounter = 1;
    private String id;
    private String name;
    private String category;
    private int quantity;

    public InventoryItem(String name, String category, int quantity) {
        this.id = generateId();
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    private String generateId() {
        return "ITEM" + idCounter++;
    }

    // Getters and setters
    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
