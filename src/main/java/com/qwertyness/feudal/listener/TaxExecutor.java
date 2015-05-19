package com.qwertyness.feudal.listener;

import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;

public class TaxExecutor extends BukkitRunnable implements Listener  {
	private Feudal plugin;
	
	public TaxExecutor(Feudal plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		if (Configuration.instance.taxTimes.contains(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
			runTaxes();
		}
	}

	public void runTaxes() {
		for(Kingdom kingdom : this.plugin.kingdomManager.kingdoms) {
			for (Fief fief : kingdom.fiefs) {
				for (UUID peasent : fief.peasents) {
					if (!this.plugin.bankManager.vaultEconomy.hasAccount(Bukkit.getOfflinePlayer(peasent))) {
						continue;
					}
					if (!(this.plugin.bankManager.vaultEconomy.getBalance(Bukkit.getOfflinePlayer(peasent)) >= fief.settings.tax)) {
						continue;
					}
					this.plugin.bankManager.vaultEconomy.withdrawPlayer(Bukkit.getOfflinePlayer(peasent), fief.settings.tax);
					fief.bank.depositMoney(fief.settings.tax);
				}
				if (kingdom.settings.taxPerPlot) {
					double tax = kingdom.settings.tax * fief.land.size();
					if (fief.bank.getBalance() >= tax) {
						fief.bank.withdrawMoney(tax);
						kingdom.imperialVault.depositMoney(tax);
					}
				}
				else {
					if (fief.bank.getBalance() >= kingdom.settings.tax) {
						fief.bank.withdrawMoney(kingdom.settings.tax);
						kingdom.imperialVault.depositMoney(kingdom.settings.tax);
					}
				}
			}
			int tax = 0;
			tax += Configuration.instance.landTax * kingdom.land.size();
			tax += Configuration.instance.fortressTax * kingdom.getFortresses().size();
			if (kingdom.imperialVault.getBalance() >= tax) {
				kingdom.imperialVault.withdrawMoney(tax);
			}
		}
	}

}
