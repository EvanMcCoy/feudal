package com.qwertyness.feudal.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.Util;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;

public class KingdomCommand implements CommandExecutor {
	private Feudal plugin;
	private List<String> openDisuniteQueries = new ArrayList<String>();
	
	public KingdomCommand(Feudal plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!command.getName().equalsIgnoreCase("kingdom") || !(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (args[0].equalsIgnoreCase("found")) {
			if (args.length < 2) {
				printSyntax(player, "found <kingdom_name>");
				return false;
			}
			found(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("disunite")) {
			disunite(player);
		}
		else if (args[0].equalsIgnoreCase("ally")) {
			
		}
		else if (args[0].equalsIgnoreCase("enemy")) {
			
		}
		else if (args[0].equalsIgnoreCase("neutral")) {
			
		}
		else if (args[0].equalsIgnoreCase("createfief")) {
			if (args.length < 2) {
				printSyntax(player, "createfief <fief_name>");
				return false;
			}
			createFief(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("disbandfief")) {
			
		}
		else if (args[0].equalsIgnoreCase("setfiefbaron")) {
			
		}
		else if (args[0].equalsIgnoreCase("setfiefbaroness")) {
			
		}
		else if (args[0].equalsIgnoreCase("allocateland")) {
			
		}
		else if (args[0].equalsIgnoreCase("deallocateland")) {
			
		}
		else if (args[0].equalsIgnoreCase("deallocateall")) {
			
		}
		else if (args[0].equalsIgnoreCase("setduke")) {
			
		}
		else if (args[0].equalsIgnoreCase("setdutchess")) {
			
		}
		else if (args[0].equalsIgnoreCase("setcounterpart")) {
			
		}
		else if (args[0].equalsIgnoreCase("setprince")) {
			
		}
		else if (args[0].equalsIgnoreCase("setprincess")) {
			
		}
		else if (args[0].equalsIgnoreCase("listearls")) {
			
		}
		else if (args[0].equalsIgnoreCase("addearl")) {
			
		}
		else if (args[0].equalsIgnoreCase("removeearl")) {
			
		}
		else if (args[0].equalsIgnoreCase("setcapital")) {
			
		}
		else if (args[0].equalsIgnoreCase("addfortress")) {
			
		}
		else if (args[0].equalsIgnoreCase("listfortresses")) {
			
		}
		else if (args[0].equalsIgnoreCase("removefortress")) {
			
		}
		else if (args[0].equalsIgnoreCase("claim")) {
			
		}
		else if (args[0].equalsIgnoreCase("autoclaim")) {
			
		}
		return false;
	}

	public void printSyntax(Player player, String syntax) {
		player.sendMessage(ChatColor.RED + "/kingdom " + syntax);
	}
	
	//Create
	public void found(String kingdomName, Player player) {
		if (plugin.kingdomManager.isKingdom(kingdomName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAKingdom);
			return;
		}
		if (!Configuration.instance.allowMultipleTitles) {
			if (Util.isInKingdom(player)) {
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyInAKingdom);
				return;
			}
		}
		
		Kingdom kingdom = new Kingdom(kingdomName, null, null, null, null, null, null, null, null, null, null, null, null, null, 
				plugin.kingdomData.get().getConfigurationSection(kingdomName));
		if (plugin.playerManager.isPlayer(player.getUniqueId())) {
			if (plugin.playerManager.getPlayer(player.getUniqueId()).male) {
				kingdom.king = player.getUniqueId();
			}
			else {
				kingdom.queen = player.getUniqueId();
			}
		}
		else {
			kingdom.king = player.getUniqueId();
		}
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.kingdomCreate);
	}
	
	//Disunite
	public void disunite(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (this.openDisuniteQueries.contains(player.getUniqueId().toString())) {
			this.plugin.kingdomManager.deleteKingdom(kingdom);
			this.openDisuniteQueries.remove(player.getUniqueId().toString());
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.kingdomDisunite);
		}
		else {
			if (kingdom == null) {
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
				return;
			}
			if (!kingdom.king.toString().equalsIgnoreCase(player.getUniqueId().toString()) && !kingdom.queen.toString().equalsIgnoreCase(player.getUniqueId().toString())) {
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
				return;
			}
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.kingdomDisuniteConfirm);
			player.sendMessage(Configuration.instance.messages.prefix + "Type the command again to confirm.");
			this.openDisuniteQueries.add(player.getUniqueId().toString());
		}
	}
	
	//Create Fief
	public void createFief(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
		}
		if (this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAFief);
			return;
		}
		Fief fief = new Fief(fiefName, null, null, null, null, null, null, null, null);
		if (plugin.playerManager.isPlayer(player.getUniqueId())) {
			if (plugin.playerManager.getPlayer(player.getUniqueId()).male) {
				fief.baron = player.getUniqueId();
			}
			else {
				fief.baroness = player.getUniqueId();
			}
		}
		else {
			fief.baron = player.getUniqueId();
		}
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.fiefCreate);
	}
}
