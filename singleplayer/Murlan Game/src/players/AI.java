package players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gamecomponents.Card;
import gamecomponents.Moves;
import models.MurlanGameHandler;

public class AI extends Player {
    private List<Card> singles;
    private List<Card> doubles;
    private List<Card> triples;
    private List<Card> bombs;
    private List<Card> stripes;

    public AI(String name) {
        super(name);

        singles = new ArrayList<Card>(15);
        doubles = new ArrayList<Card>(15);
        triples = new ArrayList<Card>(15);
        bombs = new ArrayList<Card>(15);
        stripes = new ArrayList<Card>(15);
    }

    public void onPlayerTurn(MurlanGameHandler game) {
        if (game.getLastPlayedPlayer() == this || game.getWaste().isEmpty() || game.getTimesPassed() == game.playersLeft()) {
            selectFavoriteMove();
            playMove(game);
            game.determineNextPlayerTurn();
        } else {
            selectCardsToPlay(game.getWaste(), game.getLastPlayedPlayer());
            if (getSelected().isEmpty()) {
                game.incrementTimesPassed();
                game.determineNextPlayerTurn();
            } else {
                if (Moves.canBePlayed(getSelected(), game)) {
                    playMove(game);
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    game.determineNextPlayerTurn();
                } else {
                    game.incrementTimesPassed();
                    game.determineNextPlayerTurn();
                }
            }
        }
    }

    private void playMove(MurlanGameHandler game) {
        game.getPreviousWaste().clear();
        game.getPreviousWaste().addAll(game.getWaste());
        game.getWaste().clear();
        game.getWaste().addAll(getSelected());
        game.setLastPlayed(this);
        getHand().removeAll(getSelected());
        unselect();
        game.resetTimesPassed();
        //if after playing the move, the player has finished his hand, he lets the game know that
        //game handles it by adding him to the positions list
        if (getHand().isEmpty())
            game.finished(this);
    }

    private void selectCardsToPlay(List<Card> waste, Player lastPlayed) {
        if (lastPlayed != this) {
            if (waste.size() == 0) {
                selectFavoriteMove();
            }
            if (waste.size() == 1) {
                if (!singles.isEmpty()) {
                    for (int i = 0; i < singles.size(); i++) {
                        if (singles.get(i).isBiggerThan(waste.get(0))) {
                            select(singles.get(i));
                            singles.remove(i);
                            break;
                        }
                    }
                } else {
                    if (!doubles.isEmpty()) {
                        for (int i = 0; i < doubles.size(); i = i + 2) {
                            if (doubles.get(i).isBiggerThan(waste.get(0))) {
                                select(doubles.get(i));
                                singles.add(doubles.get(i + 1));
                                doubles.remove(i);
                                doubles.remove(i);
                                break;
                            }
                        }
                    } else {
                        if (!triples.isEmpty()) {
                            for (int i = 0; i < triples.size(); i = i + 3) {
                                if (triples.get(i).isBiggerThan(waste.get(0))) {
                                    select(triples.get(i));
                                    doubles.add(triples.get(i + 1));
                                    doubles.add(triples.get(i + 2));
                                    triples.remove(i);
                                    triples.remove(i);
                                    triples.remove(i);
                                    break;
                                }
                            }
                        } else {
                            if (!stripes.isEmpty()) {
                                if (stripes.get(stripes.size() - 1).isBiggerThan(waste.get(0))) {
                                    select(stripes.get(stripes.size() - 1));
                                    stripes.remove(stripes.size() - 1);
                                    if (!Moves.areRulesMatched(stripes)) {
                                        singles.addAll(stripes);
                                        stripes.clear();
                                    }

                                }
                            }
                        }
                    }
                }
            } else if (waste.size() == 2) {
                if (!doubles.isEmpty()) {
                    for (int i = 0; i < doubles.size(); i = i + 2) {
                        if (doubles.get(i).isBiggerThan(waste.get(0))) {
                            List<Card> dbl = new ArrayList<Card>();
                            dbl.add(doubles.get(i));
                            dbl.add(doubles.get(i + 1));
                            select(dbl);
                            doubles.removeAll(dbl);
                            break;
                        }
                    }
                } else {
                    if (!triples.isEmpty()) {
                        for (int i = 0; i < triples.size(); i = i + 3) {
                            if (triples.get(i).isBiggerThan(waste.get(0))) {
                                List<Card> dbl = new ArrayList<Card>();
                                dbl.add(triples.get(i));
                                dbl.add(triples.get(i + 1));
                                singles.add(triples.get(i + 2));
                                select(dbl);
                                triples.remove(i + 2);
                                triples.removeAll(dbl);
                                break;
                            }
                        }
                    }
                }
            } else if (waste.size() == 3) {
                if (!triples.isEmpty()) {
                    for (int i = 0; i < triples.size(); i = i + 3) {
                        if (triples.get(i).isBiggerThan(waste.get(0))) {
                            List<Card> tpl = new ArrayList<Card>();
                            tpl.add(triples.get(i));
                            tpl.add(triples.get(i + 1));
                            tpl.add(triples.get(i + 2));
                            select(tpl);
                            triples.removeAll(tpl);
                            break;
                        }
                    }
                }
            } else if (waste.size() == 4) {
                if (!bombs.isEmpty()) {

                    for (int i = 0; i < bombs.size(); i = i + 4) {
                        if (bombs.get(i).isBiggerThan(waste.get(0))) {
                            List<Card> bomb = new ArrayList<Card>();
                            bomb.add(bombs.get(i));
                            bomb.add(bombs.get(i + 1));
                            bomb.add(bombs.get(i + 2));
                            bomb.add(bombs.get(i + 3));
                            select(bomb);
                            bombs.removeAll(bomb);
                            break;
                        }
                    }
                }
            } else if (waste.size() >= 5) {
                if (stripes.size() >= waste.size()) {
                    for (int i = 0; i < stripes.size() - waste.size(); i++) {
                        if (stripes.get(i).getRank() - waste.get(0).getRank() == 1) {
                            List<Card> clr = new ArrayList<Card>();
                            for (int j = i; j < waste.size(); j++) {
                                clr.add(stripes.get(j));
                            }
                            stripes.removeAll(clr);
                            if (!Moves.areRulesMatched(stripes)) {
                                singles.addAll(stripes);
                                stripes.clear();
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            selectFavoriteMove();
        }
    }

    private void selectFavoriteMove() {
        if (!stripes.isEmpty()) {
            select(stripes);
            stripes.clear();
        } else if (!triples.isEmpty() && averageRankTriples() < 10) {
            List<Card> tpl = new ArrayList<Card>();
            for (int i = 0; i < 3; i++) {
                tpl.add(triples.get(i));
            }
            select(tpl);
            triples.removeAll(tpl);
        } else if (averageRankDoubles() > averageRankSingles() && doubles.size() / 2 < singles.size()) {
            select(singles.get(0));
            singles.remove(0);
        } else if (singles.size() < doubles.size() / 2 && averageRankSingles() > averageRankDoubles()) {
            List<Card> dbl = new ArrayList<Card>();
            for (int i = 0; i < 2; i++) {
                dbl.add(doubles.get(i));
            }
            select(dbl);
            doubles.removeAll(dbl);
        } else {
            if (!singles.isEmpty()) {
                select(singles.get(0));
                singles.remove(0);
            } else {
                if (!doubles.isEmpty()) {
                    List<Card> dbl = new ArrayList<Card>();
                    dbl.add(doubles.get(0));
                    dbl.add(doubles.get(1));
                    select(dbl);
                    doubles.removeAll(dbl);
                } else {
                    if (!triples.isEmpty()) {
                        List<Card> tpl = new ArrayList<Card>();
                        tpl.add(triples.get(0));
                        tpl.add(triples.get(1));
                        tpl.add(triples.get(2));
                        select(tpl);
                        triples.removeAll(tpl);
                    } else if (!bombs.isEmpty()) {
                        List<Card> bomb = new ArrayList<Card>();
                        for (int i = 0; i < 4; i++) {
                            bomb.add(bombs.get(i));
                        }
                        select(bomb);
                        bombs.removeAll(bomb);
                    }
                }
            }
        }
    }

    private int averageRankTriples() {
        if (triples.size() == 0)
            return 0;

        int sum = 0;

        for (int i = 0; i < triples.size(); i = i + 3) {
            sum = sum + triples.get(i).getRank();
        }

        return sum / (triples.size() / 3);
    }

    private int averageRankDoubles() {
        if (doubles.size() == 0)
            return 0;

        int sum = 0;

        for (int i = 0; i < doubles.size(); i = i + 2) {
            sum = sum + doubles.get(i).getRank();
        }

        return sum / (doubles.size() / 2);
    }

    private int averageRankSingles() {
        if (singles.size() == 0)
            return 0;

        int sum = 0;

        for (int i = 0; i < singles.size(); i++) {
            sum = sum + singles.get(i).getRank();
        }

        return sum / singles.size();
    }

    private void select(List<Card> toSelect) {
        unselect();
        getSelected().addAll(toSelect);
    }

    private void select(Card toSelect) {
        unselect();
        getSelected().add(toSelect);
    }

    public void evaluateHand() {
        Collections.sort(getHand());

        checkForColours();
        checkForBombs();
        checkForTriples();
        checkForDoubles();
        checkForSingles();
    }

    private void checkForColours() {
        stripes.clear();

        for (int i = 0; i < getHand().size() - 1; i++) {
            int count = 1;

            for (int j = i + 1; j < getHand().size(); j++) {
                if (getHand().get(j).getRank() - getHand().get(j - 1).getRank() == 1 && getHand().get(j).getRank() != 16 && getHand().get(j).getRank() != 17) {
                    count++;
                    continue;
                } else
                    break;
            }

            if (count >= 5) {
                for (int n = i; n < i + count; n++) {
                    stripes.add(getHand().get(n));
                }
                i = i + count - 1;
            }
        }
    }

    private void checkForBombs() {
        bombs.clear();

        for (int i = 0; i < getHand().size() - 1; i++) {
            int count = 1;

            for (int j = i + 1; j < getHand().size(); j++) {
                if (getHand().get(j).isEqualTo(getHand().get(j - 1))) {
                    count++;
                    continue;
                } else
                    break;
            }

            if (count == 4) {
                for (int n = i; n < i + 4; n++) {
                    bombs.add(getHand().get(n));
                }
                i = i + count - 1;
            }
        }
    }

    private void checkForTriples() {
        triples.clear();

        for (int i = 0; i < getHand().size() - 1; i++) {
            int count = 1;

            for (int j = i + 1; j < getHand().size(); j++) {
                if (getHand().get(j).isEqualTo(getHand().get(j - 1))) {
                    count++;
                    continue;
                } else
                    break;
            }

            if (count == 3) {
                for (int n = i; n < i + 3; n++) {
                    triples.add(getHand().get(n));
                }
                i = i + count - 1;
            }
        }
    }

    private void checkForDoubles() {
        doubles.clear();

        List<Card> copyHand = new ArrayList<Card>(getHand());
        copyHand.removeAll(bombs);
        copyHand.removeAll(triples);

        for (int i = 0; i < copyHand.size() - 1; i++) {
            int count = 1;

            for (int j = i + 1; j < copyHand.size(); j++) {
                if (copyHand.get(j).isEqualTo(copyHand.get(j - 1))) {
                    count++;
                    continue;
                } else
                    break;
            }

            if (count == 2) {
                for (int n = i; n < i + 2; n++) {
                    doubles.add(copyHand.get(n));
                }
                i = i + count - 1;
            }
        }
    }

    private void checkForSingles() {
        singles.clear();

        List<Card> copyHand = new ArrayList<Card>(getHand());
        copyHand.removeAll(stripes);
        copyHand.removeAll(bombs);
        copyHand.removeAll(triples);
        copyHand.removeAll(doubles);

        singles.addAll(copyHand);
    }

    public void clearHand() {
        this.getHand().clear();
        singles.clear();
        doubles.clear();
        triples.clear();
        bombs.clear();
        stripes.clear();
    }

    public Card leastFavoriteCard() {
        Card temp;
        if (!singles.isEmpty()) {
            temp = singles.remove(0);
            getHand().remove(temp);
        } else if (stripes.size() > 5) {
            temp = stripes.remove(0);
            getHand().remove(temp);
        } else {
            temp = doubles.remove(0);
            singles.add(doubles.remove(0));
            getHand().remove(temp);
        }
        return temp;
    }
}
