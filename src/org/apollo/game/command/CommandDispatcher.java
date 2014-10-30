package org.apollo.game.command;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankPin;

/**
 * A class which dispatches {@link Command}s to {@link CommandListener}s.
 * @author Graham
 */
public final class CommandDispatcher {

    /**
     * A map of event listeners.
     */
    private final Map<String, CommandListener> listeners = new HashMap<String, CommandListener>();

    /**
     * Creates the command dispatcher.
     */
    public CommandDispatcher() {
        listeners.put("credits", new CreditsCommandListener());
    }

    /**
     * Dispatches a command to the appropriate listener.
     * @param player The player.
     * @param command The command.
     */
    public void dispatch(Player player, Command command) {
        final CommandListener listener = listeners.get(command.getName().toLowerCase());
        if (listener != null) {
            listener.execute(player, command);
        }
        if(command.getName().contains("setpin")) {
        	if(player.getBankPin() != null && player.isPinSet()) {
        		player.sendMessage("You already have a pin set!");
        		return;
        	}
        	player.setBankPin(new BankPin(player));
        	player.getBankPin().displayInterface();
        	System.out.println("Setting pin");
        }
        if(command.getName().contains("anim")) {
        	player.playAnimation(Animation.create(Integer.parseInt(command.getArguments()[0])));
        }
    }

    /**
     * Registers a listener with the.
     * @param command The command's name.
     * @param listener The listener.
     */
    public void register(String command, CommandListener listener) {
        listeners.put(command.toLowerCase(), listener);
    }
}