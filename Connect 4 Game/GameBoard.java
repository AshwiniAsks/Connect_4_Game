import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame {
    private JButton[][] buttons;
    private GameLogic logic;
    private Player player1, player2, currentPlayer;
    private JLabel statusLabel;
    private JLabel scoreLabel1, scoreLabel2;
    private int score1 = 0, score2 = 0;

    public GameBoard() {
        logic = new GameLogic();
        player1 = new Player("Player 1", 'R');
        player2 = new Player("Player 2", 'Y');
        currentPlayer = player1;

        setTitle("Connect 4 Game");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center on screen

        // Top status bar
        statusLabel = new JLabel(currentPlayer.getName() + "'s Turn (" + currentPlayer.getSymbol() + ")", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(30, 144, 255)); // Dodger blue
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        // Game grid panel
        JPanel gridPanel = new JPanel(new GridLayout(6, 7, 5, 5)); 
        gridPanel.setBackground(new Color(0, 51, 153)); // Dark blue board
        buttons = new JButton[6][7];

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton btn = new JButton();
                btn.setBackground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                btn.setPreferredSize(new Dimension(80, 80));

                // Make buttons look circular
                btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                    @Override
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(c.getBackground());
                        g2.fillOval(5, 5, c.getWidth() - 10, c.getHeight() - 10);
                        g2.dispose();
                    }
                });

                final int col = c;
                btn.addActionListener(e -> handleMove(col));

                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Scoreboard panel
        JPanel scorePanel = new JPanel(new GridLayout(1, 2));
        scorePanel.setBackground(new Color(240, 240, 240));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scoreLabel1 = new JLabel(player1.getName() + " (R): " + score1, SwingConstants.CENTER);
        scoreLabel1.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel2 = new JLabel(player2.getName() + " (Y): " + score2, SwingConstants.CENTER);
        scoreLabel2.setFont(new Font("Arial", Font.BOLD, 16));

        scorePanel.add(scoreLabel1);
        scorePanel.add(scoreLabel2);
        add(scorePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Handle move
    private void handleMove(int col) {
        int row = logic.dropPiece(col, currentPlayer.getSymbol());

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Column full! Choose another column.");
            return;
        }

        // Update button color
        buttons[row][col].setBackground(currentPlayer.getSymbol() == 'R' ? Color.RED : Color.YELLOW);

        // Check win/tie
        if (logic.checkWin(row, col, currentPlayer.getSymbol())) {
            if (currentPlayer == player1) {
                score1++;
                scoreLabel1.setText(player1.getName() + " (R): " + score1);
            } else {
                score2++;
                scoreLabel2.setText(player2.getName() + " (Y): " + score2);
            }

            JOptionPane.showMessageDialog(this, currentPlayer.getName() + " WINS!");
            askRestart();
            return;
        } else if (logic.isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a TIE!");
            askRestart();
            return;
        }

        // Switch player
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        statusLabel.setText(currentPlayer.getName() + "'s Turn (" + currentPlayer.getSymbol() + ")");
    }

    // Ask before restarting
    private void askRestart() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Restart Game", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    // Reset board
    private void resetGame() {
        logic = new GameLogic();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                buttons[r][c].setBackground(Color.WHITE);
            }
        }
        currentPlayer = player1;
        statusLabel.setText(currentPlayer.getName() + "'s Turn (" + currentPlayer.getSymbol() + ")");
    }
}
