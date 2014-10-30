package org.apollo.game.content;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.World;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * @author M1lkman
 * @author The Wanderer
 */
public class Consumables {

    public enum Food {

        ANCHOVIES(319, 1),
        SHRIMP(315, 3),
        COOKED_CHICKEN(2140, 3),
        COOKED_MEAT(2142, 3),
        SARDINE(325, 4),
        BREAD(2309, 5),
        HERRING(347, 5),
        MACKEREL(355, 6),
        TROUT(333, 7),
        COD(339, 7),
        PIKE(351, 8),
        SALMON(329, 9),
        TUNA(361, 10),
        CAKE(1891, 4),
        TWO_THIRDS_CAKE(1893, 4),
        SLICE_OF_CAKE(1895, 4),
        LOBSTER(379, 12),
        BASS(365, 13),
        SWORDFISH(373, 14),
        CHEESE_POTATO(6705, 16),
        MONKFISH(7946, 16),
        ANCHOVY_PIZZA(2297, 9),
        ONE_HALF_ANCHOVY_PIZZA(2299, 9),
        SHARK(385, 20),
        SEA_TURTLE(397, 21),
        PINEAPPLE_PIZZA(2301, 11),
        ONE_HALF_PINEAPPLE_PIZZA(2303, 11),
        TUNA_POTATO(7060, 22),
        MANTA_RAY(391, 22);
        
        int health;
        int id;

        private Food(int id, int health) {
            this.health = health;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public int getAddHp() {
            return health;
        }
    }

    public enum Potions {

        ATTACK(new int[]{2428, 121, 123, 125}, Skill.ATTACK, .10, 3),
        STRENGTH(new int[]{113, 115, 117, 119}, Skill.STRENGTH, .10, 3),
        DEFENCE(new int[]{2432, 133, 135, 137}, Skill.DEFENCE, .10, 3),
        AGILITY(new int[]{3032, 3034, 3036, 3038}, Skill.AGILITY, 0, 3),
        PRAYER(new int[]{2434, 139, 141, 143}, Skill.PRAYER, .25, 7),
        SUPER_ATTACK(new int[]{2436, 145, 147, 149}, Skill.ATTACK, .15, 5),
        FISHING(new int[]{2438, 151, 153, 155}, Skill.FISHING, 0, 3),
        SUPER_STRENGTH(new int[]{2440, 157, 159, 161}, Skill.STRENGTH, .15, 5),
        SUPER_DEFENCE(new int[]{2442, 163, 165, 167}, Skill.DEFENCE, .15, 5),
        RANGING(new int[]{2444, 169, 171, 173}, Skill.RANGED, .10, 4),
        MAGIC(new int[]{3040, 3042, 3044, 3046}, Skill.MAGIC, 0, 5);
        int[] potionIds;
        int skillId, bonus;
        double percentage;

        private Potions(int[] potionIds, int skillId, double percentage, int bonus) {
            this.potionIds = potionIds;
            this.skillId = skillId;
            this.percentage = percentage;
            this.bonus = bonus;
        }

        public int getSkillId() {
            return skillId;
        }

        public int getBonus() {
            return bonus;
        }

        public double getPercentage() {
            return percentage;
        }
    }
    private static final int EAT_EMOTE = 829;
    static int currentLvl;

    /**
     * Handles eating food
     * @param item Item being eaten.
     */
    public static void handleEating(final Player player, Item food, int slot, int newItem) {
        for (Food f : Food.values()) {
            if (food.getId() == f.getId()) {
                if (player.hasAttributeTag("eating")) {
                    return;
                }
                player.getAttributeTags().add("eating");
                player.sendMessage("You eat some food.");
                player.playAnimation(new Animation(EAT_EMOTE));
                player.getInventory().remove(f.getId());
                switch (f.getId()) {
                    case 1891:
                        player.getInventory().add(1893);
                        break;

                    case 1893:
                        player.getInventory().add(1895);
                        break;

                    case 2297:
                        player.getInventory().add(2299);
                        break;

                    case 2301:
                        player.getInventory().add(2303);
                        break;
                }
                World.getWorld().schedule(new ScheduledTask(3, false) {

                    @Override
                    public void execute() {
                        player.getAttributeTags().remove("eating");
                        stop();
                    }
                });
                final Skill hp = player.getSkillSet().getSkill(Skill.HITPOINTS);
                int currentLevel;
                if (hp.getCurrentLevel() < hp.getMaximumLevel() && hp.getMaximumLevel() - hp.getCurrentLevel() > f.getAddHp()) {
                    currentLevel = hp.getCurrentLevel() + f.getAddHp();
                } else {
                    currentLevel = hp.getMaximumLevel();
                }
                player.getSkillSet().setSkill(Skill.HITPOINTS, new Skill(hp.getExperience(), currentLevel, hp.getMaximumLevel()));
            } 
        }
    }

    public static void handlePotions(final Player player, int itemId) {
        for (final Potions pot : Potions.values()) {
            for (int i = 0; i < pot.potionIds.length; i++) {
                if (pot.potionIds[i] == itemId) {
                    if (player.hasAttributeTag("eating")) {
                        return;
                    }
                    player.getAttributeTags().add("eating");
                    player.sendMessage("You drink the potion...");
                    player.playAnimation(new Animation(EAT_EMOTE));
                    player.getInventory().remove(pot.potionIds[i]);
                    if (i < 3) {
                        player.getInventory().add(pot.potionIds[i + 1]);
                    } else {
                        player.getInventory().add(229);
                    }
                    final Skill skill = player.getSkillSet().getSkill(pot.getSkillId());
                    int currentLevel;
                    if (skill.getCurrentLevel() < skill.getMaximumLevel()) {
                        currentLevel = skill.getCurrentLevel() + (int) (pot.getPercentage() * skill.getMaximumLevel()) + pot.getBonus();
                    } else {
                        currentLevel = skill.getMaximumLevel() + (int) (pot.getPercentage() * skill.getMaximumLevel()) + pot.getBonus();
                    }
                    currentLvl = currentLevel;
                    player.getSkillSet().setSkill(pot.getSkillId(), new Skill(skill.getExperience(), currentLevel, skill.getMaximumLevel()));
                    World.getWorld().schedule(new ScheduledTask(3, false) {

                        @Override
                        public void execute() {
                            player.getAttributeTags().remove("eating");
                            stop();
                        }
                    });
                    World.getWorld().schedule(new ScheduledTask(100, false) {

                        int current = Consumables.currentLvl;

                        @Override
                        public void execute() {
                            current--;
                            player.getSkillSet().setSkill(pot.getSkillId(), new Skill(skill.getExperience(), current, skill.getMaximumLevel()));
                            if (current == skill.getMaximumLevel()) {
                                stop();
                            }
                        }
                    });
                }
            }
        }
    }
}
