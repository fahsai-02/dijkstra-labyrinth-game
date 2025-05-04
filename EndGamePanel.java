import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {
    public EndGamePanel(int score, boolean isWin, RunGame parent) {
        setLayout(null); // Absolute layout
        setPreferredSize(new Dimension(1920, 1080));

        // ===== Choose BG based on win/loss =====
        String bgPath = isWin 
            ? "assets\\BossFight\\BgWin.JPG"
            : "assets\\BossFight\\BgLose.JPG";  //รอใส่ภาพแพ้
        ImageIcon bgIcon = new ImageIcon(bgPath);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setBounds(0, 0, 1920, 1080);
        add(bgLabel);

        // ===== MAIN MENU Button =====
        JButton mainMenuBtn = createImageButton("assets\\BossFight\\MenuButton.PNG", 600, 750, 300, 80);
        mainMenuBtn.addActionListener(e -> parent.showMainMenu());
        add(mainMenuBtn);

        // ===== EXIT Button =====
        JButton exitBtn = createImageButton("assets\\BossFight\\Exit.PNG", 1020, 715, 300, 120);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);

        // Make buttons appear above BG
        setComponentZOrder(mainMenuBtn, 0);
        setComponentZOrder(exitBtn, 0);
        setComponentZOrder(bgLabel, 2);
    }

    private JButton createImageButton(String path, int x, int y, int w, int h) {
        ImageIcon rawIcon = new ImageIcon(path);
        Image scaled = rawIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        button.setBounds(x, y, w, h);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }
}