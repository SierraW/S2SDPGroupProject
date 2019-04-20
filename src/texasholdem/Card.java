package texasholdem;

public class Card {
    private final Suit SUIT;
    private final Value VALUE;
    private final int INDEX;
    private boolean faceUp;

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

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    @Override
    public String toString() {
        if (!isFaceUp()) {
            return "🎴 ";
        }
        return this.SUIT.toString()+this.VALUE.toString();
    }
}
