package texasholdem;

import java.util.ArrayList;

public enum HandRanking {
    ROYALFLUSH, STRAIGHTFLUSH, FOUROFAKIND, FULLHOUSE, FLUSH, STRAIGHT, THREEOFAKIND, TWOPAIR, ONEPAIR, HIGHCARD;

    public void checkRanking(ArrayList<Card> cards) {
        cards.sort(null);
        for (Card card: cards) {
            System.out.println(card);
        }
    }
}
