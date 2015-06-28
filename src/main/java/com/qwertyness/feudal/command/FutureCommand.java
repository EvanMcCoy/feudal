package com.qwertyness.feudal.command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public abstract class FutureCommand {
	protected String command;
	protected String[] subcommands;
	protected OfflinePlayer recipiant;
	protected String joinMessage;
	
	public FutureCommand(String command, String[] subcommands, OfflinePlayer recipiant) {
		this.command = command;
		this.subcommands = subcommands;
		this.recipiant = recipiant;
	}

	public abstract void run(String[] args);
	
	public String getCommand() {
		return this.command;
	}
	
	public boolean compareSubcommands(String[] args) {
		if (this.subcommands == null) {
			return true;
		}
		if (this.subcommands.length > args.length) {
			return false;
		}
		for (int i = 0;i < this.subcommands.length;i++) {
			if (!this.subcommands[i].equalsIgnoreCase(args[i])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean compareRecipiant(CommandSender sender) {
		if (this.recipiant == null) {
			return true;
		}
		if (!(sender instanceof OfflinePlayer)) {
			return false;
		}
		OfflinePlayer offlinePlayer = (OfflinePlayer) sender;
		if (offlinePlayer.getUniqueId().toString().equals(this.recipiant.getUniqueId().toString())) {
			return true;
		}
		return false;
	}
	
	public String getJoinMessage() {
		return this.joinMessage;
	}
	
	public boolean hasJoinMessage() {
		return this.joinMessage == null;
	}
	
	public FutureCommand setJoinMessage(String message) {
		this.joinMessage = ChatColor.translateAlternateColorCodes('&', message);
		return this;
	}
}
