package com.qwertyness.feudal.data.government;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.government.CivilOrganizer;

public class BankManager {
	public Economy vaultEconomy;

	public Bank loadBank(ConfigurationSection government) {
		ConfigurationSection bankSection = government.getConfigurationSection("bank");
		
		Bank bank = new Bank();
		bank.depositMoney(bankSection.getDouble("balance"));
		for (String materialString : bankSection.getConfigurationSection("inventory").getKeys(false)) {
			Material material = Material.valueOf(materialString);
			int amount = bankSection.getInt("inventory." + materialString);
			bank.depositItem(material, amount);
		}
		
		return bank;
	}
	
	public void saveBank(CivilOrganizer civilOrganizer) {
		ConfigurationSection bankSection = civilOrganizer.getDataPath().getConfigurationSection("bank");
		
		bankSection.set("balance", civilOrganizer.getBank().getBalance());
		for (Material key : civilOrganizer.getBank().getInventory().keySet()) {
			bankSection.set("inventory." + key.toString(), civilOrganizer.getBank().getInventory().get(key));
		}
	}
}
