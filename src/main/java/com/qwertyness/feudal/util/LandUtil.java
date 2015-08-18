package com.qwertyness.feudal.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Chunk;

import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.LandManager;

public class LandUtil {
	private static LandManager manager;
	
	public LandUtil(LandManager landManager) {
		manager = landManager;
	}

	public static void claimLand(Kingdom kingdom, Chunk chunk) {
		Land land = manager.getLand(toString(chunk));
		kingdom.addLand(land);
		land.kingdom = kingdom;
	}
	
	public static void claimLand(Fief fief, Chunk chunk) {
		Land land = manager.getLand(toString(chunk));
		fief.addLand(land);
		land.fief = fief;
	}
	
	public static void unclaimLand(Kingdom kingdom, Chunk chunk) {
		Land land = manager.getLand(toString(chunk));
		if (land.fief != null) {
			unclaimLand(land.fief, chunk);
		}
		kingdom.removeLand(land);
		land.kingdom = null;
	}
	
	public static void unclaimLand(Fief fief, Chunk chunk) {
		Land land = manager.getLand(toString(chunk));
		fief.removeLand(land);
		land.fief = null;
	}
	
	public static String toString(Chunk chunk) {
		if (chunk == null) {
			return null;
		}
		return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ() + ";";
	}
	
	public static List<String> toChunkStringList(List<Chunk> chunks) {
		List<String> strings = new ArrayList<String>();
		if (chunks == null) {
			return strings;
		}
		for (Chunk chunk : chunks) {
			strings.add(toString(chunk));
		}
		return strings;
	}
	
	public static int getChunkRadius(Chunk chunk0, Chunk chunk1) {
		//Might switch to distance formula
		 return Math.abs(chunk1.getX()-chunk0.getX()) + Math.abs(chunk1.getZ()-chunk0.getZ());
	}
	
	public static List<Chunk> getChunksInRadius(int radius, Chunk center) {
		List<Chunk> chunks = Arrays.asList(center);
		List<Chunk> addChunks = new ArrayList<Chunk>();
		for (int i = 0; i < radius;i++) {
			for (Chunk chunk : chunks) {
				addChunks.add(chunk.getWorld().getChunkAt(chunk.getX()+1, chunk.getZ()));
				addChunks.add(chunk.getWorld().getChunkAt(chunk.getX()-1, chunk.getZ()));
				addChunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ()+1));
				addChunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ()-1));
			}
			chunks.addAll(addChunks);
		}
		return chunks;
	}
}
