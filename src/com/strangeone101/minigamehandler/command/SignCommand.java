package com.strangeone101.minigamehandler.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.strangeone101.minigamehandler.Minigame;
import com.strangeone101.minigamehandler.MinigameHandler;
import com.strangeone101.minigamehandler.MinigameWorld;
import com.strangeone101.minigamehandler.RandomMinigameWorld;

public class SignCommand extends BaseCommand {
	
	public static Map<UUID, MinigameWorld> clickingPlayers = new HashMap<UUID, MinigameWorld>();
	public static Map<Block, MinigameWorld> signs = new HashMap<Block, MinigameWorld>();
	
	
	public SignCommand() {
		super("sign", "signs");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (args.size() == 0) {
			sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame sign <create/remove/edit> [...]");
			return;
		}
		
		if (args.get(0).equalsIgnoreCase("create")) {
			if (args.size() <= 2) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame sign create <minigame> [world]");
				return;
			}
			
			Minigame minigame = Minigame.getMinigame(args.get(1));
			
			if (minigame == null) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame not found! Do /minigame list for a list of minigames!");
				return;
			}
			
			MinigameWorld world;
			
			if (args.size() <= 2) {
				world = new RandomMinigameWorld(minigame);
			} else {
				world = Minigame.getMinigameWorld(args.get(2));
				
				if (world == null) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame World \"" + args.get(2) + "\" not found!");
					return;
				}
				
				if (world.getMinigame() != minigame) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame World \"" + args.get(2) + "\" does not support that minigame!");
					return;
				}
			}
			
			if (!clickingPlayers.containsKey(((Player)sender).getUniqueId())) {
				clickingPlayers.put(((Player)sender).getUniqueId(), world);
				
				new BukkitRunnable() {
					@Override
					public void run() {
						clickingPlayers.remove(((Player)sender).getUniqueId());
					}
				}.runTaskLater(MinigameHandler.INSTANCE, 20 * 20L);
			}
			
			sender.sendMessage(ChatColor.GREEN + "[Minigames] Click a sign to assign it to that world!");
		} else if (args.get(0).equalsIgnoreCase("remove")) {
			sender.sendMessage(ChatColor.GREEN + "[Minigames] Click a sign to remove it!");
			
			if (!clickingPlayers.containsKey(((Player)sender).getUniqueId())) {
				clickingPlayers.put(((Player)sender).getUniqueId(), null);
				
				new BukkitRunnable() {
					@Override
					public void run() {
						clickingPlayers.remove(((Player)sender).getUniqueId());
					}
				}.runTaskLater(MinigameHandler.INSTANCE, 20 * 20L);
			}
		}
		
	}
	
	public static void writeSigns() {
		File file = new File(MinigameHandler.INSTANCE.getDataFolder(), "Minigames/signs.txt");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for (Block block : signs.keySet()) {
				String line = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + "|";
				
				MinigameWorld world = signs.get(block);
				
				if (world instanceof RandomMinigameWorld) {
					line = line + ":" + world.getMinigame().getName();
				} else {
					line = line + world.getWorld().getName();
				}
				
				writer.write(line);
				writer.newLine();
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean addSign(Player player, Block block) {
		if (!clickingPlayers.containsKey(player.getUniqueId())) return false;
		
		signs.put(block, clickingPlayers.get(player.getUniqueId()));
		
		new BukkitRunnable() {
			@Override
			public void run() {
				clickingPlayers.remove(player.getUniqueId());
			}
			
		}.runTaskLater(MinigameHandler.INSTANCE, 1L);
		writeSigns();
		return true;
	}
	
	public static boolean removeSign(Player player, Block block) {
		if (!clickingPlayers.containsKey(player.getUniqueId())) return false;
		if (!signs.containsKey(block)) return false;
		
		signs.remove(block);
		
		Sign sign = (Sign) block.getState();
		sign.setLine(0, "");
		sign.setLine(1, "** Removed **");
		sign.setLine(2, "");
		sign.setLine(3, "");
		sign.update();
		
		writeSigns();
		
		return true;
	}

	@Override
	public boolean isPlayerOnly() {
		return true;
	}
}
