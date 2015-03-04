package com.qwertyness.feudal;

import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.feudal.data.ArmyManager;
import com.qwertyness.feudal.data.ChurchManager;
import com.qwertyness.feudal.data.FiefData;
import com.qwertyness.feudal.data.FiefManager;
import com.qwertyness.feudal.data.KingdomData;
import com.qwertyness.feudal.data.KingdomManager;
import com.qwertyness.feudal.data.MessageData;
import com.qwertyness.feudal.data.PlayerData;
import com.qwertyness.feudal.data.PlayerManager;

public class Feudal extends JavaPlugin {
	private static Feudal instance;
	
	public KingdomData kingdomData;
	public FiefData fiefData;
	public PlayerData playerData;
	public MessageData messageData;
	
	public KingdomManager kingdomManager;
	public FiefManager fiefManager;
	public ArmyManager armyManager;
	public ChurchManager churchManager;
	public PlayerManager playerManager;
	
	public void onEnable() {
		instance = this;
		
		this.saveDefaultConfig();
		this.kingdomData = new KingdomData(this);
		this.fiefData = new FiefData(this);
		this.playerData = new PlayerData(this);
		this.saveResource("messsages.yml", false);
		this.messageData = new MessageData(this);
		
		this.kingdomManager = new KingdomManager();
		this.fiefManager = new FiefManager();
		this.armyManager = new ArmyManager();
		this.churchManager = new ChurchManager();
		this.playerManager = new PlayerManager();
	}
	
	public void onDisable() {
		this.kingdomManager.saveAll();
		this.playerManager.saveAll();
		this.saveConfig();
		this.kingdomData.save();
		this.fiefData.save();
		this.playerData.save();
	}
	
	public static Feudal getInstance() {
		return instance;
	}
}
