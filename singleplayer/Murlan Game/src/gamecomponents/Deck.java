package gamecomponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private List<Card> playDeck;
    private static Deck instance;

    public static Deck getInstance() {
        if (instance == null)
            instance = new Deck();
        return instance;
    }

    private Deck() {
        cards = new ArrayList<Card>(54);

        for (int i = 1; i <= 4; i++) {
            for (int rank = 3; rank <= 15; rank++) {
                String suit = "";

                if (i == 1)
                    suit = "h";
                else if (i == 2)
                    suit = "d";
                else if (i == 3)
                    suit = "c";
                else if (i == 4)
                    suit = "s";

                Card temp = new Card(rank, suit);
                cards.add(temp);
            }
        }
        cards.add(new Card(16, "black"));
        cards.add(new Card(17, "red"));

        playDeck = new ArrayList<Card>(cards);
    }

    public void shuffle() {
        playDeck.clear();
        playDeck.addAll(cards);
        Collections.shuffle(playDeck);
    }

    public Card pop() {
        return playDeck.remove(0);
    }

    public boolean isEmpty() {
        return playDeck.isEmpty();
    }

    public Card cutDeck() {
        Random rand = new Random();
        int random = rand.nextInt((53 - 1) + 1) + 1;
        Card temp = playDeck.get(random);
        ArrayList<Card> tempDeck = new ArrayList<Card>();
        for (int i = random + 1; i < 54; i++) {
            tempDeck.add(playDeck.get(i));
        }
        for (int i = random - 1; i >= 0; i--) {
            tempDeck.add(playDeck.get(i));
        }
        tempDeck.add(temp);
        playDeck = tempDeck;

        return temp;
    }
}
