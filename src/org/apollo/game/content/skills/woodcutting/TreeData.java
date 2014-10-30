package org.apollo.game.content.skills.woodcutting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rodrigo Molina
 */
public enum TreeData {

	REGULAR_TREE(new int[] { 1276, 1277, 1278, 1279, 1280, 1282,
			1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319,
			1330, 1331, 1332, 1365, 1383, 1384, 3033, 3034, 3035, 3036, 3881,
			3882, 3883, 5902, 5903, 5904, 38786 }, 1511, 1, 30, 5, 1342),
	OAK_TREE(new int[] {1281, 1307}, 1521, 20, 30, 8, 1341)
	
	;
	
	private static Map<int[], TreeData> trees = new HashMap<int[], TreeData>();
	
	public static TreeData get(int id) {
		for(TreeData t : trees.values()) {
			int[] ids =  t.objectIds;
			for(int i = 0; i < ids.length; i++) {
				if(ids[i] == id)
					return t;
			}
		}
		return null;
	}
	
	static {
		for(TreeData t : TreeData.values()) {
			trees.put(t.objectIds, t);
		}
	}
	TreeData(int objectId[], int logGiven, int levelNeeded, int respawnTime, int xpGiven, int stumpId) {
		this.setLevelNeeded(levelNeeded);
		this.setLogGiven(logGiven);
		this.setObjectId(objectId);
		this.setRespawnTime(respawnTime);
		this.setXpGiven(xpGiven);
		this.setStumpId(stumpId);
	}
	private int[] objectIds;
	private int objectIdUsing;
	private int logGiven;
	private int levelNeeded;
	/**
	 * Now i'm not sure if the respawn time is a constant variable which can be used
	 * for any of the trees or if it varies on the tree.
	 */
	private int respawnTime;
	private int logsToCut;
	private int logsCut;
	private int xpGiven;
	private int stumpId;
	
	public boolean checkObjectIds(int id) {
		for(int i = 0; i < objectIds.length; i++)
			if(objectIds[i] == id)
				return true;
		return false;
	}
	
	public int[] getObjectId() {
		return objectIds;
	}
	
	public void setObjectId(int[] objectIds) {
		this.objectIds = objectIds;
	}
	
	public int getLogGiven() {
		return logGiven;
	}
	
	public void setLogGiven(int logGiven) {
		this.logGiven = logGiven;
	}
	
	public int getLevelNeeded() {
		return levelNeeded;
	}
	
	public void setLevelNeeded(int levelNeeded) {
		this.levelNeeded = levelNeeded;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}

	public int getXpGiven() {
		return xpGiven;
	}

	public void setXpGiven(int xpGiven) {
		this.xpGiven = xpGiven;
	}

	public int getObjectIdUsing() {
		return objectIdUsing;
	}

	public void setObjectIdUsing(int objectIdUsing) {
		this.objectIdUsing = objectIdUsing;
	}

	public int getStumpId() {
		return stumpId;
	}

	public void setStumpId(int stumpId) {
		this.stumpId = stumpId;
	}

	public int getLogsCut() {
		return logsCut;
	}

	public void setLogsCut(int logsCut) {
		this.logsCut = logsCut;
	}

	public int getLogsToCut() {
		return logsToCut;
	}

	public void setLogsToCut(int logsToCut) {
		this.logsToCut = logsToCut;
	}
	
}
