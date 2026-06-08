import java.io.*;
import java.util.*;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product findProduct(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void updatePrice(int id, double newPrice) {
        Product p = findProduct(id);
        if (p != null) p.setPrice(newPrice);
    }

    public void updateStock(int id, int newStock) {
        Product p = findProduct(id);
        if (p != null) p.setStock(newStock);
    }

    public void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public void displayAll() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }
        products.forEach(System.out::println);
    }

    // File Operations
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                pw.println(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock());
            }
        } catch (IOException e) {
            System.out.println("Error saving products.");
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int stock = Integer.parseInt(parts[3]);
                    products.add(new Product(id, name, price, stock));
                }
            }
        } catch (Exception e) {
            // File may not exist yet - it's okay
        }
    }
}