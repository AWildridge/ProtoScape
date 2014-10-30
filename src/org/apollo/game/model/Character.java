package org.apollo.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apollo.game.action.Action;
import org.apollo.game.action.ActionQueue;
import org.apollo.game.event.Event;
import org.apollo.game.event.EventManager;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.event.impl.DamageEvent.HitPriority;
import org.apollo.game.event.impl.ObjectAnimationEvent;
import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.event.impl.RemoveObjectEvent;
import org.apollo.game.event.impl.CreateObjectEvent;
import org.apollo.game.event.impl.SendProjectileEvent;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Inventory.StackMode;
import org.apollo.game.model.RequestManager.RequestState;
import org.apollo.game.model.RequestManager.RequestType;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;
import org.apollo.game.model.region.Region;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.impl.DeathTask;
import org.apollo.game.scheduling.impl.PoisonDrainTask;
import org.apollo.game.scheduling.impl.PrayerUpdateTask;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.scheduling.impl.SpecialEnergyRestoreTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.util.CharacterRepository;

/**
 * A {@link Character} is a living creature in the world, such as a player or
 * NPC.
 *
 * @author Graham
 */
public abstract class Character {

    /**
     * The default, i.e. spawn, location.
     */
    public static final Position DEFAULT_LOCATION = new Position(3222, 3219, 0);
    /**
     * The index of this character in the {@link CharacterRepository} it belongs
     * to.
     */
    private int index = -1;
    
    private boolean isMoving;
    /**
     * The random number generator.
     */
    public final Random random = new Random();
    /**
     * A set of hits done in this entity during the current update cycle.
     */
    private final List<DamageEvent> hits = new LinkedList<DamageEvent>();
    /**
     * Teleportation flag.
     */
    private boolean teleporting = false;
    /**
     * The walking queue.
     */
    private final WalkingQueue walkingQueue = new WalkingQueue(this);
    /*
     * This characters bonuses instance even Npcs can have bonuses
     * 
     */
    private final Bonuses bonus = new Bonuses(this);
    /**
     * The interaction mode.
     */
    private InteractionMode interactionMode;
    /**
     * The first direction.
     */
    private Direction firstDirection = Direction.NONE;
    /**
     * The second direction.
     */
    private Direction secondDirection = Direction.NONE;
    /**
     * The current position of this character.
     */
    private Position position;
    
    /**
     * The running flag.
     */
    public boolean isRunning;
    
    /**
     * The run energy flag.
     */
    public double runEnergy = 100;
    /**
     * The attack drained flag.
     */
    private boolean attackDrained;
    /**
     * The strength drained flag.
     */
    private boolean strengthDrained;
    /**
     * The defence drained flag.
     */
    private boolean defenceDrained;
    /**
     * Destroyed flag.
     */
    private boolean destroyed = false;

    /**
     * The combat state.
     */
    private final CombatState combatState = new CombatState(this);
    /**
     * The teleportation target.
     */
    private Position teleportTarget = null;
    /**
     * The centre of the last region the client has loaded.
     *
     */
    public Position lastKnownRegion;
    /**
     * A queue of actions.
     */
    private final ActionQueue actionQueue = new ActionQueue();
    /**
     * A list of local players.
     */
    private final List<Player> localPlayers = new ArrayList<Player>(); // TODO make a specialized collection?
    /**
     * A list of local NPCs.
     */
    private final List<NPC> localNPCs = new ArrayList<NPC>(); // TODO make a specialized collection?
    /**
     * A set of {@link SynchronizationBlock}s.
     */
    private SynchronizationBlockSet blockSet = new SynchronizationBlockSet();
    /**
     * Holds attribute tags applied to the character.
     */
    private ArrayList<String> attributeTags = new ArrayList<String>();
    /**
     * Holds attributes with values applied to the character.
     */
    private HashMap<String, Integer> attributes = new HashMap<String, Integer>();
    /**
     * The character's current action.
     */
    private Action<?> action; // TODO
    /**
     * The character's inventory.
     */
    private final Inventory inventory = new Inventory(InventoryConstants.INVENTORY_CAPACITY);
    /**
     * The character's equipment.
     */
    private final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);
    /**
     * The character's bank.
     */
    private final Inventory bank = new Inventory(InventoryConstants.BANK_CAPACITY, StackMode.STACK_ALWAYS);
    
    /**
     * The character's skill set.
     */
    private final SkillSet skillSet = new SkillSet();
    public boolean damageEventting;
    /**
     * The flag indicating if the run energy is being restored or not.
     */
    private boolean restoreEnergy;
    private BoundaryManager boundaryManager = new BoundaryManager();
    /**
     * The character's special energy update tick.
     */
    public SpecialEnergyRestoreTask specialUpdateTask;
    /**
     * The character's prayer update tick.
     */
    private PrayerUpdateTask prayerUpdateTask;
    /**
     * The character's poison drain tick.
     */
    private PoisonDrainTask poisonDrainTask;
    private Character interactingCharacter;
    private boolean isInCombat;
    private boolean isAggressor;

    /**
     * Creates a new character with the specified initial position.
     *
     * @param position The initial position of this character.
     */
    public Character(Position position) {
        this.position = position;
        init();
    }

    /**
     * Gets the active combat action.
     *
     * @return The active combat action.
     */
    public CombatAction getActiveCombatAction() {
        return CombatFormulae.getActiveCombatAction(this);
    }

    /**
     * Gets the width of the entity.
     *
     * @return The width of the entity.
     */
    public abstract int getWidth();

    /**
     * Gets the width of the entity.
     *
     * @return The width of the entity.
     */
    public abstract int getHeight();

    /**
     * Gets a bonus by its index.
     *
     * @param index The bonus index.
     * @return The bonus value.
     */
    public abstract int getBonus(int index);

    /**
     * Initialises this character.
     */
    private void init() {
        World.getWorld().schedule(new SkillNormalizationTask(this));
    }

    /**
     * Gets the character's inventory.
     *
     * @return The character's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /*
     * Get Bonuses
     * 
     */
    public Bonuses getBonuses() {
        return bonus;
    }

    /**
     * Gets the character's equipment.
     *
     * @return The character's equipment.
     */
    public Inventory getEquipment() {
        return equipment;
    }

    /**
     * Gets the character's bank.
     *
     * @return The character's bank.
     */
    public Inventory getBank() {
        return bank;
    }

    /**
     * Gets the local player list.
     *
     * @return The local player list.
     */
    public List<Player> getLocalPlayerList() {
        return localPlayers;
    }

    /**
     * Gets the local NPC list.
     *
     * @return The local NPC list.
     */
    public List<NPC> getLocalNPCList() {
        return localNPCs;
    }

    /**
     * @return attributes applied to the character.
     */
    public ArrayList<String> getAttributeTags() {
        return attributeTags;
    }

    /**
     * If an character has an attribute tag.
     *
     * @param tag The tag to check for.
     * @return <code>true</code> if the character has the tag
     * @return <code>false</code> if not
     */
    public boolean hasAttributeTag(String tag) {
        return attributeTags.contains(tag);
    }

    /**
     * Removes an array of tags.
     *
     * @param tags The array of tags to remove.
     */
    public void clearAttributeTags(String[] tags) {
        for (String tag : tags) {
            attributeTags.remove(tag);
        }
    }

    /**
     * @return advancedAttributes applied to the character.
     */
    public HashMap<String, Integer> getAttributes() {
        return (HashMap<String, Integer>) attributes;
    }

    /**
     * Gets an advanced attribute with a specified key.
     *
     * @param key The key to search for.
     * @return The attribute's value.
     */
    public int getAttribute(String key) {
        return attributes.get(key) == null ? 0 : attributes.get(key);
    }

    /**
     * Removes an array of keys.
     *
     * @param keys The array of keys to remove.
     */
    public void clearAttributes(String[] keys) {
        for (String key : keys) {
            attributeTags.remove(key);
        }
    }

    /**
     * Sets an advanced attribute.
     *
     * @param key The attribute key.
     * @param value The value of the attribute.
     */
    public void setAttribute(String key, int value) {
        attributes.put(key, value);
    }

    /**
     * Increases an attribute's value.
     *
     * @param key The attribute key.
     */
    public boolean increaseAttribute(String key) {
        if (attributes.get(key) == null) {
            return false;
        }
        setAttribute(key, attributes.get(key) + 1);
        return true;
    }

    /**
     * Increases an attribute by a specified amount.
     *
     * @param key The attribute key.
     * @param amount The increment amount.
     */
    public void increaseAttribute(String key, int amount) {
        for (int i = 1; i <= amount; i++) {
            increaseAttribute(key);
        }
    }

    /**
     * Decreases an attribute's value.
     *
     * @param key The attribute key.
     */
    public boolean decreaseAttribute(String key) {
        if (attributes.get(key) == null) {
            return false;
        }
        if (attributes.get(key) < 1) {
            return false;
        }
        setAttribute(key, attributes.get(key) - 1);
        return true;
    }

    /**
     * Decreases an attribute by a specified amount.
     *
     * @param key The attribute key.
     * @param amount The decrement amount.
     */
    public void decreaseAttribute(String key, int amount) {
        for (int i = 1; i <= amount; i++) {
            if (!decreaseAttribute(key)) {
                break;
            }
        }
    }

    /**
     * Checks if this player is currently teleporting.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isTeleporting() {
        return teleporting;
    }

    /**
     * Sets the teleporting flag.
     *
     * @param teleporting {@code true} if the player is teleporting,
     * {@code false} if not.
     */
    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public double getRunEnergy() {
        return runEnergy;
    }

    public void setRunEnergy(double runEnergy) {
        if (runEnergy > 100) {
            runEnergy = 100;
        }
        this.runEnergy = runEnergy;
        this.send(new SetInterfaceTextEvent(149, Integer.toString((int) this.getRunEnergy()).replace(" ", "") + "%"));
        if (runEnergy >= 100) {
            setRestoreEnergy(false);
        }
    }

    public boolean isRestoreEnergy() {
        return restoreEnergy;
    }

    public void setRestoreEnergy(boolean restoreEnergy) {
        this.restoreEnergy = restoreEnergy;
    }

    /**
     * Gets the walking queue.
     *
     * @return The walking queue.
     */
    public WalkingQueue getWalkingQueue() {
        return walkingQueue;
    }

    /**
     * Sets the next directions for this character.
     *
     * @param first The first direction.
     * @param second The second direction.
     */
    public void setDirections(Direction first, Direction second) {
        this.firstDirection = first;
        this.secondDirection = second;
    }

    /**
     * Gets the first direction.
     *
     * @return The first direction.
     */
    public Direction getFirstDirection() {
        return firstDirection;
    }

    /**
     * Gets the second direction.
     *
     * @return The second direction.
     */
    public Direction getSecondDirection() {
        return secondDirection;
    }

    /**
     * Gets the directions as an array.
     *
     * @return A zero, one or two element array containing the directions (in
     * order).
     */
    public Direction[] getDirections() {
        if (firstDirection != Direction.NONE) {
            if (secondDirection != Direction.NONE) {
                return new Direction[]{firstDirection, secondDirection};
            } else {
                return new Direction[]{firstDirection};
            }
        } else {
            return Direction.EMPTY_DIRECTION_ARRAY;
        }
    }

    /**
     * Removes this entity from the specified region.
     *
     * @param region The region.
     */
    //public abstract void removeFromRegion(Region region);

    /**
     * Adds this entity to the specified region.
     *
     * @param region The region.
     */
    //public abstract void addToRegion(Region region);

    /**
     * Gets the position of this character.
     *
     * @return The position of this character.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this character.
     *
     * @param position The position of this character.
     */
    public void setPosition(Position position) {
        this.position = position;
        /*TODO: Fix this. Doesn't let you log in right now.
        Region newRegion = World.getWorld().getRegionRepository().getRegionByPosition(position);
        Region currentRegion = World.getWorld().getRegionRepository().getRegionByPosition(this.lastKnownRegion);
        if (newRegion != currentRegion) {
            if (currentRegion != null) {
                removeFromRegion(currentRegion);
            }
            currentRegion = newRegion;
            addToRegion(currentRegion);
        }*/
    }

    /**
     * Checks if this character is active.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isActive() {
        return index != -1;
    }

    /**
     * Gets the index of this character.
     *
     * @return The index of this character.
     */
    public int getIndex() {
        synchronized (this) {
            return index;
        }
    }

    /**
     * Sets the index of this character.
     *
     * @param index The index of this character.
     */
    public void setIndex(int index) {
        synchronized (this) {
            this.index = index;
        }
    }

    /**
     * Gets the {@link SynchronizationBlockSet}.
     *
     * @return The block set.
     */
    public SynchronizationBlockSet getBlockSet() {
        return blockSet;
    }

    /**
     * Resets the block set.
     */
    public void resetBlockSet() {
        blockSet = new SynchronizationBlockSet();
    }

    /**
     * Sends an {@link Event} to either:
     * <ul>
     * <li>The client if this {@link Character} is a {@link Player}.</li>
     * <li>The AI routines if this {@link Character} is an NPC</li>
     * </ul>
     *
     * @param event The event.
     */
    public abstract void send(Event event);

    /**
     * Teleports this character to the specified position, setting the
     * appropriate flags and clearing the walking queue.
     *
     * @param position The position.
     */
    public void teleport(Position position) {
        this.teleporting = true;
        this.position = position;
        this.walkingQueue.clear();
        this.stopAction(); // TODO do it on any movement is a must.. walking queue perhaps?
    }

    /**
     * Plays the specified animation.
     *
     * @param animation The animation.
     */
    public void playAnimation(Animation animation) {
        blockSet.add(SynchronizationBlock.createAnimationBlock(animation));
    }

    /**
     * Stops the current animation.
     */
    public void stopAnimation() {
        playAnimation(Animation.STOP_ANIMATION);
    }

    /**
     * Gets the character's action sender.
     *
     * @return The character's action sender.
     */
    public abstract EventManager getEventManager();

    /**
     * Plays the specified graphic.
     *
     * @param graphic The graphic.
     */
    public void playGraphic(Graphic graphic) {
        blockSet.add(SynchronizationBlock.createGraphicBlock(graphic));
    }

    /**
     * Damages a Character.
     *
     * @param damage The damage.
     */
    public abstract void damageCharacter(DamageEvent damage);

    public void doubleDamageChracter(DamageEvent damage) {
        if (this.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() < damage.getDamageDone()) {
            damage.setDamageDone(getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel());
        }
        Skill skill = getSkillSet().getSkill(Skill.HITPOINTS);
        this.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(skill.getExperience(), skill.getCurrentLevel() - damage.getDamageDone(), skill.getMaximumLevel()));
        this.getBlockSet().add(SynchronizationBlock.createDoubleHitUpdateBlock(damage));
    }

    /**
     * The can be teleblocked flag.
     */
    public abstract boolean canBeTeleblocked();

    /**
     * Gets the mob's undefined name (Players).
     *
     * @return The mob's undefined name (Players).
     */
    public abstract String getUndefinedName();

    /**
     * Gets the mob's defined name.
     *
     * @return The mob's defined name.
     */
    public abstract String getDefinedName();

    public void damageHP(DamageEvent damage) {
        if (this.getEventManager() != null) {
            if (this.getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() < damage.getDamageDone()) {
                damage.setDamageDone(getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel());
            }
            Skill skill = getSkillSet().getSkill(Skill.HITPOINTS);
            this.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(skill.getExperience(), skill.getCurrentLevel() - damage.getDamageDone(), skill.getMaximumLevel()));
        } else {
            if (this.getHP() < damage.getDamageDone()) {
                damage.setDamageDone(this.getHP());
            }
            ((NPC) this).setHp(damage.getDamageDone());
        }
    }

    /**
     * Inflicts damage to the character.
     *
     * @param damageEvent The damage event to deal.
     * @param character The damage dealer.
     */
    public void inflictDamage(DamageEvent damageEvent, Character attacker) {
        combatState.setLastDamageEventTimer(10000);
        if (attacker != null) {
            combatState.setLastDamageEventBy(attacker);
        }

        if (!combatState.canTeleport() && !(getEventManager() != null && ((Player) this) != null
                && ((Player) this).getRequestManager().getRequestType() == RequestType.DUEL
                && ((Player) this).getRequestManager().getState() == RequestState.ACTIVE)) {
            return;
        }
        if (combatState.isDead()) {
            return;
        }

        if (damageEvent.getDamageDone() > getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel()) {
            damageEvent.setDamageDone(getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel());
        }

        getSkillSet().decreaseLevel(Skill.HITPOINTS, damageEvent.getDamageDone());
        if (combatState.getPrayer(Prayers.REDEMPTION)) {
            if (getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() < (getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel() * 0.10)) {
                combatState.resetPrayers();
                if (getEventManager() != null) {
                    sendMessage("You have run out of prayer points; you must recharge at an altar.");
                }
                Skill skill = getSkillSet().getSkill(Skill.PRAYER);
                getSkillSet().setSkill(Skill.PRAYER, new Skill(skill.getExperience(), 0, skill.getMaximumLevel()));
                getSkillSet().increaseLevel(Skill.HITPOINTS, (int) (getSkillSet().getSkill(Skill.PRAYER).getMaximumLevel() * 0.25));
                playGraphic(new Graphic(436));
            }
        }
        if (getEquipment().contains(2570)) {
            if (combatState.canTeleport() && getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() <= (getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel() * 0.10)) {
                initiateTeleport(TeleportType.NORMAL_TELEPORT, new Position(3225 + random.nextInt(1), 3218 + random.nextInt(1), 0));
                getEquipment().remove(new Item(2570, 1));
                if (getEventManager() != null) {
                    sendMessage("Your Ring of Life saves you and is destroyed in the process.");
                }
            }
        }
        if (getDamageEventQueue().size() >= 4) {
            damageEvent = new DamageEvent(damageEvent.getDamageDone(), HitPriority.LOW_PRIORITY, this.getHP(), this.getMaxHP());//if multiple people are damageEventting on an opponent, this prevents damageEvents from stacking up for a long time and looking off-beat
        }
        getDamageEventQueue().add(damageEvent);
        if (attacker != null) {
            getCombatState().getDamageMap().incrementTotalDamage(attacker, damageEvent.getDamageDone());
        }
        if (getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel() <= 0 && !combatState.isDead()) {
            combatState.setDead(true);
            getActionQueue().clearNonWalkableActions();
            getActionQueue().cancelQueuedActions();
            resetInteractingCharacter();
            getWalkingQueue().clear();
            final Character thisCharacter = this;
            if (combatState.getPrayer(Prayers.RETRIBUTION)) {
                playGraphic(new Graphic(437, 40));
                final ArrayList<Position> positionsUsed = new ArrayList<Position>();
                World.getWorld().schedule(new ScheduledTask(2, false) {

                    @Override
                    public void execute() {
                        for (Character character : World.getWorld().getCharacterRepository()) {
                            if (!character.getCombatState().isDead()) {
                                if (combatState.getLastDamageEventTimer() > (System.currentTimeMillis() + 4000)) { //10 cycles for tagging timer
                                    if (combatState.getLastDamageEventBy() != null && character != combatState.getLastDamageEventBy()) {
                                        if (getEventManager() != null) {
                                            sendMessage("I'm already under attack.");
                                        }
                                        continue;
                                    }
                                }
                                if (character.getCombatState().getLastDamageEventTimer() > (System.currentTimeMillis() + 4000)) { //10 cycles for tagging timer
                                    if (character.getCombatState().getLastDamageEventBy() != null && getCharacter() != character.getCombatState().getLastDamageEventBy()) {
                                        if (getEventManager() != null) {
                                            sendMessage("Someone else is already fighting your opponent.");
                                        }
                                        continue;
                                    }
                                }
                                if (character.getPosition().isNextTo(getPosition()) && !positionsUsed.contains(character.getPosition())) {
                                    positionsUsed.add(character.getPosition());
                                    int dmg = random.nextInt((int) (getSkillSet().getSkill(Skill.PRAYER).getMaximumLevel() * 0.25)); // +1 as its exclusive
                                    character.inflictDamage(new DamageEvent(dmg, character.getHP(), character.getMaxHP()), thisCharacter);
                                }
                            }
                        }
                    }
                });
            }
            World.getWorld().schedule(new ScheduledTask(3, false) {

                public void execute() {
                    playAnimation(getDeathAnimation());
                    this.stop();
                }
            });
            if (this instanceof NPC && ((NPC) this).getRespawnTime() != -1) {
                World.getWorld().schedule(new ScheduledTask((int) ((NPC) this).getRespawnTime(), false) {

                    @Override
                    public void execute() {
                        int id = ((NPC) Character.this).getDefinition().getId();
                        Position position = ((NPC) Character.this).getPosition();
                        NPCManager.appendNpc(id, position);
                        this.stop();
                    }
                });
            }
            World.getWorld().schedule(new DeathTask(thisCharacter, 8));
        }
    }

    /**
     * Initiates a teleport. TODO is this the best place?
     *
     * @param teleport The teleport.
     */
    public boolean initiateTeleport(TeleportType teleport, final Position position) {
        getWalkingQueue().clear();
        combatState.setCanTeleport(false);
        switch (teleport) {
            case NORMAL_TELEPORT:
                playAnimation(Animation.create(714));
                playGraphic(Graphic.create(301, 0, 100));
                World.getWorld().schedule(new ScheduledTask(3, false) {

                    @Override
                    public void execute() {
                        playAnimation(Animation.create(715));
                        setTeleportTarget(position);
                        this.stop();
                    }
                });
                World.getWorld().schedule(new ScheduledTask(5, false) {

                    @Override
                    public void execute() {
                        combatState.setCanTeleport(true);
                        this.stop();
                    }
                });
                break;
            case ANCIENT_TELEPORT:
                playAnimation(Animation.create(1979));
                playGraphic(Graphic.create(392));
                World.getWorld().schedule(new ScheduledTask(3, false) {

                    @Override
                    public void execute() {
                        setTeleportTarget(position);
                        this.stop();
                    }
                });
                World.getWorld().schedule(new ScheduledTask(4, false) {

                    @Override
                    public void execute() {
                        combatState.setCanTeleport(true);
                        this.stop();
                    }
                });
                break;
        }
        return true;
    }

    /**
     * Checks if the character can teleport.
     *
     * @return If a character can teleport.
     */
    public boolean canTeleport() {
        if (!combatState.canTeleport()) {
            return false;
        } else if (combatState.isTeleblocked()) {
            sendMessage("A magical force stops you from teleporting.");
            return false;
        } else {
            return true;
        }
    }

    public void forceChat(String text) {
        blockSet.add(SynchronizationBlock.createForceChat(text));
    }

    /**
     * Stops the current graphic.
     */
    public void stopGraphic() {
        playGraphic(Graphic.STOP_GRAPHIC);
    }

    /**
     * Sends a projectile to the position.
     *
     * @param position The position.
     * @param offsetX The offsetX.
     * @param offsetY The offsetY.
     * @param startHeight The start height.
     * @param endHeight The endHeight.
     * @param lockon The lock on.
     * @param id The id.
     * @param speed The speed.
     */
    public void playProjectile(Position position, byte offsetX, byte offsetY, int id, int delay, int angle, int speed, int startHeight, int endHeight, int lockon, int slope, int radius) {
        send(new PositionEvent(position, this.getPosition()));
        send(new SendProjectileEvent(offsetX, offsetY, id, delay, angle, speed, startHeight, endHeight, lockon, slope, radius));
    }

    /**
     * Sends a new object to the position.
     *
     * @param position The position.
     * @param id The object id.
     * @param type The object type.
     * @param face The object face.
     */
    public void sendNewObject(Position position, int id, int face, int type) {
        send(new PositionEvent(this.getPosition(), position));
        send(new CreateObjectEvent(id, face, type));
    }

    public void sendObjectAnimation(Position position, Animation anim, int tileObjectType, int orientation) {
        send(new PositionEvent(position, this.getPosition()));
        send(new ObjectAnimationEvent(tileObjectType, orientation, anim));
    }

    /**
     * Removes a object on the position.
     *
     * @param position The position.
     * @param type The object type.
     * @param face The object face.
     */
    public void sendRemoveObject(Position position, int face, int type) {
        send(new PositionEvent(position, position));
        send(new RemoveObjectEvent(face, type));
    }

    /**
     * Gets the character's skill set.
     *
     * @return The character's skill set.
     */
    public SkillSet getSkillSet() {
        return skillSet;
    }

    /**
     * Starts a new action, stopping the current one if it exists.
     *
     * @param action The new action.
     * @return A flag indicating if the action was started.
     */
    public boolean startAction(Action<?> action) {
        if (this.action != null) {
            if (this.action.equals(action)) {
                return false;
            }
            stopAction();
        }
        this.action = action;
        World.getWorld().schedule(action);
        return true; // TODO maybe this should be incorporated into the action class itself?
    }

    /**
     * Stops the current action.
     */
    public void stopAction() {
        if (action != null) {
            action.stop();
            if (this.hasAttributeTag("skilling")) {
                this.getAttributeTags().remove("skilling");
            }
            action = null;
        }
    }

    /**
     * Gets this mob instance.
     *
     * @return This mob instance.
     */
    public Character getCharacter() {
        return this;

    }

    /**
     * Turns the character to face the specified position.
     *
     * @param position The position to face.
     */
    public void turnTo(Position position) {
        blockSet.add(SynchronizationBlock.createTurnToPositionBlock(position));
    }

    /**
     * Sends a message to the character.
     *
     * @param message The message.
     */
    public void sendMessage(String message) {
        send(new ServerMessageEvent(message));
    }

    /**
     * @return the boundary manager of the character.
     */
    public BoundaryManager getBoundaryManager() {
        return boundaryManager;
    }

    /**
     * Checks if this entity has a target to teleport to.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean hasTeleportTarget() {
        return teleportTarget != null;
    }

    /**
     * Gets the teleport target.
     *
     * @return The teleport target.
     */
    public Position getTeleportTarget() {
        return teleportTarget;
    }

    /**
     * Sets the teleport target.
     *
     * @param teleportTarget The target location.
     */
    public void setTeleportTarget(Position teleportTarget) {
        getWalkingQueue().clear();
        this.teleportTarget = teleportTarget;
    }

    /**
     * Resets the teleport target.
     */
    public void resetTeleportTarget() {
        this.teleportTarget = null;
    }

    /**
     * Checks if this entity has been destroyed.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Gets the combat state.
     *
     * @return The combat state.
     */
    public CombatState getCombatState() {
        return combatState;
    }

    /**
     * Gets the hit queue.
     *
     * @return The hit queue.
     */
    public List<DamageEvent> getDamageEventQueue() {
        return hits;
    }

    public abstract int getHP();

    public abstract int getMaxHP();

    /**
     * Gets the character's default combat action.
     *
     * @return The character's default combat action.
     */
    public abstract CombatAction getDefaultCombatAction();

    /**
     * Gets the character's autocast spell.
     *
     * @return The character's autocast spell.
     */
    public abstract Spell getAutocastSpell();

    /**
     * Gets the mob's arrows.
     *
     * @return The mob's arrows.
     */
    public abstract int getArrows();

    /**
     * Gets the mob's weapon.
     *
     * @return The mob's weapon.
     */
    public abstract int getWeapon();

    /**
     * Gets the mob's spellbook ignore flag, if true, they can cast any spell
     * from any spellbook.
     *
     * @return The mob's spellbook ignore flag.
     */
    public abstract boolean spellbookIgnore();

    /**
     * Gets the mob's range weapon ignore flag, if true, they can range without
     * an item in the weapon slot.
     *
     * @return The mob's range weapon ignore flag.
     */
    public abstract boolean rangeWeaponIgnore();

    /**
     * @return the attackDrained
     */
    public boolean isAttackDrained() {
        return attackDrained;
    }

    /**
     * @param attackDrained the attackDrained to set
     */
    public void setAttackDrained(boolean attackDrained) {
        this.attackDrained = attackDrained;
    }

    /**
     * @return the strengthDrained
     */
    public boolean isStrengthDrained() {
        return strengthDrained;
    }

    /**
     * @param strengthDrained the strengthDrained to set
     */
    public void setStrengthDrained(boolean strengthDrained) {
        this.strengthDrained = strengthDrained;
    }

    /**
     * @return the defenceDrained
     */
    public boolean isDefenceDrained() {
        return defenceDrained;
    }

    /**
     * @param defenceDrained the defenceDrained to set
     */
    public void setDefenceDrained(boolean defenceDrained) {
        this.defenceDrained = defenceDrained;
    }

    /**
     * The protection prayer modifier. EG: NPCs = 1, players = 0.6.
     */
    public abstract double getProtectionPrayerModifier();

    /**
     * Sets the mob's autocast spell.
     *
     * @param spell The spell to set.
     */
    public abstract void setAutocastSpell(Spell spell);

    /**
     * Gets the damageEvent flag defined by the entity type (EG Players
     * wilderness level)
     *
     * @param victim The victim.
     * @return The damageEvent flag.
     */
    public abstract boolean canDamage(Character victim);

    /**
     * Gets the centre position of the entity.
     *
     * @return The centre position of the entity.
     */
    public abstract Position getCentrePosition();

    /**
     * Gets the projectile lockon index of this character.
     *
     * @return The projectile lockon index of this character.
     */
    public abstract int getProjectileLockonIndex();

    /**
     * Gets the character's attack animation.
     *
     * @return The character's attack animation.
     */
    public abstract Animation getAttackAnimation();

    /**
     * Gets the character's defend animation.
     *
     * @return The character's defend animation.
     */
    public abstract Animation getDefendAnimation();

    /**
     * Gets the character's death animation.
     *
     * @return The character's death animation.
     */
    public abstract Animation getDeathAnimation();

    /**
     * Gets the action queue.
     *
     * @return The action queue.
     */
    public ActionQueue getActionQueue() {
        return actionQueue;
    }

    /**
     * Checks if this entity will auto retaliate to any attacks.
     *
     * @return <code>true</code> if the entity will auto retaliate,
     * <code>false</code> if not.
     */
    public abstract boolean isAutoRetaliating();

    /**
     * The wilderness check flag.
     *
     * @return The wilderness check flag.
     */
    public abstract boolean subjectToWildernessChecks();

    /**
     * @param specialUpdateTask The specialUpdateTask to set.
     */
    public void setSpecialUpdateTask(SpecialEnergyRestoreTask specialUpdateTask) {
        this.specialUpdateTask = specialUpdateTask;
    }

    /**
     * Gets the character's special energy update tick.
     *
     * @return The character's prayer energy tick.
     */
    public SpecialEnergyRestoreTask getSpecialUpdateTask() {
        return specialUpdateTask;
    }

    /**
     * @param prayerUpdateTask The prayerUpdateTask to set.
     */
    public void setPrayerUpdateTask(PrayerUpdateTask prayerUpdateTask) {
        this.prayerUpdateTask = prayerUpdateTask;
    }

    /**
     * Gets the character's prayer update tick.
     *
     * @return The character's prayer update tick.
     */
    public PrayerUpdateTask getPrayerUpdateTask() {
        return prayerUpdateTask;
    }

    /**
     * @param poisonDrainTask the poisonDrainTask to set
     */
    public void setPoisonDrainTask(PoisonDrainTask poisonDrainTask) {
        this.poisonDrainTask = poisonDrainTask;
    }

    /**
     * Gets the character's poison drain tick.
     *
     * @return The character's poisonDrainTask.
     */
    public PoisonDrainTask getPoisonDrainTask() {
        return poisonDrainTask;
    }

    /**
     * Gets the current combat cooldown delay in milliseconds.
     *
     * @return The current combat cooldown delay.
     */
    public abstract int getCombatCooldownDelay();

    /**
     * Gets the interaction mode.
     *
     * @return The interaction mode.
     */
    public InteractionMode getInteractionMode() {
        return interactionMode;
    }

    /**
     * Checks if this character is interacting with another character.
     *
     * @return The character interaction flag.
     */
    public boolean isInteracting() {
        return interactingCharacter != null;
    }

    /**
     * Sets the interacting character.
     *
     * @param character The new character to interact with.
     */
    public void setInteractingCharacter(Character character) {
        this.interactingCharacter = character;
        this.turnTo(character.position);
    }

    /**
     * Sets the interacting entity.
     *
     * @param mode The interaction mode.
     * @param character The new entity to interact with.
     */
    public void setInteractingCharacter(InteractionMode mode, Character character) {
        this.interactionMode = mode;
        this.interactingCharacter = character;
        if (character != null) {
            this.turnTo(character.position);
        }
    }

    /**
     * Resets the interacting character.
     */
    public void resetInteractingCharacter() {
        this.interactingCharacter = null;
    }

    /**
     * Gets the interacting character.
     *
     * @return The character to interact with.
     */
    public Character getInteractingCharacter() {
        return interactingCharacter;
    }

    public boolean getInteractingCharacter(Character character) {
        if (interactingCharacter == character) {
            return true;
        }
        return false;
    }

    /**
     * Set the character's combat state.
     *
     * @param isInCombat This character's combat state.
     */
    public void setInCombat(boolean isInCombat) {
        this.isInCombat = isInCombat;
    }

    public boolean isInCombat() {
        return isInCombat;
    }

    /**
     * Gets the character's aggressor state.
     *
     * @return boolean The character's aggressor state.
     */
    public boolean getAggressorState() {
        return isAggressor;
    }

    /**
     * Sets the aggressor state for this character.
     */
    public void setAggressorState(boolean b) {
        isAggressor = b;
    }

    /**
     * The interaction mode.
     *
     * @author Graham Edgecombe
     *
     */
    public enum InteractionMode {

        ATTACK, TALK, FOLLOW, REQUEST
    }

    /**
     * An enum of teleport types.
     *
     * @author Michael
     */
    public enum TeleportType {

        NORMAL_TELEPORT, LUNAR_TELEPORT, ANCIENT_TELEPORT
    }
}
