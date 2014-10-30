package org.apollo.game.model.duel;

/**
 * @author Rodrigo Molina
 */
public enum Rules {

	NO_RANGED(6725, -1, "You cannot use ranged!"),
	
	;
	
	Rules(int id, int buttonId, String restrict) {
		this.setButtonId(buttonId);
		this.setId(buttonId);
		this.setRestrict(restrict);
	}
	
	private int buttonId;
	private int id;
	private String restrict;
	
	public int getButtonId() {
		return buttonId;
	}
	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRestrict() {
		return restrict;
	}
	public void setRestrict(String restrict) {
		this.restrict = restrict;
	}
}
