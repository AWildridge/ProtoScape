package org.apollo.game.content.skills.herblore;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

/**
 * Handles all the voids for the enum datas
 * 
 * @author Ares_
 */
public class Herblore {

    private static final int PESTLE = 233;
    private static final int VIAL_OF_WATER = 227;
    private static final int P = 187; // Not strong poison
    private static final int PP = 5937; // Medium poison
    private static final int PPP = 5940; // Strongest poison

    public static void cleanHerb(Player player, int itemId) {
        final Item item = new Item(itemId, 1);
        if (Herb.forId(itemId) == null || Herb.forId(itemId).getUnidentifiedID() != itemId) {
            return;
        }
        if (Herb.forId(itemId).getLevelRequirement() > player.getSkillSet().getSkill(Skill.HERBLORE).getCurrentLevel()) {
            player.sendMessage("You need a herblore level of " + Herb.forId(itemId).getLevelRequirement() + " to identify this herb");
            return;
        }
        player.getInventory().remove(new Item(Herb.forId(itemId).getUnidentifiedID(), 1));
        player.sendMessage("You clean the dirt off the " + item.getDefinition().getName().toLowerCase() + ".");
        player.getInventory().add(new Item(Herb.forId(itemId).getIdentifiedID(), 1));
        player.getSkillSet().addExperience(15, Herb.forId(itemId).getExperience());
    }

    public static void halfPotion(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != VIAL_OF_WATER ? itemUsed : usedWith;
        if (UnfinishedPotion.unFinForId(itemId) == null) {
            return;
        }
        if (itemUsed == VIAL_OF_WATER && usedWith == UnfinishedPotion.unFinForId(itemId).getHerb() || usedWith == VIAL_OF_WATER && itemUsed == UnfinishedPotion.unFinForId(itemId).getHerb()) {
            if (UnfinishedPotion.unFinForId(itemId).getLevel() > player.getSkillSet().getSkill(Skill.HERBLORE).getCurrentLevel()) {
                player.sendMessage("You need a herblore level of " + UnfinishedPotion.unFinForId(itemId).getLevel() + " to make this potion.");
                return;
            }
            player.playAnimation(new Animation(363));
            player.getInventory().remove(new Item(UnfinishedPotion.unFinForId(itemId).getHerb(), 1));
            player.getInventory().remove(new Item(VIAL_OF_WATER, 1));
            player.getInventory().add(new Item(UnfinishedPotion.unFinForId(itemId).getPotion(), 1));
        }
    }

    public static void finishPotion(Player player, int itemUsed, int usedWith) {
        if (FinishedPotion.finForId(itemUsed) == null) {
            return;
        }

        if (itemUsed == FinishedPotion.finForId(itemUsed).getPot() && usedWith == FinishedPotion.finForId(itemUsed).getIngredient()) {
            if (FinishedPotion.finForId(itemUsed).getLevel() > player.getSkillSet().getSkill(Skill.HERBLORE).getCurrentLevel()) {
                player.sendMessage("You need a herblore level of " + FinishedPotion.finForId(itemUsed).getLevel() + " to make this potion.");
                return;
            }
            player.playAnimation(new Animation(363));
            player.getInventory().remove(new Item(FinishedPotion.finForId(itemUsed).getPot(), 1));
            player.getInventory().remove(new Item(FinishedPotion.finForId(itemUsed).getIngredient(), 1));
            player.getInventory().add(new Item(FinishedPotion.finForId(itemUsed).getFinPot(), 1));
            player.getSkillSet().addExperience(15, FinishedPotion.finForId(itemUsed).getExp());
        }
    }

    public static void grindItem(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != PESTLE ? itemUsed : usedWith;
        final Item item = new Item(itemId, 1);
        if (ItemGrinding.grindForId(itemId) == null) {
            return;
        }
        if (itemUsed == PESTLE && usedWith == ItemGrinding.grindForId(itemId).getUngrounded() || usedWith == PESTLE && itemUsed == ItemGrinding.grindForId(itemId).getUngrounded()) {
            player.playAnimation(new Animation(364));
            player.getInventory().remove(new Item(ItemGrinding.grindForId(itemId).getUngrounded(), 1));
            player.sendMessage("You grind the " + item.getDefinition().getName().toLowerCase() + " to dust.");
            player.getInventory().add(new Item(ItemGrinding.grindForId(itemId).getGrinded(), 1));
        }
    }

    public static void executePoison(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != P ? itemUsed : usedWith;
        if (PoisonData.pForId(itemId) == null) {
            return;
        }
        if (itemUsed == P && usedWith == PoisonData.pForId(itemId).getUnPoisoned() || usedWith == P && itemUsed == PoisonData.pForId(itemId).getUnPoisoned()) {
            player.getInventory().remove(new Item(P, 1));
            player.getInventory().remove(new Item(PoisonData.pForId(itemId).getUnPoisoned(), 1));
            player.sendMessage("You put the poison the tip of your weapon.");
            player.getInventory().add(new Item(PoisonData.pForId(itemId).getPoisonedReward(), 1));
        }
    }

    public static void executePoisonP(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != PP ? itemUsed : usedWith;
        if (PoisonData.pForId(itemId) == null) {
            return;
        }
        if (itemUsed == PP && usedWith == PoisonData.pForId(itemId).getUnPoisoned() || usedWith == PP && itemUsed == PoisonData.pForId(itemId).getUnPoisoned()) {
            player.getInventory().remove(new Item(PP, 1));
            player.getInventory().remove(new Item(PoisonData.pForId(itemId).getUnPoisoned(), 1));
            player.sendMessage("You put the poison the tip of your weapon.");
            player.getInventory().add(new Item(PoisonData.pForId(itemId).getPoisonedRewardP(), 1));
        }
    }

    public static void executePoisonPP(Player player, int itemUsed, int usedWith) {
        int itemId = itemUsed != PPP ? itemUsed : usedWith;
        if (PoisonData.pForId(itemId) == null) {
            return;
        }
        if (itemUsed == PPP && usedWith == PoisonData.pForId(itemId).getUnPoisoned() || usedWith == PPP && itemUsed == PoisonData.pForId(itemId).getUnPoisoned()) {
            player.getInventory().remove(new Item(PPP, 1));
            player.getInventory().remove(new Item(PoisonData.pForId(itemId).getUnPoisoned(), 1));
            player.sendMessage("You put the poison the tip of your weapon.");
            player.getInventory().add(new Item(PoisonData.pForId(itemId).getPoisonedRewardPP(), 1));
        }
    }
}
