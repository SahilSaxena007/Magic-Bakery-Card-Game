
package bakery;
import bakery.Ingredient;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.Serializable; 

/**
 * Represents a layer in the bakery game.
 * The {@code Layer} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class Layer extends Ingredient implements Serializable{
    private List<Ingredient> recipe;
    private static final long serialVersionUID = 1L;
     
    /**
     * Constructs a new Layer with given name and recipe.
     * 
     * @param nameIn the name of the Layer
     * @param recipeIn the list of ingredients the layer's recipe
     */
    public Layer(String nameIn, List<Ingredient> recipeIn)
    {
        super(nameIn);
        if (recipeIn == null || recipeIn.isEmpty()){
            throw new WrongIngredientsException();
        }

        
        recipe = new ArrayList<>(recipeIn);
    }
    
    /**
     * Gets the recipe of the layer.
     * 
     * @return the list of ingredients in the layer's recipe
     */
    public List<Ingredient> getRecipe()
    {
        return recipe;
    }
    
    /**
     * Checks if the layer can be baked with the given ingredients.
     * 
     * @param ingredients the list of ingredients available for baking
     * @return true if the layer can be baked, false otherwise
     */
    public boolean canBake(List<Ingredient> ingredients)
    {
        // //check the no. of helpful ducks in the ingredients
        // int helpfulDucks = 0;
        // for (Ingredient ingredient : ingredients) {
        //     if (ingredient == Ingredient.HELPFUL_DUCK) {
        //         helpfulDucks++;
        //     }
        // }
        // //check if the ingredients are present in the recipe
        // int helpfulDucksUsed = 0;
        // for (Ingredient ingredient : recipe) {
        //     if (!ingredients.contains(ingredient)) {
        //         if(ingredients.contains(Ingredient.HELPFUL_DUCK)){
        //             helpfulDucksUsed++;
        //             //if there are more missing ingredients than helpful ducks then return false
        //             if(helpfulDucksUsed > helpfulDucks){
        //                 return false;
        //             }
        //         } else {
        //             return false;
        //         }
        //     }
        // }
        // return true;
        // Count the number of helpful ducks in the ingredients
        int helpfulDucks = 0;
        for (Ingredient ingredient : ingredients) {
            if (ingredient == Ingredient.HELPFUL_DUCK) {
                helpfulDucks++;
            }
        }
    
        // Count the number of missing ingredients
        int missingIngredients = 0;
        for (Ingredient ingredient : recipe) {
            if (!ingredients.contains(ingredient)) {
                missingIngredients++;
            }
        }
    
        // Check if the number of missing ingredients can be    compensated by helpful ducks
        return missingIngredients <= helpfulDucks;

    }
    
    /**
     * Gets a description of the layer's recipe
     * 
     * @return a string description of the layer's recipe
     */
    public String getRecipeDescription(){
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
     * Returns the hash code value for this layer.
     * 
     * @return the hash code value for this layer
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((recipe == null) ? 0 : recipe.hashCode());
    
        // Include the order of ingredients in the hash code calculation
        for (Ingredient ingredient : recipe) {
            result = prime * result + ((ingredient == null) ? 0 : ingredient.hashCode());
        }
    
        return result;
    }

}
