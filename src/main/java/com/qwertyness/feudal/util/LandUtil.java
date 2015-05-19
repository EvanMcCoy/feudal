package com.qwertyness.feudal.util;

import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;

public class LandUtil {

	public static void claimLand(Kingdom kingdom, Land land) {
		kingdom.land.add(land);
		land.kingdom = kingdom;
	}
	
	public static void claimLand(Fief fief, Land land) {
		fief.land.add(land);
		land.fief = fief;
	}
	
	public static void unclaimLand(Kingdom kingdom, Land land) {
		kingdom.land.remove(land);
		land.kingdom = null;
	}
	
	public static void unclaimLand(Fief fief, Land land) {
		fief.land.remove(land);
		land.fief = null;
	}
}
