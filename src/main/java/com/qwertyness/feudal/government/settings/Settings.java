package com.qwertyness.feudal.government.settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {
	public GovernmentPermission buildPermission = GovernmentPermission.CIVILIAN;
	public GovernmentPermission control = GovernmentPermission.MONARCH;
	public GovernmentPermission administrate = GovernmentPermission.ROYALTY;
	public GovernmentPermission manage = GovernmentPermission.DEPUTY;
	
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
				if (titlePermission.getTitle().equalsIgnoreCase(title)) {
					return titlePermission.hasPermission(this, level);
				}
			}
			return false;
		}
		
		public boolean comparePermission(GovernmentPermission compare) {
			if (compare.permLevel == this.permLevel) {
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
					return permission.comparePermission(this.kingdomLevel);
				case 1:
					return permission.comparePermission(this.fiefLevel);
				case 2:
					return permission.comparePermission(this.armyLevel);
				case 3:
					return permission.comparePermission(this.churchLevel);
			}
			return permission.comparePermission(GovernmentPermission.MONARCH);
		}
	}
}
