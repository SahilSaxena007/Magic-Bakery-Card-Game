package bakery;
import java.io.Serializable;
/**
 * A custom exception indicating that the provided ingredients are incorrect or invalid.
 * This exception can be thrown when attempting to use ingredients that do not meet certain criteria.
 * 
 * Represents a player in the bakery game.
 * The {@code WrongIngredientsException} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class WrongIngredientsException extends IllegalArgumentException{

    /**
     * Constructs a WrongIngredientsException with no specified detail message.
     */
    public WrongIngredientsException() {
        super();
    }
    /**
     * Constructs a WrongIngredientsException with the specified detail message.
     * 
     * @param message The detail message explaining the exception.
     */
    public WrongIngredientsException(String message) {
        super(message);
    }
}
