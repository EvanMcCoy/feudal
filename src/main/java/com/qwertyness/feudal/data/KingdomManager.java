package com.qwertyness.feudal.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.Util;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Church;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;

public class KingdomManager {

	public List<Kingdom> kingdoms;
	
	public KingdomManager() {
		this.kingdoms = new ArrayList<Kingdom>();
		for (String string : Feudal.getInstance().kingdomData.get().getKeys(false)) {
			this.registerKingdom(this.loadKingdom(string));
		}
	}
	
	public Kingdom getKingdom(String kingdomName) {
		for (Kingdom kingdom : this.kingdoms) {
			if (kingdom.name.equals(kingdomName)) {
				return kingdom;
			}
		}
		return null;
	}
	
	public boolean isKingdom(String kingdomName) {
		if (getKingdom(kingdomName) != null) {
			return true;
		}
		return false;
	}
	
	public void registerKingdom(Kingdom kingdom) {
		if (!isKingdom(kingdom.name)) {
			this.kingdoms.add(kingdom);
		}
	}
	
	public void unregisterKingdom(String kingdomName) {
		if (!isKingdom(kingdomName)) {
			return;
		}
		this.kingdoms.remove(getKingdom(kingdomName));
	}
	
	public Kingdom loadKingdom(String kingdomName) {
		ConfigurationSection kingdomSection = Feudal.getInstance().kingdomData.get().getConfigurationSection(kingdomName);
		
		String name = kingdomSection.getName();
		UUID king = (kingdomSection.getString("king") != null) ? UUID.fromString(kingdomSection.getString("king")) : null;
		UUID queen = (kingdomSection.getString("queen") != null) ? UUID.fromString(kingdomSection.getString("queen")) : null;
		UUID prince = (kingdomSection.getString("prince") != null) ? UUID.fromString(kingdomSection.getString("prince")) : null;
		UUID princess = (kingdomSection.getString("princess") != null) ? UUID.fromString(kingdomSection.getString("princess")) : null;
		UUID duke = (kingdomSection.getString("duke") != null) ? UUID.fromString(kingdomSection.getString("duke")) : null;
		UUID duchess = (kingdomSection.getString("duchess") != null) ? UUID.fromString(kingdomSection.getString("duchess")) : null;
		List<UUID> earls = Util.toUUIDList(kingdomSection.getStringList("earls"));
		Army army = Feudal.getInstance().armyManager.loadArmy(kingdomSection);
		Church church = Feudal.getInstance().churchManager.loadChurch(kingdomSection);
		List<Fief> fiefs = new ArrayList<Fief>();
		for (String string : kingdomSection.getStringList("fiefs")) {
			Feudal.getInstance().fiefManager.loadFief(kingdomSection.getName(), string);
		}
		
		Chunk capital = Util.toChunk(kingdomSection.getString("captial"));
		List<Chunk> fortresses = new ArrayList<Chunk>();
		for (String string : kingdomSection.getStringList("fortresses")) {
			fortresses.add(Util.toChunk(string));
		}
		List<Chunk> land = new ArrayList<Chunk>();
		for (String string : kingdomSection.getStringList("land")) {
			land.add(Util.toChunk(string));
		}
		
		return new Kingdom(name, king, queen, prince, princess, duke, duchess, earls, army, church, fiefs, capital, fortresses, land, kingdomSection);
	}
	
	public void saveKingdom(Kingdom kingdom) {
		//TODO: Add in name change system
		ConfigurationSection kingdomSection = kingdom.getDataPath();
		
		kingdomSection.set("king", kingdom.king.toString());
		kingdomSection.set("queen", kingdom.queen.toString());
		kingdomSection.set("prince", kingdom.prince.toString());
		kingdomSection.set("princess", kingdom.princess.toString());
		kingdomSection.set("duke", kingdom.duke.toString());
		kingdomSection.set("dutchess", kingdom.duchess.toString());
		kingdomSection.set("earls", Util.toStringList(kingdom.earls));
		Feudal.getInstance().armyManager.saveArmy(kingdom.royalArmy);
		Feudal.getInstance().churchManager.saveChurch(kingdom.highChurch);
		List<String> fiefs = new ArrayList<String>();
		for (Fief fief : kingdom.fiefs) {
			Feudal.getInstance().fiefManager.saveFief(fief);
			fiefs.add(fief.name);
		}
		kingdomSection.set("fiefs", fiefs);
		kingdomSection.set("capital", Util.toString(kingdom.capital));
		List<String> fortresses = new ArrayList<String>();
		for (Chunk chunk : kingdom.fortresses) {
			fortresses.add(Util.toString(chunk));
		}
		kingdomSection.set("fortresses", fortresses);
		List<String> land = new ArrayList<String>();
		for (Chunk chunk : kingdom.land) {
			land.add(Util.toString(chunk));
		}
		kingdomSection.set("land", land);
	}
	
	public void deleteKingdom(Kingdom kingdom) {
		Feudal.getInstance().kingdomData.get().set(kingdom.getDataPath().getCurrentPath(), null);
		this.unregisterKingdom(kingdom.getName());
	}
	
	public void saveAll() {
		for (Kingdom kingdom : this.kingdoms) {
			this.saveKingdom(kingdom);
		}
	}
	
	public Kingdom getLandOwner(Chunk chunk) {
		for (Kingdom kingdom : this.kingdoms) {
			for (Chunk testChunk : kingdom.land) {
				if (chunk.getWorld().getName().equals(testChunk.getWorld().getName()) &&
						chunk.getX() == testChunk.getX() &&
						chunk.getZ() == testChunk.getZ()) {
					return kingdom;
				}
			}	
		}
		return null;
	}
}
