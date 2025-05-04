import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {
    public EndGamePanel(int finalScore) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Congratulations! You have cleared the Boss Room!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel scoreLabel = new JLabel("Your Final Score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.addActionListener(e -> System.exit(0));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scoreLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
