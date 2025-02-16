package views;

import javax.swing.JFrame;

import models.MurlanGameHandler;

public class MurlanDisplay extends JFrame {
    private static final long serialVersionUID = 1L;
    private GamePanel gamePanel;
    private MurlanGameHandler game;

    public MurlanDisplay(MurlanGameHandler game) {
        this.game = game;
        gamePanel = new GamePanel(game);

        this.setTitle("Murlan");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gamePanel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void enableButtons() {
        gamePanel.enablePlay();
        gamePanel.enablePass();
    }

    public void disableButtons() {
        gamePanel.disablePlay();
        gamePanel.disablePass();
    }

    public void disablePass() {
        gamePanel.disablePass();
    }

    public void enablePass() {
        gamePanel.enablePass();
    }

    public void refresh() {
        gamePanel.invalidate();
        gamePanel.repaint();
    }

    public void showScore() {
        ScoresView view = new ScoresView(game, this);
    }

    public void userToCut() {
        gamePanel.setState(0);
        disableButtons();
        refresh();
    }
}
