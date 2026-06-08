import java.util.*;

public class Main {
    private static Inventory inventory = new Inventory();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inventory.loadFromFile("data/products.txt");
        if (inventory.getAllProducts().isEmpty()) {
            addSampleProducts();
        }

        System.out.println("=== 🛒 Welcome to SuperMart Billing System ===\n");

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Product Management");
            System.out.println("2. Search Product");
            System.out.println("3. New Billing");
            System.out.println("4. View Sales History");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = getValidInt();
            switch (choice) {
                case 1 -> productManagement();
                case 2 -> searchProduct();
                case 3 -> startBilling();
                case 4 -> viewSalesHistory();
                case 5 -> {
                    inventory.saveToFile("data/products.txt");
                    System.out.println("Thank you! Goodbye.");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addSampleProducts() {
        inventory.addProduct(new Product(101, "Milk (1L)", 65, 100));
        inventory.addProduct(new Product(102, "Bread", 45, 80));
        inventory.addProduct(new Product(103, "Eggs (12)", 85, 50));
        inventory.addProduct(new Product(104, "Rice (5kg)", 320, 30));
        inventory.addProduct(new Product(105, "Sugar (1kg)", 55, 60));
    }

    private static void productManagement() {
        while (true) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Price");
            System.out.println("3. Update Stock");
            System.out.println("4. Delete Product");
            System.out.println("5. View All Products");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int ch = getValidInt();
            switch (ch) {
                case 1 -> addProduct();
                case 2 -> updatePrice();
                case 3 -> updateStock();
                case 4 -> deleteProduct();
                case 5 -> inventory.displayAll();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addProduct() {
        System.out.print("Enter Product Name: ");
        String name = sc.nextLine().trim();
        double price = getValidDouble("Enter Price: ");
        int stock = getValidInt("Enter Stock: ");

        int newId = inventory.getAllProducts().stream()
                .mapToInt(Product::getId).max().orElse(100) + 1;

        inventory.addProduct(new Product(newId, name, price, stock));
        System.out.println("✅ Product added! ID: " + newId);
    }

    private static void updatePrice() {
        inventory.displayAll();
        int id = getValidInt("Enter Product ID: ");
        double price = getValidDouble("Enter New Price: ");
        inventory.updatePrice(id, price);
        System.out.println("✅ Price updated!");
    }

    private static void updateStock() {
        inventory.displayAll();
        int id = getValidInt("Enter Product ID: ");
        int stock = getValidInt("Enter New Stock: ");
        inventory.updateStock(id, stock);
        System.out.println("✅ Stock updated!");
    }

    private static void deleteProduct() {
        inventory.displayAll();
        int id = getValidInt("Enter Product ID to delete: ");
        inventory.deleteProduct(id);
        System.out.println("✅ Product deleted!");
    }

    private static void searchProduct() {
        System.out.print("Enter Product ID: ");
        int id = getValidInt();
        Product p = inventory.findProduct(id);
        System.out.println(p != null ? p : "Product not found!");
    }

    private static void startBilling() {
        List<CartItem> cart = new ArrayList<>();
        System.out.println("\n=== New Billing ===");

        while (true) {
            System.out.print("Enter Product ID (0 to finish): ");
            int id = getValidInt();
            if (id == 0) break;

            Product p = inventory.findProduct(id);
            if (p == null) {
                System.out.println("❌ Product not found!");
                continue;
            }

            int qty = getValidInt("Enter Quantity: ");
            if (qty > p.getStock() || qty <= 0) {
                System.out.println("Invalid quantity or insufficient stock!");
                continue;
            }

            cart.add(new CartItem(p, qty));
            p.reduceStock(qty);
            System.out.println("✅ Added: " + p.getName());
        }

        if (cart.isEmpty()) return;

        System.out.print("Customer Type (1-Regular / 2-Member): ");
        boolean isMember = getValidInt() == 2;

        BillingSystem.generateBill(cart, isMember, "data/sales.txt");
    }

    private static void viewSalesHistory() {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(
                java.nio.file.Paths.get("data/sales.txt"));
            System.out.println("\n=== Sales History ===");
            lines.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("No sales history found yet.");
        }
    }

    // ==================== FIXED HELPER METHODS ====================
    private static int getValidInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    private static int getValidInt(String prompt) {
        System.out.print(prompt);
        return getValidInt();
    }

    private static double getValidDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Invalid number! Try again: ");
            }
        }
    }
}