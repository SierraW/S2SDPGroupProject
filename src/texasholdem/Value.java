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

    public String toHex() {
        switch (this) {
            case ACE:
                return "D";
            case TWO:
                return "1";
            case THREE:
                return "2";
            case FOUR:
                return "3";
            case FIVE:
                return "4";
            case SIX:
                return "5";
            case SEVEN:
                return "6";
            case EIGHT:
                return "7";
            case NINE:
                return "8";
            case TEN:
                return "9";
            case JACK:
                return "A";
            case QUEEN:
                return "B";
            case KING:
                return "C";
            default:
                return "0";
        }
    }
}
