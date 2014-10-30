package org.apollo.util;

/**
 * Utitilies used for arrays, such as checking for {@link null} or empty slot arrays or contains.
 *
 *
 * @author Rodrigo Molina
 */
public class ArrayUtils {

    	/**
    	 * Check's if this {@link Object[]} contains the object specified. 
    	 * 
    	 * @param container
    	 * 	The array container which to loop through.
    	 * @param o
    	 * 	The object to check if exists.
    	 * @return true if the container contains this object.
    	 */
    	public static boolean contains(Object[] container, Object o) {
    	    if(o == null)
    		return false;
    	    for(Object a : container) {
    		if(a == o)
    		    return true;
    	    }
    	    return false;
    	}
    	
    	/**
    	 * Loops through the object, ignoring nulls, and returns the size.
    	 * 
    	 * @param o
    	 * 	The array which to loop through.
    	 * @return the size of the object, not the length but the amount of objects inside this array
    	 * which aren't nulled.
    	 */
    	public static int getSize(Object[] o) {
    	    int size = 0;
    	    for(int a = 0; a < o.length; a++) {
    		if(o[a] != null)
    		    size++;
    	    }
    	    return size;
    	}
    	
	/**
	 * Get's a free slot from this array.
	 *
	 * @param a	
	 * 	The array to search for a slot.
	 * @return a free slot which is unused or {@code null}.
	 */
	public static int getFreeSlot(Object[] a) {
	    int slot = -1;
	    for(int b =0; b < a.length; b++) {
		if(a[b] == null) {
		    slot = b;
		    break;
		}
	    }
	    return slot;
	}
}
