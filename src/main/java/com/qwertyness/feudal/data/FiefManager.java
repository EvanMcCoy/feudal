package com.qwertyness.feudal.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.Util;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.government.Church;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;

public class FiefManager {
	
	public boolean isFief(String kingdomName, String fiefName) {
		if (getFief(kingdomName, fiefName) != null) {
			return true;
		}
		return false;
	}
	
	public Fief getFief(String kingdomName, String fiefName) {
		Kingdom kingdom = Feudal.getInstance().kingdomManager.getKingdom(kingdomName);
		
		for (Fief fief : kingdom.fiefs) {
			if (fief.getName().equals(fiefName)) {
				return fief;
			}
		}
		return null;
	}
	
	public Fief loadFief(String kingdomName, String fiefName) {
		ConfigurationSection fiefSection = Feudal.getInstance().fiefData.get().getConfigurationSection(kingdomName + "." + fiefName);
		
		String name = fiefSection.getName();
		UUID baron = (fiefSection.getString("baron") != null) ? UUID.fromString(fiefSection.getString("baron")) : null;
		UUID baroness = (fiefSection.getString("baroness") != null) ? UUID.fromString(fiefSection.getString("baroness")) : null;
		List<UUID> peasents = Util.toUUIDList(fiefSection.getStringList("peasents"));
		List<UUID> serfs = Util.toUUIDList(fiefSection.getStringList("serfs"));
		Bank bank = Feudal.getInstance().bankManager.loadBank(fiefSection);
		Army army = Feudal.getInstance().armyManager.loadArmy(fiefSection);
		Church church = Feudal.getInstance().churchManager.loadChurch(fiefSection);
		Chunk capital = Util.toChunk(fiefSection.getString("capital"));
		List<Chunk> land = new ArrayList<Chunk>();
		for (String chunkString : fiefSection.getStringList("land")) {
			land.add(Util.toChunk(chunkString));
		}
		
		return new Fief(name, baron, baroness, peasents, serfs, bank, army, church, capital, land, fiefSection);
	}
	
	public void saveFief(Fief fief) {
		ConfigurationSection fiefSection = fief.getDataPath();
		
		fiefSection.set("baron", fief.baron.toString());
		fiefSection.set("baroness", fief.baroness.toString());
		fiefSection.set("peasents", Util.toStringList(fief.peasents));
		fiefSection.set("serfs", Util.toStringList(fief.serfs));
		fiefSection.set("capital", Util.toString(fief.capital));
		List<String> land = new ArrayList<String>();
		for (Chunk chunk : fief.land) {
			land.add(Util.toString(chunk));
		}
		fiefSection.set("land", land);
		Feudal.getInstance().bankManager.saveBank(fief);
		Feudal.getInstance().armyManager.saveArmy(fief.army);
		Feudal.getInstance().churchManager.saveChurch(fief.church);
	}
	
	public void deleteFief(Kingdom kingdom, Fief fief) {
		Feudal.getInstance().fiefData.get().set(fief.getDataPath().getCurrentPath(), null);
		kingdom.fiefs.remove(fief);
	}
	
	public Fief getLandOwner(Chunk chunk) {
		for (Kingdom kingdom : Feudal.getInstance().kingdomManager.kingdoms) {
			Fief fief = getLandOwner(kingdom, chunk);
			if (fief != null) {
				return fief;
			}
		}
		return null;
	}
	
	public Fief getLandOwner(Kingdom kingdom, Chunk chunk) {
		for (Fief fief : kingdom.fiefs) {
			for (Chunk testChunk : fief.land) {
				if (chunk.getWorld().getName().equals(testChunk.getWorld().getName()) &&
						chunk.getX() == testChunk.getX() &&
						chunk.getZ() == testChunk.getZ()) {
					return fief;
				}
			}	
		}
		return null;
	}
}
