package bakery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.LinkedTransferQueue;

import javax.management.openmbean.ArrayType;

import bakery.MagicBakery;
import bakery.CustomerOrder.CustomerOrderStatus;
import util.collectionFunctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import util.CardUtils;

/**
 * Represents a customer
 * 
 * The {@code Customers} class represents a simple calculator with basic operations.
 * It provides methods to perform addition, subtraction, multiplication, and division.
 *
 * @author Sahil Saxena
 * @version 1.0
 * @since 1.0
 */
public class Customers implements Serializable {
    private Collection<CustomerOrder> activeCustomers;
    private Collection<CustomerOrder> customerDeck;
    private List<CustomerOrder> inactiveCustomers;
    private Random random;
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new Customers instance with the given parameters.
     * 
     * @param deckFile the file containing customer order details
     * @param random the random number generator
     * @param layers the collection of layers available in the game
     * @param numPlayers the number of players in the game
     * @throws FileNotFoundException if the specified customer deck file is not found
     */
    public Customers(String deckFile, Random random, Collection<Layer> layers, int numPlayers) throws FileNotFoundException
    {
        if (deckFile == null) {
            throw new FileNotFoundException("Deck file cannot be null");
        }

        try {
            // Check if the file exists
            File file = new File(deckFile);
            if (!file.exists()) {
                throw new FileNotFoundException("Customer deck file not found: " +  deckFile);
            }
        } catch (SecurityException e) {
            throw new FileNotFoundException("Access to file denied: " + deckFile);
        }

        activeCustomers = new ArrayList<>();
        for (int i = 1; i<=3; i++){
            activeCustomers.add(null);
        }

        this.random = random;
        inactiveCustomers = new ArrayList<>();
        initialiseCustomerDeck(deckFile, layers, numPlayers);
    }

    /**
     * Adds a new customer order
     * 
     * @return the newly added customer order
     */
    public CustomerOrder addCustomerOrder()
    {
        CustomerOrder leavingCustomerOrder = timePasses();
        if (!customerDeck.isEmpty()){
            LinkedList<CustomerOrder> tempActiveCustomers = new LinkedList<>(activeCustomers);
            tempActiveCustomers.removeLast();
            CustomerOrder drawnCustomer = drawCustomer();
            tempActiveCustomers.add(drawnCustomer);
            activeCustomers.clear();
            activeCustomers.addAll(tempActiveCustomers);
        }else{
            throw new EmptyStackException();
        }
        if(!activeCustomers.contains(null)){
            peek().setStatus(CustomerOrderStatus.IMPATIENT);
        }
        return leavingCustomerOrder;
        
    }

    /**
     * Checks if any customer will leave soon
     * 
     * @return true if the customer will leave soon, false otherwise
     */
    public boolean customerWillLeaveSoon(){
        if(!this.activeCustomers.contains(null)){
            peek().setStatus(CustomerOrderStatus.IMPATIENT);
            return true;
        }
        if(this.customerDeck.isEmpty()) {
            LinkedList<CustomerOrder> tempActiveCustomers = new LinkedList<>(activeCustomers);
            if((tempActiveCustomers.getFirst() != null) &&
                    (tempActiveCustomers.getLast() == null)){
                peek().setStatus(CustomerOrderStatus.IMPATIENT);
                return true;
            }
        }
        return false;
    }
        
        


    /**
     * Draws a customer order from the deck
     * 
     * @return the drawn customer order
     */
    public CustomerOrder drawCustomer()
    {
        return ((Stack<CustomerOrder>)customerDeck).pop();
    }

    /**
     * Gets the collection of active customers
     * 
     * @return the collection of active customers
     */
    public Collection<CustomerOrder> getActiveCustomers() {
        return activeCustomers;
    }

    /**
     * Gets the customer deck
     * 
     * @return the customer deck
     */
    public Collection<CustomerOrder> getCustomerDeck() {
        return customerDeck;
    }

    /**
     * Gets the collection of customer orders that can be fulfilled with the given hand of ingredients
     * 
     * @param hand the list of ingredients available for baking
     * @return a collection of customer orders that can be fulfilled
     */
    public Collection<CustomerOrder> getFulfilable(List<Ingredient> hand)
    {
        List<CustomerOrder> fulfillableOrders = new ArrayList<>();
        for (CustomerOrder order : activeCustomers) {
            if (order != null && order.canFulfill(hand)) {
                fulfillableOrders.add(order);
            }
        }
        return fulfillableOrders;
    }

    /**
     * Gets a collection of inactive customers with the specified status 
     * 
     * @param status the status of inactive customers to retrieve
     * @return a collection of inactive customers with the specified status
     */
    public Collection<CustomerOrder> getInactiveCustomersWithStatus(CustomerOrderStatus status)
    {
        List<CustomerOrder> customersWithStatus = new ArrayList<>();
        for (CustomerOrder order : inactiveCustomers) {
            if (order.getStatus() == status) {
                customersWithStatus.add(order);
            }
        }
        return customersWithStatus;
    }

    /**
     * Initializes the customer deck
     * 
     * @param deckFile the file that consists the card deck
     * @param layers the layers to add
     * @param numPlayers
     */
    private void initialiseCustomerDeck(String deckFile, Collection<Layer> layers, int numPlayers)
    {

        this.customerDeck=new Stack<CustomerOrder>();

        ArrayList<CustomerOrder> Deck = new ArrayList<CustomerOrder>(CardUtils.readCustomerFile(deckFile, layers));
        Collections.shuffle(Deck, this.random);
        LinkedList<CustomerOrder> deck1=new LinkedList<CustomerOrder>();
        LinkedList<CustomerOrder> deck2=new LinkedList<CustomerOrder>();
        LinkedList<CustomerOrder> deck3=new LinkedList<CustomerOrder>();
        int[] nums=new int[3];
        switch (numPlayers) {
            case 2:
                nums[0]=4;
                nums[1]=2;
                nums[2]=1;
                break;
            case 3:
            case 4:
                nums[0]=1;
                nums[1]=2;
                nums[2]=4;
                break;
            case 5:
                nums[0]=0;
                nums[1]=1;
                nums[2]=6;
                break;
        }
        
        for(CustomerOrder order : Deck){
            if(order.getLevel()==1){
                deck1.add(order);
            }
            if(order.getLevel()==2){
                deck2.add(order);
            }
            if(order.getLevel()==3){
                deck3.add(order);
            }
        }

        for(int i=0; i<3; i++) {
            for (int j = 0; j < nums[i]; j++) {
                if (i == 0){
                    customerDeck.add(deck1.remove());
                }else if(i == 1){
                    customerDeck.add(deck2.remove());
                }else if(i == 2){
                    customerDeck.add(deck3.remove());
                }
            }
        }
        Collections.shuffle(((List<CustomerOrder>)customerDeck), random);    
    }

    /**
     * Checks if the customer deck is empty
     * 
     * @return true if the customer deck is empty, false otherwise
     */
    public boolean isEmpty()
    {
        if (size() == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Peeks at the top customer order in the deck without removing it
     * 
     * @return the top customer order in the deck
     */
    public CustomerOrder peek()
    {
        Deque<CustomerOrder> tempActiveCustomer = new LinkedList<>(activeCustomers);
        if (size() != 0){
            CustomerOrder item = tempActiveCustomer.peek();
            return item;
        }else{
            return null;
        }
    }

    /**
     * Remove the specified customer order.
     * 
     * @param customer the customer order to remove
     */
    public void remove(CustomerOrder customer)
    {
        List<CustomerOrder> tempActiveCustomers = new ArrayList<>(activeCustomers);
        tempActiveCustomers.set(tempActiveCustomers.indexOf(customer), null);
        inactiveCustomers.add(customer);
        activeCustomers.clear();
        activeCustomers.addAll(tempActiveCustomers);
    }
    
    /**
     * Gets the size of the customer deck
     * 
     * @return the size of the customer deck
     */
    public int size()
    {
        int size = 0;
        for (CustomerOrder  item: activeCustomers){
            if (item != null){
                size++;
            }
        }
        return size;
    }

    /**
     * simulates the passage of time for the customers
     * 
     * @return the customer order affected by the passage of time
     */
    public CustomerOrder timePasses()
    {
        /*
         * The timePasses method moves activeCustomers through the shop in
            accordance with the rules, ensuring that a space becomes available in the "leftmost"
            space of the "customer row". If this results in a customer leaving the shop, then that
            CustomerOrder is returned by the timePasses method, if not then the
            timePasses method returns null .
         */
        LinkedList<CustomerOrder> tempActiveCustomers = new LinkedList<>(activeCustomers);
        if(!tempActiveCustomers.isEmpty() && customerWillLeaveSoon()) {
            CustomerOrder leavingCustomerOrder = tempActiveCustomers.remove();
            tempActiveCustomers.add(null);
            leavingCustomerOrder.abandon();
            inactiveCustomers.add(leavingCustomerOrder);
            activeCustomers.clear();
            activeCustomers.addAll(tempActiveCustomers);
            return leavingCustomerOrder;
        }

        if (!customerDeck.isEmpty()){
            LinkedList<CustomerOrder> tempActiveCustomerOrders = new LinkedList<>(activeCustomers);
            tempActiveCustomerOrders.removeLastOccurrence(null);
            activeCustomers.clear();
            activeCustomers.addAll(tempActiveCustomerOrders);
        }else{
            activeCustomers.remove(null);
        }

        activeCustomers.add(null);

        if(peek() != null){
            peek().setStatus(CustomerOrder.CustomerOrderStatus.WAITING);
        }
        return null;

    
    }
    
    /**
     * Adds the specified number of customer orders from the given deck
     * to the customer deck
     * 
     * @param deck the deck of customer orders to add from
     * @param numOfCard the number of customer orders to add
     */
    private void addToDeck(List<CustomerOrder> deck, int numOfCard)
    {
        for (int i = 1; i <= numOfCard; i++)
        {
            int j = random.nextInt(deck.size());
            CustomerOrder randomOrder = deck.get(j);
            customerDeck.add(randomOrder);
            deck.remove(j);
        }
    }
}


