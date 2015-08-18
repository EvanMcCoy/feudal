package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;

public class PlayerManager {
	List<FeudalPlayer> players;
	
	public PlayerManager() {
		players = new ArrayList<FeudalPlayer>();
	}
	
	public List<FeudalPlayer> getPlayers() {
		return this.players;
	}
	
	public FeudalPlayer getPlayer(UUID player) {
		if (player != null) {
			for (FeudalPlayer feudalPlayer : this.players) {
				if (feudalPlayer.player.toString().equals(player.toString())) {
					return feudalPlayer;
				}
			}
		
			FeudalPlayer newPlayer = null;
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
			if (offlinePlayer == null) {
				return null;
			}
			else {
				newPlayer = createPlayer(player);
			}
			return newPlayer;
		}
		return null;
	}
	
	public boolean isPlayer(UUID player) {
		return this.getPlayer(player) != null;
	}
	
	public void registerPlayer(FeudalPlayer player) {
		this.players.add(player);
	}
	
	public void unregisterPlayer(FeudalPlayer player) {
		this.players.remove(player);
	}
	
	public FeudalPlayer createPlayer(UUID uuid) {
		ConfigurationSection playerSection = Feudal.getInstance().getPlayerData().get().getConfigurationSection(uuid.toString());
		if (playerSection != null) {
			return loadPlayer(uuid.toString());
		}
		playerSection = Feudal.getInstance().getPlayerData().get().createSection(uuid.toString());
		FeudalPlayer player = new FeudalPlayer(uuid, "", "", playerSection);
		registerPlayer(player);
		return player;
	}
	
	public FeudalPlayer loadPlayer(String uuid) {
		ConfigurationSection playerSection = Feudal.getInstance().getPlayerData().get().getConfigurationSection(uuid);
		
		UUID playerUUID = UUID.fromString(uuid);
		String kingdom = playerSection.getString("kingdom");
		String fief = playerSection.getString("fief");
		FeudalPlayer player = new FeudalPlayer(playerUUID, kingdom, fief, playerSection);
		registerPlayer(player);
		return player;
	}
	
	public void savePlayer(FeudalPlayer player) {
		ConfigurationSection section = player.getDataPath();
		
		section.set("kingdom", player.kingdom);
		section.set("fief", player.fief);
	}
	
	public void saveAll() {
		for (FeudalPlayer player : this.players) {
			this.savePlayer(player);
		}
	}
}
