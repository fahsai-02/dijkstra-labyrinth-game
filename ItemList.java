import java.util.*;

public class ItemList {
    public static class Item {
        public String name;
        public String effect;
        public int effectValue;
        public int basePrice;

        public Item(String name, String effect, int effectValue, int basePrice) {
            this.name = name;
            this.effect = effect;
            this.effectValue = effectValue;
            this.basePrice = basePrice;
        }

        public Item clone() {
            return new Item(name, effect, effectValue, basePrice);
        }
    }

    public static List<Item> getAllItems(int stageLevel) {
        List<Item> items = new ArrayList<>();
        double multiplier = 1 + 0.2 * (stageLevel - 1);

        items.add(new Item("WingedBoots", "Reduce MP usage to 5%", 0, (int)(25 * multiplier)));
        items.add(new Item("GreenPotion", "Increase Max HP", (int)(20 * multiplier), (int)(15 * multiplier)));
        items.add(new Item("BluePotion", "Increase MP", (int)(20 * multiplier), (int)(15 * multiplier)));
        items.add(new Item("Sword", "Increase Atk", (int)(20 * multiplier), (int)(20 * multiplier)));
        items.add(new Item("Shield", "Increase Def", (int)(5 * multiplier), (int)(25 * multiplier)));
        items.add(new Item("LuckyCharm", "More gold from battle", 0, (int)(50 * multiplier)));

        return items;
    }

    public static Item getItemByName(String name, int stageLevel) {
        return getAllItems(stageLevel).stream()
            .filter(i -> i.name.equals(name))
            .findFirst()
            .orElse(null);
    }
}
