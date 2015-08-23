package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.resource.DynamicCache;

public class PlayerManager {
	private List<FeudalPlayer> players;
	private DynamicCache<UUID> cache;
	
	public PlayerManager() {
		players = new ArrayList<FeudalPlayer>();
		
		cache = new DynamicCache<UUID>(Configuration.instance.playerCacheInterval) {
			public void decache(UUID playerUUID) {
				unregisterPlayer(playerUUID);
			}
		};
	}
	
	public List<FeudalPlayer> getPlayers() {
		return this.players;
	}
	
	public FeudalPlayer getPlayer(UUID player)  {
		if (player != null) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
			if (offlinePlayer == null)
				return null;
			cache.cached(player);
			
			for (FeudalPlayer feudalPlayer : this.players) {
				if (feudalPlayer.player.compareTo(player) == 0)
					return feudalPlayer;
			}
			
			FeudalPlayer newPlayer = null;
			newPlayer = createPlayer(player);
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
	
	public void unregisterPlayer(UUID player) {
		for (FeudalPlayer fPlayer : this.players) {
			if (fPlayer.player.compareTo(player) == 0) {
				this.players.remove(fPlayer);
			}
		}
	}
	
	public FeudalPlayer createPlayer(UUID uuid) {
		ConfigurationSection playerSection = Feudal.getInstance().getPlayerData().get().getConfigurationSection(uuid.toString());
		if (playerSection != null)
			return loadPlayer(uuid.toString());
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
		for (FeudalPlayer player : this.players)
			this.savePlayer(player);
	}
}
