package com.qwertyness.feudal.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
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
import com.qwertyness.feudal.listener.ChunkListener;

public class KingdomCommand implements CommandExecutor {
	private Feudal plugin;
	public static KingdomCommand instance;
	private List<String> openDisuniteQueries = new ArrayList<String>();
	
	public KingdomCommand(Feudal plugin) {
		this.plugin = plugin;
		instance = this;
	}

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
			if (args.length < 2) {
				printSyntax(player, "disbandfief <fief_name>");
				return false;
			}
			disbandFief(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setfiefbaron")) {
			if (args.length < 3) {
				printSyntax(player, "setfiefbaron <fief_name> <baron>");
				return false;
			}
			setFiefBaron(args[1], args[2], player);
		}
		else if (args[0].equalsIgnoreCase("setfiefbaroness")) {
			if (args.length < 3) {
				printSyntax(player, "setfiefbaroness <fief_name> <baron>");
				return false;
			}
			setFiefBaroness(args[1], args[2], player);
		}
		else if (args[0].equalsIgnoreCase("allocateland")) {
			if (args.length < 2) {
				printSyntax(player, "allocateland <fief_name>");
			}
			allocateLand(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("deallocateland")) {
			deallocateLand(player);
		}
		else if (args[0].equalsIgnoreCase("deallocateall")) {
			deallocateAll(player);
		}
		else if (args[0].equalsIgnoreCase("setduke")) {
			if (args.length < 2) {
				printSyntax(player, "setduke <duke_name>");
				return false;
			}
			setDuke(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setduchess")) {
			if (args.length < 2) {
				printSyntax(player, "setduchess <duchess_name>");
				return false;
			}
			setDuchess(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setcounterpart")) {
			if (args.length < 2) {
				printSyntax(player, "setcounterpart <counterpart_name>");
				return false;
			}
			setCounterpart(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setprince")) {
			if (args.length < 2) {
				printSyntax(player, "setprince <prince_name>");
				return false;
			}
			setPrince(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setprincess")) {
			if (args.length < 2) {
				printSyntax(player, "setprincess <princess_name>");
				return false;
			}
			setPrincess(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("listearls")) {
			listEarls(player);
		}
		else if (args[0].equalsIgnoreCase("addearl")) {
			if (args.length < 2) {
				printSyntax(player, "addearl <earl_name>");
				return false;
			}
			addEarl(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("removeearl")) {
			if (args.length < 2) {
				printSyntax(player, "removeearl <earl_index>");
				return false;
			}
			removeEarl(args[1], player);
		}
		else if (args[0].equalsIgnoreCase("setcapital")) {
			setCapital(player);
		}
		else if (args[0].equalsIgnoreCase("addfortress")) {
			addFortress(player);
		}
		else if (args[0].equalsIgnoreCase("listfortresses")) {
			listFortresses(player);
		}
		else if (args[0].equalsIgnoreCase("removefortress")) {
			removeFortress(player);
		}
		else if (args[0].equalsIgnoreCase("claim")) {
			claim(player);
		}
		else if (args[0].equalsIgnoreCase("autoclaim")) {
			autoClaim(player);
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
		
		Kingdom kingdom = new Kingdom(kingdomName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
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
			return;
		}
		if (this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAFief);
			return;
		}
		Fief fief = new Fief(fiefName, null, null, null, null, null, null, null, null, null, null);
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
	
	//Disband Feif
	public void disbandFief(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAFief);
			return;
		}
		
		Fief fief = this.plugin.fiefManager.getFief(kingdom.getName(), fiefName);
		this.plugin.fiefManager.deleteFief(kingdom, fief);
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.fiefDisband);
	}
	
	//Set fief baron/baroness
	@SuppressWarnings("deprecation")
	public void setFiefBaron(String fiefName, String baron, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player newBaron = Bukkit.getPlayer(fiefName);
		if (newBaron == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		if (this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAFief);
			return;
		}
		Fief fief = this.plugin.fiefManager.getFief(kingdom.getName(), fiefName);
		fief.baron = newBaron.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setFiefBaron);
	}
	
	@SuppressWarnings("deprecation")
	public void setFiefBaroness(String fiefName, String baroness, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player newBaroness = Bukkit.getPlayer(fiefName);
		if (newBaroness == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		if (this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAFief);
			return;
		}
		Fief fief = this.plugin.fiefManager.getFief(kingdom.getName(), fiefName);
		fief.baroness = newBaroness.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setFiefBaroness);
	}
	
	//Allocate/Deallocate land
	public void allocateLand(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()) != kingdom) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		if (this.plugin.fiefManager.getLandOwner(player.getLocation().getChunk()) != null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.landAlreadyAllocated);
			return;
		}
		if (!this.plugin.fiefManager.isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAFief);
			return;
		}
		Fief fief = this.plugin.fiefManager.getFief(kingdom.getName(), fiefName);
		fief.land.add(player.getLocation().getChunk());
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.allocateLand);
	}
	
	public void deallocateLand(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()) != kingdom) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		if (this.plugin.fiefManager.getLandOwner(player.getLocation().getChunk()) == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.landNotAllocated);
			return;
		}
		Fief fief = this.plugin.fiefManager.getLandOwner(player.getLocation().getChunk());
		fief.land.remove(player.getLocation().getChunk());
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.deallocateLand);
	}
	
	public void deallocateAll(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		for (Chunk chunk : kingdom.land) {
			Fief fief = this.plugin.fiefManager.getLandOwner(chunk);
			if (fief != null) {
				fief.land.remove(chunk);
			}
		}
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.deallocateAll);
	}
	
	@SuppressWarnings("deprecation")
	public void setDuke(String dukeName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.administrate)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player duke = Bukkit.getPlayer(dukeName);
		if (duke == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		kingdom.duke = duke.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setDuke);
	}
	
	@SuppressWarnings("deprecation")
	public void setDuchess(String duchessName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.administrate)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player duchess = Bukkit.getPlayer(duchessName);
		if (duchess == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		kingdom.duchess = duchess.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setDuchess);
	}
	
	@SuppressWarnings("deprecation")
	public void setCounterpart(String counterpartName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!kingdom.king.toString().equals(player.getUniqueId().toString()) && !kingdom.queen.toString().equals(player.getUniqueId().toString())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player counterpart = Bukkit.getPlayer(counterpartName);
		if (counterpart == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		
		if (kingdom.king.toString().equals(player.getUniqueId().toString())) {
			kingdom.queen = counterpart.getUniqueId();
		}
		else {
			kingdom.king = counterpart.getUniqueId();
		}
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setCounterpart);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrince(String princeName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.control)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player prince = Bukkit.getPlayer(princeName);
		if (prince == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		kingdom.prince = prince.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setPrince);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrincess(String princessName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.control)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player princess = Bukkit.getPlayer(princessName);
		if (princess == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		kingdom.princess = princess.getUniqueId();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setPrincess);
	}
	
	public void listEarls(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		player.sendMessage(Configuration.instance.messages.listTopStarter + kingdom.getName() + " Earls" + Configuration.instance.messages.listTopEnder);
		for (UUID earlUUID : kingdom.earls) {
			OfflinePlayer earl = Bukkit.getOfflinePlayer(earlUUID);
			player.sendMessage(Configuration.instance.messages.listIndexColor + kingdom.earls.indexOf(earlUUID) + ": "
					+ Configuration.instance.messages.listItemColor + earl.getName());
		}
		player.sendMessage(Configuration.instance.messages.listBottom);
	}
	
	@SuppressWarnings("deprecation")
	public void addEarl(String earlName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		Player earl = Bukkit.getPlayer(earlName);
		if (earl == null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAPlayer);
			return;
		}
		if (kingdom.earls.contains(earl.getUniqueId())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAnEarl);
			return;
		}
		
		kingdom.earls.add(earl.getUniqueId());
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.addEarl);
	}
	
	public void removeEarl(String earlIndex, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		int index = Integer.parseInt(earlIndex);
		if (index > (kingdom.earls.size()-1)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.invalidIndex);
			return;
		}
		
		kingdom.earls.remove(index);
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.removeEarl);
	}
	
	public void setCapital(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.control)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		if (Util.toString(kingdom.capital).equals(Util.toString(player.getLocation().getChunk()))) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyCapital);
			return;
		}
		
		kingdom.capital = player.getLocation().getChunk();
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.setCapital);
	}
	
	public void listFortresses(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.listTopStarter + kingdom.getName() + " Fortresses"
				+ Configuration.instance.messages.listTopEnder);
		for (Chunk fortress : kingdom.fortresses) {
			player.sendMessage(Configuration.instance.messages.listIndexColor + kingdom.fortresses.indexOf(fortress) 
					+ Configuration.instance.messages.listItemColor + Util.toString(fortress));
		}
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.listBottom);
	}
	
	public void addFortress(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		for (Chunk fortress : kingdom.fortresses) {
			if (Util.toString(fortress).equals(Util.toString(player.getLocation().getChunk()))) {
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.alreadyAFortress);
				return;
			}
		}
		
		kingdom.fortresses.add(player.getLocation().getChunk());
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.addFortress);
	}
	
	public void removeFortress(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		List<String> fortressStrings = new ArrayList<String>();
		boolean fortressFound = false;
		for (Chunk fortress : kingdom.fortresses) {
			fortressStrings.add(Util.toString(fortress));
			if (Util.toString(fortress).equals(Util.toString(player.getLocation().getChunk()))) {
				fortressFound = true;
			}
		}
		if (!fortressFound) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notAFortress);
			return;
		}
		
		kingdom.fortresses.remove(fortressStrings.indexOf(Util.toString(player.getLocation().getChunk())));
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.removeFortress);
	}
	
	public void claim(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()) != null) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.landAlreadyClaimed);
			return;
		}
		boolean fortressInRange = false;
		for (Chunk chunk : kingdom.fortresses) {
			if (Util.toChunkStringList(Util.getChunksInRadius(Configuration.instance.fortressRadius, chunk)).contains(Util.toString(player.getLocation().getChunk()))) {
				fortressInRange = true;
			}
		}
		if (!fortressInRange) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.noFortressInRange);
			return;
		}
		
		kingdom.land.add(player.getLocation().getChunk());
		player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.claimLand);
	}
	
	public void unclaim(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom)).hasPermission(kingdom.settings.manage)) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.kingdomManager.getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.notKingdomLand);
			return;
		}
		if (Util.toString(kingdom.capital).equals(Util.toString(player.getLocation().getChunk()))) {
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.cannotUnclaimCapital);
			return;
		}
		for (Chunk fortress : kingdom.fortresses) {
			if (Util.toString(fortress).equals(player.getLocation().getChunk())) {
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.cannotUnclaimFortress);
				return;
			}
		}
		for (Chunk land : kingdom.land) {
			if (Util.toString(land).equals(Util.toString(player.getLocation().getChunk()))) {
				kingdom.land.remove(land);
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.unclaimLand);
			}
		}
	}
	
	public void autoClaim(Player player) {
		if (ChunkListener.activeAutoclaim.contains(player.getUniqueId().toString())) {
			ChunkListener.activeAutoclaim.remove(player.getUniqueId().toString());
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.autoClaimOff);
		}
		else {
			ChunkListener.activeAutoclaim.add(player.getUniqueId().toString());
			claim(player);
			player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.autoClaimOn);
		}
	}
}
