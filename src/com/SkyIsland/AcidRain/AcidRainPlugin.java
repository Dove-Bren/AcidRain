package com.SkyIsland.AcidRain;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.api.MultiversePlugin;

public class AcidRainPlugin extends JavaPlugin {
	
	private YamlConfiguration config;
	private static final double version = 0.10;
	private RainListener rainListener;
	private final String configFilename = "config.yml";
	private MultiversePlugin mvplugin;
	
	public void onLoad() {
		checkConfig(new File(getDataFolder(), configFilename));
	}
	
	public void onEnable() {
		
		if (mvplugin == null) {
			System.out.println("error finding multiverse!");
		}
		
		config = load(new File(getDataFolder(), configFilename));
		
		System.out.println("Worlds: " + config.getString("worlds", "Wilderness"));
		
		rainListener = new RainListener(this, config.getString("worlds", "Wilderness"));
		
		getServer().getPluginManager().registerEvents(rainListener, this);
	}
	
	private void checkConfig(File configFile) {
		//First, check if the file exists. If it does, we don't need to do anything for now.
		if (configFile.exists()) {
			return;
		}
				
		//Now we set up the default config that we're going to create.
		YamlConfiguration defaultConfig = new YamlConfiguration();
		defaultConfig.set("version", version);
		defaultConfig.set("worlds", "Wilderness");
		
		
		//finally, we save this out to file.
		try {
			defaultConfig.save(configFile);
		} catch (IOException e) {
			getLogger().info("Unable to save config file!");
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("acidstate")) {
			sender.sendMessage("state: " + rainListener.getState());
			return true;
		}
		return false;
	}
	
	private YamlConfiguration load(File configFile) {
		YamlConfiguration config = new YamlConfiguration();
		
		if (!configFile.exists()) {
			checkConfig(configFile);
		}
		
		try {
			config.load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		//Before we pass it back, we do a simple version check to make sure everything is okay.
		if (config.getDouble("version") != version) {
			configFile.delete(); //delete old file
			config = load(configFile); //call this function again.			
		}
		
		return config;
	}
	
	
}
