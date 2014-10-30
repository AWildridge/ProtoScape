package org.apollo.game.model.inter.bank;

import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Player;
import org.apollo.util.Misc;

/**
 * A bank pin system..
 *
 *
 * @author Rodrigo Molina
 */
public class BankPin {
   
    	/**
    	 * The player..
    	 */
    	private Player player;
    	
    	/**
    	 * Creates a new bank pin..
    	 * 
    	 * @param player
    	 *	the player
    	 */
    	public BankPin(Player player) {
    	    this.player = player;
    	    for(int a = 0; a < pins.length; a++) {
    		    for(int i = 0; i < 4; i++) {
    			    pins[a][i] = -1;
    		    }
    	    }
    	}
    	
    	/**
    	 * Each step of the bank pin entering phase.
    	 *
    	 *
    	 * @author Rodrigo Molina
    	 */
    	private enum Step {
    	    FIRST,
    	    SECOND,
    	    THIRD,
    	    FOURTH,
    	    OPEN;
    	}

    	/**
    	 * The step that this player is in in the bank.
    	 */
    	private Step step;
    	
    	/**
    	 * The number of attempts this player has tried.
    	 */
    	private int attempts;
    	
    	/**
    	 * The max attempts this player can try before being locked out of bank.
    	 */
    	private final int MAX_ATTEMPTS = 8;
    	
    	/**
    	 * Checks if the player can open the bank or not.
    	 * 
    	 * @return true if they have already opened the bank before, false if its the first time
    	 * and they have a pin set.
    	 */
    	public boolean canOpenBank() {
    	    if (step == Step.OPEN) {
    		return true;
    	    } else {
    		displayInterface();
    		return false;
    	    }
    	}
    	
    	/**
    	 * Displays the bank pin interface.
    	 */
    	public void displayInterface() {
    		if(step == Step.OPEN)
    			return;
    	    player.send(new OpenInterfaceEvent(7424));
    	    if(step == null)
    		step = Step.FIRST;
    	    sendTextData();
    	}
    	
    	/**
    	 * The pin array which will hold the number display and the text.
    	 * 
    	 * This will also hold the bank pin.
    	 * 
    	 * The bank pin will be stored in the slot 12, 13, 14, and 15
    	 */
    	private int[][] pins = new int[13][5];
    	
    	/**
    	 * Get's a random number from the array above. 
    	 * 
    	 * @return a random number that was not ever used.
    	 */
    	private void setRandom() {
    	    int[] random = randomNumbers[Misc.random(randomNumbers.length - 1)];
    	    for(int a = 0; a < rNumbers.length; a++) {
    		rNumbers[a] = random[a];
    	    }
    	    random = null;
    	}
    	
    	/**
    	 * The random numbers generated pre-hand..
    	 * just because it was too difficult for me >.>
    	 */
    	private int[][] randomNumbers = {
    		{ 9, 1, 3, 0, 2, 5, 6, 4, 8, 7 },
    		{ 1, 7, 8, 6, 4, 0, 2, 5, 3, 9 },
    		{ 7, 9, 3, 5, 2, 0, 4, 6, 8, 1 },
    	};
    	
    	/**
    	 * The random numbers..
    	 */
    	private int[] rNumbers = new int[10];
    	
    	/**
    	 * Sends the text data.
    	 */
    	public void sendTextData() {
    	    int slot = 0;
    	    setRandom();
    	    for(int a = 14883; a <= 14892; a++) {
    		int number = rNumbers[slot];
    		String space = "";
    		int random = Misc.random(3);
    		switch(random) {
    		case 0:
    		    space = "";
    		    break;
    		case 1:
    		    space = "            ";
    		    break;
    		case 2:
    		    space = "      ";
    		    break;
    		case 3:
    		    space = "  ";
    		    break;
    		}
    		player.send(new SetInterfaceTextEvent(a, space + number));
   		pins[slot][0] = number;
    		pins[slot][1] = a - 10;
    		slot++;
    	    }
    	    rNumbers = new int[10];
    	    player.send(new SetInterfaceTextEvent(14923, "Bank of ProtoScape"));
    	    switch(step) {
    	    case FIRST:
        	player.send(new SetInterfaceTextEvent(15313, "First click the FIRST digit."));
        	player.send(new SetInterfaceTextEvent(14913, "?"));//first
            	player.send(new SetInterfaceTextEvent(14914, "?"));//second
                player.send(new SetInterfaceTextEvent(14915, "?"));//third
            	player.send(new SetInterfaceTextEvent(14916, "?"));//last
    		break;
    	    case SECOND:
    		player.send(new SetInterfaceTextEvent(15313, "Now click the SECOND digit."));
    		player.send(new SetInterfaceTextEvent(14913, "*"));//first
        	player.send(new SetInterfaceTextEvent(14914, "?"));//second
                player.send(new SetInterfaceTextEvent(14915, "?"));//third
        	player.send(new SetInterfaceTextEvent(14916, "?"));//last
		break;
    	    case THIRD:
    		player.send(new SetInterfaceTextEvent(15313, "Time for the THIRD digit."));
    	    	player.send(new SetInterfaceTextEvent(14913, "*"));//first
        	player.send(new SetInterfaceTextEvent(14914, "*"));//second
                player.send(new SetInterfaceTextEvent(14915, "?"));//third
        	player.send(new SetInterfaceTextEvent(14916, "?"));//last
		break;
    	    case FOURTH:
    		player.send(new SetInterfaceTextEvent(15313, "Finally, the FOURTH digit."));
    		player.send(new SetInterfaceTextEvent(14913, "*"));//first
        	player.send(new SetInterfaceTextEvent(14914, "*"));//second
                player.send(new SetInterfaceTextEvent(14915, "*"));//third
        	player.send(new SetInterfaceTextEvent(14916, "?"));//last
		break;
		default:
			break;
    	    }
        }
    	
    	/**
    	 * The player has clicked the button, and it's this objects responsibility to check if the button they matched matches
    	 * the bank pin.
    	 * 
    	 * @param button
    	 * 	The button id clicked.
    	 */
    	public void clickButton(int button) {
    	    int stepNumber = pins[12][step.ordinal()];
    	    for(int idx = 0; idx < pins.length; idx++) {
    		if(pins[idx][1] == button) {
    		    int number = pins[idx][0];
    		    System.out.println("Number:"+number +", StepN:"+stepNumber+", step:"+step.ordinal()+", "+step);
    		    //TODO: Figure out what to do in each of these steps like when they get it wrong..
    		    if(stepNumber == -1) {
    			    pins[12][step.ordinal()] = number;
    			    nextStep(true);
    			    displayInterface();
    			    break;
    		    } else {
    			    if(stepNumber != number) {
	    			System.out.println("pin does not match number..");
	    			attempts++;
	    			if(attempts >= MAX_ATTEMPTS) {
	    			    lockPlayer();
	    			}
	    			//then close it?
	    		    } else {
	    			nextStep(false);
	    			this.displayInterface();
	    			break;
	    		    }
    		    }
    		}
    	    }
    	}
    	
    	/**
    	 * Lock's the player out of their bank because they have tried too many attempts to 
    	 * enter the bank.
    	 * 
    	 * idk how the locking system works.. so..
    	 */
    	private void lockPlayer() {
    	    player.sendMessage("You have tried too many times.. You have been locked out of your bank.");
    	    attempts = 0;
    	}
    	
    	/**
    	 * Move's this current step to the next step.
    	 */
    	private void nextStep(boolean setNew) {
    	    switch(step) {
    	    case FIRST:
    		step = Step.SECOND;
    		break;
    	    case SECOND:
    		step = Step.THIRD;
    		break;
    	    case THIRD:
    		step = Step.FOURTH;
    		break;
    	    case FOURTH:
    		step = Step.OPEN;
    	    case OPEN:
    	  	    if(attempts >= MAX_ATTEMPTS) {
    	    		lockPlayer();
    	    		return;
    	    	    }
    	  	    if(setNew)
    	  		    player.sendMessage("You have set your pin!  Write it down, so you don't forget it.");
    	  	    else
    	  		    player.sendMessage("You have entered your bank pin correctly.");
    	    	    step = Step.OPEN;
    	    	    player.setPinSet(true);
    	    	    BankUtils.openBank(player);
    		break;
    	    }
    	}
    	
    	/**
    	 * Get's the bank pin in String format.
    	 * 
    	 * @return the {@link String} pin
    	 */
    	public String getBankPin() {
    	    int first = pins[12][0];
    	    int second = pins[12][1];
    	    int third = pins[12][2];
    	    int fourth = pins[12][3];
    	    return "" + first + second + third + fourth;
    	}
    	
    	/**
    	 * Sets the pin.
    	 * 
    	 * @param pin
    	 *	The pin entered as a string.
    	 * @throws Exception
    	 * 	If the string was not properly concursed and it does not contain only 4 digits.
    	 */
    	public void setBankPin(String pin) throws Exception {
    	    int first = Integer.parseInt(pin.substring(0, 1));
    	    int second = Integer.parseInt(pin.substring(1, 2));
    	    int third = Integer.parseInt(pin.substring(2, 3));
    	    int fourth = Integer.parseInt(pin.substring(3, 4));
    	    pins[12][0] = first;
    	    pins[12][1] = second;
    	    pins[12][2] = third;
    	    pins[12][3] = fourth;
    	}

    	/**
    	 * Sets the pin.
    	 * 
    	 * @param pin
    	 *	The pin entered as an integer.
    	 */
    	public boolean setBankPin(int pin) {
    	    try {
		setBankPin(Integer.toString(pin));
		player.setPinSet(true);
		return true;
	    } catch (Exception e) {
		player.sendMessage("Please enter only 4 digits into the interface and nothing else.");
		return false;
	    }
    	}
    	
	/**
	 * The player has requested to exit the bank pin interface.
	 */
	public void exit() {
	    
	}
	
	/**
	 * The player does not know the bank pin.
	 */
	public void dontKnowIt() {
	    
	}
}
