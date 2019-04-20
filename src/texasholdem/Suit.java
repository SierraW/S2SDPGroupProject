package texasholdem;

public enum Suit {
    SPADE, HEART, CLUB, DIAMOND;

    @Override
    public String toString() {
        switch (this) {
            case SPADE:
                return "♠️";

            case HEART:
                return "♥️";

            case CLUB:
                return "♣️";

            case DIAMOND:
                return "♦️";

            default:
                return "error";
        }
    }
}