package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PlaySongEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * Encodes a song.
 *
 *
 * @author Rodrigo Molina
 */
public class PlaySongEventEncoder extends EventEncoder<PlaySongEvent> {

        @Override
        public GamePacket encode(PlaySongEvent event) {
            GamePacketBuilder builder = new GamePacketBuilder(74);
            builder.put(DataType.SHORT, DataOrder.LITTLE, event.getSongId());
            return builder.toGamePacket();
        }

}
