import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List; 
import util.collectionFunctions;
import util.CardUtils;
import bakery.*;
import util.ConsoleUtils;
import bakery.MagicBakery.ActionType; 
public class BakeryDriver {

    public BakeryDriver() {
    }

    public static void main(String[] args)  {
        System.out.println("Welcome to the Magic Bakery.");
        System.out.print("Ready? Set ... ");
        System.out.println("*BAKE*");
        System.out.println();
        
        
        
        /* Generating Objects of required Classes */
        // Creating the MagicBakery Instance
        MagicBakery magicBakery = new MagicBakery(10,"io/ingredients.csv","io/layers.csv");
        
        // A ConsoleUtil object created
        ConsoleUtils consoleUtils = new ConsoleUtils();
        File customerDeckFile = consoleUtils.promptForFilePath("Enter the Customer File Path:");

        
        // Prompt for Player Names
        List<String> playerNames = consoleUtils.promptForNewPlayers("Let's get started! Who's playing?\n");

        
        // Starting the Game
        magicBakery.startGame(playerNames, customerDeckFile.toString());
        // Generating the required Loop
        for (int i = 1; i <= 20; i++)
        {
            // magicBakery.printGameState();
            // Printing the Current Player Name
            System.out.println(magicBakery.getCurrentPlayer().getName() + " it is your turn. Your Hand Contains: " + magicBakery.getCurrentPlayer().getHandStr());
            System.out.println("You have " + magicBakery.getActionsRemaining() + " actions remaining. What do you want to do?: ");

            ActionType action = consoleUtils.promptForAction(null, magicBakery);
            System.out.println();
            
            if (action.toString() == "DRAW_INGREDIENT")
            {
                // Drawing a chosen Ingredient from the pantry
                Collection<Ingredient> ingredients = magicBakery.getPantry();
                List<Ingredient> ingredients2 = new ArrayList<>(ingredients);
                Ingredient choice = consoleUtils.promptForIngredient(null, ingredients2);
                magicBakery.drawFromPantry(choice);
                
            }else if (action.toString() == "PASS_INGREDIENT")
            {
                // Conditional to pass the ingredient to another Player
                
                // Choosing the player to Pass the Card to
                Player recipient = consoleUtils.promptForExistingPlayer("Choose a player to Pass a Card to: ", magicBakery);

                // Choose an Ingredient to Pass:
                Ingredient ingredient =  consoleUtils.promptForIngredient("Enter the choice of Ingredient: ", collectionFunctions.getElementAt(magicBakery.getPlayers(), magicBakery.currentPlayerLoc).getHand());
                
                // Passing the Card to the Player
                magicBakery.passCard(ingredient, recipient);

            }else if (action.toString() == "BAKE_LAYER")
            {
                // Baking a layer from a list of Layers
                Collection<Layer> layers = magicBakery.getBakeableLayers();
                List<Layer> layers2 = new ArrayList<>(layers);
                Layer choice = consoleUtils.promptForLayer(null, layers2);
                magicBakery.bakeLayer(choice);

            }else if (action.toString() == "FULFIL_ORDER")
            {

            }else if (action.toString() == "REFRESH_PANTRY")
            {
                // Refreshing the Pantry
                magicBakery.refreshPantry();
            }
            
            // Deciding who the next player is
            if (magicBakery.playerActionCount.get(magicBakery.currentPlayerLoc) == 0)
            {
                magicBakery.endTurn();
            }
            
            

        }



    }



}


            // int count = 1;
            // for (Ingredient item: magicBakery.getCurrentPlayer().getHand())
            // {
            //     System.out.println("[" + count + "] " + item);
            //     count = count + 1;
            // }
            // System.out.print("Choose Ingredient to Pass: ");
            // int val = Integer.parseInt(consoleUtils.readLine());
            // Ingredient item = magicBakery.getCurrentPlayer().getHand().get(val - 1);
            // System.out.println();

            // Passing the card and reducing the turn of the player
            // System.out.println(magicBakery.getCurrentPlayer().getHand().toString());
            // System.out.println(magicBakery.getPlayers().get(r - 1).getHand().toString());
            // magicBakery.passCard(item, magicBakery.getPlayers().get(r - 1));
            // System.out.println(magicBakery.getCurrentPlayer().getHand().toString());
            // System.out.println(magicBakery.getPlayers().get(r - 1).getHand().toString());


            /* 
            System.out.print("Players: ");
            for (int j = 0; j < magicBakery.getPlayers().size(); j++)
            {
                
                System.out.println("[" + (j+1) + "]" +  magicBakery.getPlayers().get(j).getName());                    

            }
            System.out.print("Choose a player to pass Card to: ");
            int r = Integer.parseInt(consoleUtils.readLine());
            */