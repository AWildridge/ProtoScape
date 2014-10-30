package org.apollo.game.scheduling.impl;

import org.apollo.game.model.PrivateChat;
import org.apollo.game.scheduling.ScheduledTask;

public class BuildFriendsTask extends ScheduledTask {

	private int pulses = 10;
	
	public BuildFriendsTask() {
		super(0, true);
	}

	@Override
	public void execute() {
		if (pulses == 0) {
			PrivateChat.getInstance().dispatch();
			pulses = 10;
		} else {
			pulses--;
		}
	}

}