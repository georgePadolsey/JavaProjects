package horseRace.georgep.pokuit;

import java.util.Arrays;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

public class vehicleEventHandler implements Listener {
	
	@SuppressWarnings("unused")
	private Plugin plugin;
	
	public vehicleEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVehicleEnterEvent(VehicleEnterEvent e) {
		if(e.getVehicle() instanceof Horse &&
				e.getEntered() instanceof Player) {
			Player p = (Player)e.getEntered();
			Horse h = (Horse)e.getVehicle();
			if(Main.playersCurrentlyChoosingAHorse.containsKey(p.getName())) {
				
				Main.f.debug(Main.playersCurrentlyChoosingAHorse.containsKey(p.getName())+" playerCurrentlying chosing a horse after if statement");
				
				Main.playerHorseInfo.put(p.getName(), new HorseInfo(h.getStyle(), h.getColor()));
				
				Main.f.debug("playerCurrentlychoosing a horse contains"+ p.getName()+" :::"+Main.playersCurrentlyChoosingAHorse.containsKey(p.getName()));
				
				Main.f.debug("VEHICLE ENTER EVENT TRIGGERED AND PLAYERS CURRENTLY CHOOSING A HORSE CONTAINS PLAYER");
				Main.f.showPlayerToArrayOfPlayers(p.getName(), Arrays.copyOf(Main.playersCurrentlyChoosingAHorse.keySet().toArray(),Main.playersCurrentlyChoosingAHorse.keySet().toArray().length, String[].class));
				
				Main.horsesInStables.remove(h.getUniqueId());
				
				e.setCancelled(true);
				
				h.remove();
				
				Main.f.spawnRandomHorsesInStable(1);
				
				String aName = Main.playersCurrentlyChoosingAHorse.get(p.getName());
				
				Main.playersCurrentlyChoosingAHorse.remove(p.getName());
				
				Main.f.getArenaFromString(aName).addPlayer(p.getName());
				
				
			}
		}
	}
	@EventHandler
	public void onVehicleExitEvent(VehicleExitEvent e) {
		if(e.getVehicle() instanceof Horse &&
				e.getExited() instanceof Player &&
				!Main.f.signInColumnIs(e.getExited().getLocation(),"[stableAction]","start")) {
			Player p = (Player)e.getExited();
			if(Main.f.playerIsInArena(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
}
