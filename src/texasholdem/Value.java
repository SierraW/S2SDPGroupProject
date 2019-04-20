package texasholdem;

public enum Value {
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

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
                return String.valueOf(this.ordinal());
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
}
