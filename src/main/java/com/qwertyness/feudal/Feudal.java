package com.qwertyness.feudal;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.feudal.command.AttackCommand;
import com.qwertyness.feudal.command.FutureCommand;
import com.qwertyness.feudal.command.KingdomCommand;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.data.MessageData;
import com.qwertyness.feudal.data.PlayerData;
import com.qwertyness.feudal.data.PlayerManager;
import com.qwertyness.feudal.data.government.ArmyManager;
import com.qwertyness.feudal.data.government.BankManager;
import com.qwertyness.feudal.data.government.ChurchManager;
import com.qwertyness.feudal.data.government.FiefData;
import com.qwertyness.feudal.data.government.FiefManager;
import com.qwertyness.feudal.data.government.KingdomData;
import com.qwertyness.feudal.data.government.KingdomManager;
import com.qwertyness.feudal.data.government.LandData;
import com.qwertyness.feudal.data.government.LandManager;
import com.qwertyness.feudal.listener.BuildListener;
import com.qwertyness.feudal.listener.ChunkListener;
import com.qwertyness.feudal.listener.TaxExecutor;
import com.qwertyness.feudal.util.LandUtil;

public class Feudal extends JavaPlugin implements Listener {
	private static Feudal instance;
	
	private KingdomData kingdomData;
	private FiefData fiefData;
	private LandData landData;
	private PlayerData playerData;
	private MessageData messageData;
	
	private KingdomManager kingdomManager;
	private FiefManager fiefManager;
	private LandManager landManager;
	private BankManager bankManager;
	private ArmyManager armyManager;
	private ChurchManager churchManager;
	private PlayerManager playerManager;
	
	private List<FutureCommand> futureCommands;
	
	public void onEnable() {
		instance = this;
		
		this.saveDefaultConfig();
		this.kingdomData = new KingdomData(this);
		this.fiefData = new FiefData(this);
		this.landData = new LandData(this);
		this.playerData = new PlayerData(this);
		this.saveResource("messages.yml", false);
		this.messageData = new MessageData(this);
		
		//Initialize static configuration fields.
		new Configuration(this);
		
		//Initialize data managers and utilities.
		this.kingdomManager = new KingdomManager();
		this.fiefManager = new FiefManager();
		this.landManager = new LandManager();
		this.bankManager = new BankManager();
		this.armyManager = new ArmyManager();
		this.churchManager = new ChurchManager();
		this.playerManager = new PlayerManager();
		new LandUtil(this.landManager);
		this.futureCommands = new ArrayList<FutureCommand>();
		
		//Register listeners and runnables
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BuildListener(this), this);
		pm.registerEvents(new ChunkListener(this), this);
		pm.registerEvents(this, this);
		new TaxExecutor(this).runTaskTimerAsynchronously(this, 100, 1200);
		
		//Register command handlers
		this.getCommand("kingdom").setExecutor(new KingdomCommand(this));
		this.getCommand("attack").setExecutor(new AttackCommand(this));
		
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
		this.kingdomData.save();
		this.fiefData.save();
		this.playerData.save();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		for (int i = 0;i < this.futureCommands.size();i++) {
			FutureCommand futureCommand = this.futureCommands.get(i);
			if (futureCommand.getCommand().equalsIgnoreCase(command.getName()) && futureCommand.compareSubcommands(args) && futureCommand.compareRecipiant(sender)) {
				futureCommand.run(args);
			}
		}
		return false;
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
			player = new FeudalPlayer(event.getPlayer().getUniqueId(), playerSection);
		}
		this.playerManager.registerPlayer(player);
	}
	
	public KingdomData getKingdomData() {return this.kingdomData;}
	public KingdomManager getKingdomManager() {return this.kingdomManager;}
	
	public FiefData getFiefData() {return this.fiefData;}
	public FiefManager getFiefManager() {return this.fiefManager;}
	
	public LandData getLandData() {return this.landData;}
	public LandManager getLandManager() {return this.landManager;}
	
	public PlayerData getPlayerData() {return this.playerData;}
	public PlayerManager getPlayerManager() {return this.playerManager;}
	
	public MessageData getMessageData() {return this.messageData;}
	
	public BankManager getBankManager() {return this.bankManager;}
	
	public ChurchManager getChurchManager() {return this.churchManager;}
	
	public ArmyManager getArmyManager() {return this.armyManager;}
	
	public void registerFutureCommand(FutureCommand futureCommand) {this.futureCommands.add(futureCommand);}
}
