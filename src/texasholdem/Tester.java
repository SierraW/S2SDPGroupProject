package texasholdem;

public class Tester {
    public static void main(String[] args) throws Exception{
        CommandPrompt game = new CommandPrompt();
        InputHandleSystem input = new InputHandleSystem();

        System.out.println("Welcomes Message");

        while (!game.isEnded()) {
            game.comm(input.getLine("game:\\"));
        }

    }
}
