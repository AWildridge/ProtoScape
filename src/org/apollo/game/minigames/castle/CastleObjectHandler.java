package org.apollo.game.minigames.castle;

/**
 * Here we will handle the objects.
 *
 *
 * @author Rodrigo Molina
 */
public class CastleObjectHandler {

    	private short zammyMainGateHP;
    	private short saraMainGateHP;
    	
    	/**
    	 * Here we will reset the variables..
    	 */
    	public CastleObjectHandler() {
    	    setZammyMainGateHP((short) 100);
    	    setSaraMainGateHP((short) 100);
    	    
    	}

	public short getZammyMainGateHP() {
	    return zammyMainGateHP;
	}

	public void setZammyMainGateHP(short zammyMainGateHP) {
	    this.zammyMainGateHP = zammyMainGateHP;
	}

	public short getSaraMainGateHP() {
	    return saraMainGateHP;
	}

	public void setSaraMainGateHP(short saraMainGateHP) {
	    this.saraMainGateHP = saraMainGateHP;
	}
}
