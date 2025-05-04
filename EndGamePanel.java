import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGamePanel extends JPanel {
    public EndGamePanel(int finalScore, boolean isWin, RunGame runGame) {
        setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(
                isWin ? "You win! Great journey so far. Please, it's time to rest." : "You lose, please restart and try again",
                SwingConstants.CENTER
        );
        messageLabel.setFont(new Font("Serif", Font.BOLD, 28));
        messageLabel.setForeground(isWin ? Color.BLUE : Color.RED);

        JLabel scoreLabel = new JLabel("Final Score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton menuButton = new JButton("Main Menu");
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runGame.showMainMenu(); 
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(menuButton);
        buttonPanel.add(exitButton);

        add(messageLabel, BorderLayout.NORTH);
        add(scoreLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
