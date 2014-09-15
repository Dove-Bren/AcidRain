package com.SkyIsland.AcidRain;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
		PotionEffect frostbite = new PotionEffect(PotionEffectType.POISON, 100, 1); //poison 1 for 5 seconds
		
		for (String worldName : worlds) {
			World world = Bukkit.getWorld(worldName);
			for (Player player : world.getPlayers()) {
				if (player.getLocation().getBlock().getLightFromSky() == 15) {
					//outside
					player.addPotionEffect(frostbite);
				}
			}
				
		}
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
