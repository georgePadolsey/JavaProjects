package horseRace.georgep.pokuit;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class playerEventHandler implements Listener {
	
	@SuppressWarnings("unused")
	private Plugin plugin;
	
	public playerEventHandler(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Main.f.playerIsInArena(p.getName(),false)) {
			if(Main.f.signInColumnIs(e.getTo(),"[stableAction]","start")) {
				Main.f.debug("BlockTypeIsLimeWool");
				if(p.getVehicle() != null &&
						p.getVehicle() instanceof Horse) {
					Main.f.debug("TESTING");
					Horse horse = (Horse)p.getVehicle();
					Main.f.debug(horse.eject()+"");
					Main.f.debug(Main.f.returnLocationAsString(Main.f.getArenaPlayerIsIn(p.getName()).getStartSpawn()));
					horse.teleport(Main.f.getArenaPlayerIsIn(p.getName()).getStartSpawn());
					p.teleport(Main.f.getArenaPlayerIsIn(p.getName()).getStartSpawn());
					horse.setPassenger(p);
				}
			}
		}	else {
			if(Main.f.playerIsInArena(p.getName(),true)) {
				if(Main.f.signInColumnIs(e.getTo(),"[stableAction]","end")) {
					Main.f.debug("BlockTypeIsRedWool");
					arena a = Main.f.getArenaPlayerIsIn(p.getName());
					if(!a.getIfPlayerIsWinner(p.getName())) {
						a.addWinner(p.getName());
					}
				}
			}
			if(Main.f.playerIsInArena(p.getName(),true)) {
				if(Main.f.signInColumnIs(e.getTo(),"[stableAction]","boost")) {
					Main.f.debug("BlockTypeIsYellowWool");
					if(p.getVehicle() != null &&
							p.getVehicle() instanceof Horse) {
						Horse horse = (Horse)p.getVehicle();
						horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40,10));
					}
				}
			}
			if(Main.f.playerIsInArena(p.getName(),true)) {
				if(Main.f.signInColumnIs(e.getTo(),"[stableAction]","jump")) {
					Main.f.debug("BlockTypeIsBoostSign");
					if(p.getVehicle() != null &&
							p.getVehicle() instanceof Horse) {
						Horse horse = (Horse)p.getVehicle();
						
						horse.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 10));
						
					}
				}
			}
		}
	}
}
