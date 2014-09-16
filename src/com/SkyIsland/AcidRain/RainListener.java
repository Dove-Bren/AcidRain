package com.SkyIsland.AcidRain;


import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.SkyIsland.AcidRain.Events.CheckRainEvent;
import com.SkyIsland.AcidRain.Events.StartRainEvent;
import com.SkyIsland.AcidRain.Events.ToggleRainEvent;

public class RainListener implements Listener {
	
	private AcidRainPlugin plugin;
	private World world;
	
	/**
	 * Describes the state of the rain. 
	 * Possible states include:
	 * <p><ul>
	 * <li>0 : Not raining.</li>
	 * <li>1 : About to rain. Thundering.</li>
	 * <li>2 : Raining. </li>
	 * </ul></p>
	 */
	private byte state;
	private Random rand;
	private static BukkitRunnable fireEvent = new BukkitRunnable(){
	

		@Override
		public void run() {
			Bukkit.getServer().getPluginManager().callEvent(new CheckRainEvent());
		}
		
	};
	private static BukkitRunnable delayedWeather = new BukkitRunnable(){
		@Override
		public void run() {
			Bukkit.getServer().getPluginManager().callEvent(new StartRainEvent());
		}
	};
	private static BukkitRunnable toggleDownfall = new BukkitRunnable() {
		@Override
		public void run() {
			Bukkit.getServer().getPluginManager().callEvent(new ToggleRainEvent());
		}
	};
	
	
	public RainListener(AcidRainPlugin plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		rand = new Random();
		world.setStorm(false);
		
		Timer timer = new Timer(toggleDownfall, rand.nextInt(2) * 300); //timer for 1 or 2 seconds that starts the rain
		timer.start();
		
		if (this.world == null) {
			plugin.getLogger().info("Failed to load world correct! Make sure it exists: \"" + this.world.getName() + "\"");
			throw new NullPointerException();
		}
	}

	
	
	
	
	
	
	@EventHandler
	public void rainEvent(CheckRainEvent event) {
		if (state != 2) {
			return;
			//we only want this to happen if it's raining
		}
		PotionEffect frostbite = new PotionEffect(PotionEffectType.POISON, 100, 1); //poison 1 for 5 seconds
		for (Player player : world.getPlayers()) {
			if (player.getWorld().hasStorm())
			if (player.getLocation().getBlock().getLightFromSky() == 15) {
				//outside
				player.addPotionEffect(frostbite);
			}
		}
		
		//reset the timer to wait
		Timer timer = new Timer(fireEvent, 8);
		timer.start();
				
	}
	
	@EventHandler
	public void rainChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			//starting to rain
			
			if (state == 1) {
				//it's already been waiting. Now we just actually make it rain.
				state = 2;
				Timer timer = new Timer(fireEvent, 0);
				timer.start();
				
				//We also want a timer to turn the rain off in a certain amount of time
				timer = new Timer(toggleDownfall, rand.nextInt(4) * 300); //set the timer to 1-4 seconds
				timer.start();
				return;
			}
			
			//state has to be 0. We set state to 1, cancel event, and create another thread to sleep and then cause stuff to happen
			
			//Play a scary thunder sound for all players
			for (Player player : world.getPlayers()) {
				player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
			}
			
			Timer timer = new Timer(delayedWeather,60);
			timer.start(); //sets a timer that will go off in 60 seconds and perform delayedWeather
			
			state = 1;			
			event.setCancelled(true);			
		}
		else {
			//weather is turning off.
			state = 0;
			Timer timer = new Timer(toggleDownfall, rand.nextInt(2) * 300);
			timer.start(); //starts the timer is 1 or 2 seconds
		}
	}
	

	@EventHandler
	public void rainStart(StartRainEvent event) {
		//We call the previous event again. This time, state will be 1, meaning it's thundering.
		world.setStorm(true);
	}
	
	@EventHandler
	public void toggleRain(ToggleRainEvent event) {
		if (state == 3) {
			//it's raining and we want to stop that.
			world.setStorm(false);
			return;
		}
		else {
			//it's not currently raining. We want to change that.
			world.setStorm(true);
		}
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public AcidRainPlugin getPlugin() {
		return plugin;
	}
	
	
}
