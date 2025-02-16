package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamecomponents.Card;
import gamecomponents.Deck;
import players.AI;
import players.Player;
import views.MurlanDisplay;

public class MurlanGameHandler {
    private Deck deck;
    private List<Player> players;
    private List<Card> waste;
    private List<Card> previousWaste;
    private Player hasTurn;
    private Player lastPlayed;
    private MurlanDisplay display;
    private HashMap<Player, Integer> positions;
    private Scores scores;
    int timesPassed = 0;

    public MurlanGameHandler(String username) {
        createPlayers(username);
        scores = new Scores(players);
        positions = new HashMap<Player, Integer>(4);
        deck = Deck.getInstance();
        deck.shuffle();
        waste = new ArrayList<Card>();
        previousWaste = new ArrayList<Card>();

        display = new MurlanDisplay(this);
    }

    public void newGame(Player winner, Player loser) {
        previousWaste.clear();
        waste.clear();
        positions.clear();
        players.get(0).clearHand();
        for (int i = 1; i < 4; i++) {
            AI temp = (AI) players.get(i);
            temp.clearHand();
        }
        timesPassed = 0;
        hasTurn = null;
        lastPlayed = null;

        deck.shuffle();

        if (isHuman(winner)) {
            display.userToCut();
        } else {
            dealCards(players.indexOf(winner));
            display.enableButtons();
            display.refresh();

            if (swapCards(winner, loser)) {
                int i = players.indexOf(loser) - 1;
                if (i == -1)
                    i = 3;
                hasTurn = players.get(i);
            } else {
                int i = players.indexOf(winner) - 1;
                if (i == -1)
                    i = 3;
                hasTurn = players.get(i);
            }

            determineNextPlayerTurn();
        }

    }

    public void newGame() {
        previousWaste.clear();
        waste.clear();
        timesPassed = 0;
        dealCards(0);
        hasTurn = determineGameStarter();

        if (isHuman(hasTurn))
            display.disablePass();
        else {
            display.enablePass();
            AI temp = (AI) players.get(players.indexOf(hasTurn));
            temp.onPlayerTurn(this);
        }
    }

    public Player hasTurn() {
        return hasTurn;
    }

    public void setTurnTo(Player player) {
        hasTurn = player;
    }

    public boolean isHuman(Player player) {
        if (players.indexOf(player) == 0)
            return true;
        else
            return false;
    }

    public void determineNextPlayerTurn() {
        if (playersLeft() > 1) {
            int i = players.indexOf(hasTurn) + 1;

            if (i == 4)
                i = 0;

            hasTurn = players.get(i);

            if (hasTurn.handSize() > 0) {
                System.out.println(timesPassed);
                if (!isHuman(hasTurn)) {
                    AI temp = (AI) players.get(i);
                    temp.onPlayerTurn(this);
                } else {
                    if (timesPassed == playersLeft() || lastPlayed == players.get(0) || waste.isEmpty()) {
                        display.disablePass();
                    } else
                        display.enablePass();
                }
            } else {
                determineNextPlayerTurn();
            }
        } else {
            for (Player player : players) {
                if (player.getHand().size() > 0)
                    positions.put(player, 0);
            }
            scores.updateRoundScores(positions);
            display.showScore();
            display.disableButtons();
        }
    }

    public int playersLeft() {
        int n = 0;
        for (Player player : players) {
            if (player.handSize() > 0)
                n++;
        }
        return n;
    }

    private Player determineGameStarter() {
        for (Player player : players) {
            if (player.containsInHand(new Card(3, "s"))) {
                if (isHuman(player))
                    display.disablePass();

                return player;
            }
        }
        return null;
    }

    public void setLastPlayed(Player player) {
        lastPlayed = player;
    }

    public Player getLastPlayedPlayer() {
        return lastPlayed;
    }

    private void createPlayers(String username) {
        Player user = new Player(username);
        AI player2 = new AI("Player2");
        AI player3 = new AI("Player3");
        AI player4 = new AI("Player4");

        players = new ArrayList<Player>(4);
        players.add(user);
        players.add(player2);
        players.add(player3);
        players.add(player4);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getWaste() {
        return waste;
    }

    public List<Card> getPreviousWaste() {
        return previousWaste;
    }

    private boolean swapCards(Player winner, Player loser) {
        if (loser.containsInHand(new Card(16, "black")) && loser.containsInHand(new Card(17, "red")))
            return false;
        else {
            winner.addToHand(loser.highestRankingCard());
            if (!isHuman(loser)) {
                AI temp = (AI) players.get(players.indexOf(loser));
                temp.evaluateHand();
            }

            int i = players.indexOf(winner);
            AI winne = (AI) players.get(i);

            loser.addToHand(winne.leastFavoriteCard());

            display.refresh();

            return true;
        }
    }

    public void dealCards(int winnerID) {
        // winnerID eshte id e fituesit te lojes se fundit, qe letra e fundit ti perfundoje atij (letra e prere)
        // letrat nisin te ndahen nga paraardhesi i fituesit, pra player-it

        int x = winnerID - 1;
        if (x == -1)
            x = 3;

        while (!deck.isEmpty()) {
            players.get(x).addToHand(deck.pop());
            x++;

            if (x > 3)
                x = 0;
        }

        players.get(0).sort();
        for (int i = 1; i < players.size(); i++) {
            AI temp = (AI) players.get(i);
            temp.evaluateHand();
        }
    }

    public void finished(Player player) {
        positions.put(player, playersLeft());
    }

    public Card cutDeck() {
        return deck.cutDeck();
    }

    public Scores scores() {
        return scores;
    }

    public int getTimesPassed() {
        return timesPassed;
    }

    public void resetTimesPassed() {
        timesPassed = 0;
    }

    public void incrementTimesPassed() {
        timesPassed = timesPassed + 1;
        if (timesPassed > playersLeft())
            timesPassed = playersLeft();
    }
}
