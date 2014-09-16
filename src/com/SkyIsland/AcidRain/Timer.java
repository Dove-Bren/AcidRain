package com.SkyIsland.AcidRain;

import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
	
	private BukkitRunnable task;
	private AcidRainPlugin plugin;
	private long delay, period;
	public TimerType type;

	public enum TimerType {
		startRain,
		checkRain;
	};
	
	public Timer(AcidRainPlugin plugin, TimerType type, BukkitRunnable task, long delay, long period) {
		this.task = task;
		this.plugin = plugin;
		this.delay = delay;
		this.period = period;
		this.type = type;
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
		//We want to differentiate between tasks to be run once and those to be repeated. To do this, we 
		//check if period == 0. If it does, we only do it once.
		if (period != 0) {
			this.task.runTaskTimer(plugin, delay, period);
		}
		else {
			this.task.runTaskLater(plugin, delay);
		}
	}
	
	public TimerType getType() {
		return type;
	}
}
