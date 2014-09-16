package com.SkyIsland.AcidRain.Events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartRainEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private World world;
	
	public StartRainEvent(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
		
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
