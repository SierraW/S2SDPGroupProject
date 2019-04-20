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

    public String toASCII() {
        switch (this) {
            case SPADE:
                return "Spade";

            case HEART:
                return "Heart";

            case CLUB:
                return "Club";

            case DIAMOND:
                return "Diamond";

            default:
                return "error";
        }
    }
}