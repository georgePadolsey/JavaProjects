package blockBeacons.georgep.pokuit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBeacons extends JavaPlugin implements Listener {
	
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent e) {
		if(e.getInventory().getType() == InventoryType.BEACON &&
				!(e.getPlayer().isOp() || e.getPlayer().hasPermission("blockBeacons.basic"))) {
			e.setCancelled(true);
		}
	}
	
}
