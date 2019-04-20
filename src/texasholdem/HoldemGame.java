package texasholdem;

import java.util.ArrayList;

public class HoldemGame {

    private PlayingCards playingCards;
    private ArrayList<Player> players;
    private Player deskCards;
    private int numbersOfPlayers;
    private GameStatus status;
    private int sBlindBets;
    private Player currentPlayer;
    private int gameCount;
    private int count;
    private int activePlayersCount;
    private int roundHighest;
    private ArrayList<Player> winners;
    private int startsAt;
    private int placedCredit;

    HoldemGame() {
        playingCards = new PlayingCards();
        numbersOfPlayers = 4;
        setPlayers(numbersOfPlayers);
        newGame();
        gameCount = 0;
        sBlindBets = 2;
        startsAt = 0;
    }

    private void reset() {
        count = 0;
        activePlayersCount = 0;
        roundHighest = 0;
        winners = new ArrayList<>();
    }

    public void newGame() throws IllegalStateException {
        Player.indexFactory = 0;
        if (!playingCards.isEnoughSet(players.size())) {
            playingCards = new PlayingCards();
        }
        deskCards = new Player();
        deskCards.setTotalCredit(0);
        deskCards.resetRoundCredit();
        status = GameStatus.BREAK;
        gameCount += 1;
        int count = 0;
        for (Player player : players) {
            player.clearCards();
            count += 1;
            player.setActive(true);
            player.resetRoundCredit();

        }
        if (count < 2) {
            throw new IllegalStateException("Not enough players");
        }
        currentPlayer = players.get(startsAt);
        placedCredit = -2;
        reset();
    }

    public int getRoundHighest() {
        return roundHighest;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setStartsAt(int startsAt) throws IllegalArgumentException {
        if (startsAt >= players.size() || startsAt < 0) {
            throw new IllegalArgumentException("HoldemGame: set current player failed at " + startsAt);
        }
        this.startsAt = startsAt;
    }

    public void removePlayer(int playerIndex) {
        if (playerIndex > players.size() || playerIndex < 1) {
            throw new IllegalArgumentException("HoldemGame: remove player failed at " + playerIndex);
        }
        if (status != GameStatus.BREAK) {
            throw new IllegalStateException("HoldemGame: remove player failed at " + playerIndex + ". Game is currently running!");
        }
        players.remove(playerIndex - 1);
        fixPlayersIndex();
    }

    public void setPlayers(int numbersOfPlayers) throws IllegalArgumentException {
        if (numbersOfPlayers < 2) {
            throw new IllegalArgumentException("HoldemGame: set total players failed at " + numbersOfPlayers);
        }
        this.numbersOfPlayers = numbersOfPlayers;
        players = new ArrayList<>();
        Player.indexFactory = 1;
        for (int i = 0; i < numbersOfPlayers; i++) {
            players.add(new Player());
        }
    }

    public ArrayList<Player> getPlayers() { // todo may cause safety issue
        return players;
    }

    public boolean addPlayersCredit(int playerIndex, int credit) throws IllegalArgumentException {
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: add players credit failed at player " + playerIndex);
        }
        if (credit < 0 || credit > 0xf3f3f3) {
            throw new IllegalArgumentException("Hold\'emGame: add players credit failed at credit" + credit);
        }
        players.get(playerIndex - 1).setCredit(credit);
        return true;
    }

    public boolean setName(int playerIndex, String name) throws IllegalArgumentException {
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: set name failed at player " + playerIndex);
        }
        if (name == null) {
            throw new IllegalArgumentException("Hold\'emGame: set name failed at player name " + playerIndex);
        }
        players.get(playerIndex - 1).setName(name);
        return true;
    }

    public String getName(int playerIndex) throws IllegalArgumentException {
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: set name failed at player " + playerIndex);
        }
        return players.get(playerIndex - 1).getName();
    }

    public boolean setPlayersCredit(int playerIndex, int credit) throws IllegalArgumentException {
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: set players credit failed at player " + playerIndex);
        }
        if (credit < 0 || credit > 0xf3f3f3) {
            throw new IllegalArgumentException("Hold\'emGame: set players credit failed at credit" + credit);
        }
        players.get(playerIndex - 1).setTotalCredit(credit);
        return true;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getDeskPlayer() {
        return deskCards.clone();
    }

    Player debugGetDeskPlayer() {
        return deskCards;
    }

    private void removeBadPlayer() {
        for (Player player : players) {
            if (player.getCredit() < sBlindBets) {
                player.setActive(false);
            }
        }

    }

    private void fixPlayersIndex() {
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            newPlayers.add(new Player(player.getCreditObj()));
        }
        players = newPlayers;
    }

    public boolean run() {
        if (status == GameStatus.BREAK) {
            removeBadPlayer();
            if (players.size() < 2) {
                throw new IllegalStateException("Too less players!");
            }
            if (count != 0 || activePlayersCount != 0) return false;

            currentPlayer = getNextPlayer();
            roundOne();
            status = GameStatus.ROUNDONE;
        } else if (status == GameStatus.ROUNDONE) {
            return playerLoops();
        }
        if (status == GameStatus.ROUNDTWO) {
            return playerLoops();
        }
        if (status == GameStatus.ROUNDTHREE) {
            return playerLoops();
        }
        return false;
    }

    private void roundOne() {

        roundHighest = sBlindBets;

        for (int i = 0; i < 5; i++) {
            deskCards.passCard(playingCards.getFirstCard());

            if (i < 3) {
                deskCards.changeCardFaceAt(i);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                Card card = playingCards.getFirstCard();
                player.passCard(card);
            }
        }
    }

    private void roundTwo() {
        //false safe
        if (oneAndOnly()) {
            status = GameStatus.CHECK;
            return;
        }

        deskCards.changeCardFaceAt(3);
    }

    private void roundThree() {
        //false safe
        if (oneAndOnly()) {
            status = GameStatus.CHECK;
            return;
        }

        deskCards.changeCardFaceAt(4);
    }

    private boolean oneAndOnly() {
        int count = 0;
        for (Player player : players) {
            if (player.isActive()) {
                count += 1;
            }
        }
        return count == 1;
    }

    private boolean playerLoops() {
        if (currentPlayer.getRoundCredit(status) < roundHighest) {
            currentPlayer.setActive(false);
            currentPlayer = getNextPlayer();
            return true;
        }

        activePlayersCount = 0;
        for (Player player : players) {
            if (player.isActive()) {
                activePlayersCount++;
            }
        }

        if (count++ < activePlayersCount - 1 || getNextPlayer().getRoundCredit(status) != currentPlayer.getRoundCredit(status)) {
            if (!currentPlayer.isActive()) { //todo remove fail safe
                currentPlayer = getNextPlayer();
            }

            //check active
            if (oneAndOnly()) {
                status = GameStatus.CHECK;
                return true;
            }
            //start
            roundHighest = currentPlayer.getRoundCredit(status);
            currentPlayer = getNextPlayer();

            //big blind bets
            if (count == 1 && status == GameStatus.ROUNDONE) {
                if (roundHighest < sBlindBets * 2) {
                    roundHighest = sBlindBets * 2;
                }
            }

            setPoolCredit(); //todo can be better
            return true;
        }

        currentPlayer = getNextPlayer();

        if (currentPlayer.isActive()) {
            switch (status) {
                case ROUNDONE:
                    status = GameStatus.ROUNDTWO;
                    reset();
                    roundTwo();
                    break;
                case ROUNDTWO:
                    status = GameStatus.ROUNDTHREE;
                    reset();
                    roundThree();
                    break;
                case ROUNDTHREE:
                    status = GameStatus.CHECK;
                    reset();
                    System.out.println(gameEnd());
                    break;
            }
        }
        return true;
    }

    public boolean placeBet(int bets) {
        if (currentPlayer.placeCredit(roundHighest, bets, status)) {
            placedCredit = bets;
            return true;
        }
        placedCredit = -1;
        return false;
    }

    public int getPlacedCredit() {
        return placedCredit;
    }

    private void setPoolCredit() { //todo can be better
        int c1 = 0;
        int c2 = 0;
        int c3 = 0;
        for (Player player : players) {
            c1 += player.getRoundCredit(GameStatus.ROUNDONE);
            c2 += player.getRoundCredit(GameStatus.ROUNDTWO);
            c3 += player.getRoundCredit(GameStatus.ROUNDTHREE);
        }
        deskCards.setCredit(new Credit(c1, c2, c3));
    }

    public void setsBlindBets(int sBlindBets) throws IllegalArgumentException {
        if (sBlindBets < 0 || sBlindBets > 0xf3f3f3) {
            throw new IllegalArgumentException("Hold\'emGame: set blind bet failed " + sBlindBets);
        }
        this.sBlindBets = sBlindBets;
    }

    String displayGameTable() { // todo remove debug
        String outStr = "";
        outStr += GameVisualizer.viewCard(deskCards.getCards());
        for (Player player : players) {
            outStr += GameVisualizer.viewCard(player.getCards());
        }

        return outStr + "\n";
    }

    public void viewCardSet() {
        playingCards.viewCardSet();
    }

    public void shuffle() {
        playingCards.shuffleCards();
    }

    public void newCard() {
        playingCards = new PlayingCards();
    }

    private Player getNextPlayer() {
        boolean justPassPreviousPlayer = false;
        for (Player player : players) {
            if (justPassPreviousPlayer) {
                if (player.isActive()) {
                    return player;
                }
            }

            if (player.getINDEX() == currentPlayer.getINDEX()) {
                justPassPreviousPlayer = true;
            }
        }
        for (Player player : players) {
            if (justPassPreviousPlayer) {
                if (player.isActive()) {
                    return player;
                }
            }
        }
        return currentPlayer;
    }

    String gameEnd() {
        String outStr = "";
        ArrayList<Player> remainingPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isActive()) {
                player.setActive(false);
                remainingPlayers.add(player);
            }
        }

        CardCheck cardCheck = new CardCheck();

        for (Player player : remainingPlayers) {
            ReturnTwo<ArrayList<Card>, HandRanking> result = cardCheck.check(combineCards(player));
            player.setCardPoint(Long.parseLong(cardCheck.getCP(result.VALUE, result.CARDS), 16));

            outStr += (player.getName() + " (Player " + player.getINDEX() + ") score: 0x" + Long.toHexString(player.getCardPoint()) + "\n");
            for (Card card : player.getCards()) {
                outStr += (card.printCard(true) + " ");
            }
            outStr += "+ ";
            for (Card card : deskCards.getCards()) {
                outStr += (card.printCard(true) + " ");
            }
            outStr += "\nbest sets: " + result.VALUE.name() + " best five cards:\n";
            for (Card card : result.CARDS) {
                outStr += (card.printCard(true) + " ");
            }
            outStr += "\n\n";
        }

        remainingPlayers.sort(null);

        winners = new ArrayList<>();

        winners.add(remainingPlayers.get(0));
        for (int i = 1; i < remainingPlayers.size(); i++) {
            if (remainingPlayers.get(i - 1).getCardPoint() == (remainingPlayers.get(i).getCardPoint())) {
                winners.add(remainingPlayers.get(i));
            }
        }

        if (winners.size() == 1) {
            outStr += (winners.get(0).getName() + " (Player " + winners.get(0).getINDEX() + ") won\n");
            winners.get(0).setCredit(deskCards.getRoundCredit(GameStatus.CHECK));
            deskCards.setTotalCredit(0);
        } else {
            outStr += ("Multiple winner!\n");
            for (Player player : winners) {
                outStr += (player.getName() + " Player " + player.getINDEX() + ")\n"); //todo make it better
                GameVisualizer.viewPlayer(status, player);
                player.setCredit(deskCards.getCredit() / winners.size());
            }
            deskCards.setTotalCredit(0);
        }

        return outStr;
    }

    private ArrayList<Card> combineCards(Player player) {
        ArrayList<Card> newCards = new ArrayList<>(this.deskCards.getCards());
        newCards.addAll(player.getCards());
        return newCards;
    }


}
