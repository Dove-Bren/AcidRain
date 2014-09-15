package com.SkyIsland.AcidRain;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckRainEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
		
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
