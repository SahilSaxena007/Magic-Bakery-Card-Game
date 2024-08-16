package bakery;
import java.io.Serializable;
/**
 * A custom exception indicating that the maximum number of actions has been exceeded.
 * This exception can be thrown when a player attempts to take more actions than allowed.
 * 
 * The {@code TooManyActionsException} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class TooManyActionsException extends IllegalStateException{
    /**
     * The Maximum Actions
     */
    private int maxActions;
    /**
     * Constructs a TooManyActionsException with a default message.
     */
    public TooManyActionsException() {
        super("Exceeded maximum number of actions.");
    }
    /**
     * Constructs a TooManyActionsException with a custom message indicating the maximum number of actions.
     * 
     * @param maxActions The maximum number of actions allowed.
     */
    public TooManyActionsException(int maxActions) {
        super("Exceeded maximum number of actions: " + maxActions);
        this.maxActions = maxActions;
    }
    /**
     * Retrieves the maximum number of actions allowed.
     * 
     * @return The maximum number of actions allowed.
     */
    public int getMaxActions() {
        return maxActions;
    }
}
