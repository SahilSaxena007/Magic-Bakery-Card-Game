package util;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;;

public class collectionFunctions {

    public static <T> T getElementAt(Collection<T> collection, int index) {
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        int currentIndex = 0;
        for (T element : collection) {
            if (currentIndex == index) {
                return element;
            }
            currentIndex++;
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    

    public static <T> boolean removeElementAt(Collection<T> collection, int index) { 
        if (index < 0 || index >= collection.size()) {
            return false; // Index out of bounds
        }
        int currentIndex = 0;
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (currentIndex == index) {
                iterator.remove();
                return true; // Element removed successfully
            }
            currentIndex++;
        }
        return false; // Element not found (should not happen if index is valid)
    }

    

    public static <T> boolean setElementAt(Collection<T> collection, int index, T element) {
        if (index < 0 || index >= collection.size()) {
            return false; // Index out of bounds
        }
        List<T> list = new ArrayList<>(collection);
        list.set(index, element);
        collection.clear();
        collection.addAll(list);
        return true; // Element set successfully
    }


    public static void shuffleCollection(List<? extends Object> collection)
    {
        Random random = new Random();
        Collection<Object> collection2 = new ArrayList<>(collection);
        for (int i = collection.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Object temp = collectionFunctions.getElementAt(collection2, i);
            collectionFunctions.setElementAt(collection2, i,collectionFunctions.getElementAt(collection2, j));
            collectionFunctions.setElementAt(collection2, j, temp);
        }
    }
    
}
