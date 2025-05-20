import java.util.*;

public class ItemList {

    // === คลาส Item เก็บข้อมูลไอเทม 1 ชิ้น ===
    public static class Item {
        private final String name;         // ชื่อไอเทม (เช่น Sword)
        private final String type;         // ประเภทไอเทม: ATK, DEF, HP, MP, PASSIVE
        private final int value;           // ค่าที่ส่งผล เช่น +10 ATK
        private final int price;           // ราคาขายของไอเทม (ทอง)
        private final String imagePath;    // path ไปยังไฟล์ภาพของไอเทม
        private final String description;  // คำอธิบายสั้น

        public Item(String name, String type, int value, int price, String imagePath, String description) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.price = price;
            this.imagePath = imagePath;
            this.description = description;
        }

        // === Getter ===
        public String getName() { return name; }
        public String getType() { return type; }
        public int getValue() { return value; }
        public int getPrice() { return price; }
        public String getImagePath() { return imagePath; }
        public String getDescription() { return description; }

        // === แสดงผลแบบอ่านง่ายเวลาพิมพ์ออก ===
        @Override
        public String toString() {
            return name + " (" + type + ") +" + value + " [" + price + " gold]";
        }
    }

    // === ตัวเก็บข้อมูลไอเทมทั้งหมด และไอเทมที่มีในแต่ละ stage ===
    private static final Map<String, Item> allItems = new HashMap<>();
    private static final Map<Integer, List<Item>> stageItemMap = new HashMap<>();

    // === Static block: โหลดไอเทมทั้งหมดไว้ล่วงหน้า ===
    static {
        // ไอเทมใน Stage 1
        addItemToStage(1, new Item("Sword", "ATK", 10, 50, "assets/Items/Sword.PNG", "Increase attack by 10 points."));
        addItemToStage(1, new Item("Shield", "DEF", 5, 50, "assets/Items/Shield.PNG", "Increase defense by 5 points."));
        addItemToStage(1, new Item("BluePotion", "MP", 30, 30, "assets/Items/BluePotion.PNG", "Restore 30 MP."));
        addItemToStage(1, new Item("GreenPotion", "HP", 30, 30, "assets/Items/GreenPotion.PNG", "Restore 30 HP."));
        addItemToStage(1, new Item("LuckyCharm", "PASSIVE", 0, 20, "assets/Items/LuckyCharm.PNG", "Increases chance to gain more gold."));

        // ไอเทมใน Stage 2
        addItemToStage(2, new Item("Sword", "ATK", 20, 100, "assets/Items/Sword.PNG", "Increase attack by 20 points."));
        addItemToStage(2, new Item("Shield", "DEF", 20, 120, "assets/Items/Shield.PNG", "Increase defense by 20 points."));
        addItemToStage(2, new Item("BluePotion", "MP", 50, 50, "assets/Items/BluePotion.PNG", "Restore 50 MP."));
        addItemToStage(2, new Item("GreenPotion", "HP", 50, 50, "assets/Items/GreenPotion.PNG", "Restore 50 HP."));
        addItemToStage(2, new Item("WingedBoots", "PASSIVE", 0, 120, "assets/Items/WingedBoots.PNG", "Reduce MP cost when walking."));
        addItemToStage(2, new Item("LuckyCharm", "PASSIVE", 0, 60, "assets/Items/LuckyCharm.PNG", "Increases chance to gain more gold."));

        // ไอเทมใน Stage 3
        addItemToStage(3, new Item("BluePotion", "MP", 30, 20, "assets/Items/BluePotion.PNG", "Restore 30 MP."));
        addItemToStage(3, new Item("GreenPotion", "HP", 50, 30, "assets/Items/GreenPotion.PNG", "Restore 50 HP."));
        addItemToStage(3, new Item("Sword", "ATK", 100, 250, "assets/Items/Sword.PNG", "Increase attack by 100 points."));
        addItemToStage(3, new Item("Shield", "DEF", 80, 300, "assets/Items/Shield.PNG", "Increase defense by 50 points."));
        addItemToStage(3, new Item("BluePotion", "MP", 100, 70, "assets/Items/BluePotion.PNG", "Restore 100 MP."));
        addItemToStage(3, new Item("GreenPotion", "HP", 100, 70, "assets/Items/GreenPotion.PNG", "Restore 100 HP."));
    }

    // === ใส่ไอเทมเข้าใน map ของ stage และ map รวมทุกไอเทม ===
    private static void addItemToStage(int stage, Item item) {
        allItems.put(item.getName(), item);
        stageItemMap.computeIfAbsent(stage, e -> new ArrayList<>()).add(item);
    }

    // ดึงไอเทมทั้งหมดของ stage นั้น ๆ
    public static List<Item> getAllItems(int stage) {
        return stageItemMap.getOrDefault(stage, Collections.emptyList());
    }

    // ดึงไอเทมจากชื่อ (ใช้กรณีที่รู้ชื่อไอเทมแน่นอน เช่นใน inventory)
    public static Item getItemByName(String name) {
        return allItems.get(name);
    }
}
