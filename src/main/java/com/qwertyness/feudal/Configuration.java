package com.qwertyness.feudal;

import java.util.List;

import org.bukkit.ChatColor;

public class Configuration {
	private Feudal plugin;
	public static Configuration instance;
	
	public boolean allowMultipleTitles;
	public int landTax;
	public int fortressTax;
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
		public String alreadyInAKingdom;
		public String notInAKingdom;
		public String notAPlayer;
		public String invalidIndex;
		public String listTopStarter;
		public String listTopEnder;
		public String listIndexColor;
		public String listItemColor;
		public String listBottom;
		public String autoClaimOn;
		public String autoClaimOff;
		
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
		
		public Messages(Feudal plugin) {
			this.plugin = plugin;
			this.loadMessages();
		}
		
		public void loadMessages() {
			this.prefix = ChatColor.translateAlternateColorCodes('&', this.plugin.messageData.get().getString("prefix"));
		}
	}
}
