package org.apollo.game.content.skills;

import org.apollo.game.GameConstants;
import org.apollo.game.action.Action;
import org.apollo.game.action.Action.QueuePolicy;
import org.apollo.game.action.Action.WalkablePolicy;
import org.apollo.game.action.DistancedAction;
import org.apollo.game.event.impl.DamageEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.util.Misc;

/**
 * Thieving.java
 * @author The Wanderer
 */
public class Thieving {

    public enum Stalls {

        VEGETABLE(new int[]{4706, 4708}, 2, 10, 10, new int[]{1957, 1965, 1550, 1942, 1982}),
        CAKE(new int[]{2561, 630, 6163}, 5, 16, 2.5, new int[]{1891, 2309, 1901}),
        GENERAL(new int[]{4876}, 5, 16, 10, new int[]{590, 2347, 1931}),
        CRAFTING(new int[]{4874, 6166}, 5, 16, 10, new int[]{1755, 1597, 1592}),
        FOOD(new int[]{4875}, 5, 16, 10, new int[]{1963}),
        TEA(new int[]{635, 6574}, 5, 16, 2.5, new int[]{1978}),
        ROCK_CAKE(new int[]{2793}, 15, 10, 5, new int[]{2379}),
        SILK(new int[]{629, 2560, 6568}, 20, 24, 5, new int[]{950}),
        WINE(new int[]{14011}, 22, 27, 15, new int[]{1987, 1935, 7919, 1993, 1937}),
        SEED(new int[]{7053}, 27, 10, 5, new int[]{5318, 5319, 5324, 5322, 5320, 5323, 5321, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5097}),
        FUR(new int[]{632, 2563, 4278, 6571}, 35, 36, 15, new int[]{958}),
        FISH(new int[]{4705, 4707, 4277}, 42, 42, 15, new int[]{359, 331, 359, 331, 359, 331, 359, 331, 377}),
        SILVER(new int[]{628, 2565, 6164}, 50, 54, 30, new int[]{442}),
        MAGIC(new int[]{4877}, 65, 100, 60, new int[]{554, 555, 556, 557}),
        SPICE(new int[]{633, 2564, 6572}, 65, 81.3, 80, new int[]{2007}),
        SCIMITAR(new int[]{4878}, 65, 100, 60, new int[]{1323}),
        GEM(new int[]{631, 2562, 6162, 6570}, 75, 160, 180, new int[]{1617, 1619, 1621, 1623});
        int levelReq;
        int[] objects, loot;
        double exp, time;

        private Stalls(int[] objects, int levelReq, double exp, double time, int[] loot) {
            this.objects = objects;
            this.levelReq = levelReq;
            this.exp = exp;
            this.time = time;
            this.loot = loot;
        }

        private int getLevelReq() {
            return levelReq;
        }

        @SuppressWarnings("unused")
        private int[] getObjects() {
            return objects;
        }

        @SuppressWarnings("unused")
        private int[] getLoot() {
            return loot;
        }

        private double getExp() {
            return exp * GameConstants.EXP_MODIFIER;
        }

        private double getTime() {
            return time;
        }
    }

    public enum Pickpocketing {

        MAN(new int[]{1, 2, 3, 4, 5, 6}, 1, 8, new int[]{995}, new int[][]{{3, 3}}, 1),
        FARMER(new int[]{7}, 10, 14.5, new int[]{995, 5318}, new int[][]{{3, 3}, {1, 1}}, 1),
        FEMALE_HAM(new int[]{1715}, 15, 18.5, new int[]{995, 590, 1511, 1617, 1619, 1621, 1623, 321, 1739, 2138,
    4298, 4300, 4302, 4304, 4306, 4308, 4310, 1267, 249, 251,
    253, 685, 686, 687, 688, 689, 690},
        new int[][]{{7, 12}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 2),
        MALE_HAM(new int[]{1714}, 20, 22.2, new int[]{995, 590, 1511, 1617, 1619, 1621, 1623, 321, 2138,
    4298, 4300, 4302, 4304, 4306, 4308, 4310, 1267, 249, 251,
    253, 685, 686, 687, 688, 689, 690},
        new int[][]{{10, 15}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 3),
        HAM_GUARD(new int[]{1710, 1711, 1712}, 23, 22.2, new int[]{995, 1739, 249, 251, 253, 1129, 1511, 1733, 1734, 590,
    1617, 1619, 1621, 1623, 321, 2138, 4298, 4300, 4302, 4304,
    4306, 4308, 4310, 882, 884, 886, 1349, 1351, 1353, 1203,
    1205, 1207, 1265, 1267, 1269, 685, 686, 687, 688, 689,
    690},
        new int[][]{{12, 17}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}}, 3),
        WARRIOR(new int[]{15, 18}, 25, 26, new int[]{995}, new int[][]{{18, 18}}, 2),
        ROGUE(new int[]{187}, 32, 36.5, new int[]{995, 995, 995, 556, 1523, 1219, 1993, 2357},
        new int[][]{{25, 25}, {40, 40}, {45, 45}, {8, 8}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 2),
        MASTER_FARMER(new int[]{2234, 2235, 3299}, 38, 43, new int[]{5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5104, 5105,
    5106, 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298, 5299,
    5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309,
    5310, 5311, 5318, 5319, 5320, 5321, 5322, 5323, 5324},
        new int[][]{{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},
    {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 3),
        GAURD(new int[]{9, 10}, 40, 46.8, new int[]{995}, new int[][]{{30, 30}}, 2),
        FREMENNIK(new int[]{1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314}, 45, 65, new int[]{995}, new int[][]{{40, 40}}, 2),
        BANDIT_41(new int[]{1883, 1884}, 45, 65, new int[]{995}, new int[][]{{40, 40}}, 5),
        KNIGHT(new int[]{23}, 55, 84.3, new int[]{995}, new int[][]{{50, 50}}, 3),
        BANDIT_56(new int[]{1880, 1881}, 55, 84.3, new int[]{995}, new int[][]{{50, 50}}, 5),
        WATCHMAN(new int[]{34}, 65, 137.5, new int[]{995, 2309}, new int[][]{{60, 60}, {1, 1}}, 3),
        MENAPHITE(new int[]{1904, 1905}, 65, 137.5, new int[]{995}, new int[][]{{60, 60}}, 5),
        PALADIN(new int[]{20}, 70, 151.8, new int[]{995, 995, 995, 995, 562}, new int[][]{{80, 80}, {80, 80}, {80, 80}, {80, 80}, {2, 2}}, 3),
        GNOME(new int[]{66, 67, 68, 159, 160, 161, 168, 169}, 75, 198.3, new int[]{995, 444, 557, 2150, 2162, 569}, new int[][]{{200, 400}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}}, 1),
        HERO(new int[]{21}, 80, 273.3, new int[]{995, 560, 565, 1993, 1601, 444, 569}, new int[][]{{100, 300}, {2, 2}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1},}, 4),
        ELVES(new int[]{2363, 2364, 2365, 2366, 2367}, 85, 353.3, new int[]{995, 561, 560, 569, 444, 1993, 1601, 237,}, new int[][]{{250, 350}, {3, 3}, {2, 2}, {1, 1}, {1, 1}, {1, 1}}, 5);
        int[] npcIDs, items;
        int[][] quantities;
        int levelReq, damage;
        double exp;

        private Pickpocketing(int[] npcIDs, int levelReq, double exp, int[] items, int[][] quantities, int damage) {
            this.npcIDs = npcIDs;
            this.levelReq = levelReq;
            this.exp = exp;
            this.items = items;
            this.quantities = quantities;
            this.damage = damage;
        }

        private int[] getNpcIDs() {
            return npcIDs;
        }

        @SuppressWarnings("unused")
        private int[] getItems() {
            return items;
        }

        @SuppressWarnings("unused")
        private int[][] getQuantities() {
            return quantities;
        }

        private int getLevelReq() {
            return levelReq;
        }

        private int getDamage() {
            return damage;
        }

        private double getExp() {
            return exp * GameConstants.EXP_MODIFIER;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void handlePickpocketing(final Player player, int slot, Position loc) {
        if (player.hasAttributeTag("skilling")) {
            player.sendMessage("You are busy.");
            return;
        }
        for (final Pickpocketing p : Pickpocketing.values()) {
            for (int i = 0; i < p.getNpcIDs().length; i++) {
                final NPC npc = (NPC) World.getWorld().getNPCRepository().get(slot);
                int npcID = npc.getDefinition().getId();
                if (p.npcIDs[i] == npcID) {

                    if (player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() < p.getLevelReq()) {
                        player.sendMessage("You must have a Thieving level of " + p.getLevelReq() + " to pickpocket this monster.");
                        return;
                    }
                    player.turnTo(loc);
                    player.getAttributeTags().add("skilling");
                    player.startAction(new DistancedAction(0, true, player, npc.getPosition(), 1) {

                        @Override
                        public void executeAction() {
                            player.sendMessage("You attempt to pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket.");
                            player.playAnimation(new Animation(881, 1));
                            player.startAction(new DistancedAction(1, false, player, npc.getPosition(), 1) {

                                @Override
                                public void executeAction() {
                                    if (Misc.random(((player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() - p.getLevelReq()) + 2) / 2) != 1) {
                                        int j = Misc.random(p.items.length - 1);
                                        player.getInventory().add(new Item(p.items[j], (Misc.random(p.quantities[j][1] - p.quantities[j][0]) + p.quantities[j][0])));
                                        player.sendMessage("You pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket.");
                                        player.getSkillSet().addExperience(Skill.THIEVING, p.getExp());
                                        player.getAttributeTags().remove("skilling");
                                        this.stop();
                                    } else {
                                        player.getAttributeTags().add("stunned");
                                        player.sendMessage(""
                                                + "You fail to pick the " + p.toString().toLowerCase().trim().replaceAll("_", " ") + "'s pocket.");
                                        player.playGraphic(new Graphic(80, 0, 100));
                                        player.playAnimation(player.getDefendAnimation());
                                        npc.turnTo(player.getPosition());
                                        npc.playAnimation(new Animation(422));
                                        player.getWalkingQueue().clear();
                                        Skill Hitpoints = player.getSkillSet().getSkill(Skill.HITPOINTS);
                                        player.damageCharacter(new DamageEvent(p.getDamage(), Hitpoints.getCurrentLevel(), Hitpoints.getMaximumLevel()));
                                        npc.forceChat("What do you think you are doing?!");
                                        player.getAttributeTags().remove("skilling");
                                        World.getWorld().schedule(new Action(10, false, player) {

                                            @Override
                                            public void execute() {
                                                player.getAttributeTags().remove("stunned");
                                                this.stop();
                                            }

                                            @Override
                                            public QueuePolicy getQueuePolicy() {
                                                return QueuePolicy.NEVER;
                                            }

                                            @Override
                                            public WalkablePolicy getWalkablePolicy() {
                                                return WalkablePolicy.NON_WALKABLE;
                                            }
                                        });
                                        this.stop();
                                    }
                                }

                                @Override
                                public QueuePolicy getQueuePolicy() {
                                    return QueuePolicy.NEVER;
                                }

                                @Override
                                public WalkablePolicy getWalkablePolicy() {
                                    return WalkablePolicy.NON_WALKABLE;
                                }
                            });
                            this.stop();
                        }

                        @Override
                        public QueuePolicy getQueuePolicy() {
                            return QueuePolicy.NEVER;
                        }

                        @Override
                        public WalkablePolicy getWalkablePolicy() {
                            return WalkablePolicy.NON_WALKABLE;
                        }
                    });

                } else {
                    return;
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void thieveStalls(final Player player, final int objectID, final Position pos) {
        for (Stalls s : Stalls.values()) {
            for (int i = 0; i < s.objects.length; i++) {
                if (s.objects[i] == objectID) {
                    if (player.getSkillSet().getSkill(Skill.THIEVING).getCurrentLevel() < s.getLevelReq()) {
                        player.sendMessage("You must have a Thieving level of " + s.getLevelReq() + " to steal from this stall.");
                        return;
                    }
                    int j = Misc.random(s.loot.length - 1);
                    player.getInventory().add(new Item(s.loot[j]));
                    player.getSkillSet().addExperience(Skill.THIEVING, s.getExp());
                    player.playAnimation(new Animation(881, 1));
                    player.sendNewObject(pos, 634, 0, 10);
                    player.sendMessage("You manage to steal some " + new Item(s.loot[j]).getDefinition().getName().toLowerCase() + " from the stall.");
                    System.out.println(pos);
                    World.getWorld().schedule(new Action((int) (s.getTime() * 1000 / 600), false, player) {

                        @Override
                        public void execute() {
                            player.sendNewGlobalObject(pos, objectID, 0, 10);
                            this.stop();
                        }

                        @Override
                        public QueuePolicy getQueuePolicy() {
                            return QueuePolicy.NEVER;
                        }

                        @Override
                        public WalkablePolicy getWalkablePolicy() {
                            return WalkablePolicy.NON_WALKABLE;
                        }
                    });
                }
            }
        }
    }
}
