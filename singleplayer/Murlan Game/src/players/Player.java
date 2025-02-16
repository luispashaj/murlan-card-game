package players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gamecomponents.Card;

public class Player {
    private String name;

    private List<Card> hand;
    private List<Card> selected;

    public String getName() {
        return name;
    }

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<Card>(15);
        selected = new ArrayList<Card>(15);
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public List<Card> getHand() {
        return hand;
    }

    public int handSize() {
        return hand.size();
    }

    public List<Card> reversed() {
        List<Card> reversed = new ArrayList<>(hand);
        Collections.reverse(reversed);
        return reversed;
    }

    public void sort() {
        Collections.sort(hand);
    }

    public List<Card> getSelected() {
        return selected;
    }

    public void unselect() {
        selected.clear();
    }

    public void remove(Card card) {
        hand.remove(card);
    }

    public boolean containsInHand(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getRank() == card.getRank() && hand.get(i).getSuit().equals(card.getSuit()))
                return true;
        }
        return false;
    }

    public void clearHand() {
        hand.clear();
    }

    public Card highestRankingCard() {
        Collections.sort(getHand());

        Card temp = hand.get(hand.size() - 1);

        return temp;
    }
}