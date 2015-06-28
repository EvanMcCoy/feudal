package com.qwertyness.feudal.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.listener.ChunkListener;
import com.qwertyness.feudal.util.LandUtil;
import com.qwertyness.feudal.util.Util;

public class KingdomCommand implements CommandExecutor {
	private Feudal plugin;
	public static KingdomCommand instance;
	private Messages messages;
	
	private static String[] subcommands = {"found", "disunite", "ally", "enemy", "neutral", "createfief", "disbandfief", "setfiefbaron", "setfiefbaroness", "allocateland", "deallocateland", "deallocateall", "setduke",
			"setduchess", "setcounterpart", "setprince", "setprincess", "listearls", "addearl", "removeearl", "setcapital", "listfortresses", "addfortress", "removefortress", "claim", "autoclaim", "manage", "info"};
	
	public KingdomCommand(Feudal plugin) {
		this.plugin = plugin;
		instance = this;
		this.messages = Configuration.instance.messages;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!command.getName().equalsIgnoreCase("kingdom") || !(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			this.help(player);
		}
		else if (args[0].equalsIgnoreCase("found")) {
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
		else if (args[0].equalsIgnoreCase("manage")) {
			KingdomManageCommand.manageCommand(player, args);
		}
		else if (args[0].equalsIgnoreCase("info")) {
			info(player);
		}
		else {
			help(player);
		}
		return false;
	}

	public void printSyntax(Player player, String syntax) {
		player.sendMessage(ChatColor.RED + "/kingdom " + syntax);
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
		player.sendMessage("" + this.plugin.getKingdomManager().kingdoms);
	}
	
	public void info(Player player) {
		
	}
	
	//Create
	public void found(String kingdomName, Player player) {
		if (plugin.getKingdomManager().isKingdom(kingdomName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAKingdom);
			return;
		}
		if (!Configuration.instance.allowMultipleTitles) {
			if (Util.isInKingdom(player)) {
				player.sendMessage(this.messages.prefix + this.messages.alreadyInAKingdom);
				return;
			}
		}
		
		if (plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) != null) {
			player.sendMessage(this.messages.prefix + this.messages.landAlreadyClaimed);
			return;
		}
		
		if (plugin.getKingdomData().get().getConfigurationSection(kingdomName) == null) {
			plugin.getKingdomData().get().createSection(kingdomName);
		}
		ConfigurationSection section = plugin.getKingdomData().get().createSection(kingdomName);
		Kingdom kingdom = new Kingdom(kingdomName, section);
		
		LandUtil.claimLand(kingdom, player.getLocation().getChunk());
		kingdom.setCapital(player.getLocation().getChunk());
		
		plugin.getKingdomManager().registerKingdom(kingdom);
		Util.setPosition("king", kingdom, null, player);
		player.sendMessage(this.messages.prefix + this.messages.kingdomCreate);
	}
	
	//Disunite
	public void disunite(final Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().control)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		player.sendMessage(this.messages.prefix + this.messages.kingdomDisuniteConfirm);
		player.sendMessage(this.messages.prefix + "Type /kingdom confirm to confirm.");
		Feudal.getInstance().registerFutureCommand(new FutureCommand("kingdom", new String[]{"confirm"}, player) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(player.getUniqueId()));
				Feudal.getInstance().getKingdomManager().deleteKingdom(kingdom);
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.kingdomDisunite);
			}
		});
	}
	
	//Create Fief
	public void createFief(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		ConfigurationSection section = this.plugin.getFiefData().get().createSection(fiefName);
		Fief fief = new Fief(fiefName, section);
		
		fief.setBaron(player.getUniqueId());
		this.plugin.getPlayerManager().getPlayer(player.getUniqueId()).fief = fief.getName();
		player.sendMessage(this.messages.prefix + this.messages.fiefCreate);
	}
	
	//Disband Feif
	public void disbandFief(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.notAFief);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		plugin.getPlayerManager().getPlayer(player.getUniqueId()).fief = "";
		if (Util.getTitle(player, kingdom, null) == null) {
			plugin.getPlayerManager().getPlayer(player.getUniqueId()).kingdom = "";
		}
		this.plugin.getFiefManager().deleteFief(kingdom, fief);
		player.sendMessage(this.messages.prefix + this.messages.fiefDisband);
	}
	
	//Set fief baron/baroness
	@SuppressWarnings("deprecation")
	public void setFiefBaron(String fiefName, String baron, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer newBaron = Bukkit.getOfflinePlayer(fiefName);
		if (newBaron == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		fief.setBaron(newBaron.getUniqueId());
		plugin.getPlayerManager().getPlayer(newBaron.getUniqueId()).kingdom = kingdom.getName();
		plugin.getPlayerManager().getPlayer(newBaron.getUniqueId()).fief = fief.getName();
		player.sendMessage(this.messages.prefix + this.messages.setFiefBaron);
	}
	
	@SuppressWarnings("deprecation")
	public void setFiefBaroness(String fiefName, String baroness, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer newBaroness = Bukkit.getOfflinePlayer(fiefName);
		if (newBaroness == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		fief.setBaroness(newBaroness.getUniqueId());
		plugin.getPlayerManager().getPlayer(newBaroness.getUniqueId()).kingdom = kingdom.getName();
		plugin.getPlayerManager().getPlayer(newBaroness.getUniqueId()).fief = fief.getName();
		player.sendMessage(this.messages.prefix + this.messages.setFiefBaroness);
	}
	
	//Allocate/Deallocate land
	public void allocateLand(String fiefName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) != kingdom) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		if (this.plugin.getFiefManager().getLandOwner(player.getLocation().getChunk()) != null) {
			player.sendMessage(this.messages.prefix + this.messages.landAlreadyAllocated);
			return;
		}
		if (!this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.notAFief);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		plugin.getLandManager().getLand(Util.toString(player.getLocation().getChunk())).fief = fief;
		player.sendMessage(this.messages.prefix + this.messages.allocateLand);
	}
	
	public void deallocateLand(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) != kingdom) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		if (this.plugin.getFiefManager().getLandOwner(player.getLocation().getChunk()) == null) {
			player.sendMessage(this.messages.prefix + this.messages.landNotAllocated);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getLandOwner(player.getLocation().getChunk());
		LandUtil.unclaimLand(fief, player.getLocation().getChunk());
		player.sendMessage(this.messages.prefix + this.messages.deallocateLand);
	}
	
	public void deallocateAll(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		for (Land land : kingdom.getLand()) {
			Fief fief = land.fief;
			if (fief != null) {
				fief.getLand().remove(land);
			}
		}
		player.sendMessage(this.messages.prefix + this.messages.deallocateAll);
	}
	
	@SuppressWarnings("deprecation")
	public void setDuke(String dukeName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().administrate)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer duke = Bukkit.getOfflinePlayer(dukeName);
		if (duke == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		kingdom.setDuke(duke.getUniqueId());
		plugin.getPlayerManager().getPlayer(duke.getUniqueId()).kingdom = kingdom.getName();
		player.sendMessage(this.messages.prefix + this.messages.setDuke);
	}
	
	@SuppressWarnings("deprecation")
	public void setDuchess(String duchessName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().administrate)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer duchess = Bukkit.getOfflinePlayer(duchessName);
		if (duchess == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		kingdom.setDuchess(duchess.getUniqueId());
		player.sendMessage(this.messages.prefix + this.messages.setDuchess);
	}
	
	@SuppressWarnings("deprecation")
	public void setCounterpart(String counterpartName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().control)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer counterpart = Bukkit.getOfflinePlayer(counterpartName);
		if (counterpart == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		
		if (kingdom.getKing().toString().equals(player.getUniqueId().toString())) {
			kingdom.setQueen(counterpart.getUniqueId());
		}
		else {
			kingdom.setKing(counterpart.getUniqueId());
		}
		player.sendMessage(this.messages.prefix + this.messages.setCounterpart);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrince(String princeName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().control)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer prince = Bukkit.getOfflinePlayer(princeName);
		if (prince == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		kingdom.setPrince(prince.getUniqueId());
		player.sendMessage(this.messages.prefix + this.messages.setPrince);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrincess(String princessName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().control)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer princess = Bukkit.getOfflinePlayer(princessName);
		if (princess == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		kingdom.setPrincess(princess.getUniqueId());
		player.sendMessage(this.messages.prefix + this.messages.setPrincess);
	}
	
	public void listEarls(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		player.sendMessage(this.messages.listTopStarter + kingdom.getName() + " Earls" + this.messages.listTopEnder);
		for (UUID earlUUID : kingdom.getEarls()) {
			OfflinePlayer earl = Bukkit.getOfflinePlayer(earlUUID);
			player.sendMessage(this.messages.listIndexColor + kingdom.getEarls().indexOf(earlUUID) + ": "
					+ this.messages.listItemColor + earl.getName());
		}
		player.sendMessage(this.messages.listBottom);
	}
	
	@SuppressWarnings("deprecation")
	public void addEarl(String earlName, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		OfflinePlayer earl = Bukkit.getOfflinePlayer(earlName);
		if (earl == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (kingdom.isEarl(earl.getUniqueId())) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAnEarl);
			return;
		}
		
		kingdom.addEarl(earl.getUniqueId());
		player.sendMessage(this.messages.prefix + this.messages.addEarl);
	}
	
	public void removeEarl(String earlIndex, Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		int index = Integer.parseInt(earlIndex);
		if (index > (kingdom.getEarls().size()-1)) {
			player.sendMessage(this.messages.prefix + this.messages.invalidIndex);
			return;
		}
		
		kingdom.removeEarl(index);
		player.sendMessage(this.messages.prefix + this.messages.removeEarl);
	}
	
	public void setCapital(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().control)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		if (Util.toString(kingdom.getCapital()).equals(Util.toString(player.getLocation().getChunk()))) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyCapital);
			return;
		}
		
		kingdom.setCapital(player.getLocation().getChunk());
		player.sendMessage(this.messages.prefix + this.messages.setCapital);
	}
	
	public void listFortresses(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		
		player.sendMessage(this.messages.prefix + this.messages.listTopStarter + kingdom.getName() + " Fortresses"
				+ this.messages.listTopEnder);
		for (Land fortress : kingdom.getFortresses()) {
			player.sendMessage(this.messages.listIndexColor + kingdom.getFortresses().indexOf(fortress) 
					+ this.messages.listItemColor + fortress.getCoordinates());
		}
		player.sendMessage(this.messages.prefix + this.messages.listBottom);
	}
	
	public void addFortress(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		for (Land fortress : kingdom.getFortresses()) {
			if (fortress.getCoordinates().equals(Util.toString(player.getLocation().getChunk()))) {
				player.sendMessage(this.messages.prefix + this.messages.alreadyAFortress);
				return;
			}
		}
		
		plugin.getLandManager().getLand(Util.toString(player.getLocation().getChunk())).setFortress(true);
		player.sendMessage(this.messages.prefix + this.messages.addFortress);
	}
	
	public void removeFortress(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		
		Land fortressLand = plugin.getLandManager().getLand(Util.toString(player.getLocation().getChunk()));
		if (!fortressLand.isFortress()) {
			player.sendMessage(this.messages.prefix + this.messages.notAFortress);
			return;
		}
		fortressLand.setFortress(false);
		
		for (Land land : kingdom.getLand()) {
			boolean validLand = false;
			for (Land fortress : kingdom.getFortresses()) {
				if (Util.toChunkStringList(Util.getChunksInRadius(Configuration.instance.fortressRadius, fortress.getChunk())).contains(land.getCoordinates())) {
					validLand = true;
				}
			}
			if (validLand == false) {
				LandUtil.unclaimLand(kingdom, land.getChunk());
			}
		}
		
		player.sendMessage(this.messages.prefix + this.messages.removeFortress);
	}
	
	public void claim(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) != null) {
			player.sendMessage(this.messages.prefix + this.messages.landAlreadyClaimed);
			return;
		}
		boolean fortressInRange = false;
		for (Land land : kingdom.getFortresses()) {
			if (Util.toChunkStringList(Util.getChunksInRadius(Configuration.instance.fortressRadius, land.getChunk())).contains(Util.toString(player.getLocation().getChunk()))) {
				fortressInRange = true;
			}
		}
		if (!fortressInRange) {
			player.sendMessage(this.messages.prefix + this.messages.noFortressInRange);
			return;
		}
		
		LandUtil.claimLand(kingdom, player.getLocation().getChunk());
		player.sendMessage(this.messages.prefix + this.messages.claimLand);
	}
	
	public void unclaim(Player player) {
		if (!Util.isInKingdom(player)) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		Kingdom kingdom = Util.getKingdom(player);
		if (!GovernmentPermission.getGroupByKingdomTitle(Util.getTitle(player, kingdom, null)).hasPermission(kingdom.getSettings().manage)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()).getName().equals(kingdom.getName())) {
			player.sendMessage(this.messages.prefix + this.messages.notKingdomLand);
			return;
		}
		if (Util.toString(kingdom.getCapital()).equals(Util.toString(player.getLocation().getChunk()))) {
			player.sendMessage(this.messages.prefix + this.messages.cannotUnclaimCapital);
			return;
		}
		for (Land fortress : kingdom.getFortresses()) {
			if (fortress.getCoordinates().equals(player.getLocation().getChunk())) {
				player.sendMessage(this.messages.prefix + this.messages.cannotUnclaimFortress);
				return;
			}
		}
		for (Land land : kingdom.getLand()) {
			if (land.getCoordinates().equals(Util.toString(player.getLocation().getChunk()))) {
				LandUtil.unclaimLand(kingdom, land.getChunk());
				player.sendMessage(this.messages.prefix + this.messages.unclaimLand);
			}
		}
	}
	
	public void autoClaim(Player player) {
		if (ChunkListener.activeAutoclaim.contains(player.getUniqueId().toString())) {
			ChunkListener.activeAutoclaim.remove(player.getUniqueId().toString());
			player.sendMessage(this.messages.prefix + this.messages.autoClaimOff);
		}
		else {
			ChunkListener.activeAutoclaim.add(player.getUniqueId().toString());
			claim(player);
			player.sendMessage(this.messages.prefix + this.messages.autoClaimOn);
		}
	}
}
