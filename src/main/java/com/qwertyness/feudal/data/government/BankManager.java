package com.qwertyness.feudal.data.government;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.Bank;

public class BankManager {
	public Economy vaultEconomy;

	public Bank loadBank(ConfigurationSection government) {
		ConfigurationSection bankSection = government.getConfigurationSection("bank");
		Bank bank = new Bank(bankSection);
		bank.depositMoney(bankSection.getDouble("balance"));
		
		ConfigurationSection inventorySection = bankSection.getConfigurationSection("inventory");
		if (inventorySection != null) {
			for (String materialString : bankSection.getConfigurationSection("inventory").getKeys(false)) {
				Material material = Material.valueOf(materialString);
				int amount = bankSection.getInt("inventory." + materialString);
				bank.depositItem(material, amount);
			}
		}
		
		return bank;
	}
	
	public void saveBank(Bank bank) {
		ConfigurationSection bankSection = bank.getDataPath();
		bankSection.set("balance", bank.getBalance());
		for (Material key : bank.getInventory().keySet()) {
			bankSection.set("inventory." + key.toString(), bank.getInventory().get(key));
		}
	}
}
