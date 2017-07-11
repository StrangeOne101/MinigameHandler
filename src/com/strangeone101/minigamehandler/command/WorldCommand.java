package com.strangeone101.minigamehandler.command;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.strangeone101.minigamehandler.Minigame;
import com.strangeone101.minigamehandler.MinigameWorld;

public class WorldCommand extends BaseCommand {

	public WorldCommand() {
		super("worlds", "world");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (args.size() == 0) {
			sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame worlds <add/list/remove> [...]");
			return;
		}
		
		if (args.get(0).equalsIgnoreCase("add")) {
			if (args.size() <= 2) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame worlds add <minigame> <world> [worlds]");
				return;
			}
			
			Minigame minigame = Minigame.getMinigame(args.get(1));
			
			if (minigame == null) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame not found! Do /minigame list for a list of minigames!");
				return;
			}
			
			World world = Bukkit.getWorld(args.get(2));
			
			if (world == null) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "World \"" + args.get(2) + "\" not found!");
				return;
			}
			
			new MinigameWorld(minigame, world.getName());
			
			sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.GREEN + "World added to the minigame \"" + minigame.getDisplayName() + "\"!");
		} else if (args.get(0).equalsIgnoreCase("list")) {
			if (args.size() == 1) {
				sender.sendMessage(ChatColor.AQUA + "Minigame " + ChatColor.YELLOW + "| " + ChatColor.AQUA + "World");
				sender.sendMessage(ChatColor.YELLOW + "-------------------------");
				for (MinigameWorld world : Minigame.getAllMinigameWorlds()) {
					sender.sendMessage(ChatColor.AQUA + world.getMinigame().getDisplayName() + ": " + world.getWorld().getName());
				}
			} else {
				Minigame minigame = Minigame.getMinigame(args.get(1));
				
				if (minigame == null) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame not found! Do /minigame list for a list of minigames!");
					return;
				}
				
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.YELLOW + "Worlds for minigame " + ChatColor.AQUA + minigame.getDisplayName());
				
				for (MinigameWorld world : Minigame.getMinigameWorlds(minigame)) {
					sender.sendMessage(ChatColor.AQUA + "- " + world.getWorld().getName());
				}
			}
		} else if (args.get(0).equalsIgnoreCase("remove")) {
			if (args.size() <= 2) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame worlds remove <minigame> <world>");
				return;
			}
			
			Minigame minigame = Minigame.getMinigame(args.get(1));
			
			if (minigame == null) {
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame not found! Do /minigame list for a list of minigames!");
				return;
			}
			
			if (args.get(2).equalsIgnoreCase("all")) {
				if (args.size() == 3 || !args.get(3).equalsIgnoreCase("confirm")) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.YELLOW + "Please run " + ChatColor.RED + "/minigames worlds remove " + args.get(1) + " all confirm" + ChatColor.YELLOW + " to confirm");
					return;
				}
				
				Collection<MinigameWorld> worlds = Minigame.getMinigameWorlds(minigame);
				for (MinigameWorld world : worlds) {
					minigame.removeWorld(world);
				}
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + "All worlds for minigame " + ChatColor.YELLOW + minigame.getName() + ChatColor.GREEN + " removed!");
			} else {
				MinigameWorld world = Minigame.getMinigameWorld(args.get(2));
				
				if (world == null) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "World \"" + args.get(2) + "\" not found!");
					return;
				}
				
				if (world.getMinigame() != minigame) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Minigame World \"" + args.get(2) + "\" does not support that minigame!");
					return;
				}
				
				minigame.removeWorld(world);
				sender.sendMessage(ChatColor.GREEN + "[Minigames] " + "World \"" + world.getWorld().getName() + "\" removed from minigame!");
			}
			
		} else
		
		sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame worlds <add/list/remove> [...]");
	}

}
