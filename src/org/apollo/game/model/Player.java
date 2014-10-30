package org.apollo.game.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apollo.game.content.Cannon;
import org.apollo.game.content.WeaponInterfaces;
import org.apollo.game.content.DwarfCannon.BuildingStates;
import org.apollo.game.content.cluescroll.ClueScrolls;
import org.apollo.game.content.quest.QuestHolder;
import org.apollo.game.content.skills.farming.Farming;
import org.apollo.game.content.skills.farming.FarmingPatches;
import org.apollo.game.content.skills.slayer.Task;
import org.apollo.game.event.Event;
import org.apollo.game.event.EventManager;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.event.impl.PrivateChatLoadedEvent;
import org.apollo.game.event.impl.PlayerMenuEvent;
import org.apollo.game.event.impl.SendConfigEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.SetSidebarEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.minigames.FightCaves;
import org.apollo.game.minigames.castle.CastleWars;
import org.apollo.game.model.RequestManager.RequestState;
import org.apollo.game.model.combat.CombatAction;
import org.apollo.game.model.combat.CombatFormulae;
import org.apollo.game.model.combat.CombatState.CombatStyle;
import org.apollo.game.model.combat.action.MagicCombatAction.Spell;
import org.apollo.game.model.duel.Duel;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.BankPin;
import org.apollo.game.model.inter.dialog.Dialogue;
import org.apollo.game.model.inv.AppearanceInventoryListener;
import org.apollo.game.model.inv.FullInventoryListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.game.model.region.Region;
import org.apollo.game.model.skill.LevelUpSkillListener;
import org.apollo.game.model.skill.SkillListener;
import org.apollo.game.model.skill.SynchronizationSkillListener;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.net.session.GameSession;
import org.apollo.security.PlayerCredentials;

/**
 * A {@link Player} is a {@link Character} that a user is controlling.
 *
 * @author Graham
 */
public final class Player extends Character {

    /**
     * The player's shop id.
     */
    public int shopId;

    /**
     * The player's host.
     */
    private String playerhost;

    /**
     * The autocasting spell.
     */
    private Spell autocastSpell;

    /**
     * The auto retaliate flag of this player.
     */
    private boolean autoRetaliate;

    /**
     * The current wild level this player is in.
     */
    private int wildLevel;
    
    /**
     * The request manager which manages trading and duelling requests.
     */
    private final RequestManager requestManager = new RequestManager(this);
    
    /**
     * The quest holder.
     */
    private final QuestHolder questHolder = new QuestHolder(this);
    
    /**
     * The bank pin for this player.
     *
     * Will be set to default because not all players even want a bank pin set.
     */
    private BankPin bankPin;
    
    /**
     * The slayer task for this player.
     */
    private Task task;
    
    /**
     * The player's EventManager.
     */
    private EventManager eventManager = new EventManager(this);
    
    /**
     * The flag for checking if the player has a pin set or not.
     */
    private boolean pinSet;
    
    /**
     * The fight caves minigame instance.
     */
    private FightCaves fightCave = new FightCaves(this);
    /**
     * farming patch!
     */
    private FarmingPatches farmingPatch = new FarmingPatches(this);
    /**
     * The current dialogue of this player.
     */
    private Dialogue dialog;
    
    /**
     * The player's personal multi-dwarf cannon.
     */
    private Cannon cannon;
    
    /**
     * The player's building state of the cannon.
     */
    private BuildingStates build;
    
    /**
     * The fletching variable; used so that crafting/fletching do not intertwine
     */
    private boolean fletching;
    /**
     * crafting!
     */
    private boolean crafting;
    /**
     * duel arena!
     */
    private Duel duel = new Duel(this);

    /**
     * clue scroll rewards!
     */
    private ClueScrolls clues = new ClueScrolls(this);

    //TODO: Redo Farming
    public int farmingConfig[] = new int[Farming.MAX_PATCHES]; //20 patches?
    public int farmingState[] = new int[Farming.MAX_PATCHES]; //20 states for patches
    
    /**
     * Eating timer
     */
    private byte foodTimer;
    
    /**
     * A temporary queue of events sent during the login process.
     */
    private final Queue<Event> queuedEvents = new ArrayDeque<Event>();
    
    /**
     * The player's credentials.
     */
    private PlayerCredentials credentials;
    
    /**
     * The privilege level.
     */
    private PrivilegeLevel privilegeLevel = PrivilegeLevel.STANDARD;
    
    /**
     * The membership flag.
     */
    private boolean members = false;
    
    /**
     * A flag indicating if the player has designed their character.
     *
     */
    private boolean designedCharacter = false;
    
    /**
     * The {@link GameSession} currently attached to this {@link Player}.
     */
    private GameSession session;
    
    /**
     * A flag indicating if the region changed in the last cycle.
     *
     */
    private boolean regionChanged = false;
    
    /**
     * The player's appearance.
     *
     */
    private Appearance appearance = Appearance.DEFAULT_APPEARANCE;
    
    /**
     * The current maximum viewing distance of this player.
     *
     */
    private int viewingDistance = 1;
    
    /**
     * A flag which indicates there are players that couldn't be added.
     *
     */
    private boolean excessivePlayers = false;
    
    /**
     * This player's interface set.
     *
     */
    private final InterfaceSet interfaceSet = new InterfaceSet(this);
    
    /*
     * Farming set
     */
    //TODO: This isn't right...
    private final Farming farming = new Farming(this);

    /**
     * A flag indicating if the player is withdrawing items as notes.
     */
    private boolean withdrawingNotes = false; // TODO find a better place!
    /*
     * Trading
     *
     */
    private Player[] tradeOpp = new Player[2]; // 0 - actually in the duel, 1 is reqest.
    private int tradeStage;
    public List<Item> offeredItems = new ArrayList<Item>();
    private Inventory offer = new Inventory(Trade.SIZE, Inventory.StackMode.STACK_STACKABLE_ITEMS);
    /**
     * The player's friends.
     */
    private Friend friends;

    /**
     * Creates the {@link Player}.
     *
     * @param credentials The player's credentials.
     * @param position The initial position.
     */
    public Player(PlayerCredentials credentials, Position position) {
        super(position);
        init();
        this.credentials = credentials;
    }

    public Duel getDuel() {
        return duel;
    }

    public ClueScrolls getClues() {
        return clues;
    }

    /**
     * Gets the fight caves {@link fc}
     *
     * @return {@link fc}
     */
    public FightCaves getFightCaves() {
        return fightCave;
    }

    public QuestHolder getQuestHolder() {
        return questHolder;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getBonus(int index) {
        return Bonuses.getBonus(index);
    }

    @Override
    public CombatAction getDefaultCombatAction() {
        return null;
    }

    @Override
    public boolean canDamage(Character victim) {
        if (BoundaryManager.isWithinBoundaryNoZ(getPosition(), "DuelArenaFight") && BoundaryManager.isWithinBoundaryNoZ(victim.getPosition(), "DuelArenaFight")) {
            if (victim != getRequestManager().getAcquaintance()) {
                sendMessage("That player is not your target.");
                return false;
            } else if (getRequestManager().getState() != RequestState.ACTIVE) {
                sendMessage("The duel hasn't started yet.");
                return false;
            }
        } else if (victim instanceof Player && CastleWars.getCastleWars().inAnyTeam((Player) victim)) {
            if (CastleWars.getCastleWars().isEnemy(this, (Player) victim)) {
                return true;
            } else {
                sendMessage("You cannot attack your teammates");
                return false;
            }
        } else if (victim.subjectToWildernessChecks()) {
            if (!BoundaryManager.isWithinBoundaryNoZ(getPosition(), "Wilderness")) {
                sendMessage("You need to be within the wilderness to attack another player.");
                return false;
            } else if (!BoundaryManager.isWithinBoundaryNoZ(victim.getPosition(), "Wilderness")) {
                sendMessage("You cannot attack another player outside of the wilderness");
                return false;
            }
            int myWildernessLevel = 1 + (getPosition().getY() - 3520) / 8;
            int victimWildernessLevel = 1 + (victim.getPosition().getY() - 3520) / 8;
            int combatDifference = 0;
            if (getSkillSet().getCombatLevel() > victim.getSkillSet().getCombatLevel()) {
                combatDifference = getSkillSet().getCombatLevel() - victim.getSkillSet().getCombatLevel();
            } else if (victim.getSkillSet().getCombatLevel() > getSkillSet().getCombatLevel()) {
                combatDifference = victim.getSkillSet().getCombatLevel() - getSkillSet().getCombatLevel();
            }
            if (combatDifference > myWildernessLevel || combatDifference > victimWildernessLevel) {
                sendMessage("Your level difference is too great!");
                sendMessage("You need to move deeper into the wilderness.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean subjectToWildernessChecks() {
        return true;
    }

    @Override
    public Spell getAutocastSpell() {
        return autocastSpell;
    }

    @Override
    public Animation getAttackAnimation() {
        return getCombatState().getCombatStyle() == CombatStyle.AGGRESSIVE_1 ? Animation.create(423) : Animation.create(422);
    }

    @Override
    public Animation getDefendAnimation() {
        return (getEquipment().get(EquipmentConstants.SHIELD) != null || getEquipment().get(EquipmentConstants.SHIELD) != null) ? Animation.create(404) : Animation.create(424);
    }

    @Override
    public Animation getDeathAnimation() {
        return Animation.create(2304);
    }

    @Override
    public boolean isAutoRetaliating() {
        return autoRetaliate;
    }

    @Override
    public Position getCentrePosition() {
        return getPosition();
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    @Override
    public int getProjectileLockonIndex() {
        return -getIndex() - 1;
    }

    public Farming getFarming() {
        return farming;
    }

    /**
     * Gets this player's interface set.
     *
     * @return The interface set for this player.
     */
    public InterfaceSet getInterfaceSet() {
        return interfaceSet;
    }

    public void sendNewGlobalObject(Position position, int id, int face, int type) {
        for (Player player : World.getWorld().getPlayerRepository()) {
            player.sendNewObject(position, id, face, type);
        }
    }

    public void sendRemoveObjectGlobally(Position position, int face, int type) {
        for (Player player : World.getWorld().getPlayerRepository()) {
            player.sendRemoveObject(position, face, type);
        }
    }

    /**
     * Checks if there are excessive players.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isExcessivePlayersSet() {
        return excessivePlayers;
    }

    /**
     * Sets the excessive players flag.
     */
    public void flagExcessivePlayers() {
        excessivePlayers = true;
    }

    /**
     * Resets the excessive players flag.
     */
    public void resetExcessivePlayers() {
        excessivePlayers = false;
    }

    /**
     * Resets this player's viewing distance.
     */
    public void resetViewingDistance() {
        viewingDistance = 1;
    }

    /**
     * Gets this player's viewing distance.
     *
     * @return The viewing distance.
     */
    public int getViewingDistance() {
        return viewingDistance;
    }

    /**
     * Increments this player's viewing distance if it is less than the maximum
     * viewing distance.
     */
    public void incrementViewingDistance() {
        if (viewingDistance < Position.MAX_DISTANCE) {
            viewingDistance++;
        }
    }

    /**
     * Decrements this player's viewing distance if it is greater than 1.
     */
    public void decrementViewingDistance() {
        if (viewingDistance > 1) { // TODO should it be 0?
            viewingDistance--;
        }
    }

    /*@Override
    public void addToRegion(Region region) {
        region.addPlayer(this);
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removePlayer(this);
    }*/

    /**
     * Checks if this player has ever known a region.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasLastKnownRegion() {
        return lastKnownRegion != null;
    }

    /**
     * Gets the last known region.
     *
     * @return The last known region, or {@code null} if the player has never
     * known a region.
     */
    public Position getLastKnownRegion() {
        return lastKnownRegion;
    }

    /**
     * Sets the last known region.
     *
     * @param lastKnownRegion The last known region.
     */
    public void setLastKnownRegion(Position lastKnownRegion) {
        this.lastKnownRegion = lastKnownRegion;
    }

    /**
     * Gets the request manager.
     *
     * @return The request manager.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Gets the privilege level.
     *
     * @return The privilege level.
     */
    public PrivilegeLevel getPrivilegeLevel() {
        return privilegeLevel;
    }

    /**
     * Sets the privilege level.
     *
     * @param privilegeLevel The privilege level.
     */
    public void setPrivilegeLevel(PrivilegeLevel privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    /**
     * Checks if this player account has membership.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * Changes the membership status of this player.
     *
     * @param members The new membership flag.
     */
    public void setMembers(boolean members) {
        this.members = members;
    }

    public String getPlayerhost() {
        return playerhost;
    }

    public void setSession(GameSession session, boolean reconnecting, String host) {
        this.session = session;
        this.playerhost = host;
        if (!reconnecting) {
            sendInitialEvents();
        }
        getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
    }

    /**
     * Gets the player's credentials.
     *
     * @return The player's credentials.
     */
    public PlayerCredentials getCredentials() {
        return credentials;
    }

    @Override
    public void send(Event event) {
        if (isActive()) {
            if (!queuedEvents.isEmpty()) {
                for (Event queuedEvent : queuedEvents) {
                    session.dispatchEvent(queuedEvent);
                }
                queuedEvents.clear();
            }
            session.dispatchEvent(event);
        } else {
            queuedEvents.add(event);
        }
    }

    /**
     * initializes this player.
     */
    private void init() {
        initInventories();
        initSkills();

        //set running to walk.
        send(new SendConfigEvent(173, 0));
        updateQuestTab();
    }

    /**
     * Updates the quest tab for the player.
     */
    public void updateQuestTab() {
        if (getQuestHolder().hasDoneQuest(getQuestHolder().getCookAssistant())) {
            send(new SetInterfaceTextEvent(7333, "@gre@Cook's Assistant"));
        } else if (getQuestHolder().hasStartedQuest(getQuestHolder().getCookAssistant())) {
            send(new SetInterfaceTextEvent(7333, "@yel@Cook's Assistant"));
        }
        if (getQuestHolder().hasDoneQuest(getQuestHolder().getBlackKnightFortress())) {
            send(new SetInterfaceTextEvent(7332, "@gre@Black Knights' Fortress"));
        } else if (getQuestHolder().hasStartedQuest(getQuestHolder().getBlackKnightFortress())) {
            send(new SetInterfaceTextEvent(7332, "@yel@Black Knights' Fortress"));
        }
    }

    /**
     * initializes the player's skills.
     */
    private void initSkills() {
        SkillSet skills = getSkillSet();

        // synchronization listener
        SkillListener syncListener = new SynchronizationSkillListener(this);

        // level up listener
        SkillListener levelUpListener = new LevelUpSkillListener(this);

        // add the listeners
        skills.addListener(syncListener);
        skills.addListener(levelUpListener);
    }

    /**
     * initializes the player's inventories.
     */
    private void initInventories() {
        Inventory inventory = getInventory();
        Inventory bank = getBank();
        Inventory equipment = getEquipment();

        // TODO only add bank listener when it is open? (like Hyperion)
        // inventory full listeners
        InventoryListener fullInventoryListener = new FullInventoryListener(this, FullInventoryListener.FULL_INVENTORY_MESSAGE);
        InventoryListener fullBankListener = new FullInventoryListener(this, FullInventoryListener.FULL_BANK_MESSAGE);
        InventoryListener fullEquipmentListener = new FullInventoryListener(this, FullInventoryListener.FULL_EQUIPMENT_MESSAGE);

        // equipment appearance listener
        InventoryListener appearanceListener = new AppearanceInventoryListener(this);

        // synchronization listeners
        InventoryListener syncInventoryListener = new SynchronizationInventoryListener(this, SynchronizationInventoryListener.INVENTORY_ID);
        InventoryListener syncBankListener = new SynchronizationInventoryListener(this, BankConstants.BANK_INVENTORY_ID);
        InventoryListener syncEquipmentListener = new SynchronizationInventoryListener(this, SynchronizationInventoryListener.EQUIPMENT_ID);

        // add the listeners
        inventory.addListener(syncInventoryListener);
        inventory.addListener(fullInventoryListener);
        bank.addListener(syncBankListener);
        bank.addListener(fullBankListener);
        equipment.addListener(syncEquipmentListener);
        equipment.addListener(appearanceListener);
        equipment.addListener(fullEquipmentListener);
    }

    /**
     * Sends the initial events.
     */
    private void sendInitialEvents() {
        // vital initial stuff
        send(new IdAssignmentEvent(getIndex(), members));
        sendMessage("Welcome to ProtoScape.");

        // character design screen
        if (!designedCharacter) {
            interfaceSet.openWindow(3559);
        }

        // tabs TODO make a constant? look at player settings
        int[] tabs = {
            // 6299 = music tab, music disabled
            // 4445 = settings tab, music disabled
            // 12855 = ancients magic
            2423, 3917, 638, 3213, 1644, 5608, 1151, -1, 5065, 5715, 2449, 904, 147, 962,};
        for (int i = 0; i < tabs.length; i++) {
            send(new SwitchTabInterfaceEvent(i, tabs[i]));
        }

        // force skills to update
        getSkillSet().forceRefresh();
        getBonuses().refreshBonuses();
        GroundItem.getInstance().login(this);
        WeaponInterfaces.UpdateWep(this);
        getInventory().forceRefresh();
        getEquipment().forceRefresh();

        send(new PrivateChatLoadedEvent(2));
        getFriends().setLoaded(true);
        PrivateChat.getInstance().login(this);
        PrivateChat.getInstance().forceRefresh(this);
        PrivateChat.getInstance().dispatch();

        /**
         * Sends the players menus
         */
        send(new PlayerMenuEvent(3, true, "Attack"));
        send(new PlayerMenuEvent(4, false, "Follow"));
        send(new PlayerMenuEvent(5, false, "Trade with"));

    }

    /**
     * Send's configs to the player.
     *
     * @param configId The config ID.
     * @param state The state id of the config.
     */
    public void sendConfig(int configId, int state) {
        send(new SendConfigEvent(configId, state));
    }

    @Override
    public String toString() {
        return Player.class.getName() + " [username=" + credentials.getUsername() + ", privilegeLevel=" + privilegeLevel + "]";
    }

    /**
     * Sets the region changed flag.
     *
     * @param regionChanged A flag indicating if the region has changed.
     */
    public void setRegionChanged(boolean regionChanged) {
        this.regionChanged = regionChanged;
    }

    /**
     * Checks if the region has changed.
     *
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasRegionChanged() {
        return regionChanged;
    }

    /**
     * Gets the player's appearance.
     *
     * @return The appearance.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Sets the player's appearance.
     *
     * @param appearance The new appearance.
     */
    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
        this.getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
    }

    /**
     * Makes a player a npc.
     */
    public void updateToNPC(int npcId) {
        this.appearance.setNpcId(npcId);
        this.getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
    }

    @Override
    public String getUndefinedName() {
        return credentials.getUsername();
    }

    @Override
    public boolean canBeTeleblocked() {
        return true;
    }

    @Override
    public String getDefinedName() {
        return "";
    }

    @Override
    public boolean spellbookIgnore() {
        return false;
    }

    @Override
    public double getProtectionPrayerModifier() {
        return 0.6;
    }

    @Override
    public boolean rangeWeaponIgnore() {
        return false;
    }

    @Override
    public int getArrows() {
        return getEquipment().get(EquipmentConstants.ARROWS) != null ? getEquipment().get(EquipmentConstants.ARROWS).getId() : -1;
    }

    @Override
    public int getWeapon() {
        return getEquipment().get(EquipmentConstants.WEAPON) != null ? getEquipment().get(EquipmentConstants.WEAPON).getId() : -1;
    }

    @Override
    public void setAutocastSpell(Spell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * Gets the player's name, encoded as a long.
     *
     * @return The encoded player name.
     */
    public long getEncodedName() {
        return credentials.getEncodedUsername();
    }

    /**
     * Logs the player out, if possible.
     */
    public void logout() {
        PrivateChat.getInstance().logout(this);
        send(new LogoutEvent());
    }

    /**
     * Gets the game session.
     *
     * @return The game session.
     */
    public GameSession getSession() {
        return session;
    }

    @Override
    public int getHP() {
        return getSkillSet().getSkill(Skill.HITPOINTS).getCurrentLevel();
    }

    @Override
    public int getMaxHP() {
        return getSkillSet().getSkill(Skill.HITPOINTS).getMaximumLevel();
    }

    @Override
    public void damageCharacter(DamageEvent damage) {
        getBlockSet().add(SynchronizationBlock.createHitUpdateBlock(damage));
    }

    /**
     * Sets the character design flag.
     *
     * @param designedCharacter A flag indicating if the character has been
     * designed.
     */
    public void setDesignedCharacter(boolean designedCharacter) {
        this.designedCharacter = designedCharacter;
    }

    /**
     * Checks if the player has designed their character.
     *
     * @return A flag indicating if the player has designed their character.
     */
    public boolean hasDesignedCharacter() {
        return designedCharacter;
    }

    /**
     * Gets the withdrawing notes flag.
     *
     * @return The flag.
     */
    public boolean isWithdrawingNotes() {
        return withdrawingNotes;
    }

    /**
     * Sets the withdrawing notes flag.
     *
     * @param withdrawingNotes The flag.
     */
    public void setWithdrawingNotes(boolean withdrawingNotes) {
        this.withdrawingNotes = withdrawingNotes;
    }

    @Override
    public void teleport(Position position) {
        super.teleport(position); // TODO put this in the same place as Character#teleport and WalkEventHandler!!
        interfaceSet.close(); // TODO: should this be done if size == 0?
    }

    public void sendSidebarInterface(int id, int bar) {
        send(new SetSidebarEvent(id, bar));
    }

    /**
     * @param foodTimer the foodTimer to set
     */
    public void setFoodTimer(byte foodTimer) {
        this.foodTimer = foodTimer;
    }

    /**
     * @return the foodTimer
     */
    public byte getFoodTimer() {
        return foodTimer;
    }

    public Player[] getOfferOpponent() {
        return tradeOpp;
    }

    public void setOfferOpp(int index, Player e) {
        this.tradeOpp[index] = e;
    }

    public boolean hasOfferOpp() {
        return tradeOpp[1] != null;
    }

    public int getTradeStage() {
        return tradeStage;
    }

    public void setTradeStage(int i) {
        this.tradeStage = i;
    }

    public Inventory getOffer() {
        return offer;
    }

    @Override
    public int getCombatCooldownDelay() {
        return CombatFormulae.getCombatCooldownDelay(this);
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets the player's friends.
     *
     * @return The player's friends.
     */
    public Friend getFriends() {
        if (friends == null) {
            friends = new Friend(this);
        }
        return friends;
    }

    public int getWildLevel() {
        return wildLevel;
    }

    public void setWildLevel(int wild) {
        this.wildLevel = wild;
    }

    /**
     * Add a friend OR ignore.
     *
     * @param who The user.
     * @param what The event.
     * @throws Exception
     */
    public void addFriend(String who, org.apollo.game.model.Friend.Event what) throws Exception {
        friends.add(who, what, false);
    }

    /**
     * Delete a friend OR ignore.
     *
     * @param who The user.
     * @param what The event.
     * @throws Exception
     */
    public void deleteFriend(String who, org.apollo.game.model.Friend.Event what) throws Exception {
        friends.delete(who, what);
    }

    public Cannon getCannon() {
        return cannon;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public BuildingStates getBuild() {
        return build;
    }

    public void setBuild(BuildingStates build) {
        this.build = build;
    }

    public Dialogue getDialog() {
        return dialog;
    }

    public void setDialog(Dialogue dialog) {
        this.dialog = dialog;
    }

    public BankPin getBankPin() {
        return bankPin;
    }

    public void setBankPin(BankPin bankPin) {
        this.bankPin = bankPin;
    }

    public boolean isPinSet() {
        return pinSet;
    }

    public void setPinSet(boolean pinSet) {
        this.pinSet = pinSet;
    }

    public FarmingPatches getFarmingPatches() {
        return farmingPatch;
    }

    public boolean isFletching() {
        return fletching;
    }

    public void setFletching(boolean fletching) {
        this.fletching = fletching;
    }

    public boolean isCrafting() {
        return crafting;
    }

    public void setCrafting(boolean crafting) {
        this.crafting = crafting;
    }

    /**
     * An enumeration with the different privilege levels a player can have.
     *
     * @author Graham
     */
    public enum PrivilegeLevel {

        /**
         * A standard (rights 0) account.
         */
        STANDARD(0), /**
         * A player moderator (rights 1) account.
         */
        MODERATOR(1), /**
         * An administrator (rights 2) account.
         */
        ADMINISTRATOR(2);

        /**
         * Gets the privilege level for the specified numerical level.
         *
         * @param numericalLevel The numerical level.
         * @return The privilege level.
         * @throws IllegalArgumentException if the numerical level is invalid.
         */
        public static PrivilegeLevel valueOf(int numericalLevel) {
            for (PrivilegeLevel level : values()) {
                if (level.numericalLevel == numericalLevel) {
                    return level;
                }
            }
            throw new IllegalArgumentException("invalid numerical level");
        }
        /**
         * The numerical level used in the protocol.
         */
        private final int numericalLevel;

        /**
         * Creates a privilege level.
         *
         * @param numericalLevel The numerical level.
         */
        private PrivilegeLevel(int numericalLevel) {
            this.numericalLevel = numericalLevel;
        }

        /**
         * Gets the numerical level.
         *
         * @return The numerical level used in the protocol.
         */
        public int toInteger() {
            return numericalLevel;
        }
    }
}
