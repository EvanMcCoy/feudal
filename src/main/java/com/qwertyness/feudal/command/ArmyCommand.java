package com.qwertyness.feudal.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.mail.CommandMail;
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
		if (this.plugin.getMailManager().checkCommandMail(command.getName(), args, sender)) {
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
		
		Army army = Util.getArmy(player);
		if (army == null) {
			player.sendMessage(messages.prefix + messages.noArmy);
			return false;
		}
		
		boolean royal = true;
		Kingdom kingdom = Util.getKingdom(player);
		Fief fief = Util.getFief(player);
		if (kingdom != null) { 
			if (fief != null) {
				royal = false;
			}
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
	
	public void setCounterpart(Player player, String counterpart, Kingdom kingdom, Fief fief, boolean royal) {
		
	}
	
	@SuppressWarnings("deprecation")
	public void setKnight(Player player, String knight, Kingdom kingdom, Fief fief, boolean royal) {
		if (!GovernmentPermission.ROYALTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.ARMY_LEVEL)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		Player newKnight = Bukkit.getPlayer(knight);
		if (newKnight == null) {
			player.sendMessage(messages.prefix + messages.notAPlayer);
			return;
		}
		
		this.plugin.getMailManager().addMail(new CommandMail("army", new String[] {"acceptposition", kingdom.getName()}, newKnight, 
				Arrays.asList(messages.prefix + "You have been invited as the Knight of " 
						+ ((royal) ? (kingdom.getName() + "'s Army. ") : (fief.getName() + "'s (" + kingdom.getName() + ") Army."))
						+ "Type /army acceptposition "
						+ kingdom.getName()
						+ "to accept the position."), true) {

			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer((royal) ? kingdom.getArmy().getKnight() : fief.getArmy().getKnight()), false);
				Util.removePosition(plugin.getPlayerManager().getPlayer(newKnight.getUniqueId()), false);
				Util.setPosition(((royal) ? "royal" : "") + "knight", kingdom, fief, newKnight);
				Player knightPlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
				if (knightPlayer != null) {
					knightPlayer.sendMessage(messages.prefix + messages.acceptPosition);
				}
				if (player.isOnline()) {
					player.sendMessage(messages.prefix + messages.setKnight);
				}
				
			}
		});
		player.sendMessage(this.messages.prefix + this.messages.inviteKnight);
	}
	
	@SuppressWarnings("deprecation")
	public void setDame(Player player, String dame, Kingdom kingdom, Fief fief, boolean royal) {
		if (!GovernmentPermission.ROYALTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.ARMY_LEVEL)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		Player newDame = Bukkit.getPlayer(dame);
		if (newDame == null) {
			player.sendMessage(messages.prefix + messages.notAPlayer);
			return;
		}
		
		this.plugin.getMailManager().addMail(new CommandMail("army", new String[] {"acceptposition", kingdom.getName()}, newDame, 
				Arrays.asList(messages.prefix + "You have been invited as the Dame of " 
						+ ((royal) ? (kingdom.getName() + "'s Army. ") : (fief.getName() + "'s (" + kingdom.getName() + ") Army."))
						+ "Type /army acceptposition "
						+ kingdom.getName()
						+ "to accept the position."), true) {

			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer((royal) ? kingdom.getArmy().getDame() : fief.getArmy().getDame()), false);
				Util.removePosition(plugin.getPlayerManager().getPlayer(newDame.getUniqueId()), false);
				Util.setPosition(((royal) ? "royal" : "") + "dame", kingdom, fief, newDame);
				Player damePlayer = Bukkit.getPlayer(this.recipiant.getUniqueId());
				if (damePlayer != null) {
					damePlayer.sendMessage(messages.prefix + messages.acceptPosition);
				}
				if (player.isOnline()) {
					player.sendMessage(messages.prefix + messages.setDame);
				}
				
			}
		});
		player.sendMessage(this.messages.prefix + this.messages.inviteDame);
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
		
		//TODO: Start capture.
	}
}
