import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HowToPlayPanel extends JPanel {
    private Image bgImage;
    private RunGame parent;
    private JButton backButton;

    public HowToPlayPanel(RunGame parent) {
        this.parent = parent;
        setLayout(null); // ใช้ absolute layout สำหรับ back button

        // โหลดพื้นหลัง
        try {
            bgImage = new ImageIcon("assets/StartPage/BgGuide.JPG").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading HowToPlay background");
        }

        // ปุ่ม Back
        backButton = new JButton(new ImageIcon(
                new ImageIcon("assets/StartPage/Undo.PNG")
                        .getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH)
        ));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);

        backButton.addActionListener(_ -> parent.returnToMainMenu());
        add(backButton);

        // ปรับตำแหน่งปุ่มเมื่อหน้าต่าง resize
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int padding = 60;
                backButton.setBounds(padding, padding, 200, 100);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
        backButton.setBounds(15, getHeight()-175, 200, 100);
    }
}
