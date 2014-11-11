package com.SkyIsland.AcidRain;

import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends Thread {
	
	private BukkitRunnable task;
	private long delay;
	
	public Timer(BukkitRunnable task, long delay) {
		this.task = task;
		this.delay = delay;
	}
	
	@Override
	public void run() {
		try {
			sleep(delay * 200); //sleep delay ticks
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task.run();
	}
	
}
