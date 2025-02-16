package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.MurlanGameHandler;
import players.Player;

public class ScoresView extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTable table;

    public ScoresView(MurlanGameHandler game, JFrame owner) {
        List<Player> players = game.scores().getPlayers();
        ArrayList<HashMap<Player, Integer>> scores = game.scores().getScores();

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(350, 250);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                game.newGame(game.scores().lastRoundWinner(), game.scores().lastRoundLoser());
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Round nr");
        for (Player player : players) {
            model.addColumn(player.getName());
        }

        for (int i = 0; i < scores.size(); i++) {
            String[] row = {Integer.toString(i + 1), "", "", "", ""};

            for (int j = 0; j < 4; j++) {
                row[j + 1] = row[j + 1] + scores.get(i).get(players.get(j));
            }

            model.addRow(row);
        }

        String[] row = {"Total", "", "", "", ""};
        for (int i = 1; i < model.getColumnCount(); i++) {
            int sum = 0;

            for (int j = 0; j < model.getRowCount(); j++) {
                sum = sum + Integer.parseInt((String) model.getValueAt(j, i));
            }
            row[i] = Integer.toString(sum);
        }
        model.addRow(row);

        table = new JTable(model);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        JScrollPane tableContainer = new JScrollPane(table);
        panel.add(tableContainer, BorderLayout.CENTER);
        this.getContentPane().add(panel);

        this.pack();
        this.setVisible(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(350, 250);
    }
}
