import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class BillingSystem {
    private static int invoiceCounter = 1001;

    public static void generateBill(List<CartItem> cart, boolean isMember, String salesFile) {
        double subtotal = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
        double discount = calculateDiscount(subtotal, isMember);
        double finalAmount = subtotal - discount;

        String invoiceNo = "INV-" + LocalDate.now().getYear() + "-" + (invoiceCounter++);

        printReceipt(invoiceNo, cart, subtotal, discount, finalAmount);

        saveBillToFile(invoiceNo, cart, subtotal, discount, finalAmount, isMember, salesFile);
    }

    private static double calculateDiscount(double amount, boolean isMember) {
        double rate = 0;
        if (amount > 1000) rate = 0.10;
        else if (amount > 500) rate = 0.05;
        if (isMember) rate += 0.05;
        return amount * rate;
    }

    private static void printReceipt(String invoiceNo, List<CartItem> cart, 
                                     double subtotal, double discount, double total) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                  SuperMart Receipt");
        System.out.println("=".repeat(60));
        System.out.println("Invoice No : " + invoiceNo);
        System.out.println("Date       : " + LocalDate.now());
        System.out.println("-".repeat(60));
        cart.forEach(System.out::println);
        System.out.println("-".repeat(60));
        System.out.printf("Subtotal     : ₹%.2f%n", subtotal);
        System.out.printf("Discount     : ₹%.2f%n", discount);
        System.out.printf("Total        : ₹%.2f%n", total);
        System.out.println("=".repeat(60));
        System.out.println("Thank You! Visit Again ❤️");
    }

    private static void saveBillToFile(String invoiceNo, List<CartItem> cart, 
                                    double subtotal, double discount, double total, 
                                    boolean isMember, String filename) {
        try (FileWriter fw = new FileWriter(filename, true);PrintWriter pw = new PrintWriter(fw)) {
            pw.println("=== " + invoiceNo + " ===");
            pw.println("Date: " + LocalDate.now());
            pw.println("Member: " + (isMember ? "Yes" : "No"));
            for (CartItem item : cart) pw.println(item);
            pw.printf("Total: ₹%.2f%n%n", total);
        } catch (IOException e) {
            System.out.println("Warning: Could not save bill.");
        }
    }
}