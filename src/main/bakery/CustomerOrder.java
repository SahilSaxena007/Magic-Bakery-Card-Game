package bakery;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Collections;
import java.io.Serializable; 
import java.lang.IllegalArgumentException;
/**
 * Represents a customer order in the bakery game, consisting of a name, recipe, garnish, and status.
 * Customer orders are used by players to fulfill customer demands and earn points in the game.
 * 
 * The {@code CustomerOrder} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class CustomerOrder implements Serializable {
    /**
     * List of ingredients for garnishing
     */
    private List<Ingredient> garnish = new ArrayList<Ingredient>();
    /**
     * The level of the order (e.g., difficulty or importance)
     */
    private int level;
    /**
     * The name or description of the order
     */
    private String name;
    /**
     * List of ingredients required for the recipe
     */
    private List<Ingredient> recipe = new ArrayList<Ingredient>();
    /**
     * The status of the order (e.g., waiting, fulfilled, etc.)
     */
    private CustomerOrderStatus status;
    /**
     * The serial version UID for serialization and deserialization.
     * This is used to ensure that the serialized and deserialized objects
     * are compatible with the class definition.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Enumeration representing the possible status of a customer order.
     */
    public enum CustomerOrderStatus{
        WAITING,
        FULFILLED,
        GARNISHED,
        IMPATIENT,
        GIVEN_UP
    }

    /**
     * Constructs a new Customer with name, recipe, garnish, and level
     * 
     * @param nameIn the name of the Customer Order
     * @param recipeIn the list of ingredients in the Order's recipe
     * @param garnishIn the list of ingredients in the garnish
     * @param levelIn the level of the Customer Order
     */
    public CustomerOrder(String nameIn, List<Ingredient> recipeIn, List<Ingredient> garnishIn, int levelIn)
    {
        if (recipeIn == null || recipeIn.isEmpty()){
            throw new WrongIngredientsException();
        }
        name = nameIn;
        recipe = recipeIn;
        garnish = garnishIn;
        level = levelIn;
        status = CustomerOrderStatus.WAITING;
    }

    /**
     * Set's the Customer Order's status as Given Up
     * 
     */
    public void abandon()
    {
        status = CustomerOrderStatus.GIVEN_UP;
    }

    /**
     * Checks if the Customer Order can be fulfilled with the list of Ingredients
     * 
     * @param ingredients the ingredients to check for
     * @return true if the Order can be fulfilled, false otherwise
     */
    public boolean canFulfill(List<Ingredient> ingredients) {
        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
    
        for (Ingredient ingredient : recipe) {
            if (ingredient instanceof Layer) {
                Layer layer = (Layer) ingredient;
                for (Ingredient layerIngredient : layer.getRecipe()) {
                    if (!remainingIngredients.remove(layerIngredient) && !remainingIngredients.remove(Ingredient.HELPFUL_DUCK)) {
                        return false; 
                    }
                }
            } else {
                if (!remainingIngredients.remove(ingredient) && !remainingIngredients.remove(Ingredient.HELPFUL_DUCK)) {
                    return false;
                }
            }
        }
    
        for (Ingredient item : garnish) {
            if (!remainingIngredients.remove(item) && !remainingIngredients.remove(Ingredient.HELPFUL_DUCK)) {
                return false;
            }
        }
    
        return true;
    }
    
    /**
     * No idea what
     * 
     * @param ingredients the ingredients to check for
     * @return true if the order can be Garnished, false otherwise
     */
    public boolean canGarnish(List<Ingredient> ingredients)
    {
        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);

        for (Ingredient garnishIngredient : garnish) {
            if (garnishIngredient instanceof Layer) {
                Layer layer = (Layer) garnishIngredient;
                for (Ingredient layerIngredient : layer.getRecipe()) {
                    if (!remainingIngredients.remove(layerIngredient)) {
                        if (remainingIngredients.contains(Ingredient.HELPFUL_DUCK)) {
                            remainingIngredients.remove(Ingredient.HELPFUL_DUCK);
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                if (!remainingIngredients.remove(garnishIngredient)) {
                    if (remainingIngredients.contains(Ingredient.HELPFUL_DUCK)) {
                        remainingIngredients.remove(Ingredient. HELPFUL_DUCK);
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Adds to the fulfilled customer order list
     * 
     * @param ingredients the list of ingredients used to fulfill
     * @param garnish the garnish used to fulfill
     * @return list of customers orders that can be fulfilled
     * @throws WrongIngredientsException if the required ingredients are not available
     */
    public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnish) throws WrongIngredientsException {
        List<Ingredient> usedIngredients = new ArrayList<>(); // Initialize     usedIngredients list

        if (!canFulfill(ingredients)) {
            throw new WrongIngredientsException("Required ingredients are not available");
        }

        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
        for (Ingredient ingredient : recipe) {
            if (remainingIngredients.remove(ingredient)) {
                usedIngredients.add(ingredient);
            } else {
                remainingIngredients.remove(Ingredient.HELPFUL_DUCK);
                usedIngredients.add(Ingredient.HELPFUL_DUCK);
            }
        }
        if (garnish && this.garnish.size() > 0 && canGarnish(remainingIngredients)) {
            for (Ingredient garnishIngredient : this.garnish) {
                if (remainingIngredients.remove(garnishIngredient)) {
                    usedIngredients.add(garnishIngredient);
                } else {
                    remainingIngredients.remove(Ingredient.HELPFUL_DUCK);
                    usedIngredients.add(Ingredient.HELPFUL_DUCK);
                }
            }
            setStatus(CustomerOrderStatus.GARNISHED);
        }else{
            setStatus(CustomerOrderStatus.FULFILLED);
        }
        // Sort the usedIngredients list
        Collections.sort(usedIngredients);
        return usedIngredients;
    }

   
    /**
     * Gets the garnish ingredients for the bakery items
     *      
     * This method returns a list of ingredients that are used as garnish for the bakery
     * 
     * @return a list of garnish ingredients for the bakery item
     */
    public List<Ingredient> getGarnish()
    {
        return garnish;
    }

    /**
     * Gets a string representation of the Garnish in the customer order.
     * 
     * @return a string representation of the Garnish in the customer order.
     */
    public String getGarnishDescription()
    {
        String output = "";
        for (int i = 0; i < garnish.size(); i++){
            if (i == garnish.size() - 1){
                output = output + garnish.get(i);
            }
            else{
                output = output + garnish.get(i) + ", ";
            }
        }
        return output;

    }

    /**
     * Gets the level of the Customer Order
     * 
     * @return the level of the custoemr order.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Gets the Ingredients in the recipe of the customer order
     * 
     * @return the list of ingredients in the customer order's recipe
     */
    public List<Ingredient> getRecipe()
    {
        return recipe;
    }

    /**
     * Gets a string representation of the recipe in the Customer order.
     * 
     * @return a string representation of the recipe in the customer order
     */
    public String getRecipeDescription()
    {
        String output = "";
        for (int i = 0; i < recipe.size(); i++){
            if (i == recipe.size() - 1){
                output = output + recipe.get(i);
            }
            else{
                output = output + recipe.get(i) + ", ";
            }
        }
        return output;

    }

    /**
     * Gets the current status of the customer order
     * 
     * @return the current status of the customer order
     */
    public CustomerOrderStatus getStatus()
    {
        return status;
    }

    /**
     * Sets the status of the Customer Order
     * 
     * @param statusIn the status that is assigned to the Customer Order
     */
    public void setStatus(CustomerOrderStatus statusIn)
    {
        status = statusIn;
    }

    /**
     * Gets a string of the Customer Order Card name
     * 
     * @return a string of the customer order card name.
     */
    public String toString()
    {
        return name;
    }
}
