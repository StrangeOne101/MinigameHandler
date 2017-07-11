package com.strangeone101.minigamehandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

import net.md_5.bungee.api.ChatColor;

public class Minigame {
	
	private static Map<String, Minigame> minigames = new HashMap<String, Minigame>();
	private static Map<MinigameWorld, Minigame> worlds = new HashMap<MinigameWorld, Minigame>();
	
	private String name;
	private String displayName;
	
	private boolean enabled = true;
	private List<String> signText = new ArrayList<String>();
	
	public Minigame(String name) {
		this(name, false);
	}
	
	public Minigame(String name, boolean forceGenerate) {
		this.name = name;
		
		File properties = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/" + name + "/properties.ini");
		
		if (!properties.exists()) {
			MinigameHandler.INSTANCE.saveResource("resources/properties.ini", properties);
		}
		
		if (forceGenerate) {
			
		}
		
		try {
			for (String line : Files.readLines(properties, Charset.defaultCharset())) {
				if (line.startsWith("#") || !line.contains("=")) continue;
				
				String key = line.split("=", 2)[0];
				String value = line.split("=", 2)[1];
				
				if (key.equalsIgnoreCase("display-name")) displayName = ChatColor.translateAlternateColorCodes('&', value);
				else if (key.equalsIgnoreCase("enabled")) enabled = Boolean.getBoolean(value);
				else if (key.startsWith("sign-")) signText.add(ChatColor.translateAlternateColorCodes('&', value));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		minigames.put(name, this);
	}
	
	/**
	 * Adds a MinigameWorld to the minigame
	 * @param world The minigame world
	 */
	public void addWorld(MinigameWorld world) {
		worlds.put(world, this);
	}
	
	/**
	 * Remove a MinigameWorld from the minigame
	 * @param world The world to remove
	 */
	public void removeWorld(MinigameWorld world) {
		worlds.remove(world);
		
		File folder = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/" + world.getWorld().getName());
		folder.delete();
	}
	
	public static Collection<Minigame> getAllMinigames() {
		return minigames.values();
	}
	
	public static Collection<MinigameWorld> getAllMinigameWorlds() {
		return worlds.keySet();
	}
	
	public static Minigame getMinigame(String name) {
		return minigames.get(name);
	}
	
	/**
	 * Gets all worlds that are associated with one minigame
	 * @param minigame The minigame
	 * @return A list of worlds for that minigame
	 */
	public static Collection<MinigameWorld> getMinigameWorlds(Minigame minigame) {
		Collection<MinigameWorld> coll = new ArrayList<MinigameWorld>();
		
		for (MinigameWorld world : worlds.keySet()) {
			if (worlds.get(world).equals(minigame)) {
				coll.add(world);
			}
		}
		return coll;
	}
	
	/**
	 * Returns the minigameworld for the given world
	 * @param sworld The world (string)
	 * @return The minigame for that world
	 */
	public static MinigameWorld getMinigameWorld(String sworld) {
		for (MinigameWorld world : worlds.keySet()) {
			if (world.world.equals(sworld)) {
				return world;
			}
		}
		return null;
	}

	/**
	 * Gets the name of the minigame that should be displayed to users.
	 * @return The name of the minigame
	 */
	public String getDisplayName() {
		return displayName == null || displayName.equals("") ? name : displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		Util.updateINIFile(new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/" + name + "/properties.ini"), "display-name", displayName);
	}

	/**
	 * Clears all minigame data. Should only be called on plugin startup.
	 */
	protected static void clearData() {
		minigames.clear();
		worlds.clear();
	}

	public String getName() {
		return name;
	}

	public List<String> getSignText() {
		return signText;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
