package com.qwertyness.feudal.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.util.Util;

public class ArmyCommand implements CommandExecutor {
	private Feudal plugin;
	private Messages messages;
	
	private static String[] subcommands = {"setknight", "setdame", "listsoldiers", "addsoldier", "removesoldier", "buy", "sell", "capture", "weaken", "raze", "info"};

	public ArmyCommand(Feudal plugin) {
		this.plugin = plugin;
		this.messages = Configuration.instance.messages;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (this.plugin.checkFutureCommands(command.getName(), args, sender)) {
			return true;
		}
		
		if (!command.getName().equalsIgnoreCase("army")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sender must be a player!");
			return false;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			help(player);
			return false;
		}
		Army army = null;
		boolean royal = false;
		Kingdom kingdom = Util.getKingdom(player);
		Fief fief = Util.getFief(player);
		Army testArmy = Util.getArmy(player);
		if (testArmy != null) {
			army = testArmy;
		}
		else if (fief == null) {
			if (kingdom != null) {
				army = kingdom.getArmy();
				royal = true;
			}
		}
		else {
			army = fief.getArmy();
		}
		
		if (army == null) {
			player.sendMessage(messages.prefix + messages.noArmy);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("setknight")) {
			setKnight(player, args[1], kingdom, fief, royal);
		}
		else if (args[0].equalsIgnoreCase("setdame")) {
			setDame(player, args[1], kingdom, fief, royal);
		}
		else if (args[0].equalsIgnoreCase("capture")) {
			capture(player);
		}
		return false;
	}
	
	public void help(Player player) {
		player.sendMessage(this.messages.listTopStarter + "Army Subcommands" + this.messages.listTopEnder);
		String subcommandList = "";
		for (String subcommand : subcommands) {
			subcommandList += subcommand + ", ";
		}
		subcommandList = subcommandList.substring(0, subcommandList.length()-2);
		player.sendMessage(this.messages.listItemColor + subcommandList);
		player.sendMessage(this.messages.listBottom);
	}
	
	@SuppressWarnings("deprecation")
	public void setKnight(Player player, String knight, Kingdom kingdom, Fief fief, boolean royal) {
		if (!GovernmentPermission.ROYALTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.ARMY_LEVEL)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		Player knightPlayer = Bukkit.getPlayer(knight);
		if (knightPlayer == null) {
			player.sendMessage(messages.prefix + messages.notAPlayer);
			return;
		}
		Util.removePosition(this.plugin.getPlayerManager().getPlayer(knightPlayer.getUniqueId()), false);
		Util.setPosition(((royal) ? "royal" : "") + "knight", kingdom, fief, knightPlayer);
		player.sendMessage(messages.prefix + messages.setKnight);
	}
	
	@SuppressWarnings("deprecation")
	public void setDame(Player player, String dame, Kingdom kingdom, Fief fief, boolean royal) {
		if (!GovernmentPermission.ROYALTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.ARMY_LEVEL)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		Player damePlayer = Bukkit.getPlayer(dame);
		if (damePlayer == null) {
			player.sendMessage(messages.prefix + messages.notAPlayer);
			return;
		}
		Util.removePosition(this.plugin.getPlayerManager().getPlayer(damePlayer.getUniqueId()), false);
		Util.setPosition(((royal) ? "royal" : "") + "knight", kingdom, fief, damePlayer);
		player.sendMessage(messages.prefix + messages.setDame);
	}
	
	public void capture(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		Fief fief = Util.getFief(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		String title = Util.getTitle(player, kingdom, fief);
		Army army = null;
		if (!GovernmentPermission.ROYALTY.titleHasPermission(title, TitlePermission.ARMY_LEVEL)) {
			if (title.equalsIgnoreCase("royalKnight") || title.equalsIgnoreCase("royalDame")) {
				army = kingdom.getArmy();
			}
			else if (title.equalsIgnoreCase("knight") || title.equalsIgnoreCase("dame")) {
				army = Util.getFief(player).getArmy();
			}
		}
		
	}
}
