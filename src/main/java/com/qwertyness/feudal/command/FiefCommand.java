package com.qwertyness.feudal.command;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.mail.CommandMail;
import com.qwertyness.feudal.util.Util;

public class FiefCommand implements CommandExecutor {
	private Feudal plugin;
	private Messages messages;
	
	private static String[] subcommands = {"setcounterpart", "addpeasent", "removepeasent", "addserf", "removeserf", "info", "manage", "bank"};
	
	public FiefCommand(Feudal plugin) {
		this.plugin = plugin;
		this.messages = Configuration.instance.messages;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!command.getName().equalsIgnoreCase("fief")) {
			return false;
		}
		if (this.plugin.getMailManager().checkCommandMail(command.getName(), args, sender)) {
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
			return false;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			help(player);
		}
		else if (args[0].equalsIgnoreCase("setcounterpart")) {
			if (args.length < 2) {
				printSyntax(player, "setcounterpart <player_name>");
				return false;
			}
			setCounterpart(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("addpeasent")) {
			if (args.length < 2) {
				printSyntax(player, "addpeasent <player_name>");
				return false;
			}
			addPeasent(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("removepeasent")) {
			if (args.length < 2) {
				printSyntax(player, "removepeasent <index>");
				return false;
			}
			try {
				removePeasent(player, Integer.parseInt(args[1]));
			} catch(NumberFormatException e) {
				player.sendMessage(this.messages.prefix + this.messages.invalidNumber);
				return false;
			}
		}
		else if (args[0].equalsIgnoreCase("listpeasents")) {
			listPeasents(player);
		}
		else if (args[0].equalsIgnoreCase("addserf")) {
			if (args.length < 2) {
				printSyntax(player, "addserf <player_name>");
				return false;
			}
			addSerf(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("removeserf")) {
			if (args.length < 2) {
				printSyntax(player, "removeserf <index>");
				return false;
			}
			try {
				removeSerf(player, Integer.parseInt(args[1]));
			} catch(NumberFormatException e) {
				player.sendMessage(this.messages.prefix + this.messages.invalidNumber);
				return false;
			}
		}
		else if (args[0].equalsIgnoreCase("listserfs")) {
			listSerfs(player);
		}
		else if (args[0].equalsIgnoreCase("info")) {
			info(player);
		}
		else if (args[0].equalsIgnoreCase("manage")) {
			ManageCommand.manageCommand(player, args, "fief");
		}
		else if (args[0].equalsIgnoreCase("bank")) {
			BankCommand.bankCommand(player, args, "fief");
		}
		else {
			help(player);
		}
		return true;
	}
	
	public void printSyntax(Player player, String syntax) {
		player.sendMessage(ChatColor.RED + "/fief " + syntax);
	}
	
	public void help(Player player) {
		player.sendMessage(this.messages.listTopStarter + "Kingdom Subcommands" + this.messages.listTopEnder);
		String subcommandList = "";
		for (String subcommand : subcommands) {
			subcommandList += subcommand + ", ";
		}
		subcommandList = subcommandList.substring(0, subcommandList.length()-2);
		player.sendMessage(this.messages.listItemColor + subcommandList);
		player.sendMessage(this.messages.listBottom);
	}
	
	public void info(Player player) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		
		player.sendMessage(this.messages.listTopStarter + "The Fief of " + fief.getName() + this.messages.listTopEnder);
		player.sendMessage(this.messages.listIndexColor + "Baron: "
				+ this.messages.listItemColor + ((fief.getBaron() == null) ? "None" : Bukkit.getOfflinePlayer(fief.getBaron()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Baroness: "
				+ this.messages.listItemColor + ((fief.getBaroness() == null) ? "None" : Bukkit.getOfflinePlayer(fief.getBaroness()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Peasents: "
				+ this.messages.listItemColor + Util.toUIString(Util.toPlayerNameList(fief.getPeasents())));
		player.sendMessage(this.messages.listIndexColor + "Serfs: "
				+ this.messages.listItemColor + Util.toUIString(Util.toPlayerNameList(fief.getSerfs())));
		player.sendMessage(this.messages.listIndexColor + "Land: "
				+ this.messages.listItemColor + fief.getLand().size());
		int fortresses = 0;
		for (Land land : fief.getLand()) {
			if (land.isFortress()) {
				fortresses++;
			}
		}
		player.sendMessage(this.messages.listIndexColor + "Fortresses: "
				+ this.messages.listItemColor + fortresses);
		player.sendMessage(this.messages.listIndexColor + "Bank Balance: "
				+ this.messages.listItemColor + fief.getBank().getBalance());
	}

	@SuppressWarnings("deprecation")
	public void setCounterpart(Player player, String counterpart) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		String title = Util.getTitle(player, Util.getKingdom(player), fief);
		if (title == null) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!title.equalsIgnoreCase("baron") && !title.equalsIgnoreCase("baroness")) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		
		OfflinePlayer newCounterpart = Bukkit.getOfflinePlayer(counterpart);
		if (newCounterpart == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		
		if (fief.getBaron() == null) {
			this.plugin.getMailManager().addMail(new CommandMail("fief", new String[] {"acceptposition", kingdom.getName()}, newCounterpart, 
					Arrays.asList(messages.prefix + "You have been invited as the Baron of " + 
						fief.getName() + ", " + 
						kingdom.getName() + 
						". Type /fief acceptposition " + 
						kingdom.getName() + 
						" to accept the position."), true) {
				public void run(String[] args) {
					Util.removePosition(plugin.getPlayerManager().getPlayer(fief.getBaron()), false);
					Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
					Util.setPosition("baron", kingdom, fief, newCounterpart);
					Player baronPlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
					if (baronPlayer != null) {
						baronPlayer.sendMessage(messages.prefix + messages.acceptPosition);
					}
					if (player.isOnline()) {
						player.sendMessage(messages.prefix + messages.setFiefBaron);
					}
				}
			});
		}
		else if (fief.getBaroness() == null) {
			this.plugin.getMailManager().addMail(new CommandMail("fief", new String[] {"acceptposition", kingdom.getName()}, newCounterpart, 
					Arrays.asList(messages.prefix + "You have been invited as the Baroness of " + 
						fief.getName() + ", " + 
						kingdom.getName() + 
						". Type /fief acceptposition " + 
						kingdom.getName() + 
						" to accept the position."), true) {
				public void run(String[] args) {
					Util.removePosition(plugin.getPlayerManager().getPlayer(fief.getBaron()), false);
					Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
					Util.setPosition("baron", kingdom, fief, newCounterpart);
					Player baronessPlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
					if (baronessPlayer != null) {
						baronessPlayer.sendMessage(messages.prefix + messages.acceptPosition);
					}
					if (player.isOnline()) {
						player.sendMessage(messages.prefix + messages.setFiefBaron);
					}
				}
			});
		}
		player.sendMessage(this.messages.prefix + this.messages.inviteFiefCounterpart);
	}
	
	@SuppressWarnings("deprecation")
	public void addPeasent(Player player, String peasent) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		if (!fief.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.FIEF_LEVEL)) {
			player.sendMessage(this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer newPeasent = Bukkit.getOfflinePlayer(peasent);
		if (newPeasent == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		
		this.plugin.getMailManager().addMail(new CommandMail("fief", new String[] {"acceptposition", kingdom.getName()}, newPeasent,
				Arrays.asList(messages.prefix + "You have been invited as a Peasent in " + 
						fief.getName() + ", " + 
						kingdom.getName() + 
						". Type /fief acceptposition " + 
						kingdom.getName() + 
						" to accept the position."), true) {
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), false);
				Util.setPosition("peasent", kingdom, fief, newPeasent);
				Player peasentPlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
				if (peasentPlayer != null) {
					peasentPlayer.sendMessage(messages.prefix + messages.acceptPosition);
				}
				if (player.isOnline()) {
					player.sendMessage(messages.prefix + messages.addPeasent);
				}
			}
		});
		player.sendMessage(this.messages.prefix + this.messages.invitePeasent);
	}
	
	public void removePeasent(Player player, int index) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		if (!fief.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.FIEF_LEVEL)) {
			player.sendMessage(this.messages.insufficientPermission);
			return;
		}
		
		fief.removePeasent(index);
		player.sendMessage(this.messages.prefix + this.messages.removePeasent);
	}
	
	public void listPeasents(Player player) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		player.sendMessage(this.messages.listTopStarter + fief.getName() + " Peasents" + this.messages.listTopEnder);
		for (UUID peasentUUID : fief.getPeasents()) {
			OfflinePlayer peasent = Bukkit.getOfflinePlayer(peasentUUID);
			player.sendMessage(this.messages.listIndexColor + fief.getPeasents().indexOf(peasentUUID) + ": "
					+ this.messages.listItemColor + peasent.getName());
		}
		player.sendMessage(this.messages.listBottom);
	}
	
	@SuppressWarnings("deprecation")
	public void addSerf(Player player, String serf) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		if (!fief.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.FIEF_LEVEL)) {
			player.sendMessage(this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer newSerf = Bukkit.getOfflinePlayer(serf);
		if (newSerf == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		
		this.plugin.getMailManager().addMail(new CommandMail("fief", new String[] {"acceptposition", kingdom.getName()}, newSerf,
				Arrays.asList(messages.prefix + "You have been invited as a Serf in " + 
						fief.getName() + ", " + 
						kingdom.getName() + 
						". Type /fief acceptposition " + 
						kingdom.getName() + 
						" to accept the position."), true) {
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), false);
				Util.setPosition("serf", kingdom, fief, newSerf);
				Player serfPlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
				if (serfPlayer != null) {
					serfPlayer.sendMessage(messages.prefix + messages.acceptPosition);
				}
				if (player.isOnline()) {
					player.sendMessage(messages.prefix + messages.addSerf);
				}
			}
		});
		player.sendMessage(this.messages.prefix + this.messages.inviteSerf);
	}
	
	public void removeSerf(Player player, int index) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		if (!fief.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.FIEF_LEVEL)) {
			player.sendMessage(this.messages.insufficientPermission);
			return;
		}
		
		fief.removeSerf(index);
		player.sendMessage(this.messages.prefix + this.messages.removeSerf);
	}
	
	public void listSerfs(Player player) {
		Kingdom kingdom = Util.getKingdom(player);	
		Fief fief = Util.getFief(player);
		if (fief == null || kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAFief);
			return;
		}
		player.sendMessage(this.messages.listTopStarter + fief.getName() + " Serfs" + this.messages.listTopEnder);
		for (UUID serfUUID : fief.getPeasents()) {
			OfflinePlayer serf = Bukkit.getOfflinePlayer(serfUUID);
			player.sendMessage(this.messages.listIndexColor + fief.getSerfs().indexOf(serfUUID) + ": "
					+ this.messages.listItemColor + serf.getName());
		}
		player.sendMessage(this.messages.listBottom);
	}
}