package texasholdem;

public class MainGame {
    public static void main(String[] args) throws Exception{
        CommandPrompt game = new CommandPrompt();
        InputHandleSystem input = new InputHandleSystem();

        System.out.println("Texas Hold'em\ntype \"set [numbersOfPlayers]\" to set the amount of players (default: 4)\nthen type \"srun start\" to run. Or type \"help\" for more command.\nrules can be found here: https://en.wikipedia.org/wiki/Texas_hold_%27em#Rules");
        game.viewGameTable();
        while (!game.isEnded()) {
            game.comm(input.getLine("game:\\"));
        }

    }
}
