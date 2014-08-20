package minecartsOffline.regiuscraft.georgep.pokuit;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
	}
	public void onDisable() {
		
	}
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(event.getCurrentItem().getType() == Material.DISPENSER && !event.getWhoClicked().hasPermission("dispensersOffline.place")) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.DISPENSER && !event.getPlayer().hasPermission("dispensersOffline.place")) {
			event.setCancelled(true);
		}
	}
}

