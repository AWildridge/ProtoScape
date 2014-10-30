package org.apollo.net.codec.lobby;

/**
 * An enumeration with the different states the {@link LobbyDecoder} can
 * be in.
 * @author The Wanderer
 */
public enum LobbyDecoderState {

    /**
     * The lobby handshake state will wait for the username hash to be
     * received. Once it is, a server session key will be sent to the client
     * and the state will be set to the lobby header state.
     */
    LOBBY_HANDSHAKE,
    /**
     * The lobby header state will wait for the lobby type and payload length
     * to be received. These are saved, and then the state will be set to the
     * lobby payload state.
     */
    LOBBY_HEADER,
    /**
     * The lobby payload state will wait for all lobby information (such as
     * client release number, username and password).
     */
    LOBBY_PAYLOAD;
}
