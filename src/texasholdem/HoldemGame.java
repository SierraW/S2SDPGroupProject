package texasholdem;

import java.util.ArrayList;

public class HoldemGame {

    private PlayingCards playingCards;
    ArrayList<Player> players;
    Player deskCards;
    private int numbersOfPlayers;
    private GameStatus status;
    private int sBlindBets;
    private InGameDebugger debugger;
    private Player currentPlayer;
    private int startsAt;
    private int gameCount;

    HoldemGame() {
        numbersOfPlayers = 4;
        setPlayers(numbersOfPlayers);
        newGame();
        startsAt = 0;
        debugger = new InGameDebugger(this);
        gameCount = 0;
    }

    public void newGame() throws IllegalStateException{
        Player.indexFactory = 0;
        playingCards = new PlayingCards();
        deskCards = new Player();
        deskCards.setTotalCredit(0);
        status = GameStatus.BREAK;
        sBlindBets = 2;
        gameCount += 1;
        currentPlayer = players.get(startsAt);
        for (Player player : players) {
            player.clearCards();
            int count = 0;
            if (player.getCredit() > sBlindBets) {
                count+=1;
                player.setActive(true);
                player.resetRoundCredit();
            }
            if (count == 0) {
                throw new IllegalStateException("Not enough players");
            }
        }
    }

    public int getGameCount() {
        return gameCount;
    }

    public int getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(int startsAt) throws IllegalArgumentException{
        if (startsAt >= players.size() || startsAt < 0) {
            throw new IllegalArgumentException("HoldemGame: set current player failed at " + startsAt);
        }
        this.startsAt = startsAt;
    }

    public void setPlayers(int numbersOfPlayers) throws IllegalArgumentException{
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

    public String viewPlayer(int playerIndex) throws IllegalArgumentException{
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: view players failed at player " + playerIndex);
        }
        return players.get(playerIndex - 1).viewPlayer(GameStatus.BREAK);
    }

    public boolean setName(int playerIndex, String name) throws IllegalArgumentException{
        if (playerIndex < 1 || playerIndex > numbersOfPlayers) {
            throw new IllegalArgumentException("Hold\'emGame: set name failed at player " + playerIndex);
        }
        if (name == null) {
            throw new IllegalArgumentException("Hold\'emGame: set name failed at player name " + playerIndex);
        }
        players.get(playerIndex - 1).setName(name);
        return true;
    }

    public String getName(int playerIndex) throws IllegalArgumentException{
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

    public void run() throws Exception {
        if (status == GameStatus.ROUNDONE) {
            currentPlayer = players.get(startsAt);
            roundOne(GameStatus.ROUNDONE);
        }
        if (status == GameStatus.ROUNDTWO) {
            roundTwo();
        }
        if (status == GameStatus.ROUNDTHREE) {
            roundThree();
        }
        if (status == GameStatus.CHECK) {
            System.out.println(startRoundMessage(GameStatus.CHECK));
            System.out.println(gameEnd());
        }
    }

    private void roundOne(GameStatus round) throws Exception {

        playingCards.shuffleCards();
        int roundHighest = sBlindBets;

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

        System.out.println(startRoundMessage(round));
        System.out.println(displayGameTable(round));

        playerLoops(round, roundHighest);
    }

    private void roundTwo() throws Exception {
        //false safe
        if (oneAndOnly()) {
            status = GameStatus.CHECK;
            return;
        }

        deskCards.changeCardFaceAt(3);
        System.out.println(startRoundMessage(GameStatus.ROUNDTWO));
        System.out.println(displayGameTable(GameStatus.ROUNDTWO));

        playerLoops(GameStatus.ROUNDTWO, 0);
    }

    private void roundThree() throws Exception {
        //false safe
        if (oneAndOnly()) {
            status = GameStatus.CHECK;
            return;
        }

        deskCards.changeCardFaceAt(4);
        System.out.println(startRoundMessage(GameStatus.ROUNDTHREE));
        System.out.println(displayGameTable(GameStatus.ROUNDTHREE));

        playerLoops(GameStatus.ROUNDTHREE, 0);
    }

    private void checkCards() {
        System.out.println(displayGameTable(GameStatus.CHECK));
        ArrayList<Card> cards = new ArrayList<>();
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

    private void playerLoops(GameStatus round, int roundHighest) throws Exception {
        int count = 0;
        int activePlayersCount = 0;
        for (Player player: players){
            if (player.isActive()) {
                activePlayersCount++;
            }
        }
        while (count < activePlayersCount || currentPlayer.getRoundCredit(round) != roundHighest) {
            //big blind bets
            if (count++ == 1 && round == GameStatus.ROUNDONE) {
                if (roundHighest < sBlindBets * 2) {
                    roundHighest = sBlindBets * 2;
                }
            }
            //check active
            if (oneAndOnly()) {
                status = GameStatus.CHECK;
                break;
            }
            if (!currentPlayer.isActive()) {
                currentPlayer = getNextPlayer();
                continue;
            }
            //start
            debugger.getInput("Pass to player " + currentPlayer.getINDEX() + "\n enter to continue.\n\n\n\n\n\n\n"); //todo remove debug
            if (status == GameStatus.BREAK) {
                break;
            }
            System.out.println(displayGameTable(round));
            currentPlayer.playRound(roundHighest, round);
            if (!currentPlayer.isActive()) {
                currentPlayer = getNextPlayer();
                activePlayersCount--;
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                continue;
            }
            roundHighest = currentPlayer.getRoundCredit(round);

            currentPlayer = getNextPlayer();
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

            deskCards.setTotalCredit(getPoolCredit()); //todo can be better
        }

        if (currentPlayer.getRoundCredit(round) == roundHighest && currentPlayer.isActive()) {

            switch (status) {
                case ROUNDONE:
                    status = GameStatus.ROUNDTWO;
                    break;
                case ROUNDTWO:
                    status = GameStatus.ROUNDTHREE;
                    break;
                case ROUNDTHREE:
                    status = GameStatus.CHECK;
                    break;
            }
        }
    }

    private int getPoolCredit() { //todo can be better
        int cr = 0;
        for (Player player : players) {
            cr += player.getRoundCredit(GameStatus.CHECK);
        }
        return cr;
    }

    public void setsBlindBets(int sBlindBets) throws IllegalArgumentException{
        if (sBlindBets < 0 || sBlindBets > 0xf3f3f3) {
            throw new IllegalArgumentException("Hold\'emGame: set blind bet failed " + sBlindBets);
        }
        this.sBlindBets = sBlindBets;
    }

    String displayGameTable(GameStatus round) {
        String outStr = "";
        if (round == GameStatus.DEBUG) {
            outStr += deskCards.viewCard();
            for (Player player : players) {
                outStr += player.viewCard();
            }

            return outStr + "\n";
        }

        if (round == GameStatus.CHECK || round == GameStatus.BREAK) {

            for (Player player : players) {
                if (round == GameStatus.BREAK && player.equals(currentPlayer)) {
                    outStr += "🔘";
                } else {
                    outStr += " ";
                }
                outStr += player.viewPlayer(round);
            }

            if (round == GameStatus.BREAK) {
                outStr += "\nBlind bet: " + sBlindBets;
            }

            return outStr + "\n";
        }

        outStr += ("Round " + round.ordinal() + "\n");
        outStr += deskCards.viewPlayerCard();

        for (Player player : players) {
            outStr += player.viewPlayer(round);
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
        if (currentPlayer.getINDEX() + 1 > players.size()) {
            // index 0
            return players.get(0);
        } else {
            // index ++
            return players.get(currentPlayer.getINDEX());
        }
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
            player.setCardPoint(cardCheck.getCP(result.VALUE, result.CARDS));

            outStr += ("Player " + player.getINDEX() + " score: " + player.getCardPoint() + "\n");
            for (Card card : player.getCards()) {
                outStr += (card.toString() + " ");
            }
            outStr += "+ ";
            for (Card card: deskCards.getCards()) {
                outStr += (card.toString() + " ");
            }
            outStr += "\nbest sets: "+result.VALUE.name()+" best five cards:\n";
            for (Card card: result.CARDS) {
                outStr += (card.toString() + " ");
            }
            outStr += "\n\n";
        }

        remainingPlayers.sort(null);

        ArrayList<Player> winningPlayers = new ArrayList<>();


        winningPlayers.add(remainingPlayers.get(0));
        for (int i = 0; i < remainingPlayers.size() - 1; i++) {
            if (remainingPlayers.get(i).getCardPoint().equals(remainingPlayers.get(i + 1).getCardPoint())) {
                winningPlayers.add(remainingPlayers.get(i + 1));
            }
        }

        if (winningPlayers.size() == 1) {
            outStr += (winningPlayers.get(0).getName() + " (Player " + winningPlayers.get(0).getINDEX() + ") won\n");
            remainingPlayers.get(0).setCredit(deskCards.getCredit());
            deskCards.setTotalCredit(0);
        } else {
            outStr += ("Multiple winner!\n");
            for (Player player : winningPlayers) {
                outStr += (player.getName() + " Player " + player.getINDEX() + ")\n"); //todo make it better
                outStr += player.viewPlayerCard() + "\n";
                player.setCredit(deskCards.getCredit() / winningPlayers.size());
            }
            deskCards.setTotalCredit(0);
        }

        outStr += displayGameTable(GameStatus.CHECK);

        return outStr;
    }

    private ArrayList<Card> combineCards(Player player) {
        ArrayList<Card> newCards = new ArrayList<>(this.deskCards.getCards());
        newCards.addAll(player.getCards());
        return newCards;
    }

    private String startRoundMessage(GameStatus round) {
        String str = "";
        switch (round) {
            case ROUNDONE:
                str += "=========================================" + "\n" +
                        "====       =================   ==========" +"\n" +
                        "====  =====  ==============    ==========" +"\n" +
                        "====  ======  ============  =  ==========" +"\n" +
                        "====  =======  ==========  ==  ==========" +"\n" +
                        "====  ======  ===============  ==========" +"\n" +
                        "====  ====  =================  ==========" +"\n" +
                        "====      ===================  ==========" +"\n" +
                        "====   ======================  ==========" +"\n" +
                        "====  =  ====================  ==========" +"\n" +
                        "====  ===  ==================  ==========" +"\n" +
                        "====  ====  =================  ==========" +"\n" +
                        "====  =====  ================  ==========" +"\n" +
                        "====  ======  ===============  ==========" +"\n" +
                        "====  =======  ============      ========" +"\n" +
                        "=========================================";

                break;
            case ROUNDTWO:
                str += "=========================================" + "\n" +
                        "====       =============       ==========" +"\n" +
                        "====  =====  =========  ======  =========" +"\n" +
                        "====  ======  =======  ========  ========" +"\n" +
                        "====  =======  =================  =======" +"\n" +
                        "====  ======  =================  ========" +"\n" +
                        "====  ====  ==================  =========" +"\n" +
                        "====      ===================  ==========" +"\n" +
                        "====   =====================  ===========" +"\n" +
                        "====  =  ==================  ============" +"\n" +
                        "====  ===  ===============  =============" +"\n" +
                        "====  ====  =============  ==============" +"\n" +
                        "====  =====  ===========  ===============" +"\n" +
                        "====  ======  =========  ================" +"\n" +
                        "====  =======  =======           ========" +"\n" +
                        "=========================================";
                break;
            case ROUNDTHREE:
                str += "=========================================" + "\n" +
                        "====       =============       ==========" +"\n" +
                        "====  =====  =========  ======  =========" +"\n" +
                        "====  ======  =======  ========  ========" +"\n" +
                        "====  =======  =================  =======" +"\n" +
                        "====  ======  =================  ========" +"\n" +
                        "====  ====  ==================  =========" +"\n" +
                        "====      ===================  ==========" +"\n" +
                        "====   =====================  ===========" +"\n" +
                        "====  =  ====================  ==========" +"\n" +
                        "====  ===  ===================  =========" +"\n" +
                        "====  ====  ===================  ========" +"\n" +
                        "====  =====  ========  =========  =======" +"\n" +
                        "====  ======  ========  =======  ========" +"\n" +
                        "====  =======  ========         =========" +"\n" +
                        "=========================================";
                break;
            case CHECK:
                str += "Game FIN!";
                break;
        }
        return str;
    }
}
