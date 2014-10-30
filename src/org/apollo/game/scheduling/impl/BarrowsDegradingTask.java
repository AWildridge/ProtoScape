package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * BarrowsDegradingTask.java
 * @author The Wanderer
 */
public class BarrowsDegradingTask extends ScheduledTask {

    /**
     * The player.
     */
    private Player player;
    /**
     * Represents the item being degraded.
     */
    private Item item;
    
    private int index;
    
    public static int[] barrowsItems = {
        4708, 4710, 4712, 4714,
        4716, 4718, 4720, 4722,
        4724, 4726, 4728, 4730,
        4732, 4734, 4736, 4738,
        4745, 4747, 4749, 4751,
        4753, 4755, 4757, 4759
    };
    
    public static String[] barrowsNames = {
        "Ahrim", "Dharok", "Verac", "Guthan", "Kharil", "Torag"
    };

    /**
     * Creates the degrading task for the specified item.
     */
    public BarrowsDegradingTask(Player player, Item item) {
        super(1, true);
        this.player = player;
        this.item = item;
        for (int i = 0; i < barrowsItems.length; i++) {
            if (barrowsItems[i] == item.getId()) {
                index = i;
            }
        }
    }

    @Override
    public void execute() {
        if (player.isInCombat()) {
            if (item.getTickInCombat() == 6000) {
                replaceBarrowEquipment(player, index, 0, null);
            } else if (item.getTickInCombat() == 4500) {
                replaceBarrowEquipment(player, index, 1, "100");
            } else if (item.getTickInCombat() == 3000) {
                replaceBarrowEquipment(player, index, 2, "75");
            } else if (item.getTickInCombat() == 1500) {
                replaceBarrowEquipment(player, index, 3, "50");
            } else if (item.getTickInCombat() == 0) {
                replaceBarrowEquipment(player, index, 4, "25");
            }
            item.setTickInCombat(item.getTickInCombat() - 1);
            this.stop();
        }
    }

    private void replaceBarrowEquipment(Player player, int index, int offset, String percentile) {
        Item newItem = new Item(barrowsItems[index] + 148 + offset + (index * 4), 1, item.getTickInCombat());
        player.getEquipment().set(item.getEquipmentDefinition().getSlot(), newItem);
        if (percentile == null) {
            player.sendMessage("Your " + item.getDefinition().getName() + " has degraded!");
        } else if (offset == 4) {
            player.sendMessage("Your " + item.getDefinition().getName().trim().replace(percentile, "") + " has broken!");
        } else {
            player.sendMessage("Your " + item.getDefinition().getName().trim().replace(percentile, "") + " has degraded!");
        }
    }
}
