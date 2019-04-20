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
        this(sIndex + vIndex * 10);
    }

    public Card(int index){
        this.INDEX = index;
        SUIT = Suit.values()[index%10];
        VALUE = Value.values()[(index - (index % 10))/10];
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

    public String printCard(boolean isForceViewCard) {
        if (isForceViewCard) {
            return this.SUIT.toString()+this.VALUE.toString();
        }
        if (!isFaceUp()) {
            return "🎴";
        }
        return this.SUIT.toString()+this.VALUE.toString();
    }

    @Override
    public String toString() {
        return printCard(false);
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
