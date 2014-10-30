package org.apollo.game.content.skills;

import org.apollo.game.GameConstants;
import org.apollo.game.action.Action;
import org.apollo.game.action.Action.QueuePolicy;
import org.apollo.game.action.Action.WalkablePolicy;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.OpenChatInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.SetInterfaceModelEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.UpdateInventoryEvent;
import org.apollo.game.event.impl.UpdateInventoryEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.util.Misc;

/**
 * Smithing.java
 * @author The Wanderer
 */
public class Smithing {

    static int[] primaryOre = {436, 440, 442, 440, 444, 447, 449, 451};
    static int[] levelReq = {1, 15, 20, 30, 40, 50, 70, 85};
    static Item[] bars = {new Item(2349), new Item(2351), new Item(2355), new Item(2353), new Item(2357), new Item(2359), new Item(2361), new Item(2363)};
    static Item[] secondaryOres = {new Item(438), null, null, new Item(453, 2), null, new Item(453, 4), new Item(453, 6), new Item(453, 8)};
    static double[] exp = {6.2, 12.5, 13.7, 17.5, 22.5, 30.0, 37.5, 50.0};
    static int[] barNameIds = {3987, 3991, 3995, 3999, 4003, 7441, 7446, 7450};
    static int type, amt, barsReq, barIndex;
    static Position loc;
    static Item product;
    /*0 = Bronze, 1 = Iron, 2 = Steel ect ect */
    public static Item[][] smithedItems = {
        {
            new Item(1205), new Item(1277), new Item(1321), new Item(1291), new Item(1307),
            new Item(1351), new Item(1422), new Item(1337), new Item(1375), new Item(3095),
            new Item(1103), new Item(1075), new Item(1087), new Item(1117), new Item(-1),
            new Item(1139), new Item(1155), new Item(1173), new Item(1189), new Item(4819, 15),
            new Item(819, 10), new Item(39, 15), new Item(864, 5), new Item(1794), new Item(-1)
        },
        {
            new Item(1203), new Item(1279), new Item(1323), new Item(1293), new Item(1309),
            new Item(1349), new Item(1420), new Item(1335), new Item(1363), new Item(3096),
            new Item(1101), new Item(1067), new Item(1081), new Item(1115), new Item(4540),
            new Item(1137), new Item(1153), new Item(1175), new Item(1191), new Item(4820, 15),
            new Item(820, 10), new Item(40, 15), new Item(863, 5), new Item(7225), new Item(-1)
        },
        {
            new Item(1207), new Item(1281), new Item(1325), new Item(1295), new Item(1311),
            new Item(1353), new Item(1424), new Item(1339), new Item(1365), new Item(3097),
            new Item(1105), new Item(1069), new Item(1083), new Item(1119), new Item(4544),
            new Item(1141), new Item(1157), new Item(1177), new Item(1193), new Item(1539, 15),
            new Item(821, 10), new Item(41, 15), new Item(865, 5), new Item(-1), new Item(2370)
        },
        {
            new Item(1209), new Item(1285), new Item(1329), new Item(1299), new Item(1315),
            new Item(1355), new Item(1428), new Item(1343), new Item(1369), new Item(3099),
            new Item(1109), new Item(1071), new Item(1085), new Item(1121), new Item(-1),
            new Item(1143), new Item(1159), new Item(1181), new Item(1197), new Item(4822, 15),
            new Item(822, 10), new Item(42, 15), new Item(866, 5), new Item(-1), new Item(-1)
        },
        {
            new Item(1211), new Item(1287), new Item(1331), new Item(1301), new Item(1317),
            new Item(1357), new Item(1430), new Item(1345), new Item(1371), new Item(3100),
            new Item(1111), new Item(1073), new Item(1091), new Item(1123), new Item(-1),
            new Item(1145), new Item(1161), new Item(1183), new Item(1199), new Item(4823, 15),
            new Item(823, 10), new Item(43, 15), new Item(867, 5), new Item(-1), new Item(-1)
        },
        {
            new Item(1213), new Item(1289), new Item(1333), new Item(1303), new Item(1319),
            new Item(1359), new Item(1432), new Item(1347), new Item(1373), new Item(3101),
            new Item(1113), new Item(1079), new Item(1093), new Item(1127), new Item(-1),
            new Item(1147), new Item(1163), new Item(1185), new Item(1201), new Item(4824, 15),
            new Item(824, 10), new Item(44, 15), new Item(868, 5), new Item(-1), new Item(-1)
        }
    };
    public static int[] barsRequired = {
        1, 1, 2, 2, 3,
        1, 1, 3, 3, 2,
        3, 3, 3, 5, 1,
        1, 2, 2, 3, 1,
        1, 1, 1, 1, 1
    };
    /* Dagger, Sword, Scimitar, Long Sword, 2 hand sword,
     * Axe, Mace, Warhammer, Battle axe, claws,
     * Chain body, plate legs, plate skirt, platebody, oil lantern frame,
     * Medium helm, full helm, Square Shield, Kite Shield, nails,
     * dart tips, arrowtips, throwing knives, other, studs
     */
    public static int[][] levelReqs = {
        {
            1, 4, 5, 6, 14,
            1, 2, 9, 10, 13,
            11, 16, 16, 18, 0,
            3, 7, 8, 12, 4,
            4, 5, 7, 4, 0
        },
        {
            15, 19, 20, 21, 29,
            16, 17, 24, 25, 28,
            26, 31, 31, 33, 26,
            18, 22, 23, 27, 19,
            19, 20, 22, 17, 0
        },
        {
            30, 34, 35, 36, 44,
            31, 32, 39, 40, 43,
            41, 46, 46, 48, 49,
            33, 37, 38, 42, 34,
            34, 35, 37, 0, 36
        },
        {
            50, 54, 55, 56, 64,
            51, 52, 59, 60, 63,
            61, 66, 66, 68, 0,
            53, 57, 58, 62, 54,
            54, 55, 57, 0, 0
        },
        {
            70, 74, 75, 76, 84,
            71, 72, 79, 80, 83,
            71, 86, 86, 88, 0,
            73, 77, 78, 82, 74,
            74, 75, 77, 0, 0
        },
        {
            85, 89, 90, 91, 99,
            86, 87, 94, 95, 98,
            86, 99, 99, 99, 0,
            88, 92, 93, 97, 89,
            89, 90, 92, 0, 0
        }
    };
    static int[] interfaceTextIds = {
        1094, 1085, 1087, 1086, 1088,
        1091, 1093, 1083, 1092, 8429,
        1098, 1099, 1100, 1101, 11461,
        1102, 1103, 1104, 1105, 13358,
        1107, 1108, 1106, 1096, 1134
    };
    static String[] interfaceText = {
        "Dagger", "Sword", "Scimitar", "Long Sword", "2 hand sword",
        "Axe", "Mace", "Warhammer", "Battle axe", "Claws",
        "Chain body", "Plate legs", "Plate skirt", "Plate body", "Oil lantern frame",
        "Medium helm", "Full helm", "Square shield", "Kite shield", "Nails",
        "Dart tips", "Arrowtips", "Throwing knives", "other", "Studs"
    };
    static int[] barTypes = {
        2349, 2351, 2353, 2359, 2361, 2363
    };
    static int[] oneBar = {1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 13357, 1132, 1135, 11459};
    static int[] twoBars = {1089, 1113, 1114, 1116, 8428};
    static int[] threeBars = {1090, 1095, 1109, 1110, 1111, 1115, 1118};
    static int[] fiveBars = {1112};

    private static void checkLevels(Player player, int barType) {
        if (barType == 2349) {
            interfaceText[23] = "Bronze wire";
        }
        if (barType == 2351) {
            interfaceText[23] = "Iron spit";
        }
        if (barType == 2353) {
            interfaceText[14] = "Bullseye lantern";
        }
        for (int i = 0; i < barTypes.length; i++) {
            if (barType == barTypes[i]) {
                for (int j = 0; j < levelReqs[i].length; j++) {
                    if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() >= levelReqs[i][j]) {
                        player.send(new SetInterfaceTextEvent(interfaceTextIds[j], "@whi@" + interfaceText[j]));
                    }
                    if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() < levelReqs[i][j]) {
                        player.send(new SetInterfaceTextEvent(interfaceTextIds[j], "@bla@" + interfaceText[j]));
                    }
                }
            }
        }
    }

    private static void checkBars(Player player, int barType) {
        if (player.getInventory().getCount(barType) >= 1) {
            for (int i = 0; i < oneBar.length; i++) {
                player.send(new SetInterfaceTextEvent(oneBar[i], "@gre@1bar"));
            }
        }
        if (player.getInventory().getCount(barType) >= 2) {
            for (int i = 0; i < twoBars.length; i++) {
                player.send(new SetInterfaceTextEvent(twoBars[i], "@gre@2bars"));
            }
        }
        if (player.getInventory().getCount(barType) >= 3) {
            for (int i = 0; i < threeBars.length; i++) {
                player.send(new SetInterfaceTextEvent(threeBars[i], "@gre@3bars"));
            }
        }
        if (player.getInventory().getCount(barType) >= 5) {
            for (int i = 0; i < fiveBars.length; i++) {
                player.send(new SetInterfaceTextEvent(fiveBars[i], "@gre@5bars"));
            }
        }
        if (player.getInventory().getCount(barType) < 5) {
            for (int i = 0; i < fiveBars.length; i++) {
                player.send(new SetInterfaceTextEvent(fiveBars[i], "@red@5bars"));
            }
        }
        if (player.getInventory().getCount(barType) < 3) {
            for (int i = 0; i < threeBars.length; i++) {
                player.send(new SetInterfaceTextEvent(threeBars[i], "@red@3bars"));
            }
        }
        if (player.getInventory().getCount(barType) < 2) {
            for (int i = 0; i < twoBars.length; i++) {
                player.send(new SetInterfaceTextEvent(twoBars[i], "@red@2bars"));
            }
        }
        if (player.getInventory().getCount(barType) < 1) {
            for (int i = 0; i < oneBar.length; i++) {
                player.send(new SetInterfaceTextEvent(oneBar[i], "@red@1bar"));
            }
        }
    }

    public static void setupSmithingInterface(Player player, int barType, int objectId, int anvilX, int anvilY) {
        Position location = new Position(anvilX, anvilY);
        loc = location;
        type = barType;
        if (objectId == 2783) {
            if (barType == 2349) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[0][0], smithedItems[0][1], smithedItems[0][2], smithedItems[0][3], smithedItems[0][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[0][5], smithedItems[0][6], smithedItems[0][7], smithedItems[0][8], smithedItems[0][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[0][10], smithedItems[0][11], smithedItems[0][12], smithedItems[0][13], smithedItems[0][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[0][15], smithedItems[0][16], smithedItems[0][17], smithedItems[0][18], smithedItems[0][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[0][20], smithedItems[0][21], smithedItems[0][22], smithedItems[0][23], smithedItems[0][24]}));
                player.send(new SetInterfaceTextEvent(1096, "Bronze wire"));
                player.send(new SetInterfaceTextEvent(1132, "1bar"));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(11459, ""));
                player.send(new SetInterfaceTextEvent(11461, ""));
                player.send(new SetInterfaceTextEvent(1134, ""));
                player.send(new SetInterfaceTextEvent(1135, ""));
            } else if (barType == 2351) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[1][0], smithedItems[1][1], smithedItems[1][2], smithedItems[1][3], smithedItems[1][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[1][5], smithedItems[1][6], smithedItems[1][7], smithedItems[1][8], smithedItems[1][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[1][10], smithedItems[1][11], smithedItems[1][12], smithedItems[1][13], smithedItems[1][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[1][15], smithedItems[1][16], smithedItems[1][17], smithedItems[1][18], smithedItems[1][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[1][20], smithedItems[1][21], smithedItems[1][22], smithedItems[1][23], smithedItems[1][24]}));
                player.send(new SetInterfaceTextEvent(11459, "1bar"));
                player.send(new SetInterfaceTextEvent(11461, "Oil lantern frame"));
                player.send(new SetInterfaceTextEvent(1096, "Iron spit"));
                player.send(new SetInterfaceTextEvent(1132, "1bar"));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(1134, ""));
                player.send(new SetInterfaceTextEvent(1135, ""));
            } else if (barType == 2353) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[2][0], smithedItems[2][1], smithedItems[2][2], smithedItems[2][3], smithedItems[2][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[2][5], smithedItems[2][6], smithedItems[2][7], smithedItems[2][8], smithedItems[2][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[2][10], smithedItems[2][11], smithedItems[2][12], smithedItems[2][13], smithedItems[2][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[2][15], smithedItems[2][16], smithedItems[2][17], smithedItems[2][18], smithedItems[2][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[2][20], smithedItems[2][21], smithedItems[2][22], smithedItems[2][23], smithedItems[2][24]}));
                player.send(new SetInterfaceTextEvent(11459, "1bar"));
                player.send(new SetInterfaceTextEvent(11461, "Bullseye lantern"));
                player.send(new SetInterfaceTextEvent(1134, "Studs"));
                player.send(new SetInterfaceTextEvent(1135, "1bar"));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(1096, ""));
                player.send(new SetInterfaceTextEvent(1132, ""));
            } else if (barType == 2359) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[3][0], smithedItems[3][1], smithedItems[3][2], smithedItems[3][3], smithedItems[3][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[3][5], smithedItems[3][6], smithedItems[3][7], smithedItems[3][8], smithedItems[3][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[3][10], smithedItems[3][11], smithedItems[3][12], smithedItems[3][13], smithedItems[3][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[3][15], smithedItems[3][16], smithedItems[3][17], smithedItems[3][18], smithedItems[3][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[3][20], smithedItems[3][21], smithedItems[3][22], smithedItems[3][23], smithedItems[3][24]}));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(11459, ""));
                player.send(new SetInterfaceTextEvent(11461, ""));
                player.send(new SetInterfaceTextEvent(1096, ""));
                player.send(new SetInterfaceTextEvent(1132, ""));
                player.send(new SetInterfaceTextEvent(1134, ""));
                player.send(new SetInterfaceTextEvent(1135, ""));
            } else if (barType == 2361) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[4][0], smithedItems[4][1], smithedItems[4][2], smithedItems[4][3], smithedItems[4][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[4][5], smithedItems[4][6], smithedItems[4][7], smithedItems[4][8], smithedItems[4][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[4][10], smithedItems[4][11], smithedItems[4][12], smithedItems[4][13], smithedItems[4][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[4][15], smithedItems[4][16], smithedItems[4][17], smithedItems[4][18], smithedItems[4][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[4][20], smithedItems[4][21], smithedItems[4][22], smithedItems[4][23], smithedItems[4][24]}));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(11459, ""));
                player.send(new SetInterfaceTextEvent(11461, ""));
                player.send(new SetInterfaceTextEvent(1096, ""));
                player.send(new SetInterfaceTextEvent(1132, ""));
                player.send(new SetInterfaceTextEvent(1134, ""));
                player.send(new SetInterfaceTextEvent(1135, ""));
            } else if (barType == 2363) {
                player.send(new UpdateInventoryEvent(1119, new Item[]{smithedItems[5][0], smithedItems[5][1], smithedItems[5][2], smithedItems[5][3], smithedItems[5][4]}));
                player.send(new UpdateInventoryEvent(1120, new Item[]{smithedItems[5][5], smithedItems[5][6], smithedItems[5][7], smithedItems[5][8], smithedItems[5][9]}));
                player.send(new UpdateInventoryEvent(1121, new Item[]{smithedItems[5][10], smithedItems[5][11], smithedItems[5][12], smithedItems[5][13], smithedItems[5][14]}));
                player.send(new UpdateInventoryEvent(1122, new Item[]{smithedItems[5][15], smithedItems[5][16], smithedItems[5][17], smithedItems[5][18], smithedItems[5][19]}));
                player.send(new UpdateInventoryEvent(1123, new Item[]{smithedItems[5][20], smithedItems[5][21], smithedItems[5][22], smithedItems[5][23], smithedItems[5][24]}));
                checkBars(player, barType);
                checkLevels(player, barType);
                player.send(new SetInterfaceTextEvent(11459, ""));
                player.send(new SetInterfaceTextEvent(11461, ""));
                player.send(new SetInterfaceTextEvent(1096, ""));
                player.send(new SetInterfaceTextEvent(1132, ""));
                player.send(new SetInterfaceTextEvent(1134, ""));
                player.send(new SetInterfaceTextEvent(1135, ""));
            } else {
                return;
            }
        } else {
            return;
        }
        player.send(new CloseInterfaceEvent());
        player.send(new OpenInterfaceEvent(994));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void handleSmithing(final Player player, final int itemId, int amount) {
        player.send(new CloseInterfaceEvent());
        amt = amount;
        for (int i = 0; i < barTypes.length; i++) {
            if (type == barTypes[i]) {
                for (int j = 0; j < smithedItems[i].length; j++) {
                    if (itemId == smithedItems[i][j].getId()) {
                        product = smithedItems[i][j];
                        barsReq = barsRequired[j];
                        barIndex = i;
                        if(player.hasAttributeTag("skilling")) {
                            return;
                        }
                        if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() < levelReqs[i][j]) {
                            player.sendMessage("You need level " + levelReqs[i][j] + " Smithing to smith this item.");
                            return;
                        }
                        if (player.getInventory().getCount(type) < barsRequired[j]) {
                            player.sendMessage("You don't have the required amount of bars to smith this.");
                            return;
                        }
                        if (!player.getInventory().contains(2347)) {
                            player.sendMessage("You need a hammer to smith items.");
                            return;
                        }
                        player.playAnimation(new Animation(898));
                        player.turnTo(loc);
                        player.getAttributeTags().add("skilling");

                        player.startAction(new Action(4, false, player) {

                            int barsRequired = Smithing.barsReq;
                            int amount = Smithing.amt;
                            int barType = Smithing.type;
                            int index = Smithing.barIndex;
                            Item smithedItem = Smithing.product;

                            @Override
                            public void execute() {
                                if (amount > 0) {
                                    if (player.getInventory().getCount(barType) >= barsRequired) {
                                        if (amount > 1) {
                                            player.playAnimation(new Animation(898));
                                        }
                                        player.getInventory().remove(new Item(barType, barsRequired));
                                        player.getInventory().add(smithedItem);
                                        player.getSkillSet().addExperience(Skill.SMITHING, 12.5 * (index + 1) * barsRequired * GameConstants.EXP_MODIFIER);
                                        Item product = new Item(itemId);
                                        player.sendMessage("You successfully smith a " + product.getDefinition().getName().toLowerCase() + ".");
                                    } else {
                                        player.sendMessage("You have ran out of bars.");
                                        player.getAttributeTags().remove("skilling");
                                        stop();
                                    }
                                }
                                amount--;
                                if (amount == 0) {
                                    player.getAttributeTags().remove("skilling");
                                    stop();
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
                    }
                }
            } else {
                return;
            }
        }
    }

    public static void setupSmeltingInterface(Player player, int objectId, Position pos) {
        loc = pos;
        if (objectId == 11666 || objectId == 2781) {
            for (int i = 0; i < levelReq.length; i++) {
                /*if(player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() < levelReq[i]) {
                player.send(new SetInterfaceTextEvent(barNameIds[i], "\n\n\n@red@" + bars[i].getDefinition().getName().trim().replace(" bar", "")));
                } else {
                player.send(new SetInterfaceTextEvent(barNameIds[i], "@bla@" + "\n\n\n\n" + bars[i].getDefinition().getName().trim().replace(" bar", "")));
                }*/
                if (i > 2) {
                    player.send(new SetInterfaceModelEvent(2405 + i + 1, 150, bars[i].getId()));
                } else {
                    player.send(new SetInterfaceModelEvent(2405 + i, 150, bars[i].getId()));
                }
            }
            player.send(new OpenChatInterfaceEvent(2400));
        } else {
            return;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void handleSmelting(final Player player, final int index, int amount) {
        amt = amount;
        player.send(new CloseInterfaceEvent());
        if (player.hasAttributeTag("skilling")) {
            return;
        }
        if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() < levelReq[index]) {
            player.sendMessage("You need level " + levelReq[index] + " Smithing to be able to smelt this bar.");
            return;
        }
        if (!player.getInventory().contains(primaryOre[index])) {
            player.sendMessage("You don't have the required ore to smelt this bar.");
            return;
        }
        if (secondaryOres[index] != null) {
            if (!player.getInventory().contains(secondaryOres[index].getId()) || player.getInventory().getCount(secondaryOres[index].getId()) < secondaryOres[index].getAmount()) {
                player.sendMessage("You don't have the required ore to smelt this bar.");
                return;
            }
        }
        player.playAnimation(new Animation(899));
        player.getAttributeTags().add("skilling");
        player.sendMessage("You place the " + new Item(primaryOre[index]).getDefinition().getName().trim().replace(" ore", "").toLowerCase() + " into the furnace.");
        player.turnTo(new Position(loc.getX(), loc.getY() + 1));
        World.getWorld().schedule(new Action(5, false, player) {

            int amount = Smithing.amt;
            boolean stop = false;

            @Override
            public void execute() {
                if (amount >= 1) {
                    if (amount > 1) {
                        player.playAnimation(new Animation(899));
                    }
                    player.getInventory().remove(new Item(primaryOre[index]));
                    if (secondaryOres[index] != null) {
                        player.getInventory().remove(secondaryOres[index]);
                    }
                    if (index == 1) {
                        int chance;
                        if (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() <= 45) {
                            chance = (player.getSkillSet().getSkill(Skill.SMITHING).getCurrentLevel() - levelReq[index]) + 50;
                        } else {
                            chance = 80;
                        }
                        if (player.getEquipment().contains(2568)) {
                            chance = 100;
                        }
                        if (chance >= Misc.random(100)) {
                            player.getInventory().add(bars[index]);
                            player.getSkillSet().addExperience(Skill.SMITHING, exp[index] * GameConstants.EXP_MODIFIER);
                            player.sendMessage("You retrieve a bar of " + bars[index].getDefinition().getName().trim().replace(" bar", "").toLowerCase() + ".");
                        } else {
                            player.sendMessage("The iron ore was too impure and failed to create a bar.");
                        }
                    } else {
                        player.getInventory().add(bars[index]);
                        player.getSkillSet().addExperience(Skill.SMITHING, exp[index] * GameConstants.EXP_MODIFIER);
                        player.sendMessage("You retrieve a bar of " + bars[index].getDefinition().getName().trim().replace(" bar", "").toLowerCase() + ".");
                    }
                    amount--;
                    if (amount == 0) {
                        player.getAttributeTags().remove("skilling");
                        stop = true;
                        stop();
                    }
                    if (secondaryOres[index] != null && stop == false) {
                        if (!player.getInventory().contains(secondaryOres[index].getId()) || player.getInventory().getCount(secondaryOres[index].getId()) < secondaryOres[index].getAmount() || !player.getInventory().contains(primaryOre[index])) {
                            player.sendMessage("You have ran out of the required ore.");
                            player.getAttributeTags().remove("skilling");
                            stop();
                        }
                    } else if (secondaryOres[index] == null && stop == false) {
                        if (!player.getInventory().contains(primaryOre[index])) {
                            player.sendMessage("You have ran out of the required ore.");
                            player.getAttributeTags().remove("skilling");
                            stop();
                        }
                    }
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
    }
}