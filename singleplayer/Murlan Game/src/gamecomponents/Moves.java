package gamecomponents;

import java.util.Collections;
import java.util.List;

import models.MurlanGameHandler;

public class Moves {
    public static boolean canBePlayed(List<Card> toPlay, MurlanGameHandler game) {
        Collections.sort(toPlay);

        if (game.getWaste().isEmpty()) {
            return areRulesMatched(toPlay);
        }
        if (game.getLastPlayedPlayer() == game.getPlayers().get(0)) {
            return areRulesMatched(toPlay);
        }

        if (areRulesMatched(toPlay)) {
            if (toPlay.size() == 4 && game.getWaste().size() != 4) {
                return true;
            } else if (toPlay.size() == 4 && game.getWaste().size() == 4) {
                if (toPlay.get(0).isBiggerThan(game.getWaste().get(0)))
                    return true;
                else
                    return false;
            }

            if (toPlay.size() == game.getWaste().size()) {
                if (toPlay.size() == 1)
                    if (toPlay.get(0).isBiggerThan(game.getWaste().get(0)))
                        return true;
                    else
                        return false;
                else if (toPlay.size() == 2)
                    if (toPlay.get(0).isBiggerThan(game.getWaste().get(0)))
                        return true;
                    else
                        return false;
                else if (toPlay.size() == 3)
                    if (toPlay.get(0).isBiggerThan(game.getWaste().get(0)))
                        return true;
                    else
                        return false;
                else if (toPlay.size() >= 5)
                    if (toPlay.get(0).getRank() - game.getWaste().get(0).getRank() == 1)
                        return true;
                    else
                        return false;
            } else
                return false;
        } else
            return false;

        return false;
    }

    public static boolean areRulesMatched(List<Card> toPlay) {
        //bombs
        if (toPlay.size() == 4) {
            for (int i = 0; i < toPlay.size() - 1; i++) {
                if (!toPlay.get(i + 1).isEqualTo(toPlay.get(i)))
                    return false;
            }
            return true;
        }
        //singles
        if (toPlay.size() == 1) {
            return true;
        }

        //doubles
        else if (toPlay.size() == 2) {
            if (toPlay.get(0).isEqualTo(toPlay.get(1))) {
                return true;
            } else
                return false;
        }

        //triples
        else if (toPlay.size() == 3) {
            for (int i = 0; i < toPlay.size() - 1; i++) {
                if (!toPlay.get(i + 1).isEqualTo(toPlay.get(i)))
                    return false;
            }
            return true;
        }

        //stripes
        else {
            for (int i = 0; i < toPlay.size() - 1; i++) {
                if (toPlay.get(i + 1).getRank() - toPlay.get(i).getRank() != 1 || toPlay.get(i + 1).getRank() == 16 || toPlay.get(i + 1).getRank() == 17)
                    return false;
            }
            return true;
        }
    }
}
