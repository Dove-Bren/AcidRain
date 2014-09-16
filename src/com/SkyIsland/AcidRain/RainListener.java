package com.SkyIsland.AcidRain;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.SkyIsland.AcidRain.Timer.TimerType;
import com.SkyIsland.AcidRain.Events.CheckRainEvent;
import com.SkyIsland.AcidRain.Events.StartRainEvent;

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
		this.timer = null;		
	}
	
	@EventHandler
	public void rainEvent(CheckRainEvent event) {
		PotionEffect frostbite = new PotionEffect(PotionEffectType.POISON, 100, 1); //poison 1 for 5 seconds
		
		for (String worldName : worlds) {
			World world = Bukkit.getWorld(worldName);
			for (Player player : world.getPlayers()) {
				if (player.getWorld().hasStorm())
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
	
	@EventHandler
	public void rainChange(final WeatherChangeEvent event) {

		//before we do anything, we have to filter this out if it was US that triggered this via our timer.
		//to do this, we just check if Timer is null. If not, continue. If so, set to the new timer and return
		if (timer != null) {
			if (timer.getType().compareTo(TimerType.startRain) == 0) {
				timer.stop();
				timer = new Timer(this.plugin, TimerType.checkRain, fireEvent, 10, 10); //runs every 2 seconds;
				return;
			}
			if (timer.getType().compareTo(TimerType.checkRain) == 0) {
				//turning off the rain
				timer.stop();
				timer = null;
			}
		}
		
		
		if (event.toWeatherState()) {
			//it just started snowing. Or, at least, it tried!!! MWAHAHAHAHAHA
			event.setCancelled(true);
			
			
			//We define a new runnable each time so we can include information about the world
			BukkitRunnable delayedWeather = new BukkitRunnable(){
				@Override
				public void run() {
					Bukkit.getServer().getPluginManager().callEvent(new StartRainEvent(event.getWorld()));
				}
			};
			
			if (timer != null) {
				System.out.println("Timer is not null!");
			}
			
			this.timer = new Timer(this.plugin, TimerType.startRain, delayedWeather, 60, 0);
			event.getWorld().setThundering(true);
			//We cancel the weather and set it to start 60 ticks from now. Instead, we start thundering to warn players.			
		
			//we also want to make it sound like lightnight just happened
			for (Player player : event.getWorld().getPlayers()) {
				player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
			}
		}
		else {
			//turning the weather off
			//see if we have a eventFire timer going
			System.out.println("Turning off weather. Stopping timer.");
			if (timer != null) {
				timer.stop();
				System.out.println("Asked timer to stop.");
				timer = null;
				//turn off our check timer so it doesn't keep checking.
			}
		}
	}

	@EventHandler
	public void rainStart(StartRainEvent event) {
		event.getWorld().setStorm(true); //this calls the previous event. It's there that we set out new timer
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
