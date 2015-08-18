package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Configuration;

public class Flag {

	private DyeColor color;
	private List<Pattern> patterns;
	
	private ConfigurationSection dataPath;
	
	public Flag(ConfigurationSection section) {
		Flag flag = loadFlag(section);
		color = flag.color;
		patterns = flag.patterns;
		dataPath = section;
	}
	
	public Flag(DyeColor color, List<Pattern> patterns) {
		this.color = color;
		this.patterns = patterns;
	}
	
	public static Flag loadFlag(ConfigurationSection section) {
		DyeColor color;
		if (section.getString("color") == null) {
			color = Configuration.instance.defaultFlag.color;
		}
		else {
			color = DyeColor.valueOf(section.getString("color"));
			if (color == null) {
				color = Configuration.instance.defaultFlag.color;
			}
		}
		
		ConfigurationSection patternSection = section.getConfigurationSection("patterns");
		if (patternSection == null) {
			return new Flag(color, Configuration.instance.defaultFlag.patterns);
		}
		else {
			List<Pattern> patterns = new ArrayList<Pattern>();
			for (String p : patternSection.getKeys(false)) {
				DyeColor patternColor = DyeColor.valueOf(section.getString("patterns." + p + ".color"));
				if (patternColor == null) {
					patternColor = DyeColor.WHITE;
				}
				PatternType type = PatternType.valueOf(section.getString("patterns." + p + ".pattern"));
				if (type == null) {
					type = PatternType.BASE;
				}
				patterns.add(new Pattern(patternColor, type));
			}
			return new Flag(color, patterns);
		}
	}
	
	public void saveFlag() {
		dataPath.set("color", this.color.toString());
		dataPath.set("patterns", null);
		for (int i = 0;i < this.patterns.size();i++) {
			dataPath.set("patterns." + i + ".pattern", this.patterns.get(i).getPattern().toString());
			dataPath.set("patterns." + i + ".color", this.patterns.get(i).getColor().toString());
		}
	}
	

	public static List<StructureBlock> flagStructure = new ArrayList<StructureBlock>();
	
	public static void buildStructureBlockList(ConfigurationSection flagConfig) {
		for (String key : flagConfig.getKeys(false)) {
			ConfigurationSection blockSection = flagConfig.getConfigurationSection(key);
			flagStructure.add(new StructureBlock(blockSection.getInt("offsetX"), 
					blockSection.getInt("offsetY"),
					blockSection.getInt("offsetZ"), 
					blockSection.getString("material")));
		}
	}
	
	public static List<BlockState> buildStructure(Location origin, Kingdom kingdom) {
		List<BlockState> blockStates = new ArrayList<BlockState>();
		for (StructureBlock block : flagStructure) {
			Location track = origin.add(block.offsetX, block.offsetY, block.offsetZ);
			blockStates.add(track.getBlock().getState());
			track.getBlock().setType(block.material);
			if (block.material == Material.BANNER) {
				BlockState state = track.getBlock().getState();
				Banner meta = (Banner)state.getData();
				meta.setBaseColor(kingdom.getFlag().color);
				meta.setPatterns(kingdom.getFlag().patterns);
				meta.update(true, true);
			}
		}
		return blockStates;
	}
	
	public static class StructureBlock {
		int offsetX = 0;
		int offsetY = 0;
		int offsetZ = 0;
		
		Material material = Material.AIR;
		
		public StructureBlock (int x, int y, int z, String material) {
			this.offsetX = x;
			this.offsetY = y;
			this.offsetZ = z;
			
			Material testMaterial = Material.valueOf(material);
			if (testMaterial == null) {
				testMaterial = Material.AIR;
			}
			this.material = testMaterial;
		}
	}
}
