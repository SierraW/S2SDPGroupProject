package texasholdem;

public enum Value {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    @Override
    public String toString() {
        switch (this) {
            case ACE:
                return "A";
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
            case TEN:
                return String.valueOf(this.ordinal() + 2);
            case JACK:
                return "J";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            default:
                return "error";
        }
    }

    public String toChar() {
        switch (this) {
            case ACE:
                return "M";
            case TWO:
                return "A";
            case THREE:
                return "B";
            case FOUR:
                return "C";
            case FIVE:
                return "D";
            case SIX:
                return "E";
            case SEVEN:
                return "F";
            case EIGHT:
                return "G";
            case NINE:
                return "H";
            case TEN:
                return "I";
            case JACK:
                return "J";
            case QUEEN:
                return "K";
            case KING:
                return "L";
            default:
                return "error";
        }
    }
}
