package com.qwertyness.feudal.mail;

import java.util.List;

import org.bukkit.OfflinePlayer;

public abstract class CommandMail extends Mail {
	protected String command;
	protected String[] subcommands;
	
	public CommandMail(String command, String[] subcommands, OfflinePlayer recipiant, List<String> messages, boolean recurring) {
		super(messages, recipiant, recurring);
		
		this.command = command;
		this.subcommands = subcommands;
	}

	public void runWithDestroy(String[] args) {
		this.run(args);
		this.destroy();
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
}
