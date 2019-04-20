package texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class ReturnTwo<A, B> {
    final A CARDS;
    final B VALUE;

    ReturnTwo(A cards, B value) {
        CARDS = cards;
        VALUE = value;
    }
}

public class CardCheck {
    ArrayList<Card> result = new ArrayList<>();


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
            heap = cards.get(i - 1);
            if (heap.getValue().ordinal() - cards.get(i).getValue().ordinal() == 1) {
                count += 1;
                countCards.add(cards.get(i));
            } else if (heap.getValue().ordinal() == cards.get(i).getValue().ordinal()) {
                if (heap.getSuit().ordinal() < cards.get(i).getSuit().ordinal())
                    countCards.set(countCards.size() - 1, cards.get(i));
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

        for (int i = 1; i < cards.size(); i++) {
            heap = cards.get(i - 1).getSuit();
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
        resultArr.add(new Card(newResult.VALUE[0] * 10));
        resultArr.add(new Card(newResult.VALUE[1] * 10));
        return resultArr;
    }

    ReturnTwo<ArrayList<Card>, Integer[]> getKind(ArrayList<Card> cards) {
        ArrayList<Card> kindCards = new ArrayList<>();
        sortByRank(cards);
        int kindCount = 1;
        ArrayList<Card> kindCard1 = new ArrayList<>();
        ArrayList<Card> kindCard2 = new ArrayList<>();
        ArrayList<Card> kindCard3 = new ArrayList<>();

        ArrayList<ArrayList<Card>> lists = new ArrayList<>();


        Card heap;

        for (int i = 1; i < cards.size(); i++) {
            heap = cards.get(i - 1);
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
                if (kindCount >= 2 && i < cards.size() - 1) {
                    if (kindCard1.size() == 0) {
                        kindCard1 = new ArrayList<>(kindCards);

                        kindCards = new ArrayList<>();
                    } else if (kindCard2.size() == 0) {
                        kindCard2 = new ArrayList<>(kindCards);
                        kindCards = new ArrayList<>();
                    } else {
                        kindCard3 = new ArrayList<>(kindCards);
                        kindCards = new ArrayList<>();
                    }
                    kindCount = 1;
                }

            }
        }

        if (kindCard1.size() == 0) {
            kindCard1 = new ArrayList<>(kindCards);

            kindCards = new ArrayList<>();
        } else if (kindCard2.size() == 0) {
            kindCard2 = new ArrayList<>(kindCards);
            kindCards = new ArrayList<>();
        } else {
            kindCard3 = new ArrayList<>(kindCards);
            kindCards = new ArrayList<>();
        }

        lists.add(kindCard1);
        lists.add(kindCard2);
        lists.add(kindCard3);
        Collections.sort(lists, new Comparator<ArrayList>(){
            public int compare(ArrayList a1, ArrayList a2) {
                return a2.size() - a1.size();
            }
        });

        Integer[] integers = new Integer[3];
        integers[0] = lists.get(0).size();
        integers[1] = lists.get(1).size();
        integers[2] = lists.get(2).size();


        if (lists.get(0).size() == 4) {
            return new ReturnTwo<>(lists.get(0), integers);
        } else if (lists.get(0).size() == 2 && lists.get(1).size() == 2) {
            lists.get(0).addAll(lists.get(1));
            return new ReturnTwo<>( lists.get(0) , integers);
        }

        lists.get(1).addAll(lists.get(2));

        lists.get(0).addAll(lists.get(1));

        return new ReturnTwo<>(lists.get(0), integers);
    }

    ReturnTwo<ArrayList<Card>, HandRanking> check(ArrayList<Card> cards) {
        ArrayList<Card> straightResult = getStraight(cards);
        ArrayList<Card> flushResult = getFlush(cards);
        if (getFlush(straightResult).size() > 4) {
            if (getFlush(straightResult).get(0).getValue().ordinal() == 12) {
                return new ReturnTwo<>(getBestFive(cards, getFlush(straightResult), HandRanking.ROYALFLUSH), HandRanking.ROYALFLUSH);
            }

            return new ReturnTwo<>(getBestFive(cards, getFlush(straightResult), HandRanking.STRAIGHTFLUSH), HandRanking.STRAIGHTFLUSH);
        }

        ReturnTwo<ArrayList<Card>, Integer[]> kindResult = getKind(cards);
        if (kindResult.VALUE[0] == 4) {
            return new ReturnTwo<>(getBestFive(cards, kindResult.CARDS, HandRanking.FOUROFAKIND), HandRanking.FOUROFAKIND);
        }

        if (kindResult.VALUE[0] == 3 && kindResult.VALUE[1] >= 2) {
            return new ReturnTwo<>(getBestFive(cards, kindResult.CARDS, HandRanking.FULLHOUSE), HandRanking.FULLHOUSE);
        }

        if (flushResult.size() > 4) {
            return new ReturnTwo<>(getBestFive(cards, getFlush(cards), HandRanking.FLUSH), HandRanking.FLUSH);
        }

        if (straightResult.size() > 4) {
            return new ReturnTwo<>(getBestFive(cards, getStraight(cards), HandRanking.STRAIGHT), HandRanking.STRAIGHT);
        }

        if (kindResult.VALUE[0] == 3) {
            return new ReturnTwo<>(getBestFive(cards, kindResult.CARDS, HandRanking.THREEOFAKIND), HandRanking.THREEOFAKIND);
        }

        if (kindResult.VALUE[0] == 2 && kindResult.VALUE[1] == 2) {
            return new ReturnTwo<>(getBestFive(cards, kindResult.CARDS, HandRanking.TWOPAIR), HandRanking.TWOPAIR);
        }

        if (kindResult.VALUE[0] == 2) {
            return new ReturnTwo<>(getBestFive(cards, kindResult.CARDS, HandRanking.ONEPAIR), HandRanking.ONEPAIR);
        }

        return new ReturnTwo<>(getBestFive(cards, sortByIndex(cards), HandRanking.HIGHCARD), HandRanking.HIGHCARD);
    }

    private ArrayList<Card> getBestFive(ArrayList<Card> cards, ArrayList<Card> bestSets, HandRanking hr) {
        ArrayList<Card> best = new ArrayList<>(bestSets);
        if (best.size() >= 5 && hr != HandRanking.TWOPAIR) {
            while (best.size() > 5) {
                best.remove(best.size() - 1);
            }
            return best;
        }

        ArrayList<Card> remainCards = new ArrayList<>(sortByIndex(cards));
        if (!remainCards.removeAll(best)) {
            System.out.println("\n ERROR AT CARD CHECK getBestFive\n");
        }

        if (hr == HandRanking.TWOPAIR) {
            if (best.size() > 4) {
                while (best.size() > 4) {
                    remainCards.add(best.remove(best.size() - 1));
                }
            }
            best.add(remainCards.get(0));
            return best;
        }

        while (best.size() < 5) {
            best.add(remainCards.remove(0));
        }
        return best;
    }

    public String getCP(HandRanking hr, ArrayList<Card> bestFive) {
        String totalCP = "";
        totalCP = String.valueOf(hr.ordinal()+1);

        int cp = 0;

        switch (hr) {
            case HIGHCARD:
            case THREEOFAKIND:
            case FULLHOUSE:
            case FOUROFAKIND:
            case STRAIGHTFLUSH:
                for (int i = 0; i < bestFive.size(); i++) {
                    totalCP += (CalCP(bestFive.get(i), false));
                }
                return totalCP;
            case ONEPAIR:
                for (int i = 0; i < bestFive.size(); i++) {
                    if (i < 2) {
                        totalCP += (CalCP(bestFive.get(i), true));
                    } else {
                        totalCP += (CalCP(bestFive.get(i), false));
                    }
                }
                return totalCP;
            case TWOPAIR:
                for (int i = 0; i < bestFive.size(); i++) {
                    if (i < 4) {
                        totalCP += (CalCP(bestFive.get(i), true));
                    } else {
                        totalCP += (CalCP(bestFive.get(i), false));
                    }
                }
                return totalCP;
            case STRAIGHT:
            case FLUSH:
            case ROYALFLUSH:
            default:
                for (int i = 0; i < bestFive.size(); i++) {
                    totalCP += (CalCP(bestFive.get(i), true));
                }
                return totalCP;
        }
    }

    private String CalCP(Card card, boolean ignoreSuit) {
        if (ignoreSuit) {
            return card.getValue().toHex() + "0";
        }
        return card.getValue().toHex() + (card.getSuit().ordinal() + 1);
    }

}
