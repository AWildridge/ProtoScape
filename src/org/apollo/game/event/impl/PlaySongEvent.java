package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * Sends a packet to the client to play a specific song.
 *
 *
 * @author Rodrigo Molina
 */
public class PlaySongEvent extends Event {

    	/**
    	 * The song id?
    	 */
    	private int songId;
    	
    	/**
    	 * Creates a new song.
    	 * 
    	 * @param songId
    	 *	The song id.
    	 */
    	public PlaySongEvent(int songId) {
    	    this.songId = songId;
    	}
    	
    	/**
    	 * Get's the song id.
    	 * 
    	 * @return the song id.
    	 */
    	public int getSongId() {
    	    return songId;
    	}
}
