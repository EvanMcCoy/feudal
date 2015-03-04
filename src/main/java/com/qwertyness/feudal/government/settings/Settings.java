package com.qwertyness.feudal.government.settings;

import java.util.HashMap;

public class Settings {
	public GovernmentPermission buildPermission = GovernmentPermission.CIVILIAN;
	public GovernmentPermission control = GovernmentPermission.MONARCH;
	public GovernmentPermission manage = GovernmentPermission.ROYALTY;
	
	public static void inizializeDefaultPositions() {
		//Kingdom positions
		GovernmentPermission.OFFICIAL.addKingdomPosition("earl");
		GovernmentPermission.DEPUTY.addKingdomPosition("duke");
		GovernmentPermission.DEPUTY.addKingdomPosition("duchess");
		GovernmentPermission.ROYALTY.addKingdomPosition("prince");
		GovernmentPermission.ROYALTY.addKingdomPosition("princess");
		GovernmentPermission.MONARCH.addKingdomPosition("king");
		GovernmentPermission.MONARCH.addKingdomPosition("queen");
		
		GovernmentPermission.CIVILIAN.addKingdomPosition("serf");
		GovernmentPermission.CIVILIAN.addKingdomPosition("peasant");
		GovernmentPermission.OFFICIAL.addKingdomPosition("baron");
		GovernmentPermission.OFFICIAL.addKingdomPosition("baroness");
		
		GovernmentPermission.CIVILIAN.addKingdomPosition("abbot");
		GovernmentPermission.OFFICIAL.addKingdomPosition("pope");
		
		GovernmentPermission.CIVILIAN.addKingdomPosition("solider");
		GovernmentPermission.OFFICIAL.addKingdomPosition("knight");
		GovernmentPermission.OFFICIAL.addKingdomPosition("dame");
		
		//Fief positions
		GovernmentPermission.CIVILIAN.addFiefPosition("serf");
		GovernmentPermission.CIVILIAN.addFiefPosition("peasant");
		GovernmentPermission.ROYALTY.addFiefPosition("baron");
		GovernmentPermission.ROYALTY.addFiefPosition("baroness");
		
		GovernmentPermission.CIVILIAN.addFiefPosition("abbot");
		GovernmentPermission.OFFICIAL.addFiefPosition("pope");
		
		GovernmentPermission.CIVILIAN.addFiefPosition("soldier");
		GovernmentPermission.OFFICIAL.addFiefPosition("knight");
		GovernmentPermission.OFFICIAL.addFiefPosition("dame");
		
		//Church positions
		GovernmentPermission.DEPUTY.addChurchPosition("abbot");
		GovernmentPermission.ROYALTY.addChurchPosition("pope");
		
		//Army positions
		GovernmentPermission.CIVILIAN.addArmyPosition("soldier");
		GovernmentPermission.ROYALTY.addArmyPosition("knight");
		GovernmentPermission.ROYALTY.addArmyPosition("dame");
	}
	
	public enum GovernmentPermission {
		ALL(0), CIVILIAN(1), OFFICIAL(2), DEPUTY(3), ROYALTY(4), MONARCH(5);
		private static HashMap<String, GovernmentPermission> kingdomPositions = new HashMap<String, GovernmentPermission>();
		private static HashMap<String, GovernmentPermission> fiefPositions = new HashMap<String, GovernmentPermission>();
		private static HashMap<String, GovernmentPermission> churchPositions = new HashMap<String, GovernmentPermission>();
		private static HashMap<String, GovernmentPermission> armyPositions = new HashMap<String, GovernmentPermission>();
		
		private int permLevel;
		private GovernmentPermission(int permLevel) {
			this.permLevel = permLevel;
		}
		
		public void addKingdomPosition(String position) {
			this.kingdomPositions.put(position, this);
		}
		public void addFiefPosition(String position) {
			this.fiefPositions.put(position, this);
		}
		public void addChurchPosition(String position) {
			this.churchPositions.put(position, this);
		}
		public void addArmyPosition(String position) {
			this.armyPositions.put(position, this);
		}
		
		//Kingdom positions
		public static GovernmentPermission getGroupByKingdomTitle(String title) {
			for (String key : kingdomPositions.keySet()) {
				if (key.equalsIgnoreCase(title)) {
					return kingdomPositions.get(key);
				}
			}
			return GovernmentPermission.ALL;
		}
		
		public boolean hasKingdomPermission(String title) {
			return hasPermission(getGroupByKingdomTitle(title));
		}
		
		//Fief positions
		public static GovernmentPermission getGroupByFiefTitle(String title) {
			for (String key : fiefPositions.keySet()) {
				if (key.equalsIgnoreCase(title)) {
					return fiefPositions.get(key);
				}
			}
			return GovernmentPermission.ALL;
		}
		
		public boolean hasFiefPermission(String title) {
			return hasPermission(getGroupByKingdomTitle(title));
		}
		
		//Church positions
		public static GovernmentPermission getGroupByChurchTitle(String title) {
			for (String key : churchPositions.keySet()) {
				if (key.equalsIgnoreCase(title)) {
					return churchPositions.get(key);
				}
			}
			return GovernmentPermission.ALL;
		}
		
		public boolean hasChurchPermission(String title) {
			return hasPermission(getGroupByKingdomTitle(title));
		}
		
		//Army positions
		public static GovernmentPermission getGroupByArmyTitle(String title) {
			for (String key : armyPositions.keySet()) {
				if (key.equalsIgnoreCase(title)) {
					return armyPositions.get(key);
				}
			}
			return GovernmentPermission.ALL;
		}
		
		public boolean hasArmyPermission(String title) {
			return hasPermission(getGroupByKingdomTitle(title));
		}
		
		
		public boolean hasPermission(GovernmentPermission group) {
			if (this.permLevel <= group.permLevel) {
				return true;
			}
			return false;
		}
	}
}
