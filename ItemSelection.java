import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ItemSelection extends JPanel {
    private RunGame parent;
    private CharacterStatus player;
    private List<ItemList.Item> items;
    private int gold = 100; // เริ่มต้นกำหนดเงิน (สามารถเพิ่มระบบเก็บสะสมทีหลังได้)

    public ItemSelection(RunGame parent, CharacterStatus player, List<ItemList.Item> items) {
        this.parent = parent;
        this.player = player;
        this.items = items;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Item Shop - Choose Your Upgrade");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 1, 10, 10));

        for (ItemList.Item item : items) {
            JButton btn = new JButton(item.name + " (" + item.effect + ") - " + item.basePrice + " Gold");
            btn.addActionListener(e -> {
                if (gold >= item.basePrice) {
                    gold -= item.basePrice;
                    player.applyItemEffect(item.name, item.effectValue);
                    JOptionPane.showMessageDialog(this, "Purchased: " + item.name);
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough gold!");
                }
            });
            itemPanel.add(btn);
        }

        JScrollPane scroll = new JScrollPane(itemPanel);
        add(scroll, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Next Stage");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 20));
        nextBtn.addActionListener(e -> parent.nextMap());
        add(nextBtn, BorderLayout.SOUTH);
    }
}
