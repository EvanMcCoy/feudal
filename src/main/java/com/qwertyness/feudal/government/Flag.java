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
import com.qwertyness.feudal.Feudal;

public class Flag {
	public static Flag fakeInstance = new Flag(null);

	private DyeColor color;
	private List<Pattern> patterns;
	
	public Flag() {
		this.color = Configuration.instance.defaultFlag.color;
		this.patterns = Configuration.instance.defaultFlag.patterns;
	}
	
	public Flag(ConfigurationSection section) {
		if (section == null) {
			section = Feudal.getInstance().getConfig().getConfigurationSection("flags.defaultFlag");
			if (Configuration.instance.defaultFlag != null) {
				this.color = Configuration.instance.defaultFlag.color;
				this.patterns = Configuration.instance.defaultFlag.patterns;
			}
			return;
		}
		
		DyeColor testColor = DyeColor.valueOf(section.getString("color"));
		if (testColor == null) {
			testColor = DyeColor.WHITE;
		}
		this.color = testColor;
		this.patterns = new ArrayList<Pattern>();
		for (String key : section.getConfigurationSection("patterns").getKeys(false)) {
			DyeColor color = DyeColor.valueOf(section.getString("patterns." + key + ".color"));
			if (color == null) {
				color = DyeColor.WHITE;
			}
			PatternType type = PatternType.valueOf(section.getString("patterns." + key + ".pattern"));
			if (type == null) {
				type = PatternType.BASE;
			}
			this.patterns.add(new Pattern(color, type));
		}
	}
	
	public Flag(DyeColor color, List<Pattern> patterns, Kingdom kingdom) {
		this.color = color;
		this.patterns = patterns;
		kingdom.setFlag(this);
		setFlag(kingdom.getDataPath().createSection("flag"));
	}
	
	public void setFlag(ConfigurationSection section) {
		section.set("color", this.color.toString());
		section.set("patterns", null);
		for (int i = 0;i < this.patterns.size();i++) {
			section.set("patterns." + i + ".pattern", this.patterns.get(i).getPattern().toString());
			section.set("patterns." + i + ".color", this.patterns.get(i).getColor().toString());
		}
	}
	

	public static List<StructureBlock> flagStructure = new ArrayList<StructureBlock>();
	
	public static void buildStructureBlockList(ConfigurationSection flagConfig) {
		for (String key : flagConfig.getKeys(false)) {
			ConfigurationSection blockSection = flagConfig.getConfigurationSection(key);
			flagStructure.add(fakeInstance.new StructureBlock(blockSection.getInt("offsetX"), 
					blockSection.getInt("offsetY"),
					blockSection.getInt("offsetZ"), 
					blockSection.getString("material")));
		}
	}
	
	public static void buildStructure(Location origin, Kingdom kingdom) {
		for (StructureBlock block : flagStructure) {
			Location track = origin.add(block.offsetX, block.offsetY, block.offsetZ);
			track.getBlock().setType(block.material);
			if (block.material == Material.BANNER) {
				BlockState state = track.getBlock().getState();
				Banner meta = (Banner)state.getData();
				meta.setBaseColor(kingdom.getFlag().color);
				meta.setPatterns(kingdom.getFlag().patterns);
				meta.update(true, true);
			}
		}
	}
	
	public class StructureBlock {
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
