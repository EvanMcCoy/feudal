package com.qwertyness.feudal;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.feudal.command.KingdomCommand;
import com.qwertyness.feudal.data.ArmyManager;
import com.qwertyness.feudal.data.BankManager;
import com.qwertyness.feudal.data.ChurchManager;
import com.qwertyness.feudal.data.FeudalPlayer;
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

public class Feudal extends JavaPlugin implements Listener {
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
		this.saveResource("messages.yml", false);
		this.messageData = new MessageData(this);
		
		//Initialize static configuration fields.
		new Configuration(this);
		
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
		pm.registerEvents(this, this);
		new TaxExecutor(this).runTaskTimerAsynchronously(this, 100, 1200);
		
		//Register command handlers
		this.getCommand("kingdom").setExecutor(new KingdomCommand(this));
		
		//Set up vault ecnomony
		if (Configuration.useEconomy) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            bankManager.vaultEconomy = economyProvider.getProvider();
	        }
		}
		
	}
	
	public void onDisable() {
		this.saveConfig();
		this.kingdomManager.saveAll();
		this.playerManager.saveAll();
		this.playerManager.saveAll();
		this.kingdomData.save();
		this.fiefData.save();
		this.playerData.save();
	}
	
	public static Feudal getInstance() {
		return instance;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Bukkit.broadcastMessage("RunEvent");
		if (this.playerManager.isPlayer(event.getPlayer().getUniqueId())) {
			return;
		}
		Bukkit.broadcastMessage("IsNotPlayer");
		FeudalPlayer player;
		if (this.playerData.get().contains(event.getPlayer().getUniqueId().toString())) {
			Bukkit.broadcastMessage("PlayerDataContains");
			player = this.playerManager.loadPlayer(event.getPlayer().getUniqueId().toString());
		}
		else {
			ConfigurationSection playerSection = this.playerData.get().createSection(event.getPlayer().getUniqueId().toString());
			playerSection.set(event.getPlayer().getUniqueId().toString() + ".gender", true);
			player = new FeudalPlayer(event.getPlayer().getUniqueId(), true, playerSection);
		}
		this.playerManager.regiseterPlayer(player);
	}
}
