package texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayingCards {
    private ArrayList<Card> cards;
    private boolean empty;

    public PlayingCards() {
        cards = new ArrayList<>();
        generateCardSet();
        empty = false;
    }

    public void generateCardSet() {
        int count = 0;
        for (int k = 3; k >= 0; k--) {
            cards.add(new Card(12,k));
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 3; j >= 0; j--) {
                cards.add(new Card(i,j));
            }
        }
    }

    public boolean isFullSet() {
        return cards.size() == 52;
    }


    public void viewCardSet() {
        for (int i=0; i< cards.size(); i++) {
            if (i % 10 != 9) {
                System.out.print(cards.get(i).getSuit().toString() + cards.get(i).getValue().toString() + " ");
            } else {
                System.out.println(cards.get(i).getSuit().toString() + cards.get(i).getValue().toString());
            }
        }
        System.out.println();
    }

    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    public Card getFirstCard() throws IllegalAccessError{
        if (empty) {
            throw new IllegalAccessError("Card set is empty!");
        }
        if (cards.size() == 1) {
            empty = true;
        }
        return cards.remove(0);
    }

    public Card getRandomCard() throws IllegalAccessError {
        if (empty) {
            throw new IllegalAccessError("Card set is empty!");
        }
        if (cards.size() == 1) {
            empty = true;
        }
        Random random = new Random();
        return cards.remove(random.nextInt(cards.size()));
    }

}
