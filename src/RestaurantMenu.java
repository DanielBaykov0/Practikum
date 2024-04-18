import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RestaurantMenu {

    private static List<MenuItem> menu = new ArrayList<>();
    private static Map<Integer, List<List<String>>> orders = new HashMap<>();
    private static int totalTablesOccupied = 0;
    private static int totalSales = 0;
    private static double totalSalesAmount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(", ");

            if (input.equalsIgnoreCase("изход")) {
                displayStatistics();
                break;
            } else if (input.equalsIgnoreCase("продажби")) {
                displayStatistics();
            } else if (input.startsWith("инфо")) {
                String itemName = input.substring(5).trim();
                displayItemInfo(itemName);
            } else if (!parts[0].matches("\\d")) {
                initializeMenu(input);
            } else {
                processOrder(input);
            }
        }

        scanner.close();
    }

    private static void initializeMenu(String input) {
        String[] parts = input.split(", ");

        if (parts.length != 4) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        String category = parts[0].toLowerCase();
        String name = parts[1];
        int weightOrVolume = Integer.parseInt(parts[2]);
        double price = Double.parseDouble(parts[3]);

        if (!isValidCategory(category) || !isValidName(name) || !isValidWeightOrVolume(weightOrVolume) || !isValidPrice(price)) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        MenuItem item = new MenuItem(category, name, weightOrVolume, price);
        menu.add(item);
    }

    private static void processOrder(String input) {
        String[] parts = input.split(", ");
        int tableNumber = Integer.parseInt(parts[0]);
        List<String> itemsOrdered = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            itemsOrdered.add(parts[i]);
        }

        if (orders.containsKey(tableNumber)) {
            // If the table number already exists in the map,
            // append the new list of items to the existing list
            orders.get(tableNumber).add(itemsOrdered);
        } else {
            // If the table number is new, create a new list of items
            // and put it in the map
            List<List<String>> orderList = new ArrayList<>();
            orderList.add(itemsOrdered);
            orders.put(tableNumber, orderList);
        }

        totalTablesOccupied = Math.max(totalTablesOccupied, tableNumber);
        updateSales(itemsOrdered);
    }

    private static void updateSales(List<String> itemsOrdered) {
        for (String item : itemsOrdered) {
            for (MenuItem menuItem : menu) {
                if (menuItem.getName().equalsIgnoreCase(item)) {
                    totalSales++;
                    totalSalesAmount += menuItem.getPrice();
                    break;
                }
            }
        }
    }

    private static void displayStatistics() {
        System.out.println("Общо заети маси през деня: " + totalTablesOccupied);
        System.out.println("Общо продажби: " + totalSales + " – " + String.format("%.2f", totalSalesAmount));

        Map<String, Double> salesByCategory = new HashMap<>();
        Map<String, Integer> itemCountByCategory = new HashMap<>();

        // Initialize salesByCategory and itemCountByCategory maps
        for (MenuItem menuItem : menu) {
            salesByCategory.put(menuItem.getCategory(), 0.0);
            itemCountByCategory.put(menuItem.getCategory(), 0);
        }

        // Calculate sales by category
        for (List<List<String>> orderLists : orders.values()) {
            for (List<String> order : orderLists) {
                for (String item : order) {
                    for (MenuItem menuItem : menu) {
                        if (menuItem.getName().equalsIgnoreCase(item)) {
                            String category = menuItem.getCategory();
                            double currentAmount = salesByCategory.get(category);
                            double itemPrice = menuItem.getPrice();
                            salesByCategory.put(category, currentAmount + itemPrice);
                            int currentCount = itemCountByCategory.get(category);
                            itemCountByCategory.put(category, currentCount + 1);
                            break;
                        }
                    }
                }
            }
        }

        // Output sales by category
        System.out.println("По категории:");
        for (Map.Entry<String, Double> entry : salesByCategory.entrySet()) {
            String category = entry.getKey();
            double totalSales = entry.getValue();
            int itemCount = itemCountByCategory.get(category);
            System.out.println("\t- " + category + ": " + itemCount + " - " + String.format("%.2f", totalSales));
        }
    }

    private static void displayItemInfo(String itemName) {
        for (MenuItem menuItem : menu) {
            if (menuItem.getName().equalsIgnoreCase(itemName)) {
                System.out.println("Информация за продукт: " + menuItem.getName());
                System.out.println("Категория: " + menuItem.getCategory());
                System.out.println("Цена: " + String.format("%.0f", menuItem.getPrice()));
                System.out.println("Грамаж: " + menuItem.getWeightOrVolume());
                System.out.println("Калории: " + String.format("%.0f", menuItem.calculateCalories()));
                return;
            }
        }
        System.out.println("Item not found in the menu.");
    }

    private static boolean isValidCategory(String category) {
        return category.equals("салата") || category.equals("супа") || category.equals("основно ястие") || category.equals("десерт") || category.equals("напитка");
    }

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-ZА-Яа-я\\s-]+");
    }

    private static boolean isValidWeightOrVolume(int weightOrVolume) {
        return weightOrVolume >= 0 && weightOrVolume <= 1000;
    }

    private static boolean isValidPrice(double price) {
        return price >= 0 && price <= 100.00;
    }
}