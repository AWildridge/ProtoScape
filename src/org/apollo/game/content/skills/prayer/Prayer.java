package org.apollo.game.content.skills.prayer;

import org.apollo.game.action.Action;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;

/**
 * Prayer.java
 * 
 * @author The Wanderer
 */
public class Prayer {

    private static final int BURY_EMOTE = 827;

    /**
     * Handles burying bones
     * @param item Item being buried.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void handleBurying(final Player player, Item bone, int slot) {
	    	if(player.hasAttributeTag("burying")) {
	    		return;
	    	}
	    	for (BoneData b : BoneData.values()) {
	    		if (bone.getId() == b.getId()) {
	    			player.getAttributeTags().add("burying");
	    			player.sendMessage("You dig a hole in the ground...");
	    			// player.getInventory().remove(bone, slot);
	    			player.getInventory().remove(bone);
	                	player.getSkillSet().addExperience(Skill.PRAYER, b.getExp());
	                	player.playAnimation(new Animation(BURY_EMOTE));
		                World.getWorld().schedule(new Action(2, false, player) {
		                    
		                    @Override
		                    public QueuePolicy getQueuePolicy() {
		                        return QueuePolicy.NEVER;
		                    }
		                    
		                    @Override
		                    public WalkablePolicy getWalkablePolicy() {
		                        return WalkablePolicy.NON_WALKABLE;
		                    }
		                    
		                    @Override
		                    public void execute() {
		                        player.sendMessage("You bury the bones.");
		                        player.getAttributeTags().remove("burying");
		                        this.stop();
		                    }
		               });
	            }
	        }
    }
}
