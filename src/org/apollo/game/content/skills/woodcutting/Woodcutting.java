/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.game.content.skills.woodcutting;

import org.apollo.game.action.impl.ChoppingTreeAction;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.util.Misc;

/**
 * @author The Wanderer
 * @author Rodrigo Molina
 */
public class Woodcutting {


    private static HatchetData getHighestHatchet(Player player) {
        int axeId;
        for (HatchetData value : HatchetData.values()) {
            axeId = value.getHatchetId();
            if (player.getEquipment().contains(axeId)) {
                if (player.getSkillSet().getSkill(Skill.WOODCUTTING).getCurrentLevel() >= value.getLevelNeeded()) {
                    return value;
                }
            }
            if (player.getInventory().contains(axeId)) {
                if (player.getSkillSet().getSkill(Skill.WOODCUTTING).getCurrentLevel() >= value.getLevelNeeded()) {
                    return value;
                }
            }
        }
        return null;
    }

    public static void registerChopAction(Player player, TreeData tree, Position pos) {
        if (tree == null) {
            player.sendMessage("This tree does not exist.");
            return;
        }
        if (tree.getLevelNeeded() > player.getSkillSet().getSkill(Skill.WOODCUTTING).getCurrentLevel()) {
            player.sendMessage("You do not have the required level to chop down this tree.");
            return;
        }
        final HatchetData hatchetUsing = getHighestHatchet(player);
        if (hatchetUsing == null) {
            player.sendMessage("You do not have a hatchet to chop down this tree.");
            return;
        }
        final TreeData treeCutting = tree;
        player.sendMessage("You swing your hatchet at the tree.");
        player.playAnimation(hatchetUsing.getAnim());
        player.getAttributeTags().add("skilling");
        player.startAction(new ChoppingTreeAction(calculateChoppingTime(treeCutting, player), treeCutting, hatchetUsing, player, pos));
    }

    public static boolean checkLogs(TreeData treeCutting) {
        int logsCut = treeCutting.getLogsCut();
        int logsToCut = treeCutting.getLogsToCut();
        if (logsToCut == 0) {
            treeCutting.setLogsToCut(Misc.random(7) + 3);
            return false;
        }
        return logsCut != logsToCut;
    }

    /*
     * Credits to Scu11 for the Formula
     */
    public static int calculateChoppingTime(TreeData treeCutting, Player player) {
        int skill = player.getSkillSet().getSkill(Skill.WOODCUTTING).getCurrentLevel();
        int levelREQ = treeCutting.getLevelNeeded();
        int modifier = 0;
        for (HatchetData value : HatchetData.values()) {
            modifier = value.getLevelNeeded();
        }
        int randomAmt = Misc.random(3);
        double cycleCount;
        cycleCount = Math.ceil((levelREQ * 50 - skill * 10) / modifier * 0.25 - randomAmt * 4);
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        return (int) cycleCount;
    }
}
