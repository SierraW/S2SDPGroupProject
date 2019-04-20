package texasholdem;

import java.util.ArrayList;

public class Player implements Comparable<Player> {
    private final int INDEX;
    private ArrayList<Card> cards;
    protected Credit credit;
    private String name;
    private boolean active;
    private long cardPoint;

    @Override
    public int compareTo(Player o) {
        if (o.cardPoint - this.cardPoint < 0) {
            return -1;
        } else if (o.cardPoint - this.cardPoint >0) {
            return 1;
        } else {
            return 0;
        }
    }

    Player() {
        INDEX = indexGenerator();
        cards = new ArrayList<>();
        credit = new Credit();
        credit.total = 100;
        name = "";
        active = true;
        cardPoint = 0;
    }

    Player(Credit credit) {
        INDEX = indexGenerator();
        cards = new ArrayList<>();
        this.credit = credit;
        name = "";
        active = true;
        cardPoint = 0;
    }

    Player(Player player) {
        this.INDEX = player.getINDEX();
        this.cards = player.getCards();
        this.credit = player.getCreditObj();
        this.name = player.getName();
        this.active = player.isActive();
        this.cardPoint = player.getCardPoint();
    }

    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void clearCards() {
        cards = new ArrayList<>();
    }

    public ArrayList<Card> debugGetCards() { // todo remove debug mothd
        return cards;
    }

    public int getINDEX() {
        return INDEX;
    }

    public void passCard(Card card) {
        cards.add(card);
    }

    public int getRoundCredit(GameStatus round) throws IllegalArgumentException {
        return credit.creditAt(round);
    }

    public void setTotalCredit(int credit) {
        this.credit.total = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void changeCardFaceAt(int index) {
        cards.get(index).setFaceUp(!cards.get(index).isFaceUp());
    }

    public void sort() {
        cards.sort(Card.Comparators.INDEX);
    }

    public void setCardPoint(long cardPoint) {
        this.cardPoint = cardPoint;
    }

    public long getCardPoint() {
        return cardPoint;
    }

    public int getCredit() {
        return credit.total;
    }

    public void setCredit(int credit) {
        this.credit.total += credit;
    }

    public boolean placeCredit(int highestBet, int amount, GameStatus round) {
        return this.credit.place(highestBet, amount, round);
    }

    Credit getCreditObj() {
        return credit;
    }

    void setCredit(Credit credit){
        if (INDEX == 0) {
            this.credit = credit;
        }
    }

    public void resetRoundCredit() {
        this.credit.resetRoundCredit();
    }

    public Player clone() {
        return new Player(this);
    }

    static int indexFactory = 0;

    private static int indexGenerator() {
        return indexFactory++;
    }
}
