package org.apollo.net.release.r634;

import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * An implementation of {@link Release} for the 634 protocol.
 * @author The Wanderer
 */
public class Release634 extends Release {
    
    /**
     * The incoming packet lengths array.
     */
    public static final int[] PACKET_LENGTHS = {
        0, -1, 6, -1, 0, 3, -1, 6, 1, 8, //0-9
        -1, 8, 3, 2, -1, 8, 7, 8, 3, -1, //10-19
        -1, 2, 7, 8, 7, 3, 7, 7, 2, 3, //20-29
        4, 1, 4, 8, 15, 5, 7, 2, 7, 8, //30-39
        12, -1, 8, -1, 3, 7, 2, 4, 11, 6, //40-49
        -1, 3, 4, -1, 15, 4, 7, 3, 4, 8, //50-59
        8, 11, 3, 3, -1, 3, -1, -1, 2, -1, //60-69
        0, 2, 16, -1, -1, 3, 4, -1, 16, 3, //70-79
        7, 3, 18, -1, -1 //80-84
            
    };
    
    /**
     * Creates and initializes this release.
     */
    public Release634() {
        super(634, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
        init();
    }
    
    /**
     * Initializes this release by registering encoders and decoders.
     */
    private void init() {
        
    }
    
}
