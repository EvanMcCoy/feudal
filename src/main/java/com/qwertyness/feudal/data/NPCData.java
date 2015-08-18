package com.qwertyness.feudal.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.qwertyness.feudal.Feudal;

public class NPCData {
	File file = null;
	FileConfiguration config = null;
	Feudal plugin;
	
	public NPCData(Feudal plugin) {
		this.plugin = plugin;
	}
	
	public NPCData(Feudal plugin, File file) {
		this.plugin = plugin;
		this.file = file;
	}
	
	public void reload() {
	    if (file == null) {
	    	file = new File(plugin.getDataFolder(), "npcProfiles.yml");
	    }
	    config = YamlConfiguration.loadConfiguration(file);
	 
	    InputStream defConfigStream = plugin.getResource("npcProfiles.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	    }
	}
	public FileConfiguration get() {
	    if (config == null) {
	        this.reload();
	    }
	    return config;
	}
	public void save() {
	    if (config == null || file == null) {
	    return;
	    }
	    try {
	        get().save(file);
	    } catch (IOException ex) {
	        plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
	    }
	}
}
