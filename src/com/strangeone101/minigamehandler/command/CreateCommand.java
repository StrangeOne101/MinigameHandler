package com.strangeone101.minigamehandler.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.strangeone101.minigamehandler.Minigame;

public class CreateCommand extends BaseCommand {

	public CreateCommand() {
		super("create", "c");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (args.size() == 0 || args.get(0).equals("")) {
			sender.sendMessage(ChatColor.GREEN + "[Minigames] " + ChatColor.RED + "Usage is /minigame create <name> [displayname]");
			return;
		}
		
		String name = args.get(0);
		
		String displayName = "";
		if (args.size() > 1) {
			for (String s : args.subList(1, args.size())) {
				displayName = displayName + " " + s;
			}
			displayName = ChatColor.translateAlternateColorCodes('&', displayName.substring(1));
		}
	
		Minigame minigame = new Minigame(name, true);
		minigame.setDisplayName(displayName);
		
		sender.sendMessage(ChatColor.YELLOW + "Successfully created new minigame " + ChatColor.AQUA + name + ChatColor.YELLOW + "!");
	}

}
