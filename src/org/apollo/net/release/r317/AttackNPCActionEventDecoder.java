package org.apollo.net.release.r317;

import org.apollo.game.event.impl.AttackNPCActionEvent;
import org.apollo.game.model.NPC;
import org.apollo.game.model.World;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
/**
 * AttackNPCActionEventDecoder.java
 * @author The Wanderer
 * See player option handler.
 */
public class AttackNPCActionEventDecoder extends EventDecoder<AttackNPCActionEvent> {

	@Override
	public AttackNPCActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
                int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
                NPC npc = (NPC) World.getWorld().getNPCRepository().get(index);
                int npcID = npc.getDefinition().getId();
                System.out.println("[Attack NPC]- index: " + index + ", id: " + npcID);
		return new AttackNPCActionEvent(index);
	}
}
