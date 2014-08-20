package horseRace.georgep.pokuit;

import horseRace.georgep.pokuit.storedSign.storedSignActivateHandler;
import horseRace.georgep.pokuit.storedSign.storedSignActivateEvent;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

public class signEventHandler implements Listener {
	
	Plugin plugin;
	
	public signEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	@EventHandler
	void onSignChangeEvent(SignChangeEvent e) {
		String[] lines =e.getLines();
		if(lines[0].equalsIgnoreCase("[Stable]")) {			
			
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
				final arena fa = a;
				Main.storedSigns.add(new storedSign(e.getBlock().getLocation(), SignType.SIGN, a.getName(), plugin, new storedSignActivateHandler() {
					@Override
					public void onstoredSignActivateEvent(storedSignActivateEvent event) {
						
						Main.f.debug("BEAT THIS");
						Main.f.addPlayerToArena(fa, event.getPlayer().getName());
						
					}
				},false));
				
			}
		}
	}
}
