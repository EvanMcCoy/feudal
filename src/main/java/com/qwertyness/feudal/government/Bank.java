package com.qwertyness.feudal.government;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class Bank {
	private double balance = 0;
	private HashMap<Material, Integer> inventory = new HashMap<Material, Integer>();
	
	private ConfigurationSection dataPath;
	
	public Bank(ConfigurationSection dataPath) {
		this.dataPath = dataPath;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void depositMoney(double deposit) {
		this.balance += deposit;
	}
	
	public boolean withdrawMoney(double withdrawl) {
		if (this.balance >= withdrawl) {
			this.balance -= withdrawl;
			return true;
		}
		return false;
	}
	
	public HashMap<Material, Integer> getInventory() {
		return this.inventory;
	}
	
	public void depositItem(Material material, int amount) {
		if (this.inventory.keySet().contains(material)) {
			this.inventory.put(material, this.inventory.get(material)+amount);
		}
		else {
			this.inventory.put(material, amount);
		}
	}
	
	public boolean withdrawItem(Material material, int amount) {
		if (this.inventory.keySet().contains(material)) {
			if (this.inventory.get(material) >= amount) {
				this.inventory.put(material, this.inventory.get(material)-amount);
				return true;
			}
		}
		return false;
	}
	
	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
}
