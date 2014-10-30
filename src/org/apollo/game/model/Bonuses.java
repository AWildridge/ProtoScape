package org.apollo.game.model;

import org.apollo.game.event.impl.SetInterfaceTextEvent;

/**
 *
 * @author ArrowzFtw
 */
public class Bonuses {

    private Character character;

    public Bonuses(Character character) {
        this.character = character;
    }
    private static int[] bonuses = new int[BonusConstants.BONUS_LENGTH];

    public int[] getBonus() {
        return bonuses;
    }
    
    public static int getBonus(int index) {
        return bonuses[index];
    }

    public void setBonus(int bonusType, int amount) {
        bonuses[bonusType] = amount;
    }

    private void writeBonus() {
        int offset = 0;
        String text = "";
        for (int i = 0; i < BonusConstants.BONUS_LENGTH; i++) {
            if (character.getBonuses().getBonus()[i] >= 0) {
                text = BonusConstants.BonusNames[i] + ": +" + character.getBonuses().getBonus()[i];
            } else {
                text = BonusConstants.BonusNames[i] + ": -" + java.lang.Math.abs(character.getBonuses().getBonus()[i]);
            }

            if (i == 10) {
                offset = 1;
            }
            character.send(new SetInterfaceTextEvent(1675 + i + offset, "" + text + ""));
        }

    }

    private void clearBonuses() {
        for (int i = 0; i < bonuses.length; i++) {
            setBonus(i, 0);
        }
    }

    private void updateBonuses(Item item) {
        for (int i = 0; i < item.getItemBonuses().getBonuses().length; i++) {
            setBonus(i, getBonus()[i] + item.getItemBonuses().getBonuses()[i]);
        }
    }

    public void refreshBonuses() {
        Player player = (Player) character;
        player.getEventManager().sendSpecialBar();
        clearBonuses();
        Inventory equipment = character.getEquipment();

        for (int i = 0; i < equipment.capacity(); i++) {
            if (equipment.get(i) != null) {
                Item equiped = equipment.get(i);
                if(equiped.getItemBonuses() == null) {
                    continue;
                } else {
                    updateBonuses(equiped);
                }
            }
        }
        writeBonus();
    }
}