package org.apollo.game.model.combat.action.npc;

import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Character;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Player;
import org.apollo.game.model.Prayers.Prayer;
import org.apollo.game.model.World;
import org.apollo.game.model.combat.action.AbstractCombatAction;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * An plugin for jad hits.
 *
 * @author Rodrigo Molina
 */
public class TzTokJad extends AbstractCombatAction {

        @Override
        public int distance(Character attacker) {
    		return 0;
        }
        
        @Override
        public boolean canHit(Character attacker, Character victim) {
            if(!super.canHit(attacker, victim)) {
                return false;
            }
            return true;
        }

        int attack;
        
        @Override
        public void hit(final Character attacker, final Character victim) {
    		super.hit(attacker, victim);
    		int endGfx = -1;
    		int projectileId = 0;
    		int anim = 0;
    		final Player p = (Player) victim;
    		if(!victim.getPosition().isNextTo(attacker.getPosition())) {
    		    //now use melee and rape the kid lol.
    		    projectileId = -1;
    		    anim = 300;
    		    attack = 1;
    		    victim.sendMessage("MELEE!!");
    		} else {
        		final int random = 1;
        		switch(random) {
        		case 0://MAGE
        		    anim = 930;
        		    endGfx = 450;
        		    //gfx100 = 1625
        		    projectileId = 451;
        		    attack = 2;
        		    p.sendMessage("MAGEE");
        		    break;
        		case 1://RANGE
        		    anim = 930;
        		    endGfx = 451;
        		    projectileId = 1627;
        		    attack = 3;
          		    p.sendMessage("RANGEEE");
        		    break;
        		}
    		}
    	        World.getWorld().schedule(new ScheduledTask(2, false) {

    	            @Override
    	            public void execute() {
    	        	int hit = 0;
    	    		final int damage = hit;
    	    		switch(attack) {
    	    		case 1:
    	    			hit = victim.getMaxHP() - Misc.random(10);
    	    			break;
    	    		case 2:
    	    			if(p.getCombatState().getPrayer(Prayer.PROTECT_FROM_MAGIC.getPrayerId())) {
    	    				hit = 0;
	            		} else {
	            			hit = (victim.getMaxHP() - (Misc.random(victim.getMaxHP() % 50)));
	            		}
    	    			break;
    	    		case 3:
    	    			if(p.getCombatState().getPrayer(Prayer.PROTECT_FROM_MISSILES.getPrayerId())) {
    	    				hit = 0;
    	    			} else {
    	    				hit = (victim.getMaxHP() - (Misc.random(victim.getMaxHP() % 50)));
    	    			}
    	    			break;
    	    		}
    	    		if(hit > victim.getMaxHP())
	    			hit = victim.getMaxHP();
    	                victim.damageCharacter(new DamageEvent(damage, victim.getHP(), victim.getMaxHP()));
    	                victim.inflictDamage(new DamageEvent(damage, victim.getHP(), victim.getMaxHP()), attacker);
    	                this.stop();
    	            }
    	        });
                int offsetX = (attacker.getPosition().getX() - victim.getCentrePosition().getX()) * -1;
                int offsetY = (attacker.getPosition().getY() - victim.getCentrePosition().getY()) * -1;
                
    	        attacker.playProjectile(attacker.getPosition(), (byte) offsetX, (byte) offsetY, projectileId, 130, 10, 25, 5, 5, victim.getProjectileLockonIndex(), 0, 65);
    	        
                attacker.playAnimation(Animation.create(anim));
    	        
    	        attacker.playGraphic(new Graphic(endGfx, 2));
    	        
    	       // victim.getActiveCombatAction().defend(attacker, victim);
        }
}
