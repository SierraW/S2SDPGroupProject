package texasholdem;

import java.util.ArrayList;

public class InGameDebugger {
    InputHandleSystem inputHandleSystem = new InputHandleSystem();
    ArrayList<Player> players;

    InGameDebugger(ArrayList<Player> players) {
        this.players = players;
    }

//    public String getInput(String message) throws Exception {
//        switch (inputHandleSystem.getLine(message)) {
//
//        }
//    }


    private void switchCommand(String comm) {
        String[] rawC = comm.split(" ");

        switch (rawC[0]) {
            case "player":
                if (rawC[1].matches("[12][0-9]")) {
                    int pIndex = Integer.parseInt(rawC[1]);
                    if (!(pIndex > players.size() - 1)) {
                        if (rawC[2].equals("cards")) {
                                players.get(pIndex).viewPlayer(GameStatus.ROUNDONE);
                                players.get(pIndex).viewPlayer(GameStatus.ROUNDTWO);
                                players.get(pIndex).viewPlayer(GameStatus.ROUNDTHREE);

                        } else {
                            System.out.println("Bad Command, InGameDebugger switchCommand player num ???");
                        }
                    }
                }
            case "restart":


        }
    }
}
