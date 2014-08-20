package WallsPlugin.mcLoc.pokuit.georgep;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

public class entityEventHandler implements Listener {
	
	Plugin plugin;
	
	public entityEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		if(event.getEntity() instanceof EnderDragon){
			event.blockList().clear();
			event.setCancelled(true);
			return;
		}
	}
}
