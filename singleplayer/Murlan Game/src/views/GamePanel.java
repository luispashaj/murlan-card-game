package views;

import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gamecomponents.Card;
import gamecomponents.Moves;
import models.MurlanGameHandler;
import players.AI;
import players.Player;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 20;
    private int gameState;
    // = 0 at the start of the game where the user has to cut the deck
    // = 1 when the deck is clicked and the cut card is shown
    // = 2 when the game has to be played normally
    // = 3 when the winner of last round is the user, and he has to remove a card from hand to give it to the loser

    private List<Player> players;
    private Player user;
    private Map<Card, Rectangle> mapCards;
    private List<Card> selected;
    private List<Card> waste;
    private List<Card> previousWaste;
    private JButton play;
    private JButton pass;
    private Card cut;

    public GamePanel(MurlanGameHandler game) {
        this.setLayout(null);
        this.players = game.getPlayers();
        user = players.get(0);

        mapCards = new HashMap<>();
        gameState = 0;
        selected = game.getPlayers().get(0).getSelected();
        waste = game.getWaste();
        previousWaste = game.getPreviousWaste();

        play = new JButton("PLAY");
        play.setSize(66, 26);
        play.setLocation(465, 330);
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (gameState == 3) {
                    if (selected.size() == 1) {
                        if (selected.get(0).getRank() < 11) {
                            game.scores().lastRoundLoser().addToHand(selected.get(0));
                            user.remove(selected.get(0));
                            AI temp = (AI) game.scores().lastRoundLoser();
                            temp.evaluateHand();
                            invalidate();
                            repaint();
                            gameState = 2;

                            enablePlay();
                            enablePass();
                            temp.onPlayerTurn(game);
                        } else {
                            JOptionPane.showMessageDialog(null, "The rank of the selected cards should be smaller than 11", "Rank of selected card is too big", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Only 1 card can be chosen to swap with the loser of the last round", "More than 1 card selected", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (selected.size() > 0) {
                        //ensures that it is your turn
                        if (user == game.hasTurn()) {
                            if (game.getTimesPassed() == game.playersLeft()) {
                                if (Moves.areRulesMatched(selected)) {
                                    game.getPreviousWaste().clear();
                                    game.getPreviousWaste().addAll(game.getWaste());
                                    waste.clear();
                                    Collections.sort(selected);
                                    for (int i = 0; i < selected.size(); i++) {
                                        user.remove(selected.get(i));
                                        waste.add(selected.get(i));
                                    }
                                    selected.clear();
                                    game.resetTimesPassed();
                                    invalidate();
                                    repaint();

                                    if (user.handSize() == 0) {
                                        disablePlay();
                                        disablePass();
                                        game.finished(user);
                                    }

                                    game.setLastPlayed(user);
                                    game.determineNextPlayerTurn();
                                }
                            } else {
                                if (Moves.canBePlayed(selected, game)) {
                                    game.getPreviousWaste().clear();
                                    game.getPreviousWaste().addAll(game.getWaste());
                                    waste.clear();
                                    Collections.sort(selected);
                                    for (int i = 0; i < selected.size(); i++) {
                                        user.remove(selected.get(i));
                                        waste.add(selected.get(i));
                                    }
                                    selected.clear();
                                    game.resetTimesPassed();
                                    invalidate();
                                    repaint();

                                    if (user.handSize() == 0) {
                                        disablePlay();
                                        disablePass();
                                        game.finished(user);
                                    }

                                    game.setLastPlayed(user);
                                    game.determineNextPlayerTurn();
                                }
                            }
                        } else
                            System.out.println("Not your turn");
                    }
                }
            }

        });

        pass = new JButton("PASS");
        pass.setSize(66, 26);
        pass.setLocation(465, 358);
        pass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                user.unselect();
                game.incrementTimesPassed();
                invalidate();
                repaint();

                game.determineNextPlayerTurn();
            }
        });

        this.add(play);
        this.add(pass);
        disablePlay();
        disablePass();

        JLabel label1 = new JLabel(user.getName());
        label1.setSize(100, 50);
        label1.setLocation(465, 370);
        label1.setForeground(new Color(192, 192, 192));
        label1.setFont(new Font("Serif", Font.BOLD, 16));

        JLabel label2 = new JLabel(players.get(1).getName());
        label2.setSize(100, 50);
        label2.setLocation(35, 345);
        label2.setForeground(new Color(192, 192, 192));
        label2.setFont(new Font("Serif", Font.BOLD, 16));

        JLabel label3 = new JLabel(players.get(2).getName());
        label3.setSize(100, 50);
        label3.setLocation(150, 30);
        label3.setForeground(new Color(192, 192, 192));
        label3.setFont(new Font("Serif", Font.BOLD, 16));

        JLabel label4 = new JLabel(players.get(3).getName());
        label4.setSize(100, 50);
        label4.setLocation(560, 30);
        label4.setForeground(new Color(192, 192, 192));
        label4.setFont(new Font("Serif", Font.BOLD, 16));

        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (gameState == 0) {
                    Rectangle back = mapCards.get(null);
                    if (back.contains(e.getPoint())) {
                        cut = game.cutDeck();
                        gameState = 1;
                        invalidate();
                        repaint();
                    }
                } else if (gameState == 1) {
                    Rectangle back = mapCards.get(null);
                    Rectangle card = mapCards.get(cut);

                    if (back.contains(e.getPoint()) || card.contains(e.getPoint())) {
                        if (user == game.scores().lastRoundWinner()) {
                            gameState = 3;
                            enablePlay();
                            game.dealCards(players.indexOf(user));
                            user.addToHand(game.scores().lastRoundLoser().getHand().remove(game.scores().lastRoundLoser().handSize() - 1));

                            invalidate();
                            repaint();

                            JOptionPane.showMessageDialog(null, "You was the first place in last round and " + game.scores().lastRoundLoser().getName() + " was the last.\n Choose a card of rank 3 - 10 to swap with his highest ranking card.\n" + game.scores().lastRoundLoser().getName() + " has given you " + game.scores().lastRoundLoser().highestRankingCard().toString(), "Swap cards", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            game.newGame();
                            gameState = 2;
                            enablePlay();
                            invalidate();
                            repaint();
                        }
                    }
                } else {
                    for (Card card : user.reversed()) {
                        Rectangle bounds = mapCards.get(card);
                        if (bounds.contains(e.getPoint())) {
                            if (!selected.contains(card)) {
                                bounds.y -= 20;
                                repaint();
                                selected.add(card);
                                break;
                            } else {
                                bounds.y += 20;
                                repaint();
                                selected.remove(card);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public Dimension getPreferredSize() {
        return new Dimension(650, 450);
    }

    public void enablePlay() {
        play.setEnabled(true);
    }

    public void enablePass() {
        pass.setEnabled(true);
    }

    public void disablePlay() {
        play.setEnabled(false);
    }

    public void disablePass() {
        pass.setEnabled(false);
    }

    public void setState(int i) {
        gameState = i;
    }

    public void invalidate() {
        super.invalidate();
        mapCards.clear();

        if (gameState == 0) {
            int x = (getWidth() / 2) - (CARD_WIDTH / 2);
            int y = (getHeight() / 2) - (CARD_HEIGHT / 2);

            Rectangle back = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
            mapCards.put(null, back);
        } else if (gameState == 1) {
            int x = (getWidth() / 2) - CARD_WIDTH;
            int y = (getHeight() / 2) - (CARD_HEIGHT / 2);

            Rectangle back = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
            mapCards.put(null, back);

            x = x + CARD_WIDTH;
            Rectangle rect = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
            mapCards.put(cut, rect);
        } else {
            int x = (int) ((getWidth() / 2) - (CARD_WIDTH * user.handSize() / 5.0));
            int y = 330;

            for (Card card : user.getHand()) {
                Rectangle bound = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
                mapCards.put(card, bound);
                x = x + SPACING;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(new Color(128, 0, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            int x = (getWidth() / 2) - (CARD_WIDTH / 2);
            int y = (getHeight() / 2) - (CARD_HEIGHT / 2);

            Rectangle back = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.draw(back);

            Graphics2D copy = (Graphics2D) g2d.create();
            paintCard(copy, back);

            copy.dispose();
        } else if (gameState == 1) {
            int x = (getWidth() / 2) - CARD_WIDTH;
            int y = (getHeight() / 2) - (CARD_HEIGHT / 2);

            Image back = new ImageIcon("src\\cards\\back.gif").getImage();
            g2d.drawImage(back, x, y, CARD_WIDTH, CARD_HEIGHT, null);

            x = x + CARD_WIDTH;
            Image card = new ImageIcon(cut.getFileName()).getImage();
            g2d.drawImage(card, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        } else {
            for (Card card : user.getHand()) {
                Rectangle bounds = mapCards.get(card);
                if (bounds != null) {
                    g2d.setColor(Color.BLACK);
                    g2d.draw(bounds);

                    Graphics2D copy = (Graphics2D) g2d.create();
                    paintCard(copy, card, bounds);

                    copy.dispose();
                }
            }

            Image back = new ImageIcon("src\\cards\\back.gif").getImage();

            int y = 20;
            int x = 20;

            for (int i = 0; i < players.get(1).handSize(); i++) {
                g2d.drawImage(back, x, y, CARD_WIDTH, CARD_HEIGHT, null);
                y = y + SPACING;
            }

            x = 450;
            y = 20;

            for (int i = 0; i < players.get(2).handSize(); i++) {
                g2d.drawImage(back, x, y, CARD_WIDTH, CARD_HEIGHT, null);
                x = x - SPACING;
            }

            y = 330;
            x = 560;

            for (int i = 0; i < players.get(3).handSize(); i++) {
                g2d.drawImage(back, x, y, CARD_WIDTH, CARD_HEIGHT, null);
                y = y - SPACING;
            }

            x = (((getWidth() / 2) - (CARD_WIDTH / 2)) - (waste.size() * SPACING) / 2) - CARD_WIDTH / 3;
            y = (getHeight() / 2) - (CARD_HEIGHT / 2) - (CARD_HEIGHT / 3);

            for (int i = 0; i < previousWaste.size(); i++) {
                Image image = new ImageIcon(previousWaste.get(i).getFileName()).getImage();
                g2d.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
                x = x + SPACING;
            }

            x = ((getWidth() / 2) - (CARD_WIDTH / 2)) - (waste.size() * SPACING) / 2;
            y = (getHeight() / 2) - (CARD_HEIGHT / 2);

            for (int i = 0; i < waste.size(); i++) {
                Image image = new ImageIcon(waste.get(i).getFileName()).getImage();
                g2d.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
                x = x + SPACING;
            }
        }

        g2d.dispose();
    }

    private void paintCard(Graphics2D g2d, Card card, Rectangle bounds) {

        Image image = new ImageIcon(card.getFileName()).getImage();
        g2d.drawImage(image, bounds.x, bounds.y, CARD_WIDTH, CARD_HEIGHT, null);
    }

    private void paintCard(Graphics2D g2d, Rectangle bounds) {

        Image image = new ImageIcon("src\\cards\\back.gif").getImage();
        g2d.drawImage(image, bounds.x, bounds.y, CARD_WIDTH, CARD_HEIGHT, null);
    }
}