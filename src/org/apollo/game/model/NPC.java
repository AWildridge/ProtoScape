package org.apollo.game.model;

import org.apollo.game.event.Event;
import org.apollo.game.event.EventManager;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;
import org.apollo.game.model.def.CombatNPCDefinition;
import org.apollo.game.model.def.NPCDefinition;
import org.apollo.game.model.region.Region;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * NPC.java
 *
 * @author The Wanderer
 */
public class NPC extends Character {

    //TODO: Make HP set from skillset, not a variable.
    private int hp;
    private int maxHp;
    private boolean walkOverridden;
    private boolean isVisible = true;
    int maxX;
    int maxY;
    int leastX;
    int leastY;

    /**
     * The respawn time
     */
    private long respawnTime = 50;
    /**
     * The combat cooldown delay.
     */
    private int combatCooldownDelay = 4;
    /**
     * The combat definition.
     */
    private CombatNPCDefinition combatDefinition;

    private final NPCDefinition definition;

    /**
     * Creates the NPC with the specified definition.
     *
     * @param definition The definition.
     */
    public NPC(NPCDefinition definition, Position pos) {
        super(pos);
        this.definition = definition;
        this.hp = definition.getHp();
        this.maxHp = definition.getMaxHp();
        this.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(SkillSet.getExperienceForLevel(maxHp), hp, maxHp));
        if (definition != null) {
            if (CombatNPCDefinition.forId(definition.getId()) != null) {
                this.combatDefinition = CombatNPCDefinition.forId(definition.getId());
            }
        }
    }

    /**
     * The definition.
     */
    public CombatNPCDefinition getCombatDef() {
        return combatDefinition;
    }

    /**
     * Gets the NPC definition.
     *
     * @return The NPC definition.
     */
    public NPCDefinition getDefinition() {
        return definition;
    }

    @Override
    public void send(Event event) {
        // TODO Auto-generated method stub
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getLeastX() {
        return leastX;
    }

    public void setLeastX(int leastX) {
        this.leastX = leastX;
    }

    public int getLeastY() {
        return leastY;
    }

    public void setLeastY(int leastY) {
        this.leastY = leastY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setWalkOverridden(boolean walkOverridden) {
        this.walkOverridden = walkOverridden;
    }

    /*@Override
    public void addToRegion(Region region) {
        region.addNpc(this);
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removeNpc(this);
    }*/

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public int getMaxHP() {
        return maxHp;
    }

    @Override
    public void damageCharacter(DamageEvent damage) {
        if (damage.getDamageDone() > getHP()) {
            damage.setDamageDone(getHP());
        }
        getBlockSet().add(SynchronizationBlock.createHitUpdateBlock(damage));
        int currentHp = getHP() - damage.getDamageDone();
        setHp(currentHp);
    }

    @Override
    public int getWidth() {
        return definition.getSize();
    }

    @Override
    public int getHeight() {
        return definition.getSize();
    }

    @Override
    public int getBonus(int index) {
        return combatDefinition.getBonus(index);
    }

    @Override
    public int getCombatCooldownDelay() {
        return combatCooldownDelay;
    }

    @Override
    public CombatAction getDefaultCombatAction() {
        return combatDefinition.getCombatAction();
    }

    @Override
    public boolean canDamage(Character victim) {
        //return combatDefinition != null;
        return true;
    }

    @Override
    public boolean subjectToWildernessChecks() {
        return false;
    }

    @Override
    public Spell getAutocastSpell() {
        return Spell.forId(combatDefinition.getSpell().getSpellId());
    }

    @Override
    public Animation getAttackAnimation() {
        return combatDefinition.getAttack();
    }

    @Override
    public Animation getDeathAnimation() {
        return combatDefinition.getDeath();
    }

    @Override
    public Animation getDefendAnimation() {
        return combatDefinition.getDefend();
    }

    @Override
    public boolean isAutoRetaliating() {
        return true;
    }

    /**
     * @param respawnTime the respawnTime to set
     */
    public void setRespawnTime(long respawnTime) {
        this.respawnTime = respawnTime;
    }

    /**
     * @return the respawnTime
     */
    public long getRespawnTime() {
        return respawnTime;
    }

    @Override
    public Position getCentrePosition() {
        return new Position(getPosition().getX() + (getWidth() / 2), getPosition().getY() + (getHeight() / 2), getPosition().getHeight());
    }

    @Override
    public int getProjectileLockonIndex() {
        return getIndex() + 1;
    }

    @Override
    public void setAutocastSpell(Spell spell) {
        if (spell != null) {
            combatDefinition.setSpell(spell);
        }
    }

    @Override
    public EventManager getEventManager() {
        return null;
    }

    @Override
    public boolean spellbookIgnore() {
        return true;
    }

    @Override
    public String getUndefinedName() {
        return "";
    }

    @Override
    public String getDefinedName() {
        return definition.getName();
    }

    @Override
    public boolean canBeTeleblocked() {
        return false;
    }

    @Override
    public boolean rangeWeaponIgnore() {
        return true;
    }

    @Override
    public int getArrows() {
        return 1;
    }

    @Override
    public int getWeapon() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public double getProtectionPrayerModifier() {
        return 0;
    }

    public void dropLoot(Player player) {
        NPCDrop[] constantDrops = combatDefinition.getConstantDrops();
        if (constantDrops == null) {
            return;
        }
        NPCDrop[] randomDrops = combatDefinition.getRandomDrops();
        if (randomDrops == null) {
            return;
        }

        for (int i = 0; i < constantDrops.length; i++) {
            if (constantDrops[i].getFrequency() >= 1) {
                GroundItem.getInstance().create(player, constantDrops[i].getItem().getId(), constantDrops[i].getItem().getAmount(), getPosition());
                System.out.println("Dropped item " + constantDrops[i].getItem().toString() + " at frequency " + constantDrops[i].getFrequency());
            }
        }
        if (randomDrops.length > 0) {
            NPCDrop drop = null;
            while (drop == null) {
                NPCDrop randomDrop = randomDrops[random.nextInt(randomDrops.length)];
                boolean alwaysDrop = randomDrop.getFrequency() >= 1;
                if (!alwaysDrop && random.nextDouble() < randomDrop.getFrequency()) {
                    drop = randomDrop;
                }
            }
            System.out.println("Dropped item " + drop.getItem().toString() + " at frequency " + drop.getFrequency());
            GroundItem.getInstance().create(player, drop.getItem().getId(), drop.getItem().getAmount(), this.getPosition());
        }

    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
