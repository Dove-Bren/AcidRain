package com.SkyIsland.AcidRain;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RainListener implements Listener {
	
	private AcidRainPlugin plugin;
	private List<String> worlds;
	private Timer timer;
	private static BukkitRunnable fireEvent = new BukkitRunnable(){

		@Override
		public void run() {
			Bukkit.getServer().getPluginManager().callEvent(new CheckRainEvent());
		}
		
	};
	
	public RainListener(AcidRainPlugin plugin, List<String> worlds) {
		this.plugin = plugin;
		this.worlds = worlds;
		this.timer = new Timer(plugin, fireEvent, 40, 40); //runs every 2 seconds
	}
	
	@EventHandler
	public void rainEvent(CheckRainEvent event) {
		
	}
	
	@EventHandler
	public void emptyWorld(WorldUnloadEvent event) {
		timer.stop();
	}
	
	@EventHandler
	public void fillWorld(WorldLoadEvent event) {
		timer.start();		
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public void setWorlds(List<String> worlds) {
		this.worlds = worlds;
	}

	public AcidRainPlugin getPlugin() {
		return plugin;
	}

	public Timer getTimer() {
		return timer;
	}
	
	
}
