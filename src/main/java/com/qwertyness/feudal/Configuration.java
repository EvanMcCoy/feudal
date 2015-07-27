package com.qwertyness.feudal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.qwertyness.feudal.government.Flag;

public class Configuration {
	private Feudal plugin;
	public static Configuration instance;
	
	public int landTax;
	public int fortressTax;
	public static boolean useEconomy;
	//In hours
	public List<Integer> taxTimes;
	public int maxLand;
	public int fortressRadius; 
	
	public Flag defaultFlag;
	
	public Messages messages;
	
	public Configuration(Feudal plugin) {
		this.plugin = plugin;
		instance = this;
		this.loadConfiguration();
	}
	
	public void loadConfiguration() {
		this.landTax = this.plugin.getConfig().getInt("landTax");
		this.fortressTax = this.plugin.getConfig().getInt("fortressTax");
		this.taxTimes = this.plugin.getConfig().getIntegerList("taxTimes");
		this.maxLand = this.plugin.getConfig().getInt("maxLand");
		this.fortressRadius = this.plugin.getConfig().getInt("fortressRadius");
		this.defaultFlag = loadDefaultFlag();
		Flag.buildStructureBlockList(this.plugin.getConfig().getConfigurationSection("flags.structure"));
		
		this.messages = new Messages(this.plugin);
	}
	
	private Flag loadDefaultFlag() {
		ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("flags.defaultFlag");
		DyeColor testColor = DyeColor.valueOf(section.getString("color"));
		if (testColor == null) {
			testColor = DyeColor.WHITE;
		}
		DyeColor color = testColor;
		
		ConfigurationSection patternSection = section.getConfigurationSection("patterns");
		if (patternSection == null) {
			return new Flag(color, new ArrayList<Pattern>());
		}
		else {
			List<Pattern> patterns = new ArrayList<Pattern>();
			for (String p : patternSection.getKeys(false)) {
				DyeColor patternColor = DyeColor.valueOf(section.getString("patterns." + p + ".color"));
				if (patternColor == null) {
					patternColor = DyeColor.WHITE;
				}
				PatternType type = PatternType.valueOf(section.getString("patterns." + p + ".pattern"));
				if (type == null) {
					type = PatternType.BASE;
				}
				patterns.add(new Pattern(patternColor, type));
			}
			return new Flag(color, patterns);
		}
	}
	
	public class Messages {
		private Feudal plugin;
		
		public String prefix, insufficientPermission, notInAKingdom, notInAGovernment, notAPlayer, invalidIndex, invalidMaterial, invalidNumber, listTopStarter, listTopEnder, listIndexColor, listItemColor, listBottom;
		
		public String alreadyInAKingdom, kingdomCreate, alreadyAKingdom, kingdomDisunite, kingdomDisuniteConfirm, setCounterpart, setDuke, setDuchess, setPrince, setPrincess, addEarl, alreadyAnEarl, removeEarl,
			fiefCreate, alreadyAFief, fiefDisband, notAFief, setFiefBaron, setFiefBaroness;
		
		public String noArmy, setKnight, setDame, setArmyCounterpart, addSoldier, removeSoldier, capture, weaken, raze, notAnEnemy;
		
		public String notKingdomLand, landAlreadyAllocated, landNotAllocated, allocateLand, deallocateLand, deallocateAll, alreadyCapital, setCapital, alreadyAFortress, notAFortress, addFortress, removeFortress,
			landAlreadyClaimed, noFortressInRange, claimLand, cannotUnclaimCapital, cannotUnclaimFortress, unclaimLand, autoClaimOn, autoClaimOff, cannotBuild;
		
		public String insufficientMoney, insufficientBankMoney, insufficientItems, insufficientBankItems, depositMoney, withdrawMoney, depositItems, withdrawItems;
		
		public Messages(Feudal plugin) {
			this.plugin = plugin;
			this.loadMessages();
		}
		
		public void loadMessages() {
			FileConfiguration messages = this.plugin.getMessageData().get();
			this.prefix = color(messages.getString("prefix"));
			this.insufficientPermission = color(messages.getString("insufficientPermission"));
			this.notInAGovernment = color(messages.getString("notInAGovernment"));
			this.notInAKingdom = color(messages.getString("notInAKingdom"));
			this.notAPlayer = color(messages.getString("notAPlayer"));
			this.invalidIndex = color(messages.getString("invalidIndex"));
			this.invalidMaterial = color(messages.getString("invalidMaterial"));
			this.invalidNumber = color(messages.getString("invalidNumber"));
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
			
			this.noArmy = color(messages.getString("noArmy"));
			this.setKnight = color(messages.getString("setKnight"));
			this.setDame = color(messages.getString("setDame"));
			this.setArmyCounterpart = color(messages.getString("setArmyCounterpart"));
			this.addSoldier = color(messages.getString("addSoldier"));
			this.removeSoldier = color(messages.getString("removeSoldier"));
			this.capture = color(messages.getString("capture"));
			this.weaken = color(messages.getString("weaken"));
			this.raze = color(messages.getString("raze"));
			this.notAnEnemy = color(messages.getString("notAnEnemy"));
			
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
			this.cannotBuild = color(messages.getString("cannotBuild"));
			
			this.insufficientMoney = color(messages.getString("insufficientMoney"));
			this.insufficientBankMoney = color(messages.getString("insufficientBankMoney"));
			this.insufficientItems = color(messages.getString("insufficientItems"));
			this.insufficientBankItems = color(messages.getString("insufficientBankItems"));
			this.depositMoney = color(messages.getString("depositMoney"));
			this.depositItems = color(messages.getString("depositItems"));
			this.withdrawMoney = color(messages.getString("withdrawMoney"));
			this.withdrawItems = color(messages.getString("withdrawItems"));
		}
		
		public String color(String input) {
			return ChatColor.translateAlternateColorCodes('&', input);
		}
	}
}
