
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ItemSelection extends JPanel {
    private RunGame parent;
    private CharacterStatus player;
    private List<ItemList.Item> items;
    private int gold = 100;
    private int pageIndex = 0;
    private JPanel cardPanel;
    private JLayeredPane layeredPane;

    public ItemSelection(RunGame parent, CharacterStatus player, List<ItemList.Item> items) {
        this.parent = parent;
        this.player = player;
        this.items = items;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Item Shop - Choose Your Upgrade");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // ===== LAYERED PANE =====
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1920, 1080));

        // BG
        JLabel bgLabel = new JLabel(new ImageIcon("assets/Items/BgSelect.JPG"));
        bgLabel.setBounds(0, 0, 1920, 1080);
        layeredPane.add(bgLabel, JLayeredPane.DEFAULT_LAYER);

        // CARD PANEL (ใช้ absolute layout)
        cardPanel = new JPanel(null);
        cardPanel.setOpaque(false);
        cardPanel.setBounds(0, 0, 1920, 1080);
        layeredPane.add(cardPanel, JLayeredPane.PALETTE_LAYER);

        // BUTTON: PREVIOUS
        JButton prev = new JButton("<");
        prev.setFont(new Font("Arial", Font.BOLD, 24));
        prev.setBounds(100, 500, 60, 60);
        prev.addActionListener(e -> {
            if (pageIndex > 0) {
                pageIndex--;
                updateCards();
            }
        });
        layeredPane.add(prev, JLayeredPane.MODAL_LAYER);

        // BUTTON: NEXT
        JButton next = new JButton(">");
        next.setFont(new Font("Arial", Font.BOLD, 24));
        next.setBounds(1760, 500, 60, 60);
        next.addActionListener(e -> {
            if ((pageIndex + 1) * 3 < items.size()) {
                pageIndex++;
                updateCards();
            }
        });
        layeredPane.add(next, JLayeredPane.MODAL_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Next Stage");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 24));
        nextBtn.addActionListener(e -> parent.nextMap());
        add(nextBtn, BorderLayout.SOUTH);

        updateCards();
    }

    private void updateCards() {
        cardPanel.removeAll();

        int start = pageIndex * 3;
        int end = Math.min(start + 3, items.size());

        int[] xPositions = {400, 810, 1220}; // x positions for 3 cards
        int y = 300;

        for (int i = start; i < end; i++) {
            JPanel card = createItemCard(items.get(i));
            card.setBounds(xPositions[i - start], y, 300, 400);
            cardPanel.add(card);
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private JPanel createItemCard(ItemList.Item item) {
        final Image image = new ImageIcon("assets/Items/" + item.name + ".PNG").getImage();
    
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelW = getWidth();
                int panelH = getHeight();
                int imgW = image.getWidth(this);
                int imgH = image.getHeight(this);
                if (imgW <= 0 || imgH <= 0) return;
    
                double imgRatio = (double) imgW / imgH;
                double panelRatio = (double) panelW / panelH;
    
                int drawW, drawH;
                if (imgRatio > panelRatio) {
                    drawW = panelW;
                    drawH = (int) (panelW / imgRatio);
                } else {
                    drawH = panelH;
                    drawW = (int) (panelH * imgRatio);
                }
    
                int x = (panelW - drawW) / 2;
                int y = (panelH - drawH) / 2;
    
                g.drawImage(image, x, y, drawW, drawH, this);
            }
        };
    
        card.setLayout(null);
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 400));
    
        // ====== ชื่อไอเทม ======
        JLabel nameLabel = new JLabel(item.name, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBounds(50, 235, 200, 30);
        card.add(nameLabel);
    
        // ====== เอฟเฟกต์ไอเทม ======
        JLabel effectLabel = new JLabel(item.effect, JLabel.CENTER);
        effectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        effectLabel.setForeground(Color.BLACK);
        effectLabel.setBounds(50, 280, 200, 25);
        card.add(effectLabel);
    
        // ====== ราคา ======
        JLabel priceLabel = new JLabel(item.basePrice + " Gold", JLabel.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(218, 165, 32));
        priceLabel.setBounds(50, 300, 200, 30);
        card.add(priceLabel);
    
        // ====== ปุ่ม CHOOSE แบบภาพย่อ ======
        JButton buyBtn;
        try {
            ImageIcon rawIcon = new ImageIcon("assets/Items/Choose.PNG");
            Image scaled = rawIcon.getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);
    
            buyBtn = new JButton(scaledIcon);
            buyBtn.setBorderPainted(false);
            buyBtn.setContentAreaFilled(false);
            buyBtn.setFocusPainted(false);
            buyBtn.setOpaque(false);
        } catch (Exception e) {
            buyBtn = new JButton("Buy");
        }
    
        buyBtn.setBounds(75, 330, 150, 50);
        buyBtn.addActionListener(e -> {
            if (gold >= item.basePrice) {
                gold -= item.basePrice;
                player.applyItemEffect(item.name, item.effectValue);
                JOptionPane.showMessageDialog(this, "Purchased: " + item.name);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold!");
            }
        });
        card.add(buyBtn);
    
        return card;
    }
    
}
