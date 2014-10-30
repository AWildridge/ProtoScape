package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ResetAnimationEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * ResetAnimationEventEncoder.java
 * @author The Wanderer
 */
public class ResetAnimationEventEncoder extends EventEncoder<ResetAnimationEvent> {

    @Override
    public GamePacket encode(ResetAnimationEvent event) {
        GamePacketBuilder builder = new GamePacketBuilder(1);
        return builder.toGamePacket();
    }
}
