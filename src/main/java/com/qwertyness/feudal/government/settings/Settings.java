package com.qwertyness.feudal.government.settings;

import java.util.HashMap;

public class Settings {
	public GovernmentPermission buildPermission = GovernmentPermission.CIVILIAN;
	public GovernmentPermission control = GovernmentPermission.MONARCH;
	public GovernmentPermission administrate = GovernmentPermission.ROYALTY;
	public GovernmentPermission manage = GovernmentPermission.DEPUTY;
	
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
		
		GovernmentPermission.CIVILIAN.addKingdomPosition("royalabbot");
		GovernmentPermission.OFFICIAL.addKingdomPosition("royalpope");
		
		GovernmentPermission.CIVILIAN.addKingdomPosition("royalsolider");
		GovernmentPermission.OFFICIAL.addKingdomPosition("royalknight");
		GovernmentPermission.OFFICIAL.addKingdomPosition("royaldame");
		
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
		GovernmentPermission.DEPUTY.addChurchPosition("royalAbbot");
		GovernmentPermission.ROYALTY.addChurchPosition("royalPope");
		
		GovernmentPermission.ROYALTY.addArmyPosition("king");
		GovernmentPermission.ROYALTY.addArmyPosition("queen");
		GovernmentPermission.ROYALTY.addArmyPosition("prince");
		GovernmentPermission.ROYALTY.addArmyPosition("princess");
		GovernmentPermission.ROYALTY.addArmyPosition("duke");
		GovernmentPermission.ROYALTY.addArmyPosition("duchess");
		GovernmentPermission.ROYALTY.addArmyPosition("earl");
		
		GovernmentPermission.ROYALTY.addArmyPosition("baron");
		GovernmentPermission.ROYALTY.addArmyPosition("baroness");
		
		//Army positions
		GovernmentPermission.CIVILIAN.addArmyPosition("soldier");
		GovernmentPermission.ROYALTY.addArmyPosition("knight");
		GovernmentPermission.ROYALTY.addArmyPosition("dame");
		GovernmentPermission.CIVILIAN.addArmyPosition("royalSoldier");
		GovernmentPermission.ROYALTY.addArmyPosition("royalKnight");
		GovernmentPermission.ROYALTY.addArmyPosition("royalDame");
		
		GovernmentPermission.ROYALTY.addArmyPosition("king");
		GovernmentPermission.ROYALTY.addArmyPosition("queen");
		GovernmentPermission.ROYALTY.addArmyPosition("prince");
		GovernmentPermission.ROYALTY.addArmyPosition("princess");
		GovernmentPermission.ROYALTY.addArmyPosition("duke");
		GovernmentPermission.ROYALTY.addArmyPosition("duchess");
		GovernmentPermission.ROYALTY.addArmyPosition("earl");
		
		GovernmentPermission.ROYALTY.addArmyPosition("baron");
		GovernmentPermission.ROYALTY.addArmyPosition("baroness");
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
			kingdomPositions.put(position, this);
		}
		public void addFiefPosition(String position) {
			fiefPositions.put(position, this);
		}
		public void addChurchPosition(String position) {
			churchPositions.put(position, this);
		}
		public void addArmyPosition(String position) {
			armyPositions.put(position, this);
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
			return hasPermission(getGroupByFiefTitle(title));
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
			return hasPermission(getGroupByChurchTitle(title));
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
			return hasPermission(getGroupByArmyTitle(title));
		}
		
		
		public boolean hasPermission(GovernmentPermission group) {
			if (this.permLevel >= group.permLevel) {
				return true;
			}
			return false;
		}
	}
}
