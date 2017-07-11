package com.strangeone101.minigamehandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.strangeone101.minigamehandler.command.Commands;
import com.strangeone101.minigamehandler.command.DebugCommand;
import com.strangeone101.minigamehandler.listener.SignListener;
import com.strangeone101.minigamehandler.tasks.SignUpdator;

public class MinigameHandler extends JavaPlugin {
	
	public static MinigameHandler INSTANCE;
	
	@Override
	public void onEnable() {
		super.onLoad();
		
		INSTANCE = this;
		
		new ConfigManager();
		new Commands();
		new SignListener();
		
		new SignUpdator();
		
	}
	
	/**
	 * Copies a resource located in the jar to a file.
	 * 
	 * @param resourceName The filename of the resource to copy
	 * @param output The file location to copy it to. Should not exist.
	 * @return True if the operation succeeded.
	 */
	public boolean saveResource(String resourceName, File output) {
		if (getResource(resourceName) == null) return false;
		
		try {
			if (!output.exists()) {
				output.getParentFile().mkdirs();
				output.createNewFile();
			}
			
			InputStream in = getResource(resourceName);
			
			OutputStream out = new FileOutputStream(output);
	        byte[] buf = new byte[256];
	        int len;
	        
	        while ((len = in.read(buf)) > 0){
	        	out.write(buf, 0, len);
	        }
	        
	        out.close();
	        in.close();
	        
	        return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	/**
	 * Send a message to all players with debug on.
	 * @param message The message to send
	 */
	public static void debug(String message) {
		for (UUID id : DebugCommand.debugOn) {
			Player p = Bukkit.getPlayer(id);
			
			if (p != null) {
				p.sendMessage(ChatColor.GREEN + "[Minigames][Debug] " + ChatColor.YELLOW + message);
			}
		}
	}
}
