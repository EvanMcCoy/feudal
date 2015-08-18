package com.qwertyness.feudal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Church;
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
	
	public static String toPlayerName(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if (player != null) {
			return player.getName();
		}
		return "";
	}
	
	public static List<String> toPlayerNameList(List<UUID> uuids) {
		List<String> output = new ArrayList<String>();
		for (UUID uuid : uuids) {
			String playerName = toPlayerName(uuid);
			if (playerName != "") {
				output.add(playerName);
			}
		}
		return output;
	}
	
	public static String toUIString(List<String> strings) {
		String output = "";
		for (String string : strings) {
			output += string + ", ";
		}
		if (output.length() > 1) {
			output = output.substring(0, output.length()-2);
		}
		return output;
	}
	
	public static Kingdom getKingdom(Player player) {
		return getKingdom(Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId()));
	}
	
	public static Kingdom getKingdom(FeudalPlayer player) {
		Feudal plugin = Feudal.getInstance();
		
		if (player != null) {
			Kingdom kingdom = plugin.getKingdomManager().getKingdom(player.kingdom);
			if (kingdom != null) {
				return kingdom;
			}
		}
		return null;
	}
	
	public static Fief getFief(Player player) {
		return getFief(Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId()));
	}
	
	public static Fief getFief(FeudalPlayer player) {
		Feudal plugin = Feudal.getInstance();
		
		if (player != null) {
			Fief fief = plugin.getFiefManager().getFief(player.kingdom, player.fief);
			if (fief != null) {
				return fief;
			}
		}
		return null;
	}
	
	public static Army getArmy(Player player) {
		return getArmy(Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId()));
	}
	
	public static Army getArmy(FeudalPlayer player) {
		Feudal plugin = Feudal.getInstance();
		
		if (player != null) {
			Kingdom kingdom = plugin.getKingdomManager().getKingdom(player.kingdom);
			Fief fief = plugin.getFiefManager().getFief(player.kingdom, player.fief);
			Army army = null;
			if (kingdom != null) {
				if (fief != null) {
					army = fief.getArmy();
				}
				else {
					army = kingdom.getArmy();
				}
			}
			if (army != null) {
				if (toStringList(getArmyMembers(army)).contains(player.player.toString())) {
					return army;
				}
			}
		}
		return null;
	}
	
	public static Church getChurch(Player player) {
		return getChurch(Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId()));
	}
	
	public static Church getChurch(FeudalPlayer player) {
		Feudal plugin = Feudal.getInstance();
		
		if (player != null) {
			Kingdom kingdom = plugin.getKingdomManager().getKingdom(player.kingdom);
			Fief fief = plugin.getFiefManager().getFief(player.kingdom, player.fief);
			Church church = null;
			if (kingdom != null) {
				if (fief != null) {
					church = fief.getChurch();
				}
				else {
					church = kingdom.getChurch();
				}
			}
			if (church != null) {
				if (toStringList(getChurchMembers(church)).contains(player.player.toString())) {
					return church;
				}
			}
		}
		return null;
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
	
	public static void setPosition(String title, Kingdom kingdom, Fief fief, OfflinePlayer player) {
		if (player == null) {
			return;
		}
		FeudalPlayer fPlayer = Feudal.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
		if (title.equalsIgnoreCase("king")) {
			kingdom.setKing(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("queen")) {
			kingdom.setQueen(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("prince")) {
			kingdom.setPrince(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("princess")) {
			kingdom.setPrincess(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("duke")) {
			kingdom.setDuke(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("duchess")) {
			kingdom.setDuchess(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("earl")) {
			kingdom.addEarl(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalpope")) {
			kingdom.getChurch().setPope(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalabbot")) {
			kingdom.getChurch().addAbbot(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalknight")) {
			kingdom.getArmy().setKnight(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royaldame")) {
			kingdom.getArmy().setDame(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("royalsoldier")) {
			kingdom.getArmy().addSoldier(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
		}
		if (title.equalsIgnoreCase("baron")) {
			fief.setBaron(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("baroness")) {
			fief.setBaroness(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("peasent")) {
			fief.addPeasent(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("serf")) {
			fief.addSerf(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("pope")) {
			fief.getChurch().setPope(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("abbot")) {
			fief.getChurch().addAbbot(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("knight")) {
			fief.getArmy().setKnight(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("dame")) {
			fief.getArmy().setDame(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
		if (title.equalsIgnoreCase("soldier")) {
			fief.getArmy().addSoldier(player.getUniqueId());
			fPlayer.kingdom = kingdom.getName();
			fPlayer.fief = fief.getName();
		}
	}
	
	public static void removePosition(FeudalPlayer player, boolean destroy) {
		Feudal plugin = Feudal.getInstance();
		if (player == null) {
			return;
		}
		Kingdom kingdom = plugin.getKingdomManager().getKingdom(player.kingdom);
		Fief fief = null;
		
		if (kingdom != null) {
			fief = plugin.getFiefManager().getFief(kingdom.getName(), player.fief);
			if (((kingdom.getKing() != null) ? kingdom.getKing().toString() : "").equals(player.player.toString())) {
				kingdom.setKing(null);
				if (kingdom.getQueen() == null && destroy) {
					plugin.getKingdomManager().deleteKingdom(kingdom);
				}
			}
			if (((kingdom.getQueen() != null) ? kingdom.getQueen().toString() : "").equals(player.player.toString())) {
				kingdom.setQueen(null);
				if (kingdom.getKing() == null && destroy) {
					plugin.getKingdomManager().deleteKingdom(kingdom);
				}
			}
			if (((kingdom.getPrince() != null) ? kingdom.getPrince().toString() : "").equals(player.player.toString())) {
				kingdom.setPrince(null);
			}
			if (((kingdom.getPrincess() != null) ? kingdom.getPrincess().toString() : "").equals(player.player.toString())) {
				kingdom.setPrincess(null);
			}
			if (((kingdom.getDuke() != null) ? kingdom.getDuke().toString() : "").equals(player.player.toString())) {
				kingdom.setDuke(null);
			}
			if (((kingdom.getDuchess() != null) ? kingdom.getDuchess().toString() : "").equals(player.player.toString())) {
				kingdom.setDuchess(null);
			}
			if (kingdom.isEarl(player.player)) {
				kingdom.removeEarl(kingdom.getEarls().indexOf(player.player));
			}
			if (((kingdom.getChurch().getPope() != null) ? kingdom.getChurch().getPope().toString() : "").equals(player.player.toString())) {
				kingdom.getChurch().setPope(null);
			}
			if (toStringList(kingdom.getChurch().getAbbots()).contains(player.player.toString())) {
				kingdom.getChurch().removeAbbot(kingdom.getChurch().getAbbots().indexOf(player.player));
			}
			if (((kingdom.getArmy().getKnight() != null) ? kingdom.getArmy().getKnight().toString() : "").equals(player.player.toString())) {
				kingdom.getArmy().setKnight(null);
			}
			if (((kingdom.getArmy().getDame() != null) ? kingdom.getArmy().getDame().toString() : "").equals(player.player.toString())) {
				kingdom.getArmy().setDame(null);
			}
			if (toStringList(kingdom.getArmy().getSoldiers()).contains(player.player.toString())) {
				kingdom.getArmy().removeSoldier(kingdom.getArmy().getSoldiers().indexOf(player.player));
			}
		}
		if (fief != null) {
			if (((fief.getBaron() != null) ? fief.getBaron().toString() : "").equals(player.player.toString())) {
				fief.setBaron(null);
			}
			if (((fief.getBaroness() != null) ? fief.getBaroness().toString() : "").equals(player.player.toString())) {
				fief.setBaroness(null);
			}
			if (toStringList(fief.getPeasents()).contains(player.player.toString())) {
				fief.removePeasent(fief.getPeasents().indexOf(player.player));
			}
			if (toStringList(fief.getSerfs()).contains(player.player.toString())) {
				fief.removeSerf(fief.getSerfs().indexOf(player.player));
			}
			if (((fief.getChurch().getPope() != null) ? fief.getChurch().getPope().toString() : "").equals(player.player.toString())) {
				fief.getChurch().setPope(null);
			}
			if (toStringList(fief.getChurch().getAbbots()).contains(player.player.toString())) {
				fief.getChurch().removeAbbot(fief.getChurch().getAbbots().indexOf(player.player));
			}
			if (((fief.getArmy().getKnight() != null) ? fief.getArmy().getKnight().toString() : "").equals(player.player.toString())) {
				fief.getArmy().setKnight(null);
			}
			if (((fief.getArmy().getDame() != null) ? fief.getArmy().getDame().toString() : "").equals(player.player.toString())) {
				fief.getArmy().setDame(null);
			}
			if (toStringList(fief.getArmy().getSoldiers()).contains(player.player.toString())) {
				fief.getArmy().removeSoldier(fief.getArmy().getSoldiers().indexOf(player.player));
			}
		}
		player.kingdom = "";
		player.fief = "";
	}
	
	public static List<UUID> getKingdomMembers(Kingdom kingdom) {
		List<UUID> members = new ArrayList<UUID>();
		members.add(kingdom.getKing());
		members.add(kingdom.getQueen());
		members.add(kingdom.getPrince());
		members.add(kingdom.getPrincess());
		members.add(kingdom.getDuke());
		members.add(kingdom.getDuchess());
		members.addAll(kingdom.getEarls());
		for (Fief fief : kingdom.getFiefs()) {
			members.addAll(getFiefMembers(fief));
		}
		members.addAll(getArmyMembers(kingdom.getArmy()));
		members.addAll(getChurchMembers(kingdom.getChurch()));
		return members;
	}
	
	public static List<UUID> getFiefMembers(Fief fief) {
		List<UUID> members = new ArrayList<UUID>();
		members.add(fief.getBaron());
		members.add(fief.getBaroness());
		members.addAll(fief.getPeasents());
		members.addAll(fief.getSerfs());
		members.addAll(getArmyMembers(fief.getArmy()));
		members.addAll(getChurchMembers(fief.getChurch()));
		return members;
	}
	
	public static List<UUID> getArmyMembers(Army army) {
		List<UUID> members = new ArrayList<UUID>();
		members.add(army.getKnight());
		members.add(army.getDame());
		members.addAll(army.getSoldiers());
		return members;
	}
	
	public static List<UUID> getChurchMembers(Church church) {
		List<UUID> members = new ArrayList<UUID>();
		members.add(church.getPope());
		members.addAll(church.getAbbots());
		return members;
	}
}
