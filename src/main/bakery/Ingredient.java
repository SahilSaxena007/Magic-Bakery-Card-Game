package bakery;
import java.io.Serializable;
import java.lang.Comparable;
/**
 * Represents an ingredient used in baking.
 * 
 * The {@code Ingredient} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class Ingredient implements Serializable, Comparable<Ingredient>{
    /**
     * The name of the ingredient.
     */
    private String name;
    /**
     * A helpful duck ingredient instance.
     */
    public static final Ingredient HELPFUL_DUCK = new Ingredient("Helpful duck 𓅭");
    /**
     * The serial version UID for serialization and deserialization.
     * This is used to ensure that the serialized and deserialized objects
     * are compatible with the class definition.
     */
    private static final long serialVersionUID = 1l;
    /**
     * Constructs an Ingredient with the given name.
     *
     * @param nameIn The name of the ingredient.
     */
    public Ingredient(String nameIn)
    {
        //Setting the name of Card
        name = nameIn;
    }
    /**
     * Returns the hash code value for this ingredient.
     *
     * @return The hash code value for this ingredient.
     */
    public int hashCode(){
        return name.hashCode();
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        else if(o.getClass().equals(this.getClass()) && o.toString().equals(this.toString())){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Returns a string representation of the ingredient.
     *
     * @return A string representation of the ingredient.
     */
    public String toString()
    {
        return name;
    }

    /**
     * Compares this Ingredient object with the specified Layer object for order.
     * This method compares the name of this Ingredient with the string representation
     * of the specified Layer.
     * 
     * @param o The Layer object to be compared.
     * @return A negative integer, zero, or a positive integer as this Ingredient
     * is less than, equal to, or greater than the specified Layer object.
     */
    public int compareTo(Ingredient o) {
        return name.compareTo(o.toString()); 
    }
}