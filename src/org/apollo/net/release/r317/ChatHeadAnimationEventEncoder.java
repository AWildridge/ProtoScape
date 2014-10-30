package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ChatHeadAnimationEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An npc head animation event encoder.
 *
 *
 * @author Rodrigo Molina
 */
public class ChatHeadAnimationEventEncoder extends EventEncoder<ChatHeadAnimationEvent> {

        @Override
        public GamePacket encode(ChatHeadAnimationEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(200);
            builder.put(DataType.SHORT, event.getInterfaceId());
            builder.put(DataType.SHORT, event.getAnimation().getId());
            return builder.toGamePacket();
        }

}
