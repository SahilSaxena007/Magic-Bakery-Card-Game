package bakery;
import java.io.Console;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.management.openmbean.OpenDataException;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedList;
import util.CardUtils;
import util.ConsoleUtils;
import util.StringUtils;
import util.collectionFunctions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable; 
/**
 * Represents a magical bakery game
 * The {@code MagicBakery} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class MagicBakery implements Serializable {
    /** Attributes */
    private String ingredientDeckFile; 
    private String layerDeckFile;
    private String customerDeckFile;
    /**
     * The serial version UID for serialization and deserialization.
     * This is used to ensure that the serialized and deserialized objects
     * are compatible with the class definition.
     */
    private static final long serialVersionUID = 1l; 
    
    /**
     * The collection of customers
     */
    private Customers customers;

    /**
     * The collection of layers
     */
    private Collection<Layer> layers;

    /**
     * The collection of players
     */
    private Collection<Player> players;

    /**
     * The collection of ingredients in the pantry
     */
    private Collection<Ingredient> pantry;

    /**
     * The collection of ingredients in the discarded pantry
     */
    private Collection<Ingredient> pantryDiscard;
    
    /**
     * The collection of ingredients in the pantry deck
     */
    private Collection<Ingredient> pantryDeck;

    /**
     * The random number generator
     */
    private Random random;

    /** Index location of the current player*/
    public int currentPlayerLoc;

    /** List of players and their individual action count */
    public List<Integer> playerActionCount;

    /** List of Ingredients */
    public List<Ingredient> ingredients;
    
    /**
     * Enum representing the type of action in the game
     */
    public enum ActionType{ 
        DRAW_INGREDIENT,
        PASS_INGREDIENT,
        BAKE_LAYER,
        FULFIL_ORDER,
        REFRESH_PANTRY
    }

    /**
     * Constructs a magic bakery game with the given seed and the deck file names.
     * 
     * @param seed the seed for randomization
     * @param ingredientDeckFileIn the file name for ingredient deck
     * @param layerDeckFileIn the file name for layer deck
     * @throws FileNotFoundException if the specified ingredient deck file is not found
     * @throws FileNotFoundException if the specified layer deck file is not found
     */
    public MagicBakery(long seed, String ingredientDeckFileIn, String layerDeckFileIn) throws FileNotFoundException {
        players = new ArrayList<Player>();

        if (!ingredientDeckFileIn.equals("./io/ingredients.csv")) {
            throw new FileNotFoundException("Ingredient deck file not found:" + ingredientDeckFileIn);
        }

        if (!layerDeckFileIn.equals("./io/layers.csv")) {
            throw new FileNotFoundException("Layer deck file not found: " + layerDeckFileIn);
        }
        this.pantry = new ArrayList<>();
        this.pantryDiscard = new ArrayList<>();
        this.pantryDeck = new ArrayDeque<>();
        this.ingredientDeckFile = ingredientDeckFileIn;
        this.random = new Random(seed);
        this.layers = CardUtils.readLayerFile(layerDeckFileIn);
        List<CustomerOrder> customerOrders = CardUtils.readCustomerFile ("io/customers.csv", layers);
        List<Ingredient> ingredients = CardUtils.readIngredientFile(ingredientDeckFileIn);
        this.pantryDeck.addAll(ingredients);

    }

    /**
     * Bakes the specified layer if it can be baked with the current ingredients
     * 
     * @param layer the layer to bake
     * @throws TooManyActionsException if the player has already used all available actions
     * @throws WrongIngredientsException if the player does not have the required ingredients to bake the layer
     */
    public void bakeLayer(Layer layer) throws TooManyActionsException, WrongIngredientsException
    {
        // Check if the player has enough actions remaining
        if (getActionsRemaining() <= 0) {
            throw new TooManyActionsException();
        }

        // Check if the player has the required ingredients to bake the layer
        List<Ingredient> hand = getCurrentPlayer().getHand();
        if (!layer.canBake(hand)) {
            throw new WrongIngredientsException("Player does not have the required ingredients to bake the layer");
        }

        // Perform the baking process
        pantryDiscard.addAll(layer.getRecipe());
        for (Ingredient item : layer.getRecipe()) {
            getCurrentPlayer().removeFromHand(item);
        }
        getCurrentPlayer().addToHand(layer);
        layers.remove(layer);
        playerActionCount.set(currentPlayerLoc, playerActionCount.get(currentPlayerLoc) - 1);
    }

    /**
     * Draws and ingredient from the pantry deck
     * 
     * @return an ingredient from the pantry deck
     * @throws EmptyPantryException if both the pantry deck and pantry discard are empty
     */
    private Ingredient drawFromPantryDeck() throws EmptyPantryException
    {
        if (pantryDeck.isEmpty() && pantryDiscard.isEmpty()) {
            throw new EmptyPantryException();
        }
        
        if (pantryDeck.isEmpty()) {
            shufflePantryDeck();
        }
        
        Deque<Ingredient> deque = new LinkedList<>(pantryDeck);
        Ingredient drawnCard = deque.pollFirst();
        pantry.add(drawnCard);
        return drawnCard;
        
    }

    /**
     * Draws an ingredient from the pantry and adds it to the player's hand
     * 
     * @param ingredientName the ingredient to draw from the pantry
     * @throws WrongIngredientsException if the specified ingredient is not available in the pantry
     * @throws TooManyActionsException if the player has already used all available actions or available actions are more than the actions permitted for the player
     */
    public void drawFromPantry(String ingredientName) throws WrongIngredientsException, TooManyActionsException
    {
        Ingredient ingredient = null;
        for (Ingredient item : pantry) {
            if (item.toString().equalsIgnoreCase(ingredientName.trim())) {
                ingredient = item;
                break;
            }
        }
        
        if (ingredient == null) {
            throw new WrongIngredientsException();
        }

        if (getActionsRemaining() <= 0) {
            throw new TooManyActionsException();
        }

        if (getActionsRemaining()  > getActionsPermitted()){
            throw new TooManyActionsException();
        }
        

        pantry.remove(ingredient);
        getCurrentPlayer().addToHand(ingredient);
        drawFromPantryDeck();
        playerActionCount.set(currentPlayerLoc, playerActionCount.get(currentPlayerLoc) - 1);
    }

    /**
     * Draws an ingredient from the pantry and adds it to the player's hand
     * 
     * @param ingredient the ingredient to draw from the pantry
     * @throws WrongIngredientsException if the specified ingredient is not available in the pantry
     * @throws TooManyActionsException if the player has already used all available actions or available actions are more than the actions permitted for the player
     */
    public void drawFromPantry(Ingredient ingredient) throws WrongIngredientsException, TooManyActionsException
    {
        if (ingredient == null) {
            throw new WrongIngredientsException();
        }

        if (getActionsRemaining() <= 0) {
            throw new TooManyActionsException();
        }

        if (getActionsRemaining()  > getActionsPermitted()){
            throw new TooManyActionsException();
        }
        pantry.remove(ingredient);
        getCurrentPlayer().addToHand(ingredient);
        drawFromPantryDeck();
        playerActionCount.set(currentPlayerLoc, playerActionCount.get(currentPlayerLoc) - 1);
    }

    /**
     * Ends the current player's turn and moves to the next player
     * 
     * @return true if the turn was successfully completed, false otherwise
     */
    public boolean endTurn()
    {
        if (currentPlayerLoc == (players.size() - 1))
        {
            currentPlayerLoc = 0;
            System.out.println("*NEW ROUND*");
            System.out.println();
            resetAction();
        }       
        else{
            currentPlayerLoc = currentPlayerLoc + 1;
        }
        return true;
    }

    /**
     * Bakes the specified Customer Order if it can be baked with the current ingredients
     * 
     * @param customer the CustomerOrder to be baked
     * @param garnish boolean value for Garnish
     * @return the list of ingredients of the order fulfilled
     */
    public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish)
    {
        return null;
    }

    /**
     * Returns the number of actions permitted for the players
     * 
     * @return the number of actions permitted
     */
    public int getActionsPermitted()
    {
        int numberOfPlayers = players.size();
        if (numberOfPlayers <= 3 && numberOfPlayers > 0)
        {
            return 3;
        }    
        else if (numberOfPlayers <= 5 && numberOfPlayers > 3)
        {
            return 2;
        }
        return -1;
    }

    /**
     * Returns the number of actions remaining for the current player
     * 
     * @return the number of actions remaining
     */
    public int getActionsRemaining()
    {
        // Returns the actions remaining for the current player
        return playerActionCount.get(currentPlayerLoc);
    }

    /**
     * Returns a collection of bakeable layers based on the current player's hand
     * 
     * @return the collection of bakeable layers
     */
    public Collection<Layer> getBakeableLayers()
    {
        List<Layer> bakeableLayers = new ArrayList<Layer>();
        List<Ingredient> hand = collectionFunctions.getElementAt(players, currentPlayerLoc).getHand();
        for (Layer layer: layers)
        {
            boolean canBake = layer.canBake(hand);
            if (canBake && !bakeableLayers.contains(layer))
            {
                bakeableLayers.add(layer);
            }
        }
        return bakeableLayers;
    }

    /**
     * Returns the current player
     * 
     * @return the current player
     */
    public Player getCurrentPlayer()
    {
        Player currentPlayer = collectionFunctions.getElementAt(players, currentPlayerLoc);               
        return currentPlayer;
    }

    /**
     * Return the customers
     * 
     * @return the customers
     */
    public Customers getCustomers()
    {
        return this.customers;
    }

    /**
     * Returns a collection of Customer orders that can be fulfilled
     * 
     * @return the collection of fulfillable customer orders
     */
    public Collection<CustomerOrder> getFulfilableCustomers()
    {
        return null;
    }

    /**
     * Returns a collection of Customers that can be garnished
     * 
     * @return the collection of garnishable customers
     */
    public Collection<CustomerOrder> getGarnishableCustomers()
    {
        return null;
    }
    
    /**
     * Returns the Layers
     * 
     * @return the layers
     */
    public Collection<Layer> getLayers()
    {
        List<Layer> layersList = new ArrayList<Layer>();
        for (Layer layer: layers){
            if (!layersList.contains(layer)){
                layersList.add(layer);
            }
        }
        return layersList;
    }

    /**
     * Returns the pantry
     * 
     * @return the pantry
     */
    public Collection<Ingredient> getPantry()
    {
        return pantry;
    }

    /**
     * Returns the players
     * 
     * @return the players
     */
    public Collection<Player> getPlayers()
    {
        return players;
    }

    /**
     * Loads the game state for the specified file
     * 
     * @param file the file to load the state from 
     * @return the magic bakery instance loaded from the file
     * @throws IOException if there is an I/O error while reading from the file
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public static MagicBakery loadState(File file) throws IOException, ClassNotFoundException
    {
        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = objectStream.readObject();
        if (obj instanceof MagicBakery) {
            return (MagicBakery) obj;
        } else {
            throw new ClassCastException("Invalid data format");
        }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (ObjectStreamException e) {
            throw e;
        }
    }
    
    /**
     * Passes an ingredient from the current player to another player.
     * 
     * @param ingredient the ingredient to pass
     * @param recipient the recipent player
     * @throws WrongIngredientsException If the current player does not have the specified ingredient in hand.
     * @throws TooManyActionsException If the current player has used all their actions for this turn.
     */
    public void passCard(Ingredient ingredient, Player recipient) throws WrongIngredientsException, TooManyActionsException
    {
            // Remove the particular Ingredient from the current player and give it to the other player
            // Check if the current player has the ingredient in hand
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.getHand().contains(ingredient))
            {
                currentPlayer.removeFromHand(ingredient);
                recipient.addToHand(ingredient);
            }
            else
            {
                throw new WrongIngredientsException("The current player does not have the specified ingredient in hand.");
            }
            
            // Reduce the action count of the current player
            playerActionCount.set(currentPlayerLoc, playerActionCount.get(currentPlayerLoc) - 1);
            
            // Check if the current player has any actions remaining
            if (playerActionCount.get(currentPlayerLoc) < 0)
            {
                throw new TooManyActionsException();
            }
        
    }

    /**
     * Prints the customer Service record
     */
    public void printCustomerServiceRecord()
    {
        
    }

    /**
     * Prints the current game state including layers and ingredients.
     */
    public void printGameState() {
        if (layers == null || pantry == null) {
            System.out.println("Game state is not initialized.");
            return;
        }
    
        List<String> layersStrings = StringUtils.layersToStrings(layers);
        List<String> ingredientsStrings = StringUtils.ingredientsToStrings(pantry);
        System.out.println("Layers in the game:");
        for (String layerString : layersStrings) {
            System.out.println(layerString);
        }
        System.out.println("Ingredients in the pantry:");
        for (String ingredientString : ingredientsStrings) {
            System.out.println(ingredientString);
        }
    }

    /**
     * Refreshed the pantry by moving ingredients from the discarded pantry
     */
    public void refreshPantry()
    {
        if (getActionsRemaining() > 0) {
            pantryDiscard.addAll(pantry);
            pantry.clear();
    
            if (!pantryDeck.isEmpty()) {
                shufflePantryDeck();
            } else {
                shuffle();
            }
    
            for (int i = 1; i <= 5; i++) {
                Ingredient ingredient = drawFromPantryDeck();
            }
    
            playerActionCount.set(currentPlayerLoc, playerActionCount.get(currentPlayerLoc) - 1);
        } else {
            throw new TooManyActionsException();
        }
        
    }

    /**
     * Saves the current game state to the specified file
     * 
     * @param file the file to save the state to
     */
    public void saveState(File file)
    {
        try (ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Shuffles the pantry deck
     * 
     */
    private void shufflePantryDeck()
    {
        if (!pantryDiscard.isEmpty()) {
            pantryDeck.addAll(pantryDiscard);
            pantryDiscard.clear();
            // Shuffle pantryDeck
            List<Ingredient> tempList = new ArrayList<>(pantryDeck); 
            Collections.shuffle(tempList, this.random);
            pantryDeck.clear();
            for (Ingredient item: tempList){pantryDeck.add(item);}
        }
    }

    /**
     * Shuffles the pantry deck
     * 
     */
    private void shuffle()
    {
        // Add the shuffled elements back to the original 
        for (int i = pantryDeck.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Ingredient temp = collectionFunctions.getElementAt(pantryDeck, i);
            collectionFunctions.setElementAt(pantryDeck, i,collectionFunctions.getElementAt(pantryDeck, j));
            collectionFunctions.setElementAt(pantryDeck, j, temp);
        }
    }

    /**
     * Starts the game with the given player names and the customer deck file
     * 
     * @param playerNames the names of the players
     * @param customerDeckFile the file name for the customer deck
     * @throws FileNotFoundException if the specified customer deck file is not found
     * @throws IllegalArgumentException  if the number of players is less than 2 or greater than 5
     */
    public void startGame(List<String> playerNames, String customerDeckFile) throws FileNotFoundException, IllegalArgumentException
    {
        if (customerDeckFile != "./io/customers.csv"){
            throw new FileNotFoundException();
        }

        if (playerNames.size() < 2){
            throw new IllegalArgumentException();
        }

        if (playerNames.size() > 5){
            throw new IllegalArgumentException();
        }

        // Instantiating the player names
       int playerCount = 0;
        for (String player: playerNames)
        {
            players.add(new Player(player));
            playerCount = playerCount + 1;  
        }

        // Instantiating a new Customer object
        this.customers = new Customers(customerDeckFile, this.random, this.layers, playerCount);

        // Reading the CustomerOrder file
        List<CustomerOrder> CustomerOrders = CardUtils.readCustomerFile(customerDeckFile, this.layers);
        
        // Reading the Pantry
        List<Ingredient> Deck = CardUtils.readIngredientFile(ingredientDeckFile);
        Collections.shuffle(Deck, random);
        
        
        
        LinkedList<Ingredient> tempPantryDeck = new LinkedList<>(Deck);


        // Adding the CustomerOrder based on the number of players
        if (playerCount == 3 || playerCount == 5){
            for (int i = 1; i <= 2; i++){customers.addCustomerOrder();}
        }else{
            customers.addCustomerOrder();
        }
        
        for (int j = 1; j <= 5; j++){
            Ingredient val = tempPantryDeck.pollLast();
            pantry.add(val);
        }

        for (Player player: players){
            for (int i = 0; i < 3; i++){
                if (!tempPantryDeck.isEmpty()){
                    Ingredient drawnCard = tempPantryDeck.pollLast();
                    player.addToHand(drawnCard);
                }
            }
        }

        // Setting the index of the First Player
        currentPlayerLoc = 0;

        // Calling the Function that resets playerActionCount
        playerActionCount = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            playerActionCount.add(getActionsPermitted());
        }
        this.pantryDeck.clear();
        this.pantryDeck.addAll(tempPantryDeck);

    }


    /**
     * Resets the number of actions allowed for each player.
     * This method sets the number of actions permitted for each player to the value
     * determined by the current number of players in the game.
     * If the number of players is between 1 and 3 (inclusive), each player is permitted 3 actions.
     * If the number of players is between 4 and 5 (inclusive), each player is permitted 2 actions.
     * If the number of players is outside these ranges, no action is taken.
     * 
     */
    private void resetAction()
    {
        // Resets the number of actions for each Player
        int actionsVal = getActionsPermitted();
        for (int i = 0; i < players.size(); i++)
        {
            if (actionsVal != -1)
            {
                playerActionCount.set(i, actionsVal);
            }
            
        }
    }

}

 