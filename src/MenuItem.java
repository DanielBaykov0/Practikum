public class MenuItem {
    private String name;
    private double price;
    private int weightOrVolume;
    private String category;

    public MenuItem(String category, String name, int weightOrVolume, double price) {
        this.category = category;
        this.name = name;
        this.weightOrVolume = weightOrVolume;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getWeightOrVolume() {
        return weightOrVolume;
    }

    public String getCategory() {
        return category;
    }

    public double calculateCalories() {
        switch (category) {
            case "супа":
                return weightOrVolume;
            case "основно ястие":
                return weightOrVolume * 1.7;
            case "десерт":
                return weightOrVolume * 3;
            case "напитка":
                return weightOrVolume * 1.2;
            default:
                return 0;
        }
    }
}