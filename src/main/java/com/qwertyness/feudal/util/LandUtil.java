package com.qwertyness.feudal.util;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Chunk;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.government.LandManager;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;

public class LandUtil {
	private static LandManager manager;
	
	public LandUtil(LandManager landManager) {
		manager = landManager;
	}

	public static void claimLand(Kingdom kingdom, Chunk chunk) {
		Land land = checkLand(chunk);
		kingdom.addLand(land);
		land.kingdom = kingdom;
	}
	
	public static void claimLand(Fief fief, Chunk chunk) {
		Land land = checkLand(chunk);
		fief.addLand(land);
		land.fief = fief;
	}
	
	public static void unclaimLand(Kingdom kingdom, Chunk chunk) {
		Land land = checkLand(chunk);
		kingdom.removeLand(land);
		land.kingdom = null;
	}
	
	public static void unclaimLand(Fief fief, Chunk chunk) {
		Land land = checkLand(chunk);
		fief.removeLand(land);
		land.fief = null;
	}
	
	private static Land checkLand(Chunk chunk) {
		Land land = null;
		if (!manager.isLand(Util.toString(chunk))) {
			land = new Land(chunk, null, null, new ArrayList<UUID>(), Feudal.getInstance().getLandData().get().createSection(Util.toString(chunk)));
			manager.registerLand(land);
		}
		else {
			land = manager.getLand(Util.toString(chunk));
		}
		return land;
	}
}
