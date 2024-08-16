package bakery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * Represents a player in the bakery game.
 * The {@code Player} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 * 
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class Player implements Serializable {
    /**
     * The list of ingredients in the players hand
     */
    private List<Ingredient> hand;
    /**
     * The name of the player
     */
    private String name;
    /**
     * The serial version UID for serialization and deserialization.
     * This is used to ensure that the serialized and deserialized objects
     * are compatible with the class definition.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new player with the given name.
     * 
     * @param nameIn the name of the player
     */
    public Player(String nameIn){
        name = nameIn;
        // I believe that we have to assign three cards to the player from the deck, for the printing of the deck to take place.
        hand = new ArrayList<Ingredient>();
    }
    /**
     * Gets the name of the player.
     * 
     * @return the name of the player
     */
    public String getName()
    {
        return name;
    }
    /**
     * Adds ingredients to the player's hand.
     * 
     * @param ingredients the list of ingredients to add
     */
    public void addToHand(List<Ingredient> ingredients)
    {
        for (Ingredient item: ingredients)
        {
            hand.add(item);
        }
    }
    /**
     * Adds an ingredient to the player's hand.
     * 
     * @param ingredient the ingredient to add
     */
    public void addToHand(Ingredient ingredient)
    {
        hand.add(ingredient);        
    }
    /**
     * Checks if the player has a specific ingredient in their hand.
     * 
     * @param ingredient the ingredient to check for
     * @return true if the player has the ingredient, false otherwise
     */
    public boolean hasIngredient(Ingredient ingredient)
    {
        if (hand.contains(ingredient))
        {
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Removes an ingredient from the player's hand.
     * 
     * @param ingredient the ingredient to remove
     * @throws WrongIngredientsException if the specified ingredient is not found in the player's hand
     */
    public void removeFromHand(Ingredient ingredient) throws WrongIngredientsException
    {
        if (!hand.contains(ingredient)) {
            throw new WrongIngredientsException();
        }
        hand.remove(ingredient);
    }
    /**
     * Gets the list of ingredients in the player's hand.
     * 
     * @return the list of ingredients in the player's hand
     */
    public List<Ingredient> getHand()
    {
        return hand;
    }
    /**
     * Gets a string representation of the ingredients in the player's hand.
     * 
     * @return a string representation of the ingredients in the player's hand
     */
    public String getHandStr(){
        List<String> Hands = new ArrayList<String>();
        String stringHands = "";
        
        // Convert items in the hand list to title case and add them to the Hands list
        for (int i = 0; i < hand.size(); i++) {
            String word = hand.get(i).toString();
            word = word.substring(0, 1).toUpperCase() + word.substring(1);
            Hands.add(word);
        }
        
        // Sort the Hands list alphabetically
        Collections.sort(Hands);
        
        // Iterate through the Hands list to count occurrences and format stringHands
        for (int i = 0; i < Hands.size(); i++) {
            if (!stringHands.contains(Hands.get(i))) {
                int count = Collections.frequency(Hands, Hands.get(i));
                if (count == 1) {
                    stringHands = stringHands + Hands.get(i) + ", ";
                } else {
                    stringHands = stringHands + Hands.get(i) + " (x" + count + "), ";
                }
            }
        }
        
        // Remove the trailing ", " from stringHands
        int lastIndex = stringHands.lastIndexOf(", ");
        if (lastIndex != -1) {
            stringHands = stringHands.substring(0, lastIndex);
        }
        
        return stringHands.trim(); // Trim to remove leading/trailing whitespace
    }
    
    /**
     * Returns a string representation of the player.
     * 
     * @return a string representation of the player
     */
    public String toString()
    {
        return name; 
    }

}


