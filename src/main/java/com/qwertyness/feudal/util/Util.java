package com.qwertyness.feudal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Kingdom;

public class Util {

	public static List<UUID> toUUIDList(List<String> stringList) {
		List<UUID> uuidList = new ArrayList<UUID>();
		for (String string : stringList) {
			uuidList.add(UUID.fromString(string));
		}
		return uuidList;
	}
	
	public static List<String> toStringList(List<UUID> uuidList) {
		List<String> stringList = new ArrayList<String>();
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		return stringList;
	}
	
	public static Chunk toChunk(String string) {
		String[] args = string.split(";");
		World world = Bukkit.getWorld(args[0]);
		if (world == null) {
			return null;
		}
		int x = Integer.parseInt(args[1]);
		int z = Integer.parseInt(args[2]);
		return world.getChunkAt(x, z);
	}
	
	public static String toString(Chunk chunk) {
		return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ() + ";";
	}
	
	public static List<String> toChunkStringList(List<Chunk> chunks) {
		List<String> strings = new ArrayList<String>();
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
		for (int i = 0; i < radius;i++) {
			for (Chunk chunk : chunks) {
				chunks.add(chunk.getWorld().getChunkAt(chunk.getX()+1, chunk.getZ()));
				chunks.add(chunk.getWorld().getChunkAt(chunk.getX()-1, chunk.getZ()));
				chunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ()+1));
				chunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ()-1));
			}
		}
		return chunks;
	}
	
	public static Kingdom getKingdom(Player player) {
		Feudal plugin = Feudal.getInstance();
		if (plugin.playerManager.isPlayer(player.getUniqueId())) {
			return plugin.kingdomManager.getKingdom(plugin.playerManager.getPlayer(player.getUniqueId()).kingdom);
		}
		return null;
	}
	
	public static boolean isInKingdom(Player player) {
		return getKingdom(player) != null;
	}
	
	public static String getTitle(Player player, Kingdom kingdom) {
		if (kingdom.king.toString().equals(player.getUniqueId().toString())) {
			return "king";
		}
		else if (kingdom.queen.toString().equals(player.getUniqueId().toString())) {
			return "queen";
		}
		else if (kingdom.prince.toString().equals(player.getUniqueId().toString())) {
			return "prince";
		}
		else if (kingdom.princess.toString().equals(player.getUniqueId().toString())) {
			return "princess";
		}
		else if (kingdom.duke.toString().equals(player.getUniqueId().toString())) {
			return "duke";
		}
		else if (kingdom.duchess.toString().equals(player.getUniqueId().toString())) {
			return "duchess";
		}
		else if (toStringList(kingdom.earls).contains(player.getUniqueId().toString())) {
			return "earl";
		}
		else if (kingdom.highChurch.pope.toString().equals(player.getUniqueId().toString())) {
			return "pope";
		}
		else if (toStringList(kingdom.highChurch.abbots).contains(player.getUniqueId().toString())) {
			return "abbot";
		}
		else if (kingdom.royalArmy.knight.toString().equals(player.getUniqueId().toString())) {
			return "knight";
		}
		else if (kingdom.royalArmy.dame.toString().equals(player.getUniqueId().toString())) {
			return "dame";
		}
		else if (toStringList(kingdom.royalArmy.soldiers).contains(player.getUniqueId().toString())) {
			return "soldier";
		}
		return null;
	}
}
