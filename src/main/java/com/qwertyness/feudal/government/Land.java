package com.qwertyness.feudal.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.util.Util;

public class Land implements CivilOrganizer {
	private Chunk land;
	public Kingdom kingdom;
	public Fief fief;
	public List<UUID> owners;
	
	private boolean fortress = false;
	private boolean church = false;
	
	private ConfigurationSection dataPath;
	
	public Land(Chunk land, ConfigurationSection dataPath) {
		this.land = land;
		
		this.dataPath = dataPath;
	}
	
	public Land (Chunk land, Kingdom kingdom, Fief fief, List<UUID> owners, ConfigurationSection dataPath) {
		this.land = land;
		this.kingdom = kingdom;
		this.fief = fief;
		this.owners = owners;
		
		this.dataPath = dataPath;
	}
	
	public String getCoordinates() {
		return Util.toString(this.land);
	}
	
	public Chunk getChunk() {
		return this.land;
	}
	
	public boolean isFortress() {
		return this.fortress;
	}
	
	public void setFortress(boolean isFortress) {
		this.fortress = isFortress;
	}
	
	public boolean isChurch() {
		return this.church;
	}
	
	public void setChurch(boolean isChurch) {
		this.church = isChurch;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}

	// Land cannot have a bank.s
	public Bank getBank() {return null;}
	
}
