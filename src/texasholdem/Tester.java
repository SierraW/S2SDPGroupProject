package texasholdem;

public class Tester {
    public static void main(String[] args) throws Exception{
        CommandPrompt game = new CommandPrompt();
        InputHandleSystem input = new InputHandleSystem();

        System.out.println("Texas Hold'em\ntype \"set [numbersOfPlayers]\" type \"srun start\" to run.");
        game.viewGameTable();
        while (!game.isEnded()) {
            game.comm(input.getLine("game:\\"));
        }

    }
}
