import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ItemSelection extends JPanel {
    private final RunGame parent;
    private final CharacterStatus player;
    private final List<ItemList.Item> items;

    private Image backgroundImage, buyBtnImg, skipBtnImg;
    private Rectangle[] itemBounds;
    private Rectangle buyBounds, skipBounds;

    private int selectedIndex = -1;

    public ItemSelection(RunGame parent, CharacterStatus player, List<ItemList.Item> items) {
        this.parent = parent;
        this.player = player;
        this.items = items;

        try {
            backgroundImage = new ImageIcon("assets/Shop/BGShop.JPG").getImage();
            buyBtnImg = new ImageIcon("assets/Shop/BuyButton.PNG").getImage()
                    .getScaledInstance(200, 80, Image.SCALE_SMOOTH);
            skipBtnImg = new ImageIcon("assets/Shop/SkipButton.PNG").getImage()
                    .getScaledInstance(200, 80, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading shop images: " + e.getMessage());
        }

        itemBounds = new Rectangle[items.size()];

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                for (int i = 0; i < itemBounds.length; i++) {
                    if (itemBounds[i] != null && itemBounds[i].contains(p)) {
                        selectedIndex = i;
                        repaint();
                        return;
                    }
                }

                if (buyBounds != null && buyBounds.contains(p)) {
                    handleBuy();
                } else if (skipBounds != null && skipBounds.contains(p)) {
                    parent.nextMap();
                }
            }
        });
    }

    private void handleBuy() {
        if (selectedIndex >= 0) {
            ItemList.Item item = items.get(selectedIndex);
            if (player.spendGold(item.getPrice())) {
                player.applyItemEffect(item.getName(), item.getValue());
                JOptionPane.showMessageDialog(this, "You bought: " + item.getName());
                parent.nextMap();
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item first.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();

        // Background
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, w, h, this);

        // Items display
        int itemW = 200, itemH = 200, spacing = 50;
        int startX = (w - (items.size() * itemW + (items.size() - 1) * spacing)) / 2;
        int y = 150;

        for (int i = 0; i < items.size(); i++) {
            int x = startX + i * (itemW + spacing);
            itemBounds[i] = new Rectangle(x, y, itemW, itemH);
            ItemList.Item item = items.get(i);

            g.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            g.fillRoundRect(x, y, itemW, itemH, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, itemW, itemH, 20, 20);

            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(item.getName(), x + 15, y + 30);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Effect: " + item.getType() + " +" + item.getValue(), x + 15, y + 60);
            g.drawString("Price: " + item.getPrice() + " G", x + 15, y + 90);
        }

        // Buttons
        int btnY = h - 150;
        int buyX = w / 2 - 250;
        int skipX = w / 2 + 50;

        buyBounds = new Rectangle(buyX, btnY, 200, 80);
        skipBounds = new Rectangle(skipX, btnY, 200, 80);

        if (buyBtnImg != null) g.drawImage(buyBtnImg, buyX, btnY, this);
        if (skipBtnImg != null) g.drawImage(skipBtnImg, skipX, btnY, this);

        // Player gold display
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.WHITE);
        g.drawString("Your Gold: " + player.getGold(), 50, 50);
    }
}
