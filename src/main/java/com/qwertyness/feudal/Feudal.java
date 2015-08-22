package com.qwertyness.feudal;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.feudal.chat.ChatManager;
import com.qwertyness.feudal.command.ArmyCommand;
import com.qwertyness.feudal.command.BankCommand;
import com.qwertyness.feudal.command.FiefCommand;
import com.qwertyness.feudal.command.KingdomCommand;
import com.qwertyness.feudal.data.MessageData;
import com.qwertyness.feudal.data.NPCData;
import com.qwertyness.feudal.data.PlayerData;
import com.qwertyness.feudal.data.government.FiefData;
import com.qwertyness.feudal.data.government.KingdomData;
import com.qwertyness.feudal.data.government.LandData;
import com.qwertyness.feudal.government.ArmyManager;
import com.qwertyness.feudal.government.BankManager;
import com.qwertyness.feudal.government.ChurchManager;
import com.qwertyness.feudal.government.FiefManager;
import com.qwertyness.feudal.government.KingdomManager;
import com.qwertyness.feudal.government.LandManager;
import com.qwertyness.feudal.government.PlayerManager;
import com.qwertyness.feudal.government.settings.Settings;
import com.qwertyness.feudal.gui.GUIManager;
import com.qwertyness.feudal.listener.BuildListener;
import com.qwertyness.feudal.listener.ChunkListener;
import com.qwertyness.feudal.listener.TaxExecutor;
import com.qwertyness.feudal.mail.Mail;
import com.qwertyness.feudal.mail.MailManager;
import com.qwertyness.feudal.mail.MailManager.MailDeliverer;
import com.qwertyness.feudal.npc.NPCProfile;
import com.qwertyness.feudal.util.LandUtil;

import net.milkbowl.vault.economy.Economy;

public class Feudal extends JavaPlugin implements Listener {
	private static Feudal instance;
	
	private KingdomData kingdomData;
	private FiefData fiefData;
	private LandData landData;
	private PlayerData playerData;
	private MessageData messageData;
	private NPCData npcData;
	
	private KingdomManager kingdomManager;
	private FiefManager fiefManager;
	private LandManager landManager;
	private BankManager bankManager;
	private ArmyManager armyManager;
	private ChurchManager churchManager;
	private PlayerManager playerManager;
	private MailManager mailManager;
	
	public void onEnable() {
		instance = this;
		
		this.saveDefaultConfig();
		this.kingdomData = new KingdomData(this);
		this.fiefData = new FiefData(this);
		this.landData = new LandData(this);
		this.playerData = new PlayerData(this);
		this.saveResource("messages.yml", false);
		this.messageData = new MessageData(this);
		this.saveResource("npcProfiles.yml", false);
		this.npcData = new NPCData(this);
		
		//Initialize static configuration fields.
		new Configuration(this);
		
		//Initialize data managers and utilities.
		this.bankManager = new BankManager();
		this.armyManager = new ArmyManager();
		this.churchManager = new ChurchManager();
		this.fiefManager = new FiefManager();
		this.kingdomManager = new KingdomManager();
		this.landManager = new LandManager();
		this.playerManager = new PlayerManager();
		this.mailManager = new MailManager();
		new LandUtil(this.landManager);
		Settings.inizializeDefaultPositions();
		new BankCommand(this);
		for (String key : this.npcData.get().getKeys(false)) {
			NPCProfile.profiles.add(NPCProfile.fromConfigurationSection(this.npcData.get().getConfigurationSection(key)));
		}
		
		//Register listeners and runnables
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BuildListener(this), this);
		pm.registerEvents(new ChunkListener(this), this);
		pm.registerEvents(this.mailManager, this);
		pm.registerEvents(new ChatManager(), this);
		pm.registerEvents(new GUIManager(), this);
		pm.registerEvents(this, this);
		new TaxExecutor(this).runTaskTimerAsynchronously(this, 100, 1200);
		
		//Register command handlers
		this.getCommand("kingdom").setExecutor(new KingdomCommand(this));
		this.getCommand("army").setExecutor(new ArmyCommand(this));
		this.getCommand("fief").setExecutor(new FiefCommand(this));
		
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
		this.landManager.saveAll();
		this.kingdomData.save();
		this.fiefData.save();
		this.playerData.save();
		this.landData.save();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("mail")) {
			for (Mail mail : new ArrayList<Mail>(this.mailManager.getMail())) {
				if (mail.compareRecipiant(sender) && mail instanceof MailDeliverer) {
					((MailDeliverer)mail).run(args);
				}
			}
		}
		if (command.getName().equalsIgnoreCase("runtaxes")) {
			TaxExecutor.runTaxes();
		}
		return false;
	}
	
	public static Feudal getInstance() {
		return instance;
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
	
	public MailManager getMailManager() {return this.mailManager;}
}
