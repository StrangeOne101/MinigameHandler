package com.strangeone101.minigamehandler;

import java.util.Collections;
import java.util.List;

/**
 * This class allows signs to be set to no particular world,
 * but rather just a minigame. It will then send players to
 * a random world.
 */
public class RandomMinigameWorld extends MinigameWorld 
{
	public RandomMinigameWorld(Minigame minigame) {
		super(minigame, null);
		
		List<MinigameWorld> worlds = (List<MinigameWorld>) Minigame.getMinigameWorlds(getMinigame());
		
		if (worlds.size() == 0) return;
		Collections.shuffle(worlds);
	
		this.world = worlds.get(0).world;
	}
}
