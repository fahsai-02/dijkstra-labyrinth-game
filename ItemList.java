import java.util.*;

public class ItemList {

    public static class Item {
        private final String name;
        private final String type;
        private final int value;
        private final int price;

        public Item(String name, String type, int value, int price) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.price = price;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public int getValue() { return value; }
        public int getPrice() { return price; }

        @Override
        public String toString() {
            return name + " (" + type + ") +" + value + " [" + price + " gold]";
        }
    }

    private static final Map<String, Item> allItems = new HashMap<>();
    private static final Map<Integer, List<Item>> stageItemMap = new HashMap<>();

    static {
        // Stage 1 Items
        addItemToStage(1, new Item("Sword", "ATK", 10, 50));
        addItemToStage(1, new Item("Shield", "DEF", 5, 50));
        addItemToStage(1, new Item("BluePotion", "MP", 30, 30));
        addItemToStage(1, new Item("GreenPotion", "HP", 30, 30));

        // Stage 2 Items
        addItemToStage(2, new Item("Sword", "ATK", 20, 100));
        addItemToStage(2, new Item("Shield", "DEF", 10, 100));
        addItemToStage(2, new Item("BluePotion", "MP", 50, 50));
        addItemToStage(2, new Item("GreenPotion", "HP", 50, 50));
        addItemToStage(2, new Item("WingedBoots", "PASSIVE", 0, 120));

        // Stage 3 Items
        addItemToStage(3, new Item("Sword", "ATK", 40, 200));
        addItemToStage(3, new Item("Shield", "DEF", 25, 200));
        addItemToStage(3, new Item("BluePotion", "MP", 75, 70));
        addItemToStage(3, new Item("GreenPotion", "HP", 75, 70));
        addItemToStage(3, new Item("LuckyCharm", "PASSIVE", 0, 150));
    }

    private static void addItemToStage(int stage, Item item) {
        allItems.put(item.getName(), item);
        stageItemMap.computeIfAbsent(stage, k -> new ArrayList<>()).add(item);
    }

    // สำหรับร้านค้า
    public static List<Item> getAllItems(int stage) {
        return stageItemMap.getOrDefault(stage, Collections.emptyList());
    }

    // ใช้เมื่ออยากเข้าถึงไอเท็มจากชื่อ
    public static Item getItemByName(String name) {
        return allItems.get(name);
    }
}
