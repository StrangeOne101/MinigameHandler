package com.strangeone101.minigamehandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.google.common.io.Files;
import com.strangeone101.minigamehandler.command.SignCommand;

public class ConfigManager {
	
	public ConfigManager() {
		File folder = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		//This gets all folders in the Minigames folder. All of these are the base for minigames.
		//If it's not a directory, it's probably a non-important file like a readme.
		
		Minigame.clearData();
		
		for (File file : folder.listFiles()) {
			if (!file.isDirectory()) continue;
			
			MinigameHandler.INSTANCE.getLogger().info("Creating new " + file.getName() + " minigame...");
			Minigame minigame = new Minigame(file.getName());
			
			for (File innerDirectory : file.listFiles()) {
				if (!innerDirectory.isDirectory()) continue;
				
				new MinigameWorld(minigame, innerDirectory.getName());
			}
		}
		
		MinigameHandler.INSTANCE.getLogger().info("Created " + Minigame.getAllMinigames().size() + " minigames with " + Minigame.getAllMinigameWorlds().size() + " worlds!");
		
		MinigameHandler.INSTANCE.getLogger().info("Loading sign data... ");
		SignCommand.signs.clear();
		loadSigns();
		MinigameHandler.INSTANCE.getLogger().info("Loaded " + SignCommand.signs.size() + " signs!");
	}
	
	/**
	 * Loads the signs that can teleport players to minigames. Should only
	 * be called once on plugin startup.
	 */
	protected static void loadSigns() {
		//The signs.txt file stores all sign data
		File signs = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/signs.txt");
		
		try {
			if (!signs.exists()) {
				signs.createNewFile();
			}
			
			for (String line : Files.readLines(signs, Charset.defaultCharset())) {
				if (!line.matches("\\w.+,.+,.+,.+\\|\\w.+")) { //Makes sure its in the format of world, x, y, z, minigameworld
					MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: Line doesn't match! --- " + line);
					continue;
				}
				
				String sworld = line.split("\\|", 2)[0].split(",", 4)[0];
				String sx = line.split("\\|", 2)[0].split(",", 4)[1];
				String sy = line.split("\\|", 2)[0].split(",", 4)[2];
				String sz = line.split("\\|", 2)[0].split(",", 4)[3];
				
				if (!Util.isInteger(sx) || !Util.isInteger(sy) || !Util.isInteger(sz)) {
					MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: Non integers here? --- " + sworld + "," + sx + "," + sy + "," + sz);
					continue;
				}
				
				World world = Bukkit.getWorld(sworld);
				
				if (world == null) {
					MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: World \"" + sworld + "\" does not exist!");
					continue;
				}
				
				Block signBlock = world.getBlockAt(Integer.parseInt(sx), Integer.parseInt(sy), Integer.parseInt(sz));
				
				if (signBlock.getType() != Material.WALL_SIGN && signBlock.getType() != Material.SIGN_POST) {
					MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: No sign found at %w, %x, %y, %z!"
							.replaceFirst("%w", sworld).replaceFirst("%x", sx).replaceFirst("%y", sy).replaceFirst("%z", sz));
					continue;
				}
				
				String sminigameworld = line.split("\\|", 2)[1];
				MinigameWorld minigameworld;
				
				if (sminigameworld.startsWith(":")) {
					Minigame minigame = Minigame.getMinigame(sminigameworld.substring(1));
					
					if (minigame == null) {
						MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: Minigame \"" + sminigameworld + "\" not found!");
						continue;
					}
					
					minigameworld = new RandomMinigameWorld(minigame);
				} else {
					minigameworld = Minigame.getMinigameWorld(sminigameworld);
					
					if (minigameworld == null) {
						MinigameHandler.INSTANCE.getLogger().warning("Failed to read line on signs.txt: MinigameWorld \"" + sminigameworld + "\" not found!");
						continue;
					}
				}
				
				SignCommand.signs.put(signBlock, minigameworld);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
