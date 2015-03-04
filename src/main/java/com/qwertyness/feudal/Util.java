package com.qwertyness.feudal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Government;
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
	
	public static boolean governmentContainsPlayer(Government government, Player player) {
		String uuid = player.getUniqueId().toString();
		if (government.getArmy().knight.toString().equals(uuid) ||
				government.getArmy().dame.toString().equals(uuid) ||
				toStringList(government.getArmy().soldiers).contains(uuid) ||
				government.getChurch().pope.equals(uuid) ||
				toStringList(government.getChurch().abbots).contains(uuid)) {
			return true;
		}
		return false;
	}
	
	public static Kingdom getKingdom(Player player) {
		List<Kingdom> kingdoms = Feudal.getInstance().kingdomManager.kingdoms;
		String uuid = player.getUniqueId().toString();
		for (Kingdom kingdom : kingdoms) {
			if (kingdom.king.toString().equals(uuid) ||
					kingdom.queen.toString().equals(uuid) ||
					kingdom.prince.toString().equals(uuid) ||
					kingdom.princess.toString().equals(uuid) ||
					kingdom.duke.toString().equals(uuid) ||
					kingdom.duchess.toString().equals(uuid) ||
					toStringList(kingdom.earls).contains(uuid)) {
				return kingdom;
			}
			if (governmentContainsPlayer(kingdom, player)) {
				return kingdom;
			}
			for (Fief fief : kingdom.fiefs) {
				if (fief.baron.toString().equals(uuid) ||
						fief.baroness.toString().equals(uuid) ||
						toStringList(fief.serfs).contains(uuid) ||
						toStringList(fief.peasents).contains(uuid)) {
					return kingdom;
				}
				if (governmentContainsPlayer(fief, player)) {
					return kingdom;
				}
			}
		}
		return null;
	}
	
	public static boolean isInKingdom(Player player) {
		return getKingdom(player) == null;
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
