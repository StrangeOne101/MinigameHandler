package com.strangeone101.minigamehandler.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import com.strangeone101.minigamehandler.MinigameHandler;
import com.strangeone101.minigamehandler.MinigameWorld;
import com.strangeone101.minigamehandler.command.SignCommand;

public class SignUpdator extends BukkitRunnable {
	
	public SignUpdator() {
		this.runTaskTimer(MinigameHandler.INSTANCE, 20L, 20L);
	}

	@Override
	public void run() {
		for (Block signBlock : SignCommand.signs.keySet()) {
			MinigameWorld world = SignCommand.signs.get(signBlock);
			
			if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN_POST) {
				Sign sign = (Sign) signBlock.getState();
				
				boolean newText = false;
				for (int i = 0; i < 4 && i < world.getSignText().size(); i++) {
					//Set the text on the sign and update things like the player count
					String s = replaceStuff(world, world.getSignText().get(i));
					if (!sign.getLine(i).equals(s)) newText = true;
					sign.setLine(i, s);
					MinigameHandler.debug("Text line " + i + ": " + s);
				}
				if (newText) {
					sign.update();
				}
				
			} else {
				MinigameHandler.debug("Not a sign!");
			}
		}
	}
	
	public String replaceStuff(MinigameWorld world, String text) {
		return text.replace("{maxplayers}", world.getMaxPlayers() + "")
				.replace("{players}", world.getWorld().getPlayers().size() + "")
				.replace("{name}", world.getDisplayName() + "");
	}

}
