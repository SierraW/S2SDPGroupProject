package texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayingCards {
    private ArrayList<Card> cards;
    private boolean empty;

    public PlayingCards() {
        cards = new ArrayList<>(52);
        generateCardSet();
        empty = false;
    }

    public void generateCardSet() {
        int count = 0;
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                cards.add(new Card(i,j));
            }
        }
    }

    public void viewCardSet() {
        for (int i=0; i< cards.size(); i++) {
            if (i % 10 != 9) {
                System.out.print(cards.get(i) + " ");
            } else {
                System.out.println(cards.get(i));
            }
        }
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
