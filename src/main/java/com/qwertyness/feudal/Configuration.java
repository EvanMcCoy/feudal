package com.qwertyness.feudal;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	private Feudal plugin;
	public static Configuration instance;
	
	public boolean allowMultipleTitles;
	public int landTax;
	public int fortressTax;
	public static boolean useEconomy;
	//In hours
	public List<Integer> taxTimes;
	public int maxLand;
	public int fortressRadius;
	
	public Messages messages;
	
	public Configuration(Feudal plugin) {
		this.plugin = plugin;
		instance = this;
		this.loadConfiguration();
	}
	
	public void loadConfiguration() {
		allowMultipleTitles = this.plugin.getConfig().getBoolean("allowMultipleTitles");
		this.landTax = this.plugin.getConfig().getInt("landTax");
		this.fortressTax = this.plugin.getConfig().getInt("fortressTax");
		this.taxTimes = this.plugin.getConfig().getIntegerList("taxTimes");
		
		this.messages = new Messages(this.plugin);
	}
	
	public class Messages {
		private Feudal plugin;
		
		public String prefix;
		public String insufficientPermission;
		public String notInAKingdom;
		public String notAPlayer;
		public String invalidIndex;
		public String listTopStarter;
		public String listTopEnder;
		public String listIndexColor;
		public String listItemColor;
		public String listBottom;
		
		public String alreadyInAKingdom;
		public String kingdomCreate;
		public String alreadyAKingdom;
		public String kingdomDisunite;
		public String kingdomDisuniteConfirm;
		public String setCounterpart;
		public String setDuke;
		public String setDuchess;
		public String setPrince;
		public String setPrincess;
		public String addEarl;
		public String alreadyAnEarl;
		public String removeEarl;
		public String fiefCreate;
		public String alreadyAFief;
		public String fiefDisband;
		public String notAFief;
		public String setFiefBaron;
		public String setFiefBaroness;
		
		
		
		public String notKingdomLand;
		public String landAlreadyAllocated;
		public String landNotAllocated;
		public String allocateLand;
		public String deallocateLand;
		public String deallocateAll;
		public String alreadyCapital;
		public String setCapital;
		public String alreadyAFortress;
		public String notAFortress;
		public String addFortress;
		public String removeFortress;
		public String landAlreadyClaimed;
		public String noFortressInRange;
		public String claimLand;
		public String cannotUnclaimCapital;
		public String cannotUnclaimFortress;
		public String unclaimLand;
		public String autoClaimOn;
		public String autoClaimOff;
		
		public Messages(Feudal plugin) {
			this.plugin = plugin;
			this.loadMessages();
		}
		
		public void loadMessages() {
			FileConfiguration messages = this.plugin.messageData.get();
			this.prefix = color(messages.getString("prefix"));
			this.insufficientPermission = color(messages.getString("insufficientPermission"));
			this.notInAKingdom = color(messages.getString("notInAKingdom"));
			this.notAPlayer = color(messages.getString("notAPlayer"));
			this.invalidIndex = color(messages.getString("invalidIndex"));
			this.listTopStarter = color(messages.getString("listTopStarter"));
			this.listTopEnder = color(messages.getString("listTopEnder"));
			this.listIndexColor = color(messages.getString("listIndexColor"));
			this.listItemColor = color(messages.getString("listItemColor"));
			this.listBottom = color(messages.getString("listBottom"));
			
			this.alreadyInAKingdom = color(messages.getString("alreadyInAKingdom"));
			this.kingdomCreate = color(messages.getString("kingdomCreate"));
			this.alreadyAKingdom = color(messages.getString("alreadyAKingdom"));
			this.kingdomDisunite = color(messages.getString("kingdomDisunite"));
			this.kingdomDisuniteConfirm = color(messages.getString("kingdomDisuniteConfirm"));
			
			this.setCounterpart = color(messages.getString("setCounterpart"));
			this.setDuke = color(messages.getString("setDuke"));
			this.setDuchess = color(messages.getString("setDuchess"));
			this.setPrince = color(messages.getString("setPrince"));
			this.setPrincess = color(messages.getString("setPrincess"));
			this.addEarl = color(messages.getString("addEarl"));
			this.alreadyAnEarl = color(messages.getString("alreadyAnEarl"));
			this.removeEarl = color(messages.getString("removeEarl"));
			
			this.fiefCreate = color(messages.getString("fiefCreate"));
			this.alreadyAFief = color(messages.getString("alreadyAFief"));
			this.fiefDisband = color(messages.getString("fiefDisband"));
			this.notAFief = color(messages.getString("notAFief"));
			this.setFiefBaron = color(messages.getString("setFiefBaron"));
			this.setFiefBaroness = color(messages.getString("setFiefBaroness"));
			
			
			
			this.notKingdomLand = color(messages.getString("notKingdomLand"));
			this.landAlreadyAllocated = color(messages.getString("landAlreadyAllocated"));
			this.allocateLand = color(messages.getString("allocateLand"));
			this.deallocateLand = color(messages.getString("deallocateLand"));
			this.deallocateAll = color(messages.getString("deallocateAll"));
			this.alreadyCapital = color(messages.getString("alreadyCapital"));
			this.setCapital = color(messages.getString("setCapital"));
			this.alreadyAFortress = color(messages.getString("alreadyAFortress"));
			this.notAFortress = color(messages.getString("notAFortress"));
			this.addFortress = color(messages.getString("addFortress"));
			this.removeFortress = color(messages.getString("removeFortress"));
			this.landAlreadyClaimed = color(messages.getString("landAlreadyClaimed"));
			this.noFortressInRange = color(messages.getString("noFortressInRange"));
			this.claimLand = color(messages.getString("claimLand"));
			this.cannotUnclaimCapital = color(messages.getString("cannotUnclaimCapital"));
			this.cannotUnclaimFortress = color(messages.getString("cannotUnclaimFortress"));
			this.unclaimLand = color(messages.getString("unclaimLand"));
			this.autoClaimOn = color(messages.getString("autoClaimOn"));
			this.autoClaimOff = color(messages.getString("autoClaimOff"));
		}
		
		public String color(String input) {
			return ChatColor.translateAlternateColorCodes('&', input);
		}
	}
}
