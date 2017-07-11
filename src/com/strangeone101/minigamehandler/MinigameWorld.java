package com.strangeone101.minigamehandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.google.common.io.Files;

import net.md_5.bungee.api.ChatColor;

public class MinigameWorld {
	
	protected Minigame minigame;
	protected String world;
	
	private String displayName;
	
	private int maxPlayers = 8;
	private boolean enabled = true;
	private List<String> signText = new ArrayList<String>();

	public MinigameWorld(Minigame minigame, String world) {
		this.minigame = minigame;
		this.world = world;
		
		if (world == null) return;
		
		World bworld = Bukkit.getWorld(world);

		if (bworld == null) {
			MinigameHandler.INSTANCE.getLogger().warning("Could not locate world \"" + world + "\" for minigame " + minigame.getDisplayName());
			return;
		}
		
		loadProperties();
		
		if (!(this instanceof RandomMinigameWorld)) {
			minigame.addWorld(this);
		}
	}
	
	public Minigame getMinigame() {
		return minigame;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	
	/**
	 * Whether the minigame world exists or not
	 * @return if the world exists
	 */
	public boolean exists() {
		return getWorld() != null;
	}
	
	/**
	 * Load all the properties for the world
	 */
	private void loadProperties() {
		File properties = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/" + minigame.getName() + "/" + world + "/properties.ini");
		
		if (!properties.exists()) {
			MinigameHandler.INSTANCE.saveResource("resources/properties.ini", properties);
		}
		
		try {
			for (String line : Files.readLines(properties, Charset.defaultCharset())) {
				if (line.startsWith("#") || !line.contains("=")) continue;
				
				String key = line.split("=", 2)[0];
				String value = line.split("=", 2)[1];
				
				MinigameHandler.INSTANCE.getLogger().info("Debug: " + key + " | " + value);
				
				if (key.equalsIgnoreCase("max-players")) maxPlayers = Integer.parseInt(value);
				else if (key.equalsIgnoreCase("display-name")) displayName = ChatColor.translateAlternateColorCodes('&', value);
				else if (key.equalsIgnoreCase("enabled")) enabled = Boolean.getBoolean(value);
				else if (key.startsWith("sign-")) signText.add(ChatColor.translateAlternateColorCodes('&', value));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDisplayName() {
		return displayName != null || displayName.equals("") ? displayName : world;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public List<String> getSignText() {
		return signText.size() == 0 ? minigame.getSignText() : signText;
	}
	
	public boolean isEnabled() {
		return enabled && minigame.isEnabled();
	}
}
