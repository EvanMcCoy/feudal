package com.qwertyness.feudal.resource;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.qwertyness.feudal.Feudal;

/**
 * @param <T> the type of value to check against cache map.
 */
public abstract class DynamicCache<T> {
	private HashMap<T, Boolean> cacheHandler;
	private int cacheInterval;
	private BukkitTask loop;
	
	/**
	 * @param cacheInterval Interval at which to stage decaching operations, in Minecraft ticks
	 */
	public DynamicCache(int cacheInterval) {
		cacheHandler = new HashMap<T, Boolean>();
		this.cacheInterval = cacheInterval;
		loop();
	}
	
	/**
	 * Resets specified value to a cache state of true.
	 * @param cached The object to set as recently cached
	 */
	public void cached(T cached) {
		cacheHandler.put(cached, true);
	}
	
	/**
	 * Steps all elements down toward decache.  True > False, False > {@link #decache(T) decache}.
	 */
	public void stepCache() {
		for (T key : new ArrayList<T>(cacheHandler.keySet())) {
			if (cacheHandler.get(key) == true) {
				cacheHandler.put(key, false);
			}
			else {
				cacheHandler.remove(key);
				decache(key);
			}
		}
	}
	
	public abstract void decache(T object);
	
	public void loop() {
		loop = new BukkitRunnable() {
			public void run() {
				stepCache();
			}
		}.runTaskTimer(Feudal.getInstance(), 0, cacheInterval);
	}
	
	public void cancel() {
		loop.cancel();
	}
}
