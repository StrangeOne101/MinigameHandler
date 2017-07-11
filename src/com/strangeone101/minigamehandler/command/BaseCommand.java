package com.strangeone101.minigamehandler.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public abstract class BaseCommand {
	
	public static Map<String, BaseCommand> commands = new HashMap<String, BaseCommand>();

	public BaseCommand(String command, String... aliases) {
		commands.put(command.toLowerCase(), this);
		
		for (String s : aliases) {
			commands.put(s, this);
		}
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String paramString, String[] args) {
		if (!sender.hasPermission("minigamehandler.command")) {
			sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "You don't have permission for this command!");
			return true;
		}
		
		if (args.length == 0) {
			sendHelp(sender);
			return true;
		}
		
		for (String command : commands.keySet()) {
			if (command.equalsIgnoreCase(args[0])) {
				if (!(sender instanceof Player) && commands.get(command).isPlayerOnly()) {
					sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "You don't must be a player to run this command!");
					return true;
				}
				commands.get(command).execute(sender, Arrays.asList(args).subList(1, args.length));
			}
		}
		return true;
	}
	
	/**
	 * Sends the command usage to the sender
	 * @param sender The sender
	 */
	private static void sendHelp(CommandSender sender) {
		
		//TODO Fill this out
		sender.sendMessage("coming soon");
	}

	public abstract void execute(CommandSender sender, List<String> args);
	
	/**
	 * Whether the command can only be run by a player.
	 * If set to true, the command will not run in the
	 * console.
	 * @return If the command can only be run by players
	 */
	public boolean isPlayerOnly() {
		return false;
	}

}
