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
	private static Feudal plugin;
	
	public TaxExecutor(Feudal fPlugin) {
		plugin = fPlugin;
	}
	
	public void run() {
		if (Configuration.instance.taxTimes.contains(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
			runTaxes();
		}
	}

	public static void runTaxes() {
		if (!Configuration.useEconomy) {
			return;
		}
		for(Kingdom kingdom : plugin.getKingdomManager().kingdoms) {
			for (Fief fief : kingdom.getFiefs()) {
				for (UUID peasent : fief.getPeasents()) {
					if (!plugin.getBankManager().vaultEconomy.hasAccount(Bukkit.getOfflinePlayer(peasent))) {
						continue;
					}
					if (!(plugin.getBankManager().vaultEconomy.getBalance(Bukkit.getOfflinePlayer(peasent)) >= fief.settings.getTax())) {
						continue;
					}
					plugin.getBankManager().vaultEconomy.withdrawPlayer(Bukkit.getOfflinePlayer(peasent), fief.settings.getTax());
					fief.getBank().depositMoney(fief.settings.getTax());
				}
				if (kingdom.getSettings().doTaxPerPlot()) {
					double tax = kingdom.getSettings().getTax() * fief.getLand().size();
					if (fief.getBank().getBalance() >= tax) {
						fief.getBank().withdrawMoney(tax);
						kingdom.getBank().depositMoney(tax);
					}
				}
				else {
					if (fief.getBank().getBalance() >= kingdom.getSettings().getTax()) {
						fief.getBank().withdrawMoney(kingdom.getSettings().getTax());
						kingdom.getBank().depositMoney(kingdom.getSettings().getTax());
					}
				}
			}
			int tax = 0;
			tax += Configuration.instance.landTax * kingdom.getLand().size();
			tax += Configuration.instance.fortressTax * kingdom.getFortresses().size();
			if (kingdom.getBank().getBalance() >= tax) {
				kingdom.getBank().withdrawMoney(tax);
			}
		}
	}

}
