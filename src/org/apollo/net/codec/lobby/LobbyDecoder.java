package org.apollo.net.codec.lobby;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.burtleburtle.bob.rand.IsaacRandom;
import org.apollo.util.ChannelBufferUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * LobbyDecoder.java
 * @author The Wanderer
 */
public class LobbyDecoder extends StatefulFrameDecoder<LobbyDecoderState> {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(LobbyDecoder.class.getName());
    /**
     * The secure random number generator.
     */
    private static final SecureRandom random = new SecureRandom();
    /**
     * The id.
     */
    private int id;
    /**
     * The size.
     */
    private int size;
    /**
     * The signature;
     */
    private byte[] signature = new byte[24];

    /**
     * Creates the login decoder with the default initial state.
     */
    public LobbyDecoder() {
        super(LobbyDecoderState.LOBBY_HANDSHAKE, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer buffer, LobbyDecoderState state) throws Exception {
        switch (state) {
            case LOBBY_HANDSHAKE:
                return decodeHandshake(ctx, channel, buffer);
            case LOBBY_HEADER:
                return decodeHeader(ctx, channel, buffer);
            case LOBBY_PAYLOAD:
                return decodePayload(ctx, channel, buffer);
            default:
                throw new Exception("Invalid lobby decoder state");
        }
    }

    private Object decodeHandshake(ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer buffer) throws Exception {
        if (buffer.readable()) {
            id = buffer.readUnsignedByte() & 0xFF;
            if (id != 19) {
                LOGGER.log(Level.INFO, "Unknown lobby credentials packet -> id: {0}", id);
            }
            setState(LobbyDecoderState.LOBBY_HEADER);
        }
        return null;
    }

    private Object decodeHeader(ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() >= 2) {
            int x = buffer.readUnsignedByte() & 0xFF;
            size = id << 8 | x;

            setState(LobbyDecoderState.LOBBY_PAYLOAD);
        }
        return null;
    }

    private Object decodePayload(ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() - 2 >= size) {
            int releaseNumber = buffer.readInt();
            //TODO: Create a way to get the release number from the {@link ServerContext} without creating a new instance
            if (releaseNumber != 634) {
                /*Invalid client revision*/
                //TODO: Create a collection of lobby client connected for a lobby server.
                throw new Exception("Invalid revision number!");
            }
            int blockSize = buffer.readUnsignedShort();
            int position = buffer.arrayOffset();
            //TODO: RSA
            if (buffer.readUnsignedByte() != 10) {
                throw new Exception("Invalid lobby response!");
            }

            int[] cipherKeys = new int[4];
            for (int i = 0; i < cipherKeys.length; i++) {
                cipherKeys[i] = buffer.readInt();
            }
            buffer.readLong();
            String password = ChannelBufferUtil.readString(buffer);
            buffer.readLong();
            buffer.readLong();
            buffer.readerIndex(position + blockSize);
            ChannelBufferUtil.decipherXtea(buffer, cipherKeys, buffer.arrayOffset(), size - buffer.arrayOffset());
            String username = ChannelBufferUtil.readString(buffer);
            buffer.readUnsignedByte();
            buffer.readUnsignedByte();
            buffer.readBytes(signature, 0, 24);
            ChannelBufferUtil.readString(buffer);
            buffer.readInt();
            IsaacRandom decodingRandom = new IsaacRandom(cipherKeys);
            for (int i = 0; i < cipherKeys.length; i++) {
                cipherKeys[i] += 50;
            }
            IsaacRandom encodingRandom = new IsaacRandom(cipherKeys);
            return null;
        }
        return null;
    }
}
