package org.apollo.game.content.skills.woodcutting;

import java.util.HashMap;

import org.apollo.game.model.Animation;

/**
 * @author Rodrigo Molina
 */
public enum HatchetData {

	BRONZE(1351, 1, Animation.create(879)),
	IRON(1349, 1, Animation.create(877)),
	STEEL(1353, 5, Animation.create(875)),
	BLACK(1361, 10, Animation.create(873)),
	MITHRIL(1351, 20, Animation.create(871)),
	ADAMENT(1357, 30, Animation.create(869)),
	RUNE(1359, 40, Animation.create(867));
	
	HatchetData(int hatchetId, int levelNeeded, Animation anim) {
		this.setAnim(anim);
		this.setHatchetId(hatchetId);
		this.setLevelNeeded(levelNeeded);
	}
	private static HashMap<Integer, HatchetData> hatchet = new HashMap<Integer, HatchetData>();
	public static HatchetData get(int id) {
		return hatchet.get(id);
	}
	static {
		for(HatchetData h : HatchetData.values())
			hatchet.put(h.hatchetId, h);
	}
	private int hatchetId;
	private int levelNeeded;
	private Animation anim;

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	public int getHatchetId() {
		return hatchetId;
	}

	public void setHatchetId(int hatchetId) {
		this.hatchetId = hatchetId;
	}

	public int getLevelNeeded() {
		return levelNeeded;
	}

	public void setLevelNeeded(int levelNeeded) {
		this.levelNeeded = levelNeeded;
	}
}
