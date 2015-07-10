package com.qwertyness.feudal.command;

import java.util.ArrayList;
import java.util.List;
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
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.listener.ChunkListener;
import com.qwertyness.feudal.util.LandUtil;
import com.qwertyness.feudal.util.Util;

public class KingdomCommand implements CommandExecutor {
	private Feudal plugin;
	public static KingdomCommand instance;
	private Messages messages;
	
	private static String[] subcommands = {"found", "disunite", "ally", "enemy", "neutral", "createfief", "disbandfief", "setfiefbaron", "setfiefbaroness", "allocateland", "deallocateland", "deallocateall", "setduke",
			"setduchess", "setcounterpart", "setprince", "setprincess", "listearls", "addearl", "removeearl", "setcapital", "listfortresses", "addfortress", "removefortress", "claim", "autoclaim", "bank", "manage", "info"};
	
	public KingdomCommand(Feudal plugin) {
		this.plugin = plugin;
		instance = this;
		this.messages = Configuration.instance.messages;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (this.plugin.checkFutureCommands(command.getName(), args, sender)) {
			return true;
		}
		
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
		else if (args[0].equalsIgnoreCase("bank")) {
			BankCommand.bankCommand(player, args, "kingdom");
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
	}
	
	public void info(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		player.sendMessage(this.messages.listTopStarter + "The Kingdom of " + kingdom.getName() + this.messages.listTopEnder);
		player.sendMessage(this.messages.listIndexColor + "King: "
				+ this.messages.listItemColor + ((kingdom.getKing() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getKing()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Queen: "
				+ this.messages.listItemColor + ((kingdom.getQueen() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getQueen()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Prince: "
				+ this.messages.listItemColor + ((kingdom.getPrince() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getPrince()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Princess: "
				+ this.messages.listItemColor + ((kingdom.getPrincess() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getPrincess()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Duke: "
				+ this.messages.listItemColor + ((kingdom.getDuke() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getDuke()).getName()));
		player.sendMessage(this.messages.listIndexColor + "Duchess: "
				+ this.messages.listItemColor + ((kingdom.getDuchess() == null) ? "None" : Bukkit.getOfflinePlayer(kingdom.getDuchess()).getName()));
		String earls = Util.toUIString(Util.toPlayerNameList(kingdom.getEarls()));
		player.sendMessage(this.messages.listIndexColor + "Earls: "
				+ this.messages.listItemColor + ((earls.equals("")) ? "None" : earls));
		List<String> fiefNames = new ArrayList<String>();
		for (Fief fief : kingdom.getFiefs()) {
			fiefNames.add(fief.getName());
		}
		String fiefs = Util.toUIString(fiefNames);
		player.sendMessage(this.messages.listIndexColor + "Fiefs: "
				+ this.messages.listItemColor + ((fiefs.equals("")) ? "None" : fiefs));
		player.sendMessage(this.messages.listIndexColor + "Capital - "
				+ this.messages.listItemColor + "X: " + kingdom.getCapital().getX() + ", Z: " + kingdom.getCapital().getZ() + ", World: " 
				+ kingdom.getCapital().getWorld().getName());
		player.sendMessage(this.messages.listIndexColor + "Land: "
				+ this.messages.listItemColor + kingdom.getLand().size());
		int fortresses = 0;
		for (Land land : kingdom.getLand()) {
			if (land.isFortress()) {
				fortresses++;
			}
		}
		player.sendMessage(this.messages.listIndexColor + "Fortresses: "
				+ this.messages.listItemColor + fortresses);
		player.sendMessage(this.messages.listIndexColor + "Bank Balance: "
				+ this.messages.listItemColor + kingdom.getBank().getBalance());
	}
	
	//Create
	public void found(String kingdomName, Player player) {
		if (plugin.getKingdomManager().isKingdom(kingdomName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAKingdom);
			return;
		}
		if (Util.getKingdom(player) != null) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyInAKingdom);
			return;
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
		if (!kingdom.getSettings().getControlPermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		player.sendMessage(this.messages.prefix + this.messages.kingdomDisuniteConfirm);
		player.sendMessage(this.messages.prefix + "Type /kingdom confirm to confirm.");
		Feudal.getInstance().registerFutureCommand(new FutureCommand("kingdom", new String[]{"confirm"}, player) {
			@Override
			public void run(String[] args) {
				for (UUID uuid : Util.getKingdomMembers(kingdom)) {
					Util.removePosition(plugin.getPlayerManager().getPlayer(uuid), false);
				}
				Feudal.getInstance().getKingdomManager().deleteKingdom(kingdom);
				player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.kingdomDisunite);
			}
		});
	}
	
	//Create Fief
	public void createFief(String fiefName, Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		ConfigurationSection section = this.plugin.getFiefData().get().createSection(fiefName);
		Fief fief = new Fief(fiefName, section);
		kingdom.addFief(fief);
		
		player.sendMessage(this.messages.prefix + this.messages.fiefCreate);
	}
	
	//Disband Feif
	public void disbandFief(String fiefName, Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		if (!this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.notAFief);
			return;
		}
		Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		plugin.getPlayerManager().getPlayer(player.getUniqueId()).fief = "";
		for (UUID uuid : Util.getFiefMembers(fief)) {
			Util.removePosition(this.plugin.getPlayerManager().getPlayer(uuid), false);
		}
		this.plugin.getFiefManager().deleteFief(kingdom, fief);
		player.sendMessage(this.messages.prefix + this.messages.fiefDisband);
	}
	
	//Set fief baron/baroness
	@SuppressWarnings("deprecation")
	public void setFiefBaron(String fiefName, String baron, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer newBaron = Bukkit.getOfflinePlayer(fiefName);
		if (newBaron == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		final Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);
		
		this.plugin.registerFutureCommand(new FutureCommand("fief", new String[] {"acceptposition", kingdom.getName()}, newBaron) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(fief.getBaron()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("baron", kingdom, fief, newBaron);
			}
			
		}).setJoinMessage("You have been invited as the Baron of " + fief.getName() + ", " + kingdom.getName() + ". Type /fief acceptposition " + kingdom.getName() + " to accept the position.");
		
		player.sendMessage(this.messages.prefix + this.messages.setFiefBaron);
	}
	
	@SuppressWarnings("deprecation")
	public void setFiefBaroness(String fiefName, String baroness, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer newBaroness = Bukkit.getOfflinePlayer(fiefName);
		if (newBaroness == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (this.plugin.getFiefManager().isFief(kingdom.getName(), fiefName)) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAFief);
			return;
		}
		final Fief fief = this.plugin.getFiefManager().getFief(kingdom.getName(), fiefName);

		this.plugin.registerFutureCommand(new FutureCommand("fief", new String[] {"acceptposition", kingdom.getName()}, newBaroness) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(fief.getBaroness()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("baroness", kingdom, fief, newBaroness);
			}
			
		}).setJoinMessage("You have been invited as the Baroness of " + fief.getName() + ", " + kingdom.getName() + ". Type /fief acceptposition " + kingdom.getName() + " to accept the position.");
		
		player.sendMessage(this.messages.prefix + this.messages.setFiefBaroness);
	}
	
	//Allocate/Deallocate land
	public void allocateLand(String fiefName, Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getAdministratePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer duke = Bukkit.getOfflinePlayer(dukeName);
		if (duke == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}

		this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, duke) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getDuke()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("duke", kingdom, null, duke);
			}
			
		}).setJoinMessage("You have been invited as the Duke of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		
		player.sendMessage(this.messages.prefix + this.messages.setDuke);
	}
	
	@SuppressWarnings("deprecation")
	public void setDuchess(String duchessName, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getAdministratePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer duchess = Bukkit.getOfflinePlayer(duchessName);
		if (duchess == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}

		this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, duchess) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getDuchess()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("duchess", kingdom, null, duchess);
			}
			
		}).setJoinMessage("You have been invited as the Duchess of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		player.sendMessage(this.messages.prefix + this.messages.setDuchess);
	}
	
	@SuppressWarnings("deprecation")
	public void setCounterpart(String counterpartName, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (kingdom.getKing() == null) {
			if (!kingdom.getQueen().toString().equals(player.getUniqueId().toString())) {
				player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
				return;
			}
		}
		else {
			if (!kingdom.getKing().toString().equals(player.getUniqueId().toString())) {
				player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
				return;
			}
		}
		final OfflinePlayer counterpart = Bukkit.getOfflinePlayer(counterpartName);
		if (counterpart == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		
		if (kingdom.getKing().toString().equals(player.getUniqueId().toString())) {
			this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, counterpart) {
				@Override
				public void run(String[] args) {
					Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getQueen()), false);
					Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), false);
					Util.setPosition("queen", kingdom, null, counterpart);
				}
				
			}).setJoinMessage("You have been invited as the Queen of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		}
		else {
			this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, counterpart) {
				@Override
				public void run(String[] args) {
					Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getKing()), false);
					Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), false);
					Util.setPosition("king", kingdom, null, counterpart);
				}
				
			}).setJoinMessage("You have been invited as the King of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
			
		}
		player.sendMessage(this.messages.prefix + this.messages.setCounterpart);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrince(String princeName, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getControlPermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer prince = Bukkit.getOfflinePlayer(princeName);
		if (prince == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}

		this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, prince) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getPrince()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("prince", kingdom, null, prince);
			}
			
		}).setJoinMessage("You have been invited as the Prince of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		
		player.sendMessage(this.messages.prefix + this.messages.setPrince);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrincess(String princessName, Player player) {
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getControlPermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer princess = Bukkit.getOfflinePlayer(princessName);
		if (princess == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}

		this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, princess) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(kingdom.getPrincess()), true);
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("princess", kingdom, null, princess);
			}
			
		}).setJoinMessage("You have been invited as the Princess of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		
		player.sendMessage(this.messages.prefix + this.messages.setPrincess);
	}
	
	public void listEarls(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
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
		final Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		final OfflinePlayer earl = Bukkit.getOfflinePlayer(earlName);
		if (earl == null) {
			player.sendMessage(this.messages.prefix + this.messages.notAPlayer);
			return;
		}
		if (kingdom.isEarl(earl.getUniqueId())) {
			player.sendMessage(this.messages.prefix + this.messages.alreadyAnEarl);
			return;
		}
		this.plugin.registerFutureCommand(new FutureCommand("kingdom", new String[] {"acceptposition", kingdom.getName()}, earl) {
			@Override
			public void run(String[] args) {
				Util.removePosition(plugin.getPlayerManager().getPlayer(this.recipiant.getUniqueId()), true);
				Util.setPosition("earl", kingdom, null, earl);
			}
			
		}).setJoinMessage("You have been invited as an Earl of " + kingdom.getName() + ". Type /kingdom acceptposition " + kingdom.getName() + " to accept the position.");
		player.sendMessage(this.messages.prefix + this.messages.addEarl);
	}
	
	public void removeEarl(String earlIndex, Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
			player.sendMessage(this.messages.prefix + this.messages.insufficientPermission);
			return;
		}
		int index = Integer.parseInt(earlIndex);
		if (index > (kingdom.getEarls().size()-1)) {
			player.sendMessage(this.messages.prefix + this.messages.invalidIndex);
			return;
		}
		Util.removePosition(this.plugin.getPlayerManager().getPlayer(kingdom.getEarls().get(index)), true);
		player.sendMessage(this.messages.prefix + this.messages.removeEarl);
	}
	
	public void setCapital(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getControlPermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		
		player.sendMessage(this.messages.prefix + this.messages.listTopStarter + kingdom.getName() + " Fortresses"
				+ this.messages.listTopEnder);
		for (Land fortress : kingdom.getFortresses()) {
			player.sendMessage(this.messages.listIndexColor + kingdom.getFortresses().indexOf(fortress) 
					+ this.messages.listItemColor + fortress.getCoordinates());
		}
		player.sendMessage(this.messages.prefix + this.messages.listBottom);
	}
	
	public void addFortress(Player player) {
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
		Kingdom kingdom = Util.getKingdom(player);
		if (kingdom == null) {
			player.sendMessage(this.messages.prefix + this.messages.notInAKingdom);
			return;
		}
		if (!kingdom.getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, kingdom, null), TitlePermission.KINGDOM_LEVEL)) {
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
