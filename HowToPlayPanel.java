import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HowToPlayPanel extends JPanel {

    private Image bgImage;       // ภาพพื้นหลังของหน้า How To Play
    private RunGame parent;      // main controller สำหรับเปลี่ยนหน้าจอ
    private JButton backButton;  // ปุ่มกลับเมนูหลัก

    public HowToPlayPanel(RunGame parent) {
        this.parent = parent;
        setLayout(null); // ใช้ layout manual เพราะต้องกำหนดตำแหน่งปุ่มเอง

        // โหลดรูปพื้นหลังของหน้าคู่มือ
        try {
            bgImage = new ImageIcon("assets/StartPage/BgGuide.JPG").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading HowToPlay background");
        }

        // ปุ่มย้อนกลับ
        backButton = new JButton(new ImageIcon(
                new ImageIcon("assets/StartPage/Undo.PNG")
                        .getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH)
        ));

        // ปิด border และพื้นหลัง
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);

        // เมื่อคลิกปุ่มย้อนกลับ กลับไปหน้าหลัก
        backButton.addActionListener(e -> parent.returnToMainMenu());
        add(backButton);

        // ถ้ามีการ resize หน้าจอ ให้ reposition ปุ่มใหม่
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int padding = 60;
                backButton.setBounds(padding, padding, 200, 100); // ตำแหน่งใหม่ของปุ่ม
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // วาดภาพพื้นหลังเต็มหน้าจอ
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        // ปรับตำแหน่งปุ่มกลับให้อยู่ล่างซ้ายเสมอ
        backButton.setBounds(15, getHeight() - 175, 200, 100);
    }
}
