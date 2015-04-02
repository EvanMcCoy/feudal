package com.qwertyness.feudal;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.feudal.command.KingdomCommand;
import com.qwertyness.feudal.data.ArmyManager;
import com.qwertyness.feudal.data.BankManager;
import com.qwertyness.feudal.data.ChurchManager;
import com.qwertyness.feudal.data.FiefData;
import com.qwertyness.feudal.data.FiefManager;
import com.qwertyness.feudal.data.KingdomData;
import com.qwertyness.feudal.data.KingdomManager;
import com.qwertyness.feudal.data.MessageData;
import com.qwertyness.feudal.data.PlayerData;
import com.qwertyness.feudal.data.PlayerManager;
import com.qwertyness.feudal.listener.BuildListener;
import com.qwertyness.feudal.listener.ChunkListener;
import com.qwertyness.feudal.listener.TaxExecutor;

public class Feudal extends JavaPlugin {
	private static Feudal instance;
	
	public KingdomData kingdomData;
	public FiefData fiefData;
	public PlayerData playerData;
	public MessageData messageData;
	
	public KingdomManager kingdomManager;
	public FiefManager fiefManager;
	public BankManager bankManager;
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
		this.bankManager = new BankManager();
		this.armyManager = new ArmyManager();
		this.churchManager = new ChurchManager();
		this.playerManager = new PlayerManager();
		
		//Register listeners and runnables
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BuildListener(this), this);
		pm.registerEvents(new ChunkListener(this), this);
		new TaxExecutor(this).runTaskTimerAsynchronously(this, 100, 1200);
		
		//Register command handlers
		this.getCommand("kingdom").setExecutor(new KingdomCommand(this));
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
