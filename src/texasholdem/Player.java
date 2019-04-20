package texasholdem;

import java.util.ArrayList;

public class Player implements Comparable<Player> {
    private final int INDEX;
    private ArrayList<Card> cards;
    private Credit credit;
    private String name;
    private boolean active;
    InputHandleSystem inputHandleSystem = new InputHandleSystem();
    private String cardPoint;

    @Override
    public int compareTo(Player o) {
        return o.cardPoint.compareTo(this.cardPoint);
    }

    public Player() {
        INDEX = indexGenerator();
        cards = new ArrayList<>();
        credit = new Credit();
        credit.total = 100;
        name = "";
        active = true;
        cardPoint = "";
    }

    String viewPlayer(GameStatus round) {
        String outStr = "";
        if (INDEX != 0) {
            outStr += ( name + " (Player " + INDEX + "): ");
        }
        for (Card card : cards) {
            if (card != null && active) {
                outStr += (card.printCard() + " ");
            } else {
                outStr += ("  ");
            }
        }
        if (round == GameStatus.BREAK) {
            outStr += ("    Player total credit: " + credit.total + "\n");
        } else {
            outStr += ("Total bet: " + credit.round + "    Player total credit: " + credit.total + "\n");
        }
        return outStr;
    }

    String viewPlayerCard() {
        String outSt = "";
        if (INDEX == 0) {
            outSt += "Desk:    ";
        } else {
            outSt += (name + " (Player " + INDEX + "): ");
        }
        for (Card card : cards) {
            if (card != null) {
                outSt += (card.printCard() + " ");
            }
        }
        if (INDEX == 0) {
            outSt += "Pool: " + this.credit.total + "\n";
        }
        return outSt + "\n";
    }

    String viewCard() {
        String outStr = "";
        for (Card card : cards) {
            card.setFaceUp(true);
        }
        outStr = this.viewPlayerCard();
        for (Card card : cards) {
            card.setFaceUp(false);
        }
        return outStr;
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

    public void playRound(int highest, GameStatus round) throws Exception {
        System.out.print(this.viewCard());
        System.out.println("Your credit: " + credit.total + "  current round: " + credit.creditAt(round) + "  round highest bet: " + highest);
        System.out.println("Your current bet: " + credit.creditAt(round));

        while (!(credit.place(highest, inputHandleSystem.getInt("add bet(at least " + credit.difference(highest, round) + "): \n", Domain.hasMinimum, 0), round))) {
            if (inputHandleSystem.getChar("bad input, you sure you want to exit this game? (y/n)\n", 'y', 'n') == 'y') {
                active = false;
                return;
            }
        }
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

    public void setCardPoint(String cardPoint) {
        this.cardPoint = cardPoint;
    }

    public String getCardPoint() {
        return cardPoint;
    }

    public int getCredit() {
        return credit.total;
    }

    public void setCredit(int credit) {
        this.credit.total += credit;
    }

    public void resetRoundCredit() {
        this.credit.resetRoundCredit();
    }

    static int indexFactory = 0;

    private static int indexGenerator() {
        return indexFactory++;
    }
}
