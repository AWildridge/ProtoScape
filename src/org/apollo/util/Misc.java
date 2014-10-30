package org.apollo.util;

/**
 * Contains miscellaneous methods.
 * Misc.java
 * @author The Wanderer
 */
public class Misc {
    
    /**
     * @param range The range that the numbers can be generated from.
     * @return Returns a random number from 0 to range+.
     */
    public static int random(int range) {
        return (int)(Math.random() * (range + 1));
    }
    public static double random(double range) {
            return (Math.random() * (range + 1));
    }
    
    public static int minutesToSeconds(double minutes) {
	return (int) Math.round(minutes * 60);
    }	
    	/**
    	 * A method which cuts off all numbers after '.' besides the first 2.
    	 * 
    	 * @param r
    	 * 	The string to cut off.
    	 * @return the cut off string
    	 */
	public static String cutOff(String r) {
		char buf[] = r.toCharArray();
		int last = -1;
		for (int j = 0; j < buf.length; j++) {
			if (buf[j] == '.') {
			    try {
					if (buf[++j] >= '0' && buf[++j] >= '0' && buf[++j] >= '0') {
						buf[j] = 0x20;
						last = j;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					last = -1;
					break;
				}
			}
		}
		if (last != -1) {
			for (int a = last; a < buf.length; a++) {
				buf[a] = 0x20;
			}
		}
		return new String(buf, 0, buf.length);
	}
	
	/**
	 * Formats a name for display.
	 * @param s The name.
	 * @return The formatted name.
	 */
	public static String formatName(String s) {
		return fixName(s.replace(" ", "_"));
	}
	
	/**
	 * Method that fixes capitalization in a name.
	 * @param s The name.
	 * @return The formatted name.
	 */
	private static String fixName(final String s) {
		if(s.length() > 0) {
			final char ac[] = s.toCharArray();
			for(int j = 0; j < ac.length; j++)
				if(ac[j] == '_' | ac[j] == '-') {
					ac[j] = ' ';
					if((j + 1 < ac.length) && (ac[j + 1] >= 'a')
							&& (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}

			if((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}
}
