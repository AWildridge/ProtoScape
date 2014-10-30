package org.apollo.game.model.combat.action.npc;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Character;
import org.apollo.game.model.combat.action.AbstractCombatAction;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;

/**
 * The dark wizards special attacks.
 *
 *
 * @author Rodrigo Molina
 */
public class DarkWizard extends AbstractCombatAction {

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
    
        @Override
        public void hit(final Character attacker, final Character victim) {
           System.out.println("WTFFFFFFFFFFFF");
    		super.hit(attacker, victim);
    		attacker.turnTo(victim.getPosition());
    		Spell earthStrike = Spell.EARTH_STRIKE;
    		int anim = 0;
    		int projectileId = 51;
    		
                int offsetX = (attacker.getPosition().getX() - victim.getCentrePosition().getX()) * -1;
                int offsetY = (attacker.getPosition().getY() - victim.getCentrePosition().getY()) * -1;
                
    	        attacker.playProjectile(attacker.getPosition(), (byte) offsetX, (byte) offsetY, projectileId, 10, 75, 25, 43, 36, victim.getProjectileLockonIndex(), 10, 36);

                //attacker.playAnimation(Animation.create(anim));

                victim.getActiveCombatAction().defend(attacker, victim);
                victim.sendMessage("yo");
        }
}
