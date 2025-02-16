package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

import models.MurlanGameHandler;

public class MainGUI extends JFrame {
    private JFrame frame;
    private JTextField textField;
    private JButton button;

    public static void main(String[] args) throws IOException {
        new MainGUI();
    }

    public MainGUI() {
        frame = new JFrame("Murlan Game");
        frame.setLayout(new BorderLayout());
        JLabel background = new JLabel(new ImageIcon("src\\cards\\background.gif"));
        frame.add(background);
        background.setLayout(new FlowLayout());

        ActionListener listener = a -> {
            if (textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Textfield cannot be empty, please enter your name", "Textfield Empty", JOptionPane.ERROR_MESSAGE);
            } else if (textField.getText().length() > 12) {
                JOptionPane.showMessageDialog(frame, "Maximum number of characters is 12", "Name too long", JOptionPane.ERROR_MESSAGE);
                textField.setText("Name");
            } else {
                frame.dispose();
                MurlanGameHandler game = new MurlanGameHandler(textField.getText());
            }
        };

        textField = new JTextField("Name");
        textField.selectAll();
        textField.setToolTipText("Enter your name");
        textField.setColumns(10);
        textField.setBackground(Color.LIGHT_GRAY);
        textField.addActionListener(listener);
        textField.setPreferredSize(new Dimension(200, 26));

        button = new JButton("Enter");
        button.addActionListener(listener);

        background.add(textField);
        background.add(button);

        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(background);
        frame.setVisible(true);
    }


}