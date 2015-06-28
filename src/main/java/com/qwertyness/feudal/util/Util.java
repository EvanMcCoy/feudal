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
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;

public class Util {

	public static List<UUID> toUUIDList(List<String> stringList) {
		List<UUID> uuidList = new ArrayList<UUID>();
		if (stringList == null) {
			return uuidList;
		}
		for (String string : stringList) {
			uuidList.add(UUID.fromString(string));
		}
		return uuidList;
	}
	
	public static List<String> toStringList(List<UUID> uuidList) {
		List<String> stringList = new ArrayList<String>();
		if (uuidList == null) {
			return stringList;
		}
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		return stringList;
	}
	
	public static Chunk toChunk(String string) {
		if (string == null) {
			return null;
		}
		if (string.equals("")) {
			return null;
		}
		String[] args = string.split(";");
		World world = Bukkit.getWorld(args[0]);
		if (world == null) {
			return null;
		}
		int x = 0;
		int z = 0;
		try {
			x = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			return null;
		}
		
		return world.getChunkAt(x, z);
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
		FeudalPlayer fPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		if (fPlayer != null) {
			if (fPlayer.kingdom != null) {
				return plugin.getKingdomManager().getKingdom(plugin.getPlayerManager().getPlayer(player.getUniqueId()).kingdom);
			}
		}
		return null;
	}
	
	public static Fief getFief(Player player) {
		Feudal plugin = Feudal.getInstance();
		FeudalPlayer feudalPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		if (feudalPlayer != null) {
			if (feudalPlayer.kingdom != null && feudalPlayer.fief != null) {
				return plugin.getFiefManager().getFief(feudalPlayer.kingdom, feudalPlayer.fief);
			}
		}
		return null;
	}
	
	public static boolean isInKingdom(Player player) {
		return getKingdom(player) != null;
	}
	
	public static String getTitle(Player player, Kingdom kingdom, Fief fief) {
		if (kingdom != null) {
			if (((kingdom.getKing() != null) ? kingdom.getKing().toString() : "").equals(player.getUniqueId().toString())) {
				return "king";
			}
			if (((kingdom.getQueen() != null) ? kingdom.getQueen().toString() : "").equals(player.getUniqueId().toString())) {
				return "queen";
			}
			if (((kingdom.getPrincess() != null) ? kingdom.getPrince().toString() : "").equals(player.getUniqueId().toString())) {
				return "prince";
			}
			if (((kingdom.getPrincess() != null) ? kingdom.getPrincess().toString() : "").equals(player.getUniqueId().toString())) {
				return "princess";
			}
			if (((kingdom.getDuke() != null) ? kingdom.getDuke().toString() : "").equals(player.getUniqueId().toString())) {
				return "duke";
			}
			if (((kingdom.getDuchess() != null) ? kingdom.getDuchess().toString() : "").equals(player.getUniqueId().toString())) {
				return "duchess";
			}
			if (kingdom.isEarl(player.getUniqueId())) {
				return "earl";
			}
			if (((kingdom.getChurch().getPope() != null) ? kingdom.getChurch().getPope().toString() : "").equals(player.getUniqueId().toString())) {
				return "royalpope";
			}
			if (toStringList(kingdom.getChurch().getAbbots()).contains(player.getUniqueId().toString())) {
				return "royalabbot";
			}
			if (((kingdom.getArmy().getKnight() != null) ? kingdom.getArmy().getKnight().toString() : "").equals(player.getUniqueId().toString())) {
				return "royalknight";
			}
			if (((kingdom.getArmy().getDame() != null) ? kingdom.getArmy().getDame().toString() : "").equals(player.getUniqueId().toString())) {
				return "royaldame";
			}
			if (toStringList(kingdom.getArmy().getSoldiers()).contains(player.getUniqueId().toString())) {
				return "royalsoldier";
			}
		}
		if (fief != null) {
			return getFiefTitle(fief, player);
		}
		else {
			if (kingdom != null) {
				for (Fief iterateFief : kingdom.getFiefs()) {
					return getFiefTitle(iterateFief, player);
				}
			}
		}
		return null;
	}
	
	private static String getFiefTitle(Fief fief, Player player) {
		if (((fief.getBaron() != null) ? fief.getBaron().toString() : "").equals(player.getUniqueId().toString())) {
			return "baron";
		}
		else if (((fief.getBaroness() != null) ? fief.getBaroness().toString() : "").equals(player.getUniqueId().toString())) {
			return "baroness";
		}
		else if (toStringList(fief.getPeasents()).contains(player.getUniqueId().toString())) {
			return "peasent";
		}
		else if (toStringList(fief.getSerfs()).contains(player.getUniqueId().toString())) {
			return "serf";
		}
		else if (((fief.getChurch().getPope() != null) ? fief.getChurch().getPope().toString() : "").equals(player.getUniqueId().toString())) {
			return "pope";
		}
		else if (toStringList(fief.getChurch().getAbbots()).contains(player.getUniqueId().toString())) {
			return "abbot";
		}
		else if (((fief.getArmy().getKnight() != null) ? fief.getArmy().getKnight().toString() : "").equals(player.getUniqueId().toString())) {
			return "knight";
		}
		else if (((fief.getArmy().getDame() != null) ? fief.getArmy().getDame().toString() : "").equals(player.getUniqueId().toString())) {
			return "dame";
		}
		else if (toStringList(fief.getArmy().getSoldiers()).contains(player.getUniqueId().toString())) {
			return "soldier";
		}
		return null;
	}
	
	public static void setPosition(String title, Kingdom kingdom, Fief fief, Player player) {
		FeudalPlayer fPlayer = Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
		if (title.equalsIgnoreCase("king")) {
			kingdom.setKing(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("queen")) {
			kingdom.setQueen(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("prince")) {
			kingdom.setPrince(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("princess")) {
			kingdom.setPrincess(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("duke")) {
			kingdom.setDuke(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("duchess")) {
			kingdom.setDuchess(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("earl")) {
			kingdom.addEarl(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalpope")) {
			kingdom.getChurch().setPope(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalabbot")) {
			kingdom.getChurch().addAbbot(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalknight")) {
			kingdom.getArmy().setKnight(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royaldame")) {
			kingdom.getArmy().setDame(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalsoldier")) {
			kingdom.getArmy().addSoldier(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("baron")) {
			fief.setBaron(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("baroness")) {
			fief.setBaroness(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("peasent")) {
			fief.addPeasent(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("serf")) {
			fief.addSerf(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("pope")) {
			fief.getChurch().setPope(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("abbot")) {
			fief.getChurch().addAbbot(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("knight")) {
			fief.getArmy().setKnight(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("dame")) {
			fief.getArmy().setDame(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("soldier")) {
			fief.getArmy().addSoldier(player.getUniqueId());
			removePosition(fPlayer);
			fPlayer.fief = fief.getName();
		}
	}
	
	public static void removePosition(FeudalPlayer player) {
		Feudal plugin = Feudal.getInstance();
		
		Kingdom kingdom = plugin.getKingdomManager().getKingdom(player.kingdom);
		Fief fief = plugin.getFiefManager().getFief(kingdom.getName(), player.fief);
		
		if (kingdom != null) {
			if (((kingdom.getKing() != null) ? kingdom.getKing().toString() : "").equals(player.player.toString())) {
				kingdom.setKing(null);
				if (kingdom.getQueen() == null) {
					plugin.getKingdomManager().deleteKingdom(kingdom);
				}
				player.kingdom = "";
			}
			if (((kingdom.getQueen() != null) ? kingdom.getQueen().toString() : "").equals(player.player.toString())) {
				kingdom.setQueen(null);
				if (kingdom.getKing() == null) {
					plugin.getKingdomManager().deleteKingdom(kingdom);
				}
				player.kingdom = "";
			}
			if (((kingdom.getPrincess() != null) ? kingdom.getPrince().toString() : "").equals(player.player.toString())) {
				kingdom.setPrince(null);
				player.kingdom = "";
			}
			if (((kingdom.getPrincess() != null) ? kingdom.getPrincess().toString() : "").equals(player.player.toString())) {
				kingdom.setPrincess(null);
				player.kingdom = "";
			}
			if (((kingdom.getDuke() != null) ? kingdom.getDuke().toString() : "").equals(player.player.toString())) {
				kingdom.setDuke(null);
				player.kingdom = "";
			}
			if (((kingdom.getDuchess() != null) ? kingdom.getDuchess().toString() : "").equals(player.player.toString())) {
				kingdom.setDuchess(null);
				player.kingdom = "";
			}
			if (kingdom.isEarl(player.player)) {
				kingdom.removeEarl(kingdom.getEarls().indexOf(player.player));
				player.kingdom = "";
			}
			if (((kingdom.getChurch().getPope() != null) ? kingdom.getChurch().getPope().toString() : "").equals(player.player.toString())) {
				kingdom.getChurch().setPope(null);
				player.kingdom = "";
			}
			if (toStringList(kingdom.getChurch().getAbbots()).contains(player.player.toString())) {
				kingdom.getChurch().removeAbbot(kingdom.getChurch().getAbbots().indexOf(player.player));
				player.kingdom = "";
			}
			if (((kingdom.getArmy().getKnight() != null) ? kingdom.getArmy().getKnight().toString() : "").equals(player.player.toString())) {
				kingdom.getArmy().setKnight(null);
				player.kingdom = "";
			}
			if (((kingdom.getArmy().getDame() != null) ? kingdom.getArmy().getDame().toString() : "").equals(player.player.toString())) {
				kingdom.getArmy().setDame(null);
				player.kingdom = "";
			}
			if (toStringList(kingdom.getArmy().getSoldiers()).contains(player.player.toString())) {
				kingdom.getArmy().removeSoldier(kingdom.getArmy().getSoldiers().indexOf(player.player));
				player.kingdom = "";
			}
		}
		if (fief != null) {
			if (((fief.getBaron() != null) ? fief.getBaron().toString() : "").equals(player.player.toString())) {
				fief.setBaron(null);
				player.fief = "";
			}
			if (((fief.getBaroness() != null) ? fief.getBaroness().toString() : "").equals(player.player.toString())) {
				fief.setBaroness(null);
				player.fief = "";
			}
			if (toStringList(fief.getPeasents()).contains(player.player.toString())) {
				fief.removePeasent(fief.getPeasents().indexOf(player.player));
				player.fief = "";
			}
			if (toStringList(fief.getSerfs()).contains(player.player.toString())) {
				fief.removeSerf(fief.getSerfs().indexOf(player.player));
				player.fief = "";
			}
			if (((fief.getChurch().getPope() != null) ? fief.getChurch().getPope().toString() : "").equals(player.player.toString())) {
				fief.getChurch().setPope(null);
				player.fief = "";
			}
			if (toStringList(fief.getChurch().getAbbots()).contains(player.player.toString())) {
				fief.getChurch().removeAbbot(fief.getChurch().getAbbots().indexOf(player.player));
				player.fief = "";
			}
			if (((fief.getArmy().getKnight() != null) ? fief.getArmy().getKnight().toString() : "").equals(player.player.toString())) {
				fief.getArmy().setKnight(null);
				player.fief = "";
			}
			if (((fief.getArmy().getDame() != null) ? fief.getArmy().getDame().toString() : "").equals(player.player.toString())) {
				fief.getArmy().setDame(null);
				player.fief = "";
			}
			if (toStringList(fief.getArmy().getSoldiers()).contains(player.player.toString())) {
				fief.getArmy().removeSoldier(fief.getArmy().getSoldiers().indexOf(player.player));
				player.fief = "";
			}
		}
	}
}
