package logOfDissapearAser.georgep.pokuit;

import logOfDissapearAser.georgep.pokuit.listeners.PlayerEventHandlers;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable(){
		new PlayerEventHandlers(this);
	}
	
	public void onDisable(){}
	
	
}
