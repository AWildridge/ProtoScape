package org.apollo.util;

import org.apollo.net.NetworkConstants;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A utility class which provides extra {@link ChannelBuffer}-related methods
 * which deal with data types used in the protocol.
 * @author Graham
 */
public final class ChannelBufferUtil {

    /**
     * Reads a string from the specified buffer.
     * @param buffer The buffer.
     * @return The string.
     */
    public static String readString(ChannelBuffer buffer) {
        StringBuilder builder = new StringBuilder();
        int character;
        while (buffer.readable() && ((character = buffer.readUnsignedByte()) != NetworkConstants.STRING_TERMINATOR)) {
            builder.append((char) character);
        }
        return builder.toString();
    }

    /**
     * Deciphers data within the buffer with the XTEA algorithm.
     * @param keys The XTEA keys.
     * @param off The offset in the buffer.
     * @param len The amount of bytes to decipher.
     */
    public static void decipherXtea(ChannelBuffer buffer, int[] keys, int off, int len) {
        int position = buffer.arrayOffset();
        buffer.readerIndex(off);
        int amountBlocks = (len - off) / 8;
        for (int i = 0; i < amountBlocks; i++) {
            int v0 = buffer.readInt();
            int v1 = buffer.readInt();
            int counter = -957401312;
            int delta = -1640531527;
            int cycle = 32;
            while ((cycle-- ^ 0xffffffff) < -1) {
                v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (counter + keys[0x3 & counter >>> 11]);
                counter -= delta;
                v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (counter + keys[counter & 0x3]);
            }
            buffer.readerIndex(buffer.arrayOffset() - 8);
            buffer.writeInt(v0);
            buffer.writeInt(v1);
        }
        buffer.readerIndex(position);
    }

    /**
     * Default private constructor to prevent instantiation by other classes.
     */
    private ChannelBufferUtil() {
    }
}
