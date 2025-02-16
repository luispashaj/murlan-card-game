package gamecomponents;

public class Card implements Comparable<Card> {
    // ranks vary from 3 to 15
    // 14 is the Black Joker and 15 is the Red Joker
    private int rank;
    // suits are 4, hearts (h), spades (s), diamonds (d), clubs (c)
    private String suit;

    public Card(int rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public boolean isBiggerThan(Card card) {
        if (rank > card.getRank())
            return true;
        else
            return false;
    }

    public boolean isEqualTo(Card card) {
        if (rank == card.rank)
            return true;
        else
            return false;
    }

    public int getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getFileName() {
        if (rank == 10) return "src\\cards\\t" + suit + ".gif";
        if (rank == 11) return "src\\cards\\j" + suit + ".gif";
        if (rank == 12) return "src\\cards\\q" + suit + ".gif";
        if (rank == 13) return "src\\cards\\k" + suit + ".gif";
        if (rank == 14) return "src\\cards\\a" + suit + ".gif";
        if (rank == 15) return "src\\cards\\2" + suit + ".gif";
        if (rank == 16) return "src\\cards\\blackjoker.gif";
        if (rank == 17) return "src\\cards\\redjoker.gif";
        return "src\\cards\\" + rank + suit + ".gif";
    }

    @Override
    public int compareTo(Card card) {
        return rank - card.getRank();
    }

    public String toString() {
        if (rank == 16)
            return "Black Joker";
        else if (rank == 17)
            return "Red Joker";
        else {
            String s = "";

            if (rank == 15) {
                s = s + "2";

                if (suit.equals("h"))
                    s = s + " of hearts";
                else if (suit.equals("d"))
                    s = s + " of diamonds";
                else if (suit.equals("c"))
                    s = s + " of clubs";
                else if (suit.equals("s"))
                    s = s + " of spades";

                return s;
            } else if (rank == 14) {
                s = s + "Ace";

                if (suit.equals("h"))
                    s = s + " of hearts";
                else if (suit.equals("d"))
                    s = s + " of diamonds";
                else if (suit.equals("c"))
                    s = s + " of clubs";
                else if (suit.equals("s"))
                    s = s + " of spades";

                return s;
            }

            if (suit.equals("h"))
                s = s + " of hearts";
            else if (suit.equals("d"))
                s = s + " of diamonds";
            else if (suit.equals("c"))
                s = s + " of clubs";
            else if (suit.equals("s"))
                s = s + " of spades";

            return s;
        }
    }
}
