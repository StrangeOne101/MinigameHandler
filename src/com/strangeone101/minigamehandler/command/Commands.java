package com.strangeone101.minigamehandler.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands {
	
	public Commands() {
		
		new WorldCommand();
		new CreateCommand();
		new ReloadCommand();
		new SignCommand();
		new DebugCommand();
		
		Bukkit.getPluginCommand("minigame").setExecutor(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
				return BaseCommand.onCommand(sender, cmd, string, args);
			}
		});
	}

}
