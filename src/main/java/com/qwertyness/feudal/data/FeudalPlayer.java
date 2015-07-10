package com.qwertyness.feudal.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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
	
	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
}
