package com.SkyIsland.AcidRain;

import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
	
	private BukkitRunnable task;
	private AcidRainPlugin plugin;
	private long delay, period;
	
	public Timer(AcidRainPlugin plugin, BukkitRunnable task, long delay, long period) {
		this.task = task;
		start();
	}
	
	/**
	 * Tries to stop the timer. Returns true if it worked, and false if it fails.
	 * @return
	 */
	public boolean stop() {
		task.cancel();
		return true;
	}
	
	public void start() {
		this.task.runTaskTimer(plugin, delay, period);
	}
}
