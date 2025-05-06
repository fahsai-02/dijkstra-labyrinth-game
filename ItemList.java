import java.util.*;

public class ItemList {

    public static class Item {
        private final String name;
        private final String type;
        private final int value;
        private final int price;
        private final String imagePath;     
        private final String description; 

        public Item(String name, String type, int value, int price, String imagePath, String description) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.price = price;
            this.imagePath = imagePath;
            this.description = description;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public int getValue() { return value; }
        public int getPrice() { return price; }
        public String getImagePath() { return imagePath; }
        public String getDescription() { return description; }

        @Override
        public String toString() {
            return name + " (" + type + ") +" + value + " [" + price + " gold]";
        }
    }

    private static final Map<String, Item> allItems = new HashMap<>();
    private static final Map<Integer, List<Item>> stageItemMap = new HashMap<>();

    static {        // Stage 1
        addItemToStage(1, new Item("Sword", "ATK", 10, 50, "assets/Items/Sword.PNG", "Increase attack by 10 points."));
        addItemToStage(1, new Item("Shield", "DEF", 5, 50, "assets/Items/Shield.PNG", "Increase defense by 5 points."));
        addItemToStage(1, new Item("BluePotion", "MP", 30, 30, "assets/Items/BluePotion.PNG", "Restore 30 MP."));
        addItemToStage(1, new Item("GreenPotion", "HP", 30, 30, "assets/Items/GreenPotion.PNG", "Restore 30 HP."));
        addItemToStage(1, new Item("LuckyCharm", "PASSIVE", 0, 20, "assets/Items/LuckyCharm.PNG", "Increases chance to gain more gold."));
        
        // Stage 2
        addItemToStage(2, new Item("Sword", "ATK", 20, 100, "assets/Items/Sword.PNG", "Increase attack by 20 points."));
        addItemToStage(2, new Item("Shield", "DEF", 20, 120, "assets/Items/Shield.PNG", "Increase defense by 20 points."));
        addItemToStage(2, new Item("BluePotion", "MP", 50, 50, "assets/Items/BluePotion.PNG", "Restore 50 MP."));
        addItemToStage(2, new Item("GreenPotion", "HP", 50, 50, "assets/Items/GreenPotion.PNG", "Restore 50 HP."));
        addItemToStage(2, new Item("WingedBoots", "PASSIVE", 0, 120, "assets/Items/WingedBoots.PNG", "Reduce MP cost when walking."));
        addItemToStage(2, new Item("LuckyCharm", "PASSIVE", 0, 60, "assets/Items/LuckyCharm.PNG", "Increases chance to gain more gold."));
        
        // Stage 3
        addItemToStage(3, new Item("BluePotion", "MP", 30, 20, "assets/Items/BluePotion.PNG", "Restore 30 MP."));
        addItemToStage(3, new Item("GreenPotion", "HP", 50, 30, "assets/Items/GreenPotion.PNG", "Restore 50 HP."));
        addItemToStage(3, new Item("Sword", "ATK", 100, 250, "assets/Items/Sword.PNG", "Increase attack by 100 points."));
        addItemToStage(3, new Item("Shield", "DEF", 80, 300, "assets/Items/Shield.PNG", "Increase defense by 50 points."));
        addItemToStage(3, new Item("BluePotion", "MP", 100, 70, "assets/Items/BluePotion.PNG", "Restore 100 MP."));
        addItemToStage(3, new Item("GreenPotion", "HP", 100, 70, "assets/Items/GreenPotion.PNG", "Restore 100 HP."));
    }

    private static void addItemToStage(int stage, Item item) {
        allItems.put(item.getName(), item);
        stageItemMap.computeIfAbsent(stage, e -> new ArrayList<>()).add(item);
    }

    public static List<Item> getAllItems(int stage) {
        return stageItemMap.getOrDefault(stage, Collections.emptyList());
    }

    public static Item getItemByName(String name) {
        return allItems.get(name);
    }
}
