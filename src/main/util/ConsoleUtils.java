package util;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import bakery.CustomerOrder;
import bakery.Ingredient;
import bakery.Layer;
import bakery.MagicBakery;
import bakery.Player;
import bakery.MagicBakery.ActionType;


/**
 * Utility class for handling console input/output operations.
 * 
 * The {@code ConsoleUtils} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0 
 */
public class ConsoleUtils {
    /**
     * Console Object
     */
    private Console console;


    /**
     * Constructs a new ConsoleUtils object
     * 
     * Initializes the console attribute with the system console object obtained from system.console().
     */
    public ConsoleUtils() {
        console = System.console();
    }


    /**
     * Reads a line of input from the console.
     * 
     * @return the input string
     */
    public String readLine() {
        return console.readLine();
    }


    /**
     * Reads a line of input from the console with a formatted prompt.
     * 
     * @param fmt  the format string for the prompt
     * @param args arguments referenced by the format specifiers in the format string
     * @return the input string
     */
    public String readLine(String fmt, Object... args) {
        String prompt = String.format(fmt, args);
        return console.readLine(prompt);
    }



    /**
     * Prompts the user to choose an action from the available actions.
     * 
     * @param prompt the prompt message
     * @param bakery the MagicBakery instance
     * @return the chosen action
     */
    public ActionType promptForAction(String prompt, MagicBakery bakery) {
        // Display available actions
        int count = 1;
        List<ActionType> actionType = new ArrayList<ActionType>();
        for (ActionType action : ActionType.values()) {
            if (action.toString() == "BAKE_LAYER" && bakery.getBakeableLayers().isEmpty())
            {
                continue;                
            }

            if (action.toString() == "FULFIL_ORDER" )
            {
                continue;
            }

            System.out.println("   " + count + ". " + action.toString());
            actionType.add(action);
            count = count + 1;
        }


        int choice;
        while (true) {
            String input = console.readLine("(Enter the Number of Choice)");
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= actionType.size()) {
                    break;
                } else {
                    System.out.println("Invalid Input! Enter a number within range");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input! Enter a number");
            }
        }


        return actionType.get(choice - 1);
    }


    /**
     * Prompts the user to choose a customer from the list of customers.
     * 
     * @param prompt    the prompt message
     * @param customers the list of customers
     * @return the chosen customer
     */
    public CustomerOrder promptForCustomer(String prompt, Collection<CustomerOrder> customers) {
        return (CustomerOrder) promptEnumerateCollection(prompt, new ArrayList<>(customers));
    }


    /**
     * Prompts the user to choose an existing player from the list of players.
     * 
     * @param prompt the prompt message
     * @param bakery the MagicBakery instance
     * @return the chosen player
     */
    public Player promptForExistingPlayer(String prompt, MagicBakery bakery) {
        // Conditional function to check the number of players
        Collection<Player> players = bakery.getPlayers();
        List<Player> players2 = new ArrayList<>(players);
        int numberOfPlayers = bakery.getPlayers().size();
        int currentPlayerLoc = bakery.currentPlayerLoc;
        if (numberOfPlayers == 2)
        {
           // Checking which one of the two is the current active player
           Player currentPlayer = bakery.getCurrentPlayer();
           if (currentPlayer.equals(players2.get(0)))
           {
               // Setting the currentPlayerLoc
               // bakery.currentPlayerLoc = 1;
               return players2.get(1);
           }
           else
           {
               // Setting the currentPlayerLoc
               // bakery.currentPlayerLoc = 0;
               return players2.get(0);
           }
           

        }
        else {
           Player chosenPlayer = (Player) promptEnumerateCollection("Enter the choice of Player", new ArrayList<>(players2));
           return  chosenPlayer;          
        }
    }


    /**
     * Prompts the user to enter a file path.
     * 
     * @param prompt the prompt message
     * @return the file specified by the path
     */
    public File promptForFilePath(String prompt) {
        String response = console.readLine(prompt);
        response = response.trim().toLowerCase();  
        File file;
        if (response.equals("customers") || response.equals("customer"))
        {
            file = new File("io/customers.csv");            
        }
        else if(response.equals("ingredients") || response.equals("ingredient"))
        {
            file = new File("io/ingredients.csv");
        }
        else if(response.equals("layer") || response.equals("layers"))
        {
            file = new File("io/layers.csv");
        }
        else {
            System.out.println("Invalid input. Please enter 'customers', 'ingredients', or 'layers'.");
            file = promptForFilePath(prompt); // Recursive call to prompt again
        }
    
        return file;
    }


    /**
     * Prompts the user to choose an ingredient from the list of ingredients.
     * 
     * @param prompt      the prompt message
     * @param ingredients the list of ingredients
     * @return the chosen ingredient
     */
    public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients) {
        if (ingredients.size() == 1)
        {
            // Returning the Only Ingredient there is
            return new ArrayList<>(ingredients).get(0);
        }
        else {
            Ingredient chosenIngredient = (Ingredient) promptEnumerateCollection("Enter the choice of Ingredient ",new ArrayList<>(ingredients));
            return  chosenIngredient;
        }
    }

    
    /**
     * Prompts the user to enter names for new players.
     * 
     * @param prompt the prompt message
     * @return the list of names for new players
     */
    public List<String> promptForNewPlayers(String prompt) {
        List<String> listOfPlayers = new ArrayList<String>();
        // Prompt for the first player
        String playerName = console.readLine("Player 1 name? ");
        listOfPlayers.add(playerName);
        // Prompt for the second player
        playerName = console.readLine("Player 2 name? ");
        listOfPlayers.add(playerName);
        int playerNumber = 3; // Start with player 3
        // Prompt for additional players
        while (true) {
            if (!promptForYesNo("Add another player? Yes/No")) {
                break;
            }
            playerName = console.readLine("Player " + playerNumber + " name? ");
            listOfPlayers.add(playerName);
            playerNumber++;
        }
        return listOfPlayers;
    }


    /**
     * Prompts the user to start or load the game.
     * 
     * @param prompt the prompt message
     * @return true if the user chooses to start, false if the user chooses to load
     */
    public boolean promptForStartLoad(String prompt) {
        String response = console.readLine(prompt);
        response.trim().toLowerCase(); 
        if (response.equals("start"))   
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Prompts the user for a yes or no response.
     * 
     * @param prompt the prompt message
     * @return true if the user responds with "yes", false otherwise
     */
    public boolean promptForYesNo(String prompt) {
        String response = console.readLine(prompt + " (Yes/No): ");
        return response.trim().equalsIgnoreCase("yes");
    }

    /**
     * Prompts the user to choose an ingredient from the list of ingredients.
     * 
     * @param prompt the prompt message
     * @param layers the list of layer
     * @return Layer that was chosen
     */
    public Layer promptForLayer(String prompt, List<Layer> layers) {
        if (layers.size() == 1)
        {
            // Returning the Only Ingredient there is
            return layers.get(0);
        }
        else {
            Layer chosenLayer = (Layer) promptEnumerateCollection("Enter the choice of Ingredient ", new ArrayList<Object>(layers));
            return  chosenLayer;
        }
    }

    /**
     * To choose from the enumerated collection
     * 
     * @param prompt the prompt message
     * @param collection the collection to enumerate
     * @return the object chosen from the collection
     */
    private Object promptEnumerateCollection(String prompt, Collection<Object> collection) {
        // Display the list of items to choose from
        System.out.println("List to choose from:");
        for (int i = 0; i < collection.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + collectionFunctions.getElementAt(collection, i).toString());
        }


        // Prompt the user to select an item
        int choice;
        while (true) {
            String input = console.readLine(prompt + " (Enter the number of your choice): ");
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= collection.size()) {
                    break; // Valid input
                } else {
                    System.out.println("Invalid input. Please enter a number within the range.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }


        // Return the selected item
        return new ArrayList<>(collection).get(choice - 1);
    }
}





