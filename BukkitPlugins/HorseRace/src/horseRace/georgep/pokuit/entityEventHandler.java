package horseRace.georgep.pokuit;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class entityEventHandler implements Listener {
	
	@SuppressWarnings("unused")
	private Plugin plugin;
	
	public entityEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(e.getEntityType() == EntityType.HORSE) {
			Horse h = (Horse) e.getEntity();
			try {
				if(h.getPassenger() instanceof Player &&
						Main.f.playerIsInArena(((Player)h.getPassenger()).getName())) {
					e.setCancelled(true);
				}
			} catch(NullPointerException ex) {};
		}
	}
}
