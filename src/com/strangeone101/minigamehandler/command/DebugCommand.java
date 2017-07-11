package com.strangeone101.minigamehandler.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand extends BaseCommand {
	
	public static List<UUID> debugOn = new ArrayList<UUID>();

	public DebugCommand() {
		super("debug");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		UUID id = ((Player)sender).getUniqueId();
		
		if (debugOn.contains(id)) {
			debugOn.remove(id);
			sender.sendMessage(ChatColor.GREEN + "[Minigames] Debug toggled off");
		} else {
			debugOn.add(id);
			sender.sendMessage(ChatColor.GREEN + "[Minigames] Debug toggled on");
		}

	}
	
	@Override
	public boolean isPlayerOnly() {
		return true;
	}

}
