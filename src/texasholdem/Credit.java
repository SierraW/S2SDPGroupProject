package texasholdem;

public class Credit {
    int total;
    int round;
    private int roundOne;
    private int roundTwo;
    private int roundThree;

    Credit() {
        total = 0;
        round = 0;
        roundOne = 0;
        roundTwo = 0;
        roundThree = 0;
    }

    Credit(int roundOne, int roundTwo, int roundThree) {
        this.roundOne = roundOne;
        this.roundTwo = roundTwo;
        this.roundThree = roundThree;
        this.round = roundOne+ roundTwo + roundThree;
        this.total = this.round;
    }

    int difference(int highestBet, GameStatus round) throws IllegalArgumentException {
        switch (round) {
            case ROUNDONE:
                return highestBet - roundOne;
            case ROUNDTWO:
                return highestBet - roundTwo;
            case ROUNDTHREE:
                return highestBet - roundThree;
            default:
                throw new IllegalArgumentException("InvalidCall");
        }
    }

    boolean place(int highestBet, int amount, GameStatus round) {
        if (round != GameStatus.ROUNDONE && round != GameStatus.ROUNDTWO && round != GameStatus.ROUNDTHREE) {
            return false;
        }

        if (amount == 0) {
            if (difference(highestBet, round) == 0) {
                return true;
            }

            return (total == 0);
        }
        if (amount >= difference(highestBet, round)) {
            if (total - amount < 0) {
                return false;
            }
            //place successful
            total -= amount;
            this.setCreditAt(amount, round);

            return true;
        } else {
            if (total - amount == 0) {
                //place successful
                total -= amount;
                this.setCreditAt(amount, round);

                return true;
            }
        }
        return false;
    }

    void resetRoundCredit() {
        round = 0;
        roundOne = 0;
        roundTwo = 0;
        roundThree = 0;
    }

    int creditAt(GameStatus round) {
        switch (round) {
            case ROUNDONE:
                return roundOne;
            case ROUNDTWO:
                return roundTwo;
            case ROUNDTHREE:
                return roundThree;
            default:
                return this.round;
        }
    }

    private void setCreditAt(int credit, GameStatus round) {
        switch (round) {
            case ROUNDONE:
                roundOne += credit;
                this.round += credit;
                break;
            case ROUNDTWO:
                roundTwo += credit;
                this.round += credit;
                break;
            case ROUNDTHREE:
                roundThree += credit;
                this.round += credit;
        }
    }
}
