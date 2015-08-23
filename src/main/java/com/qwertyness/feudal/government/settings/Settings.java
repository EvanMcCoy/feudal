package com.qwertyness.feudal.government.settings;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class Settings {
	private GovernmentPermission buildPermission = GovernmentPermission.CIVILIAN;
	private GovernmentPermission control = GovernmentPermission.MONARCH;
	private GovernmentPermission administrate = GovernmentPermission.ROYALTY;
	private GovernmentPermission manage = GovernmentPermission.DEPUTY;
	
	private boolean taxPerPlot = true;
	private int tax = 5;
	//In percent
	private int blockTax = 50;
	
	private ConfigurationSection dataPath;
	
	public Settings(ConfigurationSection section, boolean defaultSettings) {
		if (defaultSettings) {
			section.createSection("settings");
		}
		throw new InvalidParameterException("Cannot use Settings(ConfigurationSection section, boolean defaultSettings) where defaultSettings = false. Use Settings(ConfigurationSection section) instead.");
	}
	
	public Settings(ConfigurationSection section) {
		String buildPermission = section.getString("buildPermission");
		if (buildPermission != null) {
			if (GovernmentPermission.valueOf(buildPermission) != null) {
				this.buildPermission = GovernmentPermission.valueOf(buildPermission);
			}
		}
		
		String control = section.getString("buildPermission");
		if (control != null) {
			if (GovernmentPermission.valueOf(control) != null) {
				this.control = GovernmentPermission.valueOf(control);
			}
		}
		
		String administrate = section.getString("administrate");
		if (administrate != null) {
			if (GovernmentPermission.valueOf(administrate) != null) {
				this.administrate = GovernmentPermission.valueOf(administrate);
			}
		}
		
		String manage = section.getString("manage");
		if (manage != null) {
			if (GovernmentPermission.valueOf(manage) != null) {
				this.manage = GovernmentPermission.valueOf(manage);
			}
		}
		
		this.taxPerPlot = section.getBoolean("taxPerPlot");
		this.tax = section.getInt("tax");
		this.blockTax = section.getInt("blockTax");
		if (blockTax > 100 || blockTax < 0) {
			this.blockTax = 0;
		}
		this.dataPath = section;
	}
	
	public Settings(GovernmentPermission buildPermission, GovernmentPermission control, GovernmentPermission administrate, GovernmentPermission manage, boolean taxPerPlot, int tax, int blockTax, ConfigurationSection dataPath) {
		this.buildPermission = buildPermission;
		this.control = control;
		this.administrate = administrate;
		this.manage = manage;
		
		this.taxPerPlot = taxPerPlot;
		this.tax = tax;
		this.blockTax = blockTax;
		this.dataPath = dataPath;
	}
	
	public GovernmentPermission getBuildPermission() {return this.buildPermission;}
	public void setBuildPermission(GovernmentPermission buildPermission) {this.buildPermission = buildPermission;}
	
	public GovernmentPermission getControlPermission() {return this.control;}
	public void setControlPermission(GovernmentPermission control) {this.control = control;}
	public GovernmentPermission getAdministratePermission() {return this.administrate;}
	public void setAdministratePermission(GovernmentPermission administrate) {this.administrate = administrate;}
	public GovernmentPermission getManagePermission() {return this.manage;}
	public void setManagePermission(GovernmentPermission manage) {this.manage = manage;}
	
	public boolean doTaxPerPlot() {return taxPerPlot;}
	public void setDoTaxPerPlot(boolean taxPerPlot) {this.taxPerPlot = taxPerPlot;}
	
	public int getTax() {return this.tax;}
	public void setTax(int tax) {this.tax = tax;}
	
	public int getBlockTaxPercent() {return this.blockTax;}
	public void setBlockTaxPercent(int blockTax) {this.blockTax = blockTax;}
	
	public void saveSettings() {
		dataPath.set("buildPermission", this.buildPermission.toString());
		dataPath.set("control", this.control.toString());
		dataPath.set("administrate", this.administrate.toString());
		dataPath.set("manage", this.manage.toString());
		
		dataPath.set("taxPerPlot", this.taxPerPlot);
		dataPath.set("tax", this.tax);
		dataPath.set("blockTax", this.blockTax);
	}
	
	public static void inizializeDefaultPositions() {
		GovernmentPermission.titles.add(new TitlePermission("king", GovernmentPermission.MONARCH, GovernmentPermission.MONARCH, GovernmentPermission.MONARCH, GovernmentPermission.MONARCH));
		GovernmentPermission.titles.add(new TitlePermission("queen", GovernmentPermission.MONARCH, GovernmentPermission.MONARCH, GovernmentPermission.MONARCH, GovernmentPermission.MONARCH));
		GovernmentPermission.titles.add(new TitlePermission("prince", GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("princess", GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("duke", GovernmentPermission.DEPUTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("duchess", GovernmentPermission.DEPUTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("earl", GovernmentPermission.OFFICIAL, GovernmentPermission.DEPUTY, GovernmentPermission.DEPUTY, GovernmentPermission.DEPUTY));
		
		GovernmentPermission.titles.add(new TitlePermission("royalpope", GovernmentPermission.OFFICIAL, GovernmentPermission.OFFICIAL, GovernmentPermission.CIVILIAN, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("royalabbot", GovernmentPermission.OFFICIAL, GovernmentPermission.OFFICIAL, GovernmentPermission.CIVILIAN, GovernmentPermission.DEPUTY));
		
		GovernmentPermission.titles.add(new TitlePermission("royalknight", GovernmentPermission.OFFICIAL, GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.CIVILIAN));
		GovernmentPermission.titles.add(new TitlePermission("royaldame", GovernmentPermission.OFFICIAL, GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.CIVILIAN));
		GovernmentPermission.titles.add(new TitlePermission("royalsoldier", GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN));
		
		GovernmentPermission.titles.add(new TitlePermission("baron", GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("baroness", GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("peasent", GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN));
		GovernmentPermission.titles.add(new TitlePermission("serf", GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN));
		
		GovernmentPermission.titles.add(new TitlePermission("pope", GovernmentPermission.CIVILIAN, GovernmentPermission.OFFICIAL, GovernmentPermission.CIVILIAN, GovernmentPermission.ROYALTY));
		GovernmentPermission.titles.add(new TitlePermission("abbot", GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.DEPUTY));
		
		GovernmentPermission.titles.add(new TitlePermission("knight", GovernmentPermission.CIVILIAN, GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.CIVILIAN));
		GovernmentPermission.titles.add(new TitlePermission("dame", GovernmentPermission.CIVILIAN, GovernmentPermission.OFFICIAL, GovernmentPermission.ROYALTY, GovernmentPermission.CIVILIAN));
		GovernmentPermission.titles.add(new TitlePermission("soldier", GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN, GovernmentPermission.CIVILIAN));
	}
	
	public static enum GovernmentPermission {
		ALL(0), CIVILIAN(1), OFFICIAL(2), DEPUTY(3), ROYALTY(4), MONARCH(5);
		public static List<TitlePermission> titles = new ArrayList<TitlePermission>();
		
		private int permLevel;
		private GovernmentPermission(int permLevel) {
			this.permLevel = permLevel;
		}
		
		public boolean titleHasPermission(String title, int level) {
			for (TitlePermission titlePermission : titles) {
				if (titlePermission.getTitle().equalsIgnoreCase(title) && titlePermission.hasPermission(this, level)) {
					return true;
				}
			}
			return false;
		}
		
		public boolean checkPermission(GovernmentPermission compare) {
			if (compare.permLevel >= this.permLevel) {
				return true;
			}
			return false;
		}
	}
	
	public static class TitlePermission {
		private String title;
		private GovernmentPermission kingdomLevel;
		private GovernmentPermission fiefLevel;
		private GovernmentPermission armyLevel;
		private GovernmentPermission churchLevel;
		
		public static int KINGDOM_LEVEL = 0;
		public static int FIEF_LEVEL = 1;
		public static int ARMY_LEVEL = 2;
		public static int CHURCH_LEVEL = 3;
		
		public TitlePermission(String title, GovernmentPermission kingdomLevel, GovernmentPermission fiefLevel, GovernmentPermission armyLevel, GovernmentPermission churchLevel) {
			this.title = title;
			this.kingdomLevel = kingdomLevel;
			this.fiefLevel = fiefLevel;
			this.armyLevel = armyLevel;
			this.churchLevel = churchLevel;
		}
		
		public String getTitle() {
			return this.title;
		}
		
		public boolean hasPermission(GovernmentPermission permission, int level) {
			switch (level) {
				case 0:
					return permission.checkPermission(this.kingdomLevel);
				case 1:
					return permission.checkPermission(this.fiefLevel);
				case 2:
					return permission.checkPermission(this.armyLevel);
				case 3:
					return permission.checkPermission(this.churchLevel);
			}
			return permission.checkPermission(GovernmentPermission.MONARCH);
		}
	}
}
