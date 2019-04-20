package texasholdem;

import java.util.Comparator;

public class Card implements Comparable<Card>{
    private final Suit SUIT;
    private final Value VALUE;
    private final int INDEX;
    private boolean faceUp;

    @Override
    public int compareTo(Card card) {
        return Comparators.INDEX.compare(this, card);
    }

    public Card(int vIndex, int sIndex) {
        this(sIndex*100 + vIndex);
    }

    public Card(int index){
        this.INDEX = index;
        SUIT = Suit.values()[(index - (index % 100))/100];
        VALUE = Value.values()[index % 100];
        faceUp = false;
    }

    public Suit getSuit() {
        return SUIT;
    }

    public Value getValue() {
        return VALUE;
    }

    public int getINDEX() {
        return INDEX;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    @Override
    public String toString() {
        if (!isFaceUp()) {
            return " 🎴 ";
        }
        return this.SUIT.toString()+this.VALUE.toString();
    }

    public static class Comparators {
        public static Comparator<Card> INDEX = new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o2.INDEX - o1.INDEX ;
            }
        };
        public static Comparator<Card> SUIT = new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return  o2.SUIT.ordinal() - o1.SUIT.ordinal();
            }
        };
        public static Comparator<Card> VALUE = new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o2.VALUE.ordinal() - o1.VALUE.ordinal() ;
            }
        };
    }
}
