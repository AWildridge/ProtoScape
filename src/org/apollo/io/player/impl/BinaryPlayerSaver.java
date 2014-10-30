package org.apollo.io.player.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.apollo.game.content.quest.Quest;
import org.apollo.game.content.quest.QuestGeneral;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Friend;
import org.apollo.game.model.Friend.Event;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.io.player.PlayerSaver;
import org.apollo.util.StreamUtil;

/**
 * A {@link PlayerSaver} implementation that saves player data to a binary
 * file.
 * @author Graham
 */
public final class BinaryPlayerSaver implements PlayerSaver {

	@Override
	public void savePlayer(Player player) throws Exception {
		File f = BinaryPlayerUtil.getFile(player.getUndefinedName());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		try {
			// write credentials and privileges
			StreamUtil.writeString(out, player.getUndefinedName());
			StreamUtil.writeString(out, player.getCredentials().getPassword());
			out.writeByte(player.getPrivilegeLevel().toInteger());
			out.writeBoolean(player.isMembers());

			// write position
			Position position = player.getPosition();
			out.writeShort(position.getX());
			out.writeShort(position.getY());
			out.writeByte(position.getHeight());

			// write appearance
			out.writeBoolean(player.hasDesignedCharacter());
			Appearance appearance = player.getAppearance();
			out.writeByte(appearance.getGender().toInteger());
			int[] style = appearance.getStyle();
			for (int i = 0; i < style.length; i++) {
				out.writeByte(style[i]);
			}
			int[] colors = appearance.getColors();
			for (int i = 0; i < colors.length; i++) {
				out.writeByte(colors[i]);
			}
			out.flush();

			// write inventories
			writeInventory(out, player.getInventory());
			writeInventory(out, player.getEquipment());
			writeInventory(out, player.getBank());

			// write skills
			SkillSet skills = player.getSkillSet();
			out.writeByte(skills.size());
			for (int i = 0; i < skills.size(); i++) {
				Skill skill = skills.getSkill(i);
				out.writeByte(skill.getCurrentLevel());
				out.writeDouble(skill.getExperience());
			}
			
			// write friends
			Friend friends = player.getFriends();
			out.writeByte(friends.size());
			for (Entry<String, Event> entry : friends.getFriends().entrySet()) {
				StreamUtil.writeString(out, entry.getKey());
				out.writeByte(friends.getValue(entry.getValue()));
			}
			
			//bank pin
			if(player.isPinSet() && player.getBankPin() != null) {
			    out.writeBoolean(player.isPinSet());
			    out.writeUTF(player.getBankPin().getBankPin());
			}
			
			// writing quests
			//fuck.. 
			if(player.getQuestHolder() != null) {
				for(int i = 0; i < player.getQuestHolder().getQuestsStarted().length; i++) {
					Quest quest = player.getQuestHolder().getQuestsStarted()[i];
					if(quest == null) break;
					out.writeUTF("Quest started:"+quest.toString());//or something like that..
					//now we have to load the inner steps.
					if(!(quest instanceof QuestGeneral)) return;
					QuestGeneral general = (QuestGeneral) quest;
					out.writeInt(general.getStepSlot());
					out.writeInt(general.getCurrentStep().getStep());
				}
				for(int p=0; p < player.getQuestHolder().getQuestsFinished().length; p++) {
					
				}
			}
			
		} finally {
			out.close();
		}
	}

	/**
	 * Writes an inventory to the specified output stream.
	 * @param out The output stream.
	 * @param inventory The inventory.
	 * @throws IOException if an I/O error occurs.
	 */
	private void writeInventory(DataOutputStream out, Inventory inventory) throws IOException {
		int capacity = inventory.capacity();
		out.writeShort(capacity);

		for (int slot = 0; slot < capacity; slot++) {
			Item item = inventory.get(slot);
			if (item != null) {
				out.writeShort(item.getId() + 1);
				out.writeInt(item.getAmount());
			} else {
				out.writeShort(0);
				out.writeInt(0);
			}
		}
	}

}