package org.apollo.game.model;


/**
 * The request manager manages 
 * @author Graham Edgecombe
 *
 */
public class RequestManager {

    /**
     * Represents the different types of request.
     * 
     * @author Graham Edgecombe
     */
    public enum RequestType {

        /**
         * A trade request.
         */
        TRADE("tradereq"),
        /**
         * A duel request.
         */
        DUEL("duelreq");
        /**
         * The client-side name of the request.
         */
        private String clientName;

        /**
         * Creates a type of request.
         * @param clientName The name of the request client-side.
         */
        private RequestType(String clientName) {
            this.clientName = clientName;
        }

        /**
         * Gets the client name.
         * @return The client name.
         */
        public String getClientName() {
            return clientName;
        }

        public void setClientName(String s) {
            clientName = s;
        }
    }

    /**
     * Holds the different states the manager can be in.
     * 
     * @author Graham Edgecombe
     */
    public enum RequestState {

        /**
         * Nobody has offered a request.
         */
        NORMAL,
        /**
         * Somebody has offered some kind of request.
         */
        REQUESTED,
        /**
         * The player is participating in an existing request of this type, so
         * cannot accept new requests at all.
         */
        PARTICIPATING,
        /**
         * The the request is active - dueling only.
         */
        ACTIVE,
        /**
         * The request has finished - dueling only.
         */
        FINISHED;
    }
    /**
     * The player.
     */
    @SuppressWarnings("unused")
    private Player player;
    /**
     * The current state.
     */
    private RequestState state = RequestState.NORMAL;

    /**
     * Gets the current state.
     * @return The current state.
     */
    public RequestState getState() {
        return state;
    }

    /**
     * Sets the state.
     * @param state The state to set.
     */
    public void setState(RequestState state) {
        this.state = state;
    }
    /**
     * The current request type.
     */
    private RequestType requestType;
    /**
     * The current 'acquaintance'.
     */
    private Player acquaintance;

    /**
     * Sets the acquaintance.
     * @param acquaintance The acquaintance to set.
     */
    public void setAcquaintance(Player acquaintance) {
        this.acquaintance = acquaintance;
    }

    /**
     * The current 'acquaintance'.
     * @return the acquaintance
     */
    public Player getAcquaintance() {
        return acquaintance;
    }

    /**
     * Creates the request manager.
     * @param player The player whose requests the manager is managing.
     */
    public RequestManager(Player player) {
        this.player = player;
    }

    public void setRequestState(RequestState r) {
        state = r;
    }

    public void setRequestType(RequestType r) {
        requestType = r;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}