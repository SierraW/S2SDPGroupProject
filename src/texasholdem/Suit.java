package texasholdem;

public enum Suit {
    DIAMOND, CLUB, HEART, SPADE;

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