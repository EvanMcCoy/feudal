package com.qwertyness.feudal.government;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.qwertyness.feudal.Feudal;

import net.milkbowl.vault.economy.Economy;

public class BankManager {
	public Economy vaultEconomy;
	
	public BankManager() {
		RegisteredServiceProvider<Economy> economyProvider = Feudal.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            vaultEconomy = economyProvider.getProvider();
        }
        else {
        	Feudal.getInstance().getLogger().severe(ChatColor.RED + "Failed to load Vault economy! Install vault or disable banks in the configuration.");
        }
	}

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
