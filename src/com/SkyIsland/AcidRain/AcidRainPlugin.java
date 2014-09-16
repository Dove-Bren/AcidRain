package com.SkyIsland.AcidRain;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AcidRainPlugin extends JavaPlugin {
	
	private YamlConfiguration config;
	private static final double version = 0.02;
	private RainListener rainls;
	private final String configFilename = "config.yml";
	
	public void onLoad() {
		checkConfig(new File(getDataFolder(), configFilename));
	}
	
	public void onEnable() {
		config = load(new File(getDataFolder(), configFilename));
		rainls = new RainListener(this, config.getStringList("worlds"));
		
		getServer().getPluginManager().registerEvents(rainls, this);
	}
	
	private void checkConfig(File configFile) {
		//First, check if the file exists. If it does, we don't need to do anything for now.
		if (configFile.exists()) {
			return;
		}
				
		//Now we set up the default config that we're going to create.
		YamlConfiguration defaultConfig = new YamlConfiguration();
		LinkedList<String> worldList = new LinkedList<String>();
		worldList.add("Wilderness");
		defaultConfig.set("version", version);
		defaultConfig.set("worlds", worldList);
		
		
		//finally, we save this out to file.
		try {
			defaultConfig.save(configFile);
		} catch (IOException e) {
			getLogger().info("Unable to save config file!");
			e.printStackTrace();
		}
		
		
		
		
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
