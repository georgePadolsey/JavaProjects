package logOfDissapearAser.georgep.pokuit.listeners;

import org.bukkit.Effect;
//import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerEventHandlers implements Listener {
	
	Plugin plugin;
	
	public PlayerEventHandlers(Plugin plugin) {
		this.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/*----- FOR THE FITSOLDIER | DeltaWars TODO @EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.EXPLODE, 1, 1);
	}*/
	
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation(), Effect.SMOKE, 4);
	}
}
