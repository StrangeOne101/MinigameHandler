package com.strangeone101.minigamehandler.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.strangeone101.minigamehandler.MinigameHandler;
import com.strangeone101.minigamehandler.MinigameWorld;
import com.strangeone101.minigamehandler.command.SignCommand;

public class SignListener implements Listener {
	
	public SignListener() {
		Bukkit.getPluginManager().registerEvents(this, MinigameHandler.INSTANCE);
	}

	@EventHandler
	public void onHit(PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				if (SignCommand.clickingPlayers.keySet().contains(e.getPlayer().getUniqueId())) {
					e.setCancelled(true);
					if (SignCommand.signs.containsKey(e.getClickedBlock())) {
						e.getPlayer().sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Sign already registered to another world!");
					} else {
						if (SignCommand.clickingPlayers.get(e.getPlayer().getUniqueId()) == null) { //Remove
							SignCommand.removeSign(e.getPlayer(), e.getClickedBlock());
							e.getPlayer().sendMessage(ChatColor.GREEN + "[Minigames] Sign deleted!");
						} else {
							SignCommand.addSign(e.getPlayer(), e.getClickedBlock());
							e.getPlayer().sendMessage(ChatColor.GREEN + "[Minigames] Sign registered to minigame world!");
						}
					}
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){ //Right click the sign. So TP the player.
					MinigameWorld mworld = SignCommand.signs.get(e.getClickedBlock());
					World world = mworld.getWorld();
					
					Location loc = world.getSpawnLocation();
					
					e.getPlayer().sendMessage(ChatColor.GREEN + "Teleporting you to " + mworld.getDisplayName() + "...");
					e.getPlayer().teleport(loc);
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		
		if (e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST) {
			if (SignCommand.clickingPlayers.keySet().contains(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
			}
			
			if (SignCommand.signs.containsKey(e.getBlock())) {
				e.setCancelled(true);
				
				if (e.getPlayer().hasPermission("minigamehandler.command")) {
					e.getPlayer().sendMessage(ChatColor.GREEN + "[Minigames]" + ChatColor.RED + " Remove the sign with /minigame sign remove");
				}
			}
		}
	}
}
