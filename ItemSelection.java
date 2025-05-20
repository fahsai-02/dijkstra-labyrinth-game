import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemSelection extends JPanel {
    private final RunGame parent;                        // ตัวควบคุมหลักสำหรับสลับหน้าจอ
    private final CharacterStatus player;                // สถานะผู้เล่น (เก็บ gold, apply stat)
    private final java.util.List<ItemList.Item> items;   // ไอเทมทั้งหมดที่ให้เลือก

    private Image backgroundImage, chooseBtnImg, leftArrowImg, rightArrowImg, checkmarkImg;

    private Rectangle[] visibleItemBounds;               // Bounding box ของไอเทมบนจอ (3 ชิ้น)
    private Rectangle chooseBtnBounds, leftArrowBounds, rightArrowBounds;

    private final Set<Integer> selectedIndexes = new HashSet<>(); // ไอเทมที่ถูกเลือก
    private int startIndex = 0;                          // index แรกของหน้า (ใช้สำหรับ scroll)
    private final int ITEMS_PER_PAGE = 3;                // แสดงไอเทมได้กี่ชิ้นต่อหน้า

    public ItemSelection(RunGame parent, CharacterStatus player, java.util.List<ItemList.Item> items) {
        this.parent = parent;
        this.player = player;
        this.items = items;

        // โหลดรูปทั้งหมดที่ใช้ในหน้าร้านค้า
        try {
            backgroundImage = new ImageIcon("assets/Items/BgSelect.JPG").getImage();
            chooseBtnImg = new ImageIcon("assets/Items/Choose.PNG").getImage();
            leftArrowImg = new ImageIcon("assets/Items/Left.PNG").getImage();
            rightArrowImg = new ImageIcon("assets/Items/Right.PNG").getImage();
            checkmarkImg = new ImageIcon("assets/Items/Correct.PNG")
                    .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading images: " + e.getMessage());
        }

        visibleItemBounds = new Rectangle[ITEMS_PER_PAGE];

        // ตรวจจับการคลิกเมาส์
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                // กดเลื่อนหน้าไปทางซ้าย
                if (leftArrowBounds != null && leftArrowBounds.contains(p) && startIndex > 0) {
                    startIndex -= ITEMS_PER_PAGE;
                    repaint();
                    return;
                }

                // กดเลื่อนหน้าไปทางขวา
                if (rightArrowBounds != null && rightArrowBounds.contains(p)
                        && startIndex + ITEMS_PER_PAGE < items.size()) {
                    startIndex += ITEMS_PER_PAGE;
                    repaint();
                    return;
                }

                // กดเลือกหรือยกเลิกเลือกไอเทม
                for (int i = 0; i < visibleItemBounds.length; i++) {
                    int index = startIndex + i;
                    if (index < items.size() && visibleItemBounds[i] != null && visibleItemBounds[i].contains(p)) {
                        if (selectedIndexes.contains(index)) {
                            selectedIndexes.remove(index);
                        } else {
                            selectedIndexes.add(index);
                        }
                        repaint();
                        return;
                    }
                }

                // กดปุ่ม Choose เพื่อยืนยันการเลือก
                if (chooseBtnBounds != null && chooseBtnBounds.contains(p)) {
                    handleChoose();
                }
            }
        });
    }

    // เมื่อกด Choose: ตรวจสอบ gold, บันทึกของ, หรือแจ้งเตือน
    private void handleChoose() {
        if (!selectedIndexes.isEmpty()) {
            java.util.List<ItemList.Item> selectedItems = new ArrayList<>();
            int totalCost = 0;

            for (int index : selectedIndexes) {
                ItemList.Item item = items.get(index);
                totalCost += item.getPrice();
                selectedItems.add(item);
            }

            if (player.getGold() >= totalCost) {
                player.spendGold(totalCost); // หักเงิน
                for (ItemList.Item item : selectedItems) {
                    player.applyItemEffect(item.getName(), item.getValue());
                }
                JOptionPane.showMessageDialog(this, "Purchased " + selectedItems.size() + " item(s)!");
                parent.nextMap(); // ไปหน้าต่อไป (เช่น map stage)
            } else {
                JOptionPane.showMessageDialog(this, "Not enough gold to buy selected items.");
            }
        } else {
            // ไม่เลือกไอเทมเลย — ยืนยันว่าจะข้าม
            int confirm = JOptionPane.showConfirmDialog(this,
                    "You didn't select any item.\nDo you want to skip?",
                    "Skip", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                parent.nextMap();
            }
        }
    }

    // วาดหน้าร้านทั้งหมด: bg, ไอเทม, ปุ่ม, ลูกศร, gold
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth(), h = getHeight();

        // วาดพื้นหลัง
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, w, h, this);

        // === วาดไอเทมบนจอ ===
        int itemW = 250, itemH = 300;
        int spacing = 40;
        int startX = (w - (ITEMS_PER_PAGE * itemW + (ITEMS_PER_PAGE - 1) * spacing)) / 2;
        int y = 180;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            int index = startIndex + i;
            if (index >= items.size()) continue;

            int x = startX + i * (itemW + spacing);
            visibleItemBounds[i] = new Rectangle(x, y, itemW, itemH);
            ItemList.Item item = items.get(index);

            BufferedImage cardImage = null;
            try {
                File imageFile = new File(item.getImagePath());
                cardImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (cardImage != null) {
                g.drawImage(cardImage, x, y, itemW, itemH, this);
            } else {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, itemW, itemH);
            }

            // แสดงชื่อ, รายละเอียด, ราคาใต้รูป
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString(item.getName(), x + 30, y + 195);

            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(item.getDescription(), x + 30, y + 230);
            g.drawString("Price: " + item.getPrice() + " G", x + 30, y + 270);

            // ติ๊ก ✔ แสดงถ้าเลือกอยู่
            if (selectedIndexes.contains(index) && checkmarkImg != null) {
                g.drawImage(checkmarkImg, x + itemW - 60, y + 10, this);
            }
        }

        // === ปุ่ม Choose ===
        int btnW = 250, btnH = 80;
        int btnX = (w - btnW) / 2;
        int btnY = h - 120;
        chooseBtnBounds = new Rectangle(btnX, btnY, btnW, btnH);
        if (chooseBtnImg != null)
            g.drawImage(chooseBtnImg, btnX, btnY, btnW, btnH, this);

        // === ปุ่ม Arrow ซ้าย/ขวา ===
        int arrowW = 60, arrowH = 60;
        int arrowY = y + itemH / 2;

        leftArrowBounds = new Rectangle(50, arrowY, arrowW, arrowH);
        rightArrowBounds = new Rectangle(w - 110, arrowY, arrowW, arrowH);

        if (startIndex > 0 && leftArrowImg != null)
            g.drawImage(leftArrowImg, 50, arrowY, arrowW, arrowH, this);
        if (startIndex + ITEMS_PER_PAGE < items.size() && rightArrowImg != null)
            g.drawImage(rightArrowImg, w - 110, arrowY, arrowW, arrowH, this);

        // === แสดง gold ของผู้เล่นมุมขวาบน ===
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Gold: " + player.getGold(), w - 170, 100);
    }
}
