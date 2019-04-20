package texasholdem;

import java.util.ArrayList;

public class Player {
    private final int INDEX;
    private ArrayList<Card> cards;
    private Credit credit;
    private int roundPlacedCredit;
    private boolean active;
    InputHandleSystem inputHandleSystem = new InputHandleSystem();

    public Player() {
        INDEX = indexGenerator();
        cards = new ArrayList<>(5);
        credit = new Credit();
        credit.total = 100;
        roundPlacedCredit = 0;
        active = true;
    }

    public void viewPlayer(GameStatus round) {
        if (INDEX != 0) {
            System.out.print("Player " + INDEX + ": ");
        }
        for (Card card : cards) {
            if (card != null && active) {
                System.out.print(card + " ");
            } else {
                System.out.print("\t\t");
            }
        }
        System.out.println("Total bet: "+ credit.round + "\tCurrent round: " + credit.creditAt(round));
    }

    public void viewPlayerCard() {
        if (INDEX == 0) {
            System.out.print("Desk    : ");
        } else {
            System.out.print("Player " + INDEX + ": ");
        }
        for (Card card : cards) {
            if (card != null) {
                System.out.print(card + " ");
            }
        }
        System.out.println();
    }

    private void viewCard() {
        for (Card card: cards){
            card.setFaceUp(true);
        }
        this.viewPlayerCard();
        for (Card card: cards){
            card.setFaceUp(false);
        }
    }

    public int getINDEX() {
        return INDEX;
    }

    public void passCard(Card card) {
        cards.add(card);
    }

    public int getRoundCredit(GameStatus round) throws IllegalArgumentException{
        return credit.creditAt(round);
    }

    public void setTotalCredit(int credit) {
        this.credit.total = credit;
    }

    public void playRound(int highest, int pool, GameStatus round) throws Exception {
        this.viewCard();
        int difference = highest - roundPlacedCredit;
        System.out.println("Your credit: " + credit.total + "\t pool: " + pool + "\t highest bet: " + highest);
        System.out.println("Your current bet: " + credit.creditAt(round));

        while (!(credit.place(highest, CommandPrompt.askForInt("add bet(at least " + credit.difference(highest,round) + "): \n"), round))){
            if (CommandPrompt.askForAns("bad input, you sure you want to exit this game? (y/n)\n")) {
                active = false;
                return;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void changeCardFaceAt(int index) {
        cards.get(index).setFaceUp(!cards.get(index).isFaceUp());
    }

    static int indexFactory = 0;
    private static int indexGenerator() {
        return indexFactory++;
    }
}
