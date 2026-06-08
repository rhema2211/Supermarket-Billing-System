public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }

    public void reduceStock(int quantity) {
        this.stock = Math.max(0, this.stock - quantity);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | ₹%.2f | Stock: %d", id, name, price, stock);
    }
}