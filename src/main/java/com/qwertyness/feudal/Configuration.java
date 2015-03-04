package com.qwertyness.feudal;

import org.bukkit.ChatColor;

public class Configuration {
	private Feudal plugin;
	public static Configuration instance;
	
	public boolean allowMultipleTitles;
	public int landTax;
	public int fortressTax;
	//In hours
	public int taxFrequency;
	
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
		this.taxFrequency = this.plugin.getConfig().getInt("taxFrequency");
		
		this.messages = new Messages(this.plugin);
	}
	
	public class Messages {
		private Feudal plugin;
		
		public String prefix;
		public String insufficientPermission;
		public String alreadyInAKingdom;
		public String notInAKingdom;
		
		public String kingdomCreate;
		public String alreadyAKingdom;
		public String kingdomDisunite;
		public String kingdomDisuniteConfirm;
		
		public String fiefCreate;
		public String alreadyAFief;
		public String fiefDisband;
		
		public Messages(Feudal plugin) {
			this.plugin = plugin;
			this.loadMessages();
		}
		
		public void loadMessages() {
			this.prefix = ChatColor.translateAlternateColorCodes('&', this.plugin.messageData.get().getString("prefix"));
		}
	}
}
