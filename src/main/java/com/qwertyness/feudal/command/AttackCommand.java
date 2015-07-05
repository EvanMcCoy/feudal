package com.qwertyness.feudal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.util.Util;

public class AttackCommand implements CommandExecutor {
	private Feudal plugin;
	private Messages messages;
	
	private static String[] subcommands = {"capture", "weaken", "raze"};

	public AttackCommand(Feudal plugin) {
		this.plugin = plugin;
		this.messages = Configuration.instance.messages;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!command.getName().equalsIgnoreCase("attack")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sender must be a player!");
			return false;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			help(player);
		}
		else if (args[1].equalsIgnoreCase("capture")) {
			capture(player);
		}
		return false;
	}
	
	public void help(Player player) {
		player.sendMessage(this.messages.listTopStarter + "Attack Subcommands" + this.messages.listTopEnder);
		String subcommandList = "";
		for (String subcommand : subcommands) {
			subcommandList += subcommand + ", ";
		}
		subcommandList = subcommandList.substring(0, subcommandList.length()-2);
		player.sendMessage(this.messages.listItemColor + subcommandList);
		player.sendMessage(this.messages.listBottom);
	}
	
	public void capture(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		String title = Util.getTitle(player, kingdom, null);
		Army army = null;
		if (!GovernmentPermission.ROYALTY.titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			if (title.equalsIgnoreCase("royalKnight") || title.equalsIgnoreCase("royalDame")) {
				army = kingdom.getArmy();
			}
			else if (title.equalsIgnoreCase("knight") || title.equalsIgnoreCase("dame")) {
				army = Util.getFief(player).getArmy();
			}
		}
		
	}
}
