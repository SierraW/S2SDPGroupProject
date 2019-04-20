package texasholdem;

import java.util.ArrayList;
import java.util.Collections;


public class CardCheck {
    ArrayList<Card> result = new ArrayList<>();

    private class ReturnTwo<A, B> {
        final A CARDS;
        final B COUNT;

        ReturnTwo(A cards, B count) {
            CARDS = cards;
            COUNT = count;
        }
    }

    void checkRanking(ArrayList<Card> cards) {
        cards.sort(null);
        for (Card card : cards) {
            System.out.println(card.getSuit().toString() + card.getValue().toString());
        }
    }

    ArrayList<Card> sortByRank(ArrayList<Card> cards) {
        cards.sort(Card.Comparators.VALUE);
        return cards;
    }

    ArrayList<Card> sortBySuit(ArrayList<Card> cards) {
        cards.sort(Card.Comparators.SUIT);
        return cards;
    }

    ArrayList<Card> sortByIndex(ArrayList<Card> cards) {
        cards.sort(Card.Comparators.INDEX);
        return cards;
    }

    ArrayList<Card> getStraight(ArrayList<Card> cards) {
        ArrayList<Card> countCards = new ArrayList<>();
        ArrayList<Card> longestStraight = new ArrayList<>();
        sortByRank(cards);

        int count = 1;
        int highestCount = 0;
        Card heap;
        countCards.add(cards.get(0));

        for (int i = 1; i < cards.size(); i++) {
            heap = cards.get(i-1);
            if (heap.getValue().ordinal() - cards.get(i).getValue().ordinal() == 1) {
                count += 1;
                countCards.add(cards.get(i));
            } else if (heap.getValue().ordinal() == cards.get(i).getValue().ordinal()) {
                if (heap.getSuit().ordinal() < cards.get(i).getSuit().ordinal())
                    countCards.set(i-1, cards.get(i));
            } else {
                if (count > highestCount) {
                    highestCount = count;
                    longestStraight = new ArrayList<>(countCards);
                }
                
                count = 1;
                countCards = new ArrayList<>();
                countCards.add(cards.get(i));
            }
        }
        return longestStraight;
    }

    ArrayList<Card> getFlush(ArrayList<Card> cards) {
        ArrayList<Card> flushCards = new ArrayList<>();
        ArrayList<Card> longestFlush = new ArrayList<>();
        sortBySuit(cards);
        int flushCount = 1;
        int highestCount = 0;
        Suit heap;
        flushCards.add(cards.get(0));
        
        for (int i = 1; i<cards.size(); i++) {
            heap = cards.get(i-1).getSuit();
            if (heap.ordinal() - cards.get(i).getSuit().ordinal() == 0) {
                flushCount += 1;
                flushCards.add(cards.get(i));
            } else {
                if (flushCount > highestCount) {
                    highestCount = flushCount;
                    longestFlush = new ArrayList<>(flushCards);
                }
                
                flushCount = 1;
                flushCards = new ArrayList<>();
                flushCards.add(cards.get(i));
            }
        }
        return longestFlush;
    }

    ArrayList<Card> debugGetKind(ArrayList<Card> cards) { //todo remove debug method
        ReturnTwo<ArrayList<Card>, Integer[]> newResult = getKind(cards);
        ArrayList<Card> resultArr = new ArrayList<>(newResult.CARDS);
        resultArr.add(new Card(newResult.COUNT[0]));
        resultArr.add(new Card(newResult.COUNT[1]));
        return resultArr;
    }

    ReturnTwo<ArrayList<Card>, Integer[]> getKind(ArrayList<Card> cards) {
        ArrayList<Card> kindCards = new ArrayList<>();
        ArrayList<Integer> highestCountsArr = new ArrayList<>();
        highestCountsArr.add(1);    //make sure array list has more than one elements
        sortByRank(cards);
        int kindCount = 1;
        int highestCount = 0;
        Card heap;

        for (int i = 1; i<cards.size(); i++) {
            heap = cards.get(i-1);
            if (heap.getValue().ordinal() == cards.get(i).getValue().ordinal()) {

                if (kindCards.size() > 0) {
                    if (kindCards.get(kindCards.size() - 1).getINDEX() != heap.getINDEX()) {
                        kindCards.add(heap);
                        kindCards.add(cards.get(i));
                    } else {
                        kindCards.add(cards.get(i));
                    }
                } else {
                    kindCards.add(heap);
                    kindCards.add(cards.get(i));
                }

                kindCount += 1;

            } else {
                if (kindCount >= highestCount) {
                    highestCount = kindCount;
                    highestCountsArr.add(kindCount);
                }
                kindCount = 1;
            }
        }

        Collections.sort(highestCountsArr, Collections.reverseOrder());

        Integer[] integers = highestCountsArr.toArray(new Integer[highestCountsArr.size()]);
        return new ReturnTwo<>(kindCards, integers);
    }

    ArrayList<Card> check(ArrayList<Card> cards) {
        ArrayList<Card> straightResult = getStraight(cards);
        ArrayList<Card> flushResult = getFlush(cards);
        if(getFlush(straightResult).size() > 4) {
            if (getFlush(straightResult).get(0).getValue().ordinal() == 12){
                System.out.println("Royal Flush");
                return getFlush(straightResult);
            }

            System.out.println("straight flush");
            return getFlush(straightResult);
        }

        ReturnTwo<ArrayList<Card>, Integer[]> kindResult = getKind(cards);
        if(kindResult.COUNT[0] == 4) {
            System.out.println("4 of a kind");
            return kindResult.CARDS;
        }

        if(kindResult.COUNT[0] == 3 && kindResult.COUNT[1] == 2) {
            System.out.println("Full House");
            return kindResult.CARDS;
        }

        if(flushResult.size() > 4) {
            System.out.println("Flush");
            return getFlush(cards);
        }

        if(straightResult.size() > 4) {
            System.out.println("Straight");
            return getStraight(cards);
        }

        if(kindResult.COUNT[0] == 3) {
            System.out.println("Three of a Kind");
            return kindResult.CARDS;
        }

        if(kindResult.COUNT[0] == 2 && kindResult.COUNT[1] == 2) {
            System.out.println("Two Pair");
            return kindResult.CARDS;
        }

        if(kindResult.COUNT[0] == 2) {
            System.out.println("One Pair");
            return kindResult.CARDS;
        }

        System.out.println("High Card");
        return sortByIndex(cards);
    }


}
