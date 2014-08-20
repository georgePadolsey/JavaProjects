package WallsPlugin.mcLoc.pokuit.georgep;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import WallsPlugin.mcLoc.pokuit.georgep.storedSign.storedSignActivateEvent;
import WallsPlugin.mcLoc.pokuit.georgep.storedSign.storedSignActivateHandler;

public class signEventHandler implements Listener {
	
	Plugin plugin;
	
	public signEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	@EventHandler
	void onSignChangeEvent(SignChangeEvent e) {
		String[] lines =e.getLines();
		if(lines[0].equalsIgnoreCase("[Walls]")) {			
			
			if(lines[2].equalsIgnoreCase("pressure")) {
				if(lines[3].equalsIgnoreCase("wood")) {
					e.getBlock().setType(Material.WOOD_PLATE);
				} else {
					e.getBlock().setType(Material.STONE_PLATE);
				}
			} else {
				arena a;
				try {
					
					a = Main.f.getArenaFromString(lines[1]);
					
				}catch(NullPointerException ex) {
					
					return;
					
				}
					
				Main.storeSigns.add(new storedSign(e.getBlock().getLocation(), SignType.SIGN, a.getName(), plugin, new storedSignActivateHandler() {
					@Override
					public void onstoredSignActivateEvent(
							storedSignActivateEvent event) {
						Main.f.getArenaFromString(event.getArenaName()).openTeamChoice(event.getPlayer().getName());
					}
				},false));
				
			}
		}
	}
}
