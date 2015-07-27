package com.qwertyness.feudal.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Configuration.Messages;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.government.Church;
import com.qwertyness.feudal.government.CivilOrganizer;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.util.Util;

public class BankCommand {
	private static Feudal plugin;
	private static Messages messages;
	
	public BankCommand(Feudal fPlugin) {
		plugin = fPlugin;
		messages = Configuration.instance.messages;
	}

	public static void bankCommand(Player player, String[] args, String commandOrigin) {
		FeudalPlayer fPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		Kingdom kingdom = plugin.getKingdomManager().getKingdom(fPlayer.kingdom);
		Fief fief = plugin.getFiefManager().getFief(fPlayer.kingdom, fPlayer.fief);
		Bank bank = null;
		CivilOrganizer government = null;
		if (commandOrigin.equalsIgnoreCase("kingdom")) {
			if (kingdom != null) {
				bank = kingdom.getBank();
				government = kingdom;
			}
		}
		else if (commandOrigin.equalsIgnoreCase("kingdomarmy")) {
			if (kingdom != null) {
				bank = kingdom.getArmy().getBank();
				government = kingdom.getArmy();
			}
		}
		else if (commandOrigin.equalsIgnoreCase("kingdomchurch")) {
			if (kingdom != null) {
				bank = kingdom.getChurch().getBank();
				government = kingdom.getChurch();
			}
		}
		else if (commandOrigin.equalsIgnoreCase("fief")) {
			if (fief != null) {
				bank = fief.getBank();
				government = fief;
			}
		}
		else if (commandOrigin.equalsIgnoreCase("fiefarmy")) {
			if (fief != null) {
				bank = fief.getArmy().getBank();
				government = fief.getArmy();
			}
		}
		else if (commandOrigin.equalsIgnoreCase("fiefchurch")) {
			if (fief != null) {
				bank = fief.getChurch().getBank();
				government = fief.getChurch();
			}
		}
		
		if (bank == null) {
			player.sendMessage(messages.prefix + messages.notInAGovernment);
			return;
		}
		
		if (args.length < 2) {
			player.sendMessage(messages.listTopStarter + "Bank Information" + messages.listTopEnder);
			player.sendMessage(messages.listIndexColor + "Balance: " + messages.listItemColor + bank.getBalance());
			player.sendMessage(messages.listIndexColor + "Type /<command> bank lookup <material> to check inventory.");
			player.sendMessage(messages.listBottom);
		}
		else {
			if (args[1].equalsIgnoreCase("lookup")) {
				if (args.length < 3) {
					printSyntax(player, "lookup <material>");
					return;
				}
				lookup(player, kingdom, fief, government, bank, args[2]);
			}
			else if (args[1].equalsIgnoreCase("deposit")) {
				try {
					deposit(player, bank, Double.parseDouble(args[2]));
				} catch(NumberFormatException e) {
					player.sendMessage(messages.prefix + messages.invalidNumber);
				}
			}
			else if (args[1].equalsIgnoreCase("withdraw")) {
				try {
					withdraw(player, kingdom, fief, government, bank, Double.parseDouble(args[2]));
				} catch(NumberFormatException e) {
					player.sendMessage(messages.prefix + messages.invalidNumber);
				}
			}
			else if (args[1].equalsIgnoreCase("deposititem")) {
				Material material = Material.valueOf(args[2]);
				if (material != null) {
					try {
						depositItem(player, bank, Material.valueOf(args[2]), Integer.parseInt(args[3]));
					} catch(NumberFormatException e) {
						player.sendMessage(messages.prefix + messages.invalidNumber);
					}
				}
				else {
					player.sendMessage(messages.prefix + messages.invalidMaterial);
				}
			}
			else if (args[1].equalsIgnoreCase("withdrawitem")) {
				Material material = Material.valueOf(args[2]);
				if (material != null) {
					try {
						withdrawItem(player, kingdom, fief, government, bank, Material.valueOf(args[2]), Integer.parseInt(args[3]));
					} catch(NumberFormatException e) {
						player.sendMessage(messages.prefix + messages.invalidNumber);
					}
				}
				else {
					player.sendMessage(messages.prefix + messages.invalidMaterial);
				}
			}
		}
	}
	
	private static void printSyntax(Player player, String syntax) {
		player.sendMessage(ChatColor.RED + "/<command> bank " + syntax);
	}
	
	public static void lookup(Player player, Kingdom kingdom, Fief fief, CivilOrganizer government, Bank bank, String materialName) {
		if (!hasPermission(player, kingdom, fief, government)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		
		Material material = Material.valueOf(materialName);
		if (material == null) {
			player.sendMessage(messages.prefix + messages.invalidMaterial);
			return;
		}
		
		player.sendMessage(messages.listTopStarter + "Bank Inventory Lookup" + messages.listTopEnder);
		int amount = 0;
		if (bank.getInventory().get(material) != null) {
			amount = ((int)bank.getInventory().get(material));
		}
		player.sendMessage(messages.listIndexColor + material.toString() + ": " + messages.listItemColor + amount);
		player.sendMessage(messages.listBottom);
	}
	
	public static void deposit(Player player, Bank bank, double amount) {
		double balance = Feudal.getInstance().getBankManager().vaultEconomy.getBalance(player);
		if (balance < amount) {
			player.sendMessage(messages.prefix + messages.insufficientMoney);
			return;
		}
		Feudal.getInstance().getBankManager().vaultEconomy.withdrawPlayer(player, amount);
		bank.depositMoney(amount);
		player.sendMessage(messages.prefix + messages.depositMoney);
	}
	
	public static void withdraw(Player player, Kingdom kingdom, Fief fief, CivilOrganizer government, Bank bank, double amount) {
		if (!hasPermission(player, kingdom, fief, government)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		if (bank.getBalance() < amount) {
			player.sendMessage(messages.prefix + messages.insufficientBankMoney);
			return;
		}
		bank.withdrawMoney(amount);
		Feudal.getInstance().getBankManager().vaultEconomy.depositPlayer(player, amount);
		player.sendMessage(messages.prefix + messages.withdrawMoney);
	}
	
	public static void depositItem(Player player, Bank bank, Material material, int amount) {
		int balance = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item.getType() == material) {
				balance += item.getAmount();
			}
		}
		if (balance < amount) {
			player.sendMessage(messages.prefix + messages.insufficientItems);
			return;
		}
		bank.depositItem(material, amount);
		if (amount >= player.getItemInHand().getAmount()) {
			amount -= player.getItemInHand().getAmount();
			player.setItemInHand(null);
		}
		else {
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - amount);
			amount = 0;
		}
		for (int i = 0;i < player.getInventory().getSize();i++) {
			if (amount <= 0) {
				break;
			}
			if (player.getInventory().getItem(i).getType() == material) {
				if (amount >= player.getInventory().getItem(i).getAmount()) {
					amount -= player.getInventory().getItem(i).getAmount();
					player.getInventory().setItem(i, null);
				}
				else {
					player.getInventory().getItem(i).setAmount(player.getInventory().getItem(i).getAmount() - amount);
					amount = 0;
				}
			}
		}
		player.sendMessage(messages.prefix + messages.depositItems);
	}
	
	public static void withdrawItem(Player player, Kingdom kingdom, Fief fief, CivilOrganizer government, Bank bank, Material material, int amount) {
		if (!hasPermission(player, kingdom, fief, government)) {
			player.sendMessage(messages.prefix + messages.insufficientPermission);
			return;
		}
		if (bank.getInventory().get(material) < amount) {
			player.sendMessage(messages.prefix + messages.insufficientBankItems);
			return;
		}
		bank.withdrawItem(material, amount);
		player.getInventory().addItem(new ItemStack(material, amount));
		player.sendMessage(messages.prefix + messages.withdrawItems);
	}
	
	private static boolean hasPermission(Player player, Kingdom kingdom, Fief fief, CivilOrganizer government) {
		if (government instanceof Kingdom) {
			return ((Kingdom) government).getSettings().getManagePermission().titleHasPermission(Util.getTitle(player, (Kingdom) government, null), TitlePermission.KINGDOM_LEVEL);
		}
		else if (government instanceof Fief) {
			return GovernmentPermission.DEPUTY.titleHasPermission(Util.getTitle(player, kingdom, (Fief) government), TitlePermission.FIEF_LEVEL);
		}
		else if (government instanceof Army) {
			return GovernmentPermission.DEPUTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.ARMY_LEVEL);
		}
		else if (government instanceof Church) {
			return GovernmentPermission.DEPUTY.titleHasPermission(Util.getTitle(player, kingdom, fief), TitlePermission.CHURCH_LEVEL);
		}
		return false;
	}
}
