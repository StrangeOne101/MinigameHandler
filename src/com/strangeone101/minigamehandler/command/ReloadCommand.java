package com.strangeone101.minigamehandler.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.strangeone101.minigamehandler.MinigameHandler;

import net.md_5.bungee.api.ChatColor;

public class ReloadCommand extends BaseCommand {

	public ReloadCommand() {
		super("reload");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		new BukkitRunnable() {

			@Override
			public void run() {
				MinigameHandler.INSTANCE.onLoad();
				sender.sendMessage(ChatColor.YELLOW + "MinigameHandler reloaded!");
			}
			
		}.runTaskLater(MinigameHandler.INSTANCE, 1L);
	}
}
