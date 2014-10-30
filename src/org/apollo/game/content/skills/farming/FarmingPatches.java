package org.apollo.game.content.skills.farming;

import org.apollo.game.content.skills.farming.FarmingConfigs.PatchConfigIds;
import org.apollo.game.content.skills.farming.HerbGrowing.HerbGrow;
import org.apollo.game.content.skills.farming.HerbGrowing.OfflineGrow;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;


/**
 * Farming patches ..
 * 
 * There is one more farming patch in Port Phantis? or something. It's in
 * ectophial. I did not include that because I did not think anybody would
 * really want it.
 * 
 * 
 * @author Rodrigo Molina
 */
public class FarmingPatches {

    public static final int SPADE = 952;
    public static final int PLANT_SEED = 2291;
    public static final int RAKE = 5341;
    public static final int SEED_DIBBER = 5343;
    public static final int PICK_PLANT = 2274;
    public static final int SUPER_COMPOST = 6034;
    public static final int PLANT_CURE = 6036;
    public static final int TROWEL = 5325;

    /**
     * The player farming
     */
    private Player player;

    /**
     * another place..
     */

    /**
     * The local patches in falador (under falador near draynor) and catherby.
     */
    private Patch fallyNWAllotment = new Patch(PatchConfigIds.ALOTTMENT, 8550);
    private Patch fallyHerb = new Patch(PatchConfigIds.HERB_PATCH, 8150);
    private Patch fallySEAllotment = new Patch(PatchConfigIds.ALOTTMENT, 8551);
    private Patch fallyFlower = new Patch(PatchConfigIds.FLOWER_PATCH, 7847);

    /**
     * The patches in catherby.
     */
    private Patch cnAlot = new Patch(PatchConfigIds.ALOTTMENT, 8552);
    private Patch csAlot = new Patch(PatchConfigIds.ALOTTMENT, 8553);
    private Patch cmFlower = new Patch(PatchConfigIds.FLOWER_PATCH, 7848);
    private Patch ceHerb = new Patch(PatchConfigIds.HERB_PATCH, 8151);

    /**
     * Patches in ardy..
     */
    private Patch ardyHerb = new Patch(PatchConfigIds.HERB_PATCH, 8152);
    private Patch ardyFlower = new Patch(PatchConfigIds.FLOWER_PATCH, 7849);
    private Patch nortArdyAlot = new Patch(PatchConfigIds.ARDY_ALOTTMENT, 8554);
    private Patch southArdyAlott = new Patch(PatchConfigIds.ARDY_ALOTTMENT, 8555);

    /**
     * The tree patches in fally and rimmington.
     */
    private Patch fallyTree = new Patch(PatchConfigIds.TREE_PATCH, 8389, true);
    private Patch rimmingTree = new Patch(PatchConfigIds.TREE_PATCH, 8388, true);
    private Patch varrockTree = new Patch(PatchConfigIds.TREE_PATCH, 8391, true);
    private Patch lumbyTree = new Patch(PatchConfigIds.TREE_PATCH, 8390, true);

    public FarmingPatches(Player player) {
	this.player = player;
    }

    private Patch getPatch(int object) {
	switch (object) {
	case 8389:
	    return fallyTree;
	case 8390:
	    return lumbyTree;
	case 8391:
	    return varrockTree;
	case 8388:
	    return rimmingTree;
	case 7847:
	    return fallyFlower;
	case 7848:
	    return cmFlower;
	case 7849:
	    return ardyFlower;
	case 8550:
	    return fallyNWAllotment;
	case 8551:
	    return fallySEAllotment;
	case 8552:
	    return cnAlot;
	case 8553:
	    return csAlot;
	case 8554:
	    return nortArdyAlot;
	case 8555:
	    return southArdyAlott;
	case 8150:
	    return fallyHerb;
	case 8151:
	    return ceHerb;
	case 8152:
	    return ardyHerb;
	}
	return null;
    }

    public void handleInspection(int object) {
	Patch p = getPatch(object);
	if (p.getState() >= 0 && p.getState() <= 3) {
	    player.sendMessage("This crop needs to be harvested.");
	} else if (p.getState() > 3) {
	    if (p.isWatered()) {
		player.sendMessage("You check your patch for any signs of disease and find that it is clean and watered.");
	    } else if (p.isDiseased()) {
		player.sendMessage("You check your patch for any signs of disease and find disease.");
	    }
	    if (p.isDead()) {
		player.sendMessage("Your patch is dead due to not caring.");
	    }
	}
    }

    public void handleHarvest(int object) {
	Patch patch = getPatch(object);
	if (patch.isTree()) {
	    if (patch.getTreeHealth() > 0) {
		// chop nigga.
		if (!patch.isDiseased()) {
		    player.sendMessage("You examine the tree for any signs of disease and find that it is in perfect heatlh.");
		} else {
		    player.sendMessage("You examine the tree for any signs of disease and find that it is not in perfect heatlh.");
		}
	    }
	    player.sendMessage(
		    "this is a tree and wishing to chop..");
	    return;
	}
	if (!player.getInventory().contains(SPADE)) {
	    player.sendMessage("You need a spade to harvest this plant!");
	    return;
	}
	if (patch.getInfo() != null) {
	    if (patch.isDead()) {
		player.sendMessage("You can't harvest from a dead plant!");
		return;
	    }
	    if (patch.getItemCount() > 0) {
		player.playAnimation(Animation.create(2286));
		player.getInventory().remove(patch.getInfo().getHarvestedItem());
		patch.setItemCount(patch.getItemCount() - 1);
		player.getSkillSet().addExperience(Skill.FARMING, patch.getInfo().getXpGivenHarvested());
	    } else {
		patch.setState(FarmingConfigs.NO_ALOTTMENT);
		sendConfig(patch.getObjectId(), patch.getState());
		player.sendMessage("There are no more plants to be harvested.");
	    }
	}
    }

    private int compostState;
    // private boolean isSuperComposted;
    // private boolean isRegularComposted;
    private boolean isRotting;
    private int timer;

    private void handleCompost(int itemUsed) {
	// 7836 or 7837, 7838
	player.sendMessage("This still being worked on.");
	if (compostState >= 100 && isRotting) {
	    player.sendMessage("You can't add anymore.");
	    return;
	} else if (!isRotting && compostState >= 100) {
	    isRotting = true;
	    int oneH = 60 * 60;
	    timer = oneH;
	    sendConfig(511, 100);
	    World.getWorld().schedule(new ScheduledTask(1, false) {
		@Override
		public void execute() {
		    if (timer == 0) {
			player.sendMessage("Your compost bin is ready.");
			stop();
		    } else
			timer--;
		}
	    });
	    return;
	}

	// 20 is empty
	// 10 is half full
	// 15 is full with close option
	// 100 is closed.
	sendConfig(511, 110);
    }

    public void handleUsed(int object, int itemUsed) {
	if (object == 7836 || object == 7837) {
	    handleCompost(itemUsed);
	    return;
	}
	final Patch clicked = getPatch(object);
	if (clicked == null) {
	    player.sendMessage(
		    "Why is that object not registered?");
	    return;
	}
	if (!noSeed(clicked, itemUsed))
	    return;
	int fl = player.getSkillSet().getSkill(Skill.FARMING).getCurrentLevel();
	PatchInfo pt = PatchInfo.forId(itemUsed);
	if (pt != null) {
	    if (player.getInventory().getCount(itemUsed) < 3) {
		player.sendMessage(
			"You need more than 3 seeds to plant this seed.");
		return;
	    }
	    if (pt.check(object)) {
		player.sendMessage(
			"You cannot use this seed on this plant!");
		return;
	    }
	    if (fl >= pt.getLevelNeeded()) {
		clicked.setState(pt.getPatchState());
		clicked.setSeedId(itemUsed);
		player.playAnimation(Animation.create(PLANT_SEED));
		player.getInventory().remove(itemUsed, 3);
		clicked.setItemCount(pt.getHarvestCount());
		if (clicked.getItemCount() >= 467) {
		    clicked.setTree(true);
		}
		if (pt.getTreeHealth() > 0) {
		    clicked.setTreeHealth(pt.getTreeHealth());
		}
		clicked.setInfo(pt);
		player.getSkillSet().addExperience(Skill.FARMING, pt.getXpGivenPlanted());
		HerbGrowing.getHerbGrowing().add(new HerbGrow(player.getUndefinedName(), clicked.getConfigId(),clicked, pt, pt.getTime()));
		sendConfig(clicked.getObjectId(),clicked.getState());
		pt = null;
	    } else {
		player.sendMessage(
			"You need a higher level to plant this seed.");
	    }
	} else {
	    player.sendMessage(
		    "You can't use this item on this patch.");
	}
    }

    private boolean raking;

    private boolean noSeed(final Patch clicked, int itemUsed) {
	if(itemUsed == PLANT_CURE) {
	    if(clicked.isDiseased()) {
		player.sendMessage("You use your plant cure on the disease patch, and cure it.");
		clicked.setDiseased(false);
		player.getInventory().remove(itemUsed);
		clicked.setState(clicked.getState() - 129);
		sendConfig(clicked.getObjectId(), clicked.getState());
	    } else {
		player.sendMessage("Your patch is not diseased.");
	    }
	    return false;
	}
	if (itemUsed == RAKE) {
	    if (clicked.getState() >= 0 && clicked.getState() < 3) {
		if (raking)
		    return false;
		if (!player.getInventory().add(6055))
		    return false;
		raking = true;
		player.playAnimation(Animation.create(2273));
		World.getWorld().schedule(new ScheduledTask(0, false) {
		    @Override
		    public void execute() {
			if (clicked.getSeedId() >= 0 && clicked.getState() < 3) {
			    clicked.addState(1);
			    sendConfig(clicked.getObjectId(), clicked.getState());
			} else {
			    raking = false;
			    player.playAnimation(Animation.STOP_ANIMATION);
			    stop();
			}
		    }
		});
		return false;
	    }
	} else if (isWateringcan(itemUsed)) {
	    if (!clicked.isWatered() && clicked.getState() >= 3) {
		player.playAnimation(Animation.create(2293));
		player.getInventory().remove(itemUsed);
		if (itemUsed >= 5332)
		    player.getInventory().add(itemUsed - 1, 1);
		else
		    player.getInventory().add(5331);
		clicked.setState(addWater(clicked.getState()));
		sendConfig(clicked.getObjectId(), clicked.getState());
		clicked.setWatered(true);
		clicked.addChance(30);
		return false;
	    } else if (clicked.isWatered()) {
		player.sendMessage("These plants are already watered.");
		return false;
	    } else {
		player.sendMessage("You cannot water these plants.");
		return false;
	    }
	} else if (itemUsed == SUPER_COMPOST) {
	    clicked.addChance(40);
	    clicked.setSupercompost(true);
	    return false;
	}
	if (clicked.getState() >= 3) {
	    if (itemUsed == RAKE) {
		player.sendMessage("You cannot use a rake on the seeds growing.");
		return false;
	    }
	    if (clicked.getState() > 3) {
		player.sendMessage("You cannot plant anymore seeds.  Watch it grow.");
		return false;
	    }
	}
	if (!player.getInventory().contains(5343)) {
	    player.sendMessage("You need a seed dibber to plant seeds.");
	    return false;
	}
	return true;
    }

    public void login() {
	OfflineGrow grow = HerbGrowing.getHerbGrowing().find(player.getUndefinedName());
	if (grow != null) {
	    for (int i = 0; i < grow.getHerbGrows().length; i++) {
		if (grow.getHerbGrows()[i] != null) {
		    int amount = grow.getAmounts()[i];
		    for (int a = 0; a < amount; a++) {
			nextGrowingState(grow.getHerbGrows()[i], null);
		    }
		}
	    }
	} else {
	    Patch array[] = toArray();
	    for (int a = 0; a < array.length; a++) {
		if (toArray()[a] != null) {
		    Patch p = array[a];
		    sendConfig(p.getObjectId(), p.getState());
		}
	    }
	}
    }

    private boolean checkDiseaseChance(Patch p) {
	int chance = p.getChance();
	return Misc.random(chance) == 0;// dont know what else to do with it..
    }

    private void startDisease(Patch p) {
	// some other things should be added possibly.
	p.setDiseased(true);
	p.setState(addDisease(p.getInfo().getPatchState()));
	sendConfig(p.getObjectId(),p.getState());
	player.sendMessage("Your plant is diseased! Oh no!");
    }

    protected void nextGrowingState(HerbGrow hg, Patch patch1) {
	Patch patch = patch1;
	if (patch == null)
	    patch = getPatch(hg.getPatch().getObjectId());
	if (patch != null) {
	    if (patch.getState() >= hg.getInfo().getPatchState() && patch.getState() <= hg.getInfo().getPatchState() + 4 || patch.getState() >= addWater(hg.getInfo().getPatchState()) && patch.getState() <= addWater(hg.getInfo().getPatchState()) + 4) {
		if (patch.isWatered()) {
		    if (patch.getState() >= addWater(hg.getInfo().getPatchState()) + 3) {
			hg.getPatch().setState(hg.getInfo().getPatchState() + 4);
		    } else {
			hg.getPatch().addState(1);
		    }
		} else if (checkDiseaseChance(patch) && !patch.isDiseased()) {
		    startDisease(patch);
		} else if (patch.isDiseased() && patch.getState() >= addDisease(hg.getInfo().getPatchState()) + 3) {
		    hg.getPatch().setDead(true);
		    player.sendMessage("Your plant is dead now! Oh no!");
		    sendConfig(hg.getPatch().getObjectId(), addDead(hg.getPatch().getState()));// send the dead config.
		    return;
		} else {
		    hg.getPatch().addState(1);
		}
		hg.resetTime();
		HerbGrowing.getHerbGrowing().add(hg);
		sendConfig(hg.getPatch().getObjectId(), hg.getPatch().getState());
	    } else {
		player.sendMessage("Your plant is ready!");
	    }
	}
    }

    public void sendConfig(int objectId, int state) {
	switch (objectId) {
	// flowers
	case 7847:
	case 7848:
	case 7489:
	    player.sendConfig(PatchConfigIds.FLOWER_PATCH, ((ardyFlower.getState() << 16) + ((/* ecto */0 << 8) << 16) + ((1 << 8) + fallyFlower.getState()) + (cmFlower.getState() << 8)));
	    break;

	// alottments in catherby then south fally
	case 8551:
	case 8550:
	case 8552:
	case 8553:
	    player.sendConfig(PatchConfigIds.ALOTTMENT, ((cnAlot.getState() << 16) + ((csAlot.getState() << 8) << 16) + ((1 << 8) + fallyNWAllotment.getState()) + (fallySEAllotment.getState() << 8)));
	    break;

	// alottments in ecto place then ardy
	case 8555:
	case 8554:
	    player.sendConfig(PatchConfigIds.ARDY_ALOTTMENT, ((0/* ecto */<< 16) + ((0/* ecto */<< 8) << 16)+ ((1 << 8) + nortArdyAlot.getState()) + (southArdyAlott.getState() << 8)));
	    break;

	// trees
	case 8388:
	case 8391:
	case 8390:
	case 8389:
	    player.sendConfig(PatchConfigIds.TREE_PATCH, ((varrockTree.getState() << 16) + ((lumbyTree.getState() << 8) << 16) + ((1 << 8) + rimmingTree.getState()) + (fallyTree.getState() << 8)));
	    break;

	// herbs
	case 8150:
	case 8151:
	case 8152:
	    player.sendConfig(PatchConfigIds.HERB_PATCH, ((ardyHerb.getState() << 16)+ ((0/* ecto herb goes here */<< 8) << 16)+ ((1 << 8) + fallyHerb.getState()) + (ceHerb.getState() << 8)));
	    break;

	default:
	    //the object is a compost bin haha.  we still need the formula for compost bin.
	    player.sendConfig(objectId, state);
	}
    }

    private boolean isWateringcan(int item) {
	return item >= 5331 && item <= 5340;
    }

    private int addDisease(int v) {
	return v + 129;
    }

    private int addWater(int v) {
	return v + 64;
    }

    private int addDead(int v) {
	return v + 64;// 193
    }

    public Patch[] patchNeedWeedGrow() {
	Patch[] p = new Patch[8];
	int anti = 0;// instead of looping through the array each tick, we can
		     // just null it
	Patch array[] = toArray();
	for (int a = 0; a < array.length; a++) {
	    if (array[a] != null) {
		if (array[a].getState() > FarmingConfigs.HIGH_ALOTTMENT && array[a].getState() <= FarmingConfigs.NO_ALOTTMENT) {
		    p[a] = array[a];
		    anti++;
		}
	    }
	}
	if (anti == 0) {// no patch weed needs to be grown.
	    return null;
	}
	return p;
    }

    public Patch[] toArray() {
	return new Patch[] { fallyNWAllotment, fallyFlower, fallyHerb, fallySEAllotment, cnAlot,
		csAlot, cmFlower, ceHerb, ardyHerb, ardyFlower, nortArdyAlot,
		southArdyAlott, fallyTree, rimmingTree, varrockTree, lumbyTree };
    }

    public void fromArray(Patch patch[]) {
	fallyNWAllotment = patch[0];
	fallyFlower = patch[1];
	fallyHerb = patch[2];
	fallySEAllotment = patch[3];
	cnAlot = patch[4];
	csAlot = patch[5];
	cmFlower = patch[6];
	ceHerb = patch[7];
	ardyHerb = patch[8];
	ardyFlower = patch[9];
	nortArdyAlot = patch[10];
	southArdyAlott = patch[11];
	fallyTree = patch[12];
	rimmingTree = patch[13];
	varrockTree = patch[14];
	lumbyTree = patch[15];
    }
}
