package com.qwertyness.feudal.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.BankManager;

public class FeudalPlayer {
	public UUID player;
	public String kingdom;
	public String fief;
	
	private ConfigurationSection dataPath;
	
	public FeudalPlayer(UUID player, String kingdom, String fief, ConfigurationSection section) {
		this.player = player;
		this.kingdom = kingdom;
		this.fief = fief;
		this.dataPath = section;
	}
	
	public Player toPlayer() {
		return Bukkit.getPlayer(this.player);
	}
	
	public double getBalance() {
		BankManager bm = Feudal.getInstance().getBankManager();
		if (bm.vaultEconomy.hasAccount(Bukkit.getOfflinePlayer(player))) {
			return bm.vaultEconomy.getBalance(Bukkit.getOfflinePlayer(player)); 
		}
		return 0;
	}
	
	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
}
