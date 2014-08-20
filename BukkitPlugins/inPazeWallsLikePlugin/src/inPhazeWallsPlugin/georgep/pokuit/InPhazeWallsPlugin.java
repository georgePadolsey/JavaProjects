package inPhazeWallsPlugin.georgep.pokuit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import uapi.georgep.pokuit.UAPIPlugin;
import uapi.georgep.pokuit.UAPI.UAPI;

public class InPhazeWallsPlugin extends JavaPlugin {
	
	public static UAPI uapi = null;
	
	public void onEnable() {
		uapi = getUAPI();
	}
	
	public void onDisable() {
		
	}
	
	public UAPI getUAPI() {
		return ((UAPIPlugin)Bukkit.getPluginManager().getPlugin("UAPI")).getAPI(this);
	}
}
