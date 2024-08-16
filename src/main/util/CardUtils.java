package util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bakery.CustomerOrder;
import bakery.Ingredient;
import bakery.Layer;
import java.util.Collection;

/** Utility class for handling various operations related to bakery orders.
 * 
 * The {@code CardUtils} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class CardUtils {
    /**
    * Private constructor to prevent instantiation of this utility class.
    */
    private CardUtils() { 
        throw new IllegalStateException("Utility class");
    }

    /**
     * Reads a customer order file and returns a list of customer orders.
     *
     * @param path the path to the customer order file
     * @param layers the list of available layers
     * @return the list of customer orders read from the file
     */
    public static List<CustomerOrder> readCustomerFile(String path, Collection<Layer> layers) 
    {
        List<CustomerOrder> customerDeck = new ArrayList<CustomerOrder>();
        try{
            FileReader customerFile = new FileReader(path);
            BufferedReader customerStream = new BufferedReader(customerFile);
            customerStream.readLine();
            String line;
            while ((line = customerStream.readLine()) != null)
            {
                CustomerOrder tempCustomerOrder = stringToCustomerOrder(line, layers);
                customerDeck.add(tempCustomerOrder);

            }
            customerStream.close();
        }
        catch (FileNotFoundException e){
            System.out.println("\nNo File was read");
        }
        catch (IOException e){
            System.out.println("\nThere was a problem reading the file");
        }
        return customerDeck;
    }
    
    /**
     * Reads an ingredient file and returns a list of ingredients
     * 
     * @param path the path to the ingredient file
     * @return the list of ingredients read from the file
     * @throws FileNotFoundException if the specified file is not found
     */
    public static List<Ingredient> readIngredientFile(String path) throws FileNotFoundException{  
        List<Ingredient> pantryDeck = new ArrayList<Ingredient>();
        try{
            FileReader ingredientFile = new FileReader(path);
            BufferedReader ingredientStream = new BufferedReader(ingredientFile);
            ingredientStream.readLine();
            String line;
            while ((line = ingredientStream.readLine()) != null){
                List<Ingredient> tempPantryDeck =  stringToIngredients(line);
                for (Ingredient item: tempPantryDeck)
                {
                    pantryDeck.add(item);
                }
            }
            ingredientStream.close();
        }
        catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e){
            System.out.println("\nThere was a problem reading the file");
        }
        return pantryDeck;    
    }

    /**
     * Reads a layer file and returns a list of layers.
     * 
     * @param path the path to the layer file
     * @return the list of layers read from the file
     * @throws FileNotFoundException if the specified file is not found
     */
    public static List<Layer> readLayerFile(String path) throws FileNotFoundException
    {
        List<Layer> layerDeck = new ArrayList<Layer>();
        try{
            FileReader layerFile = new FileReader(path);
            BufferedReader layerStream = new BufferedReader(layerFile);
            layerStream.readLine();
            String line;
            while ((line = layerStream.readLine()) != null){
                List<Layer> tempLayerDeck = stringToLayers(line);
                for (Layer item: tempLayerDeck)
                {
                    layerDeck.add(item);
                }
            }
            layerStream.close();
        }
        catch (FileNotFoundException e){
            throw e;
        }
        catch (IOException e){
            System.out.println("\nThere was a problem reading the file");
        }
        return layerDeck;
    }
    
    private static CustomerOrder stringToCustomerOrder(String str, Collection<Layer> layers) throws FileNotFoundException
    {
        String[] parts = str.split(",");
        if (parts.length == 3 || parts.length == 4)
        {
            List<Ingredient> ingredients = readIngredientFile("io/ingredients.csv");
            int level = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String[] recipe = parts[2].split(";");
            List<Ingredient> tempRecipe = new ArrayList<Ingredient>();
            for (int i = 0; i < recipe.length; i++)
            {
                String val = recipe[i].trim();
                boolean isIngredient = true;
                for (Layer layer: layers)
                {
                    if (layer.toString().equals(val))
                    {
                        isIngredient = false;
                        tempRecipe.add(layer);
                        break;
                    }
                }

                if (isIngredient)
                {
                    tempRecipe.add(new Ingredient(val));
                }
            }
            
            List<Ingredient> tempGarnish = new ArrayList<Ingredient>();
            if (parts.length == 4){
                String[] garnish = parts[3].split(";");
                for (int i = 0; i < garnish.length; i++)
                {
                    String val = garnish[i].trim();
                    boolean isIngredient = true;
                    for (Layer layer: layers)
                    {
                        if (layer.toString().equals(val))
                        {
                            isIngredient = false;
                            tempGarnish.add(layer);
                            break;
                        }
                    }

                    if (isIngredient)
                    {
                        tempGarnish.add(new Ingredient(val));
                    }

                }
            }
            CustomerOrder customerOrder = new CustomerOrder(name, tempRecipe, tempGarnish, level);     
            return customerOrder;      
        }
        else 
        {
            System.err.println("Invalid Line format in Layer file");
            return null;
        }
    }
    
    private static List<Ingredient> stringToIngredients(String str){
        List<Ingredient> tempPantryDeck = new ArrayList<Ingredient>();
        String[] parts = str.split(",");
        if (parts.length == 2)
        {
            String ingredientName = parts[0].trim();
            int count = Integer.parseInt(parts[1].trim());
            for (int i=1; i <= count; i++)
            {
                tempPantryDeck.add(new Ingredient(ingredientName));
            }
        }else 
        {
            System.err.println("Invalid Line format in Ingredient file");
        }
        return tempPantryDeck;
    }

    
    private static List<Layer> stringToLayers(String str)
    {
        List<Layer> tempLayerDeck = new ArrayList<Layer>();
        String[] parts = str.split(",");
        if (parts.length == 2)
        {
            List<Ingredient> tempRecipe = new ArrayList<Ingredient>();
            String recipeName = parts[0].trim();
            String[] recipe = parts[1].split(";");
            for (int i = 0; i < recipe.length; i++)
            {
                String val = recipe[i].trim();
                tempRecipe.add(new Ingredient(val));
            }
            for (int i = 1; i <= 4; i++)
            {
                tempLayerDeck.add(new Layer(recipeName, tempRecipe));
            }
        }else 
        {
            System.err.println("Invalid Line format in Layer file");
        }
        return tempLayerDeck;
    }
}
