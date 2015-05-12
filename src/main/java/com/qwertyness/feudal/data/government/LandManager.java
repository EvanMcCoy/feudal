package com.qwertyness.feudal.data.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.util.Util;

public class LandManager {
	private Feudal plugin;

	public List<Land> land = new ArrayList<Land>();
	
	public LandManager() {
		this.plugin = Feudal.getInstance();
		for (String key : this.plugin.landData.get().getKeys(false)) {
			if (Util.toChunk(key) != null) {
				this.registerLand(loadLand(key));
			}
		}
	}
	
	public void registerLand(Land land) {
		if (!this.land.contains(land)) {
			this.land.add(land);
		}
	}
	
	public void unregisterLand(String coordinates) {
		for (Land land : this.land) {
			if (land.getCoordinates().equals(coordinates)) {
				this.land.remove(land);
			}
		}
	}
	
	public Land getLand(String coordinates) {
		for (Land land : this.land) {
			if (land.getCoordinates().equals(coordinates)) {
				return land;
			}
		}
		return null;
	}
	
	public Land loadLand(String coordinates) {
		ConfigurationSection section = this.plugin.landData.get().getConfigurationSection(coordinates);
		
		Chunk chunk = Util.toChunk(coordinates);
		Kingdom kingdom = this.plugin.kingdomManager.getKingdom(section.getString("kingdom"));
		Fief fief = this.plugin.fiefManager.getFief((kingdom == null) ? "" : kingdom.name, section.getString("fief"));
		List<UUID> owners = Util.toUUIDList(section.getStringList("owners"));
		
		Land land = new Land(chunk, kingdom, fief, owners, section);
		
		if (kingdom != null) {
			kingdom.land.add(land);
		}
		if (fief != null) {
			fief.land.add(land);
		}
		return land;
	}
	
	public void saveLand(Land land) {
		ConfigurationSection section = land.getDataPath();
		
		section.set("kingdom", land.kingdom);
		section.set("fief", land.fief);
		section.set("owners", Util.toStringList(land.owners));
	}
	
	public void saveAll() {
		for (Land land : this.land) {
			this.saveLand(land);
		}
	}
}
