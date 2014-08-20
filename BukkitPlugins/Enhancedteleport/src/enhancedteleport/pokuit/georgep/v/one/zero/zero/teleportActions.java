package enhancedteleport.pokuit.georgep.v.one.zero.zero;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class teleportActions {
	
	Plugin plugin;
	BukkitTask timer;
	
	public teleportActions(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public  void futuristicTeleportEffect(final Player p, final Location tp) {
		final Location pl = p.getLocation();
		timer = plugin.getServer().getScheduler().runTaskTimer(plugin, new BukkitRunnable() {
			
			int yAbove = 0;
			int timesRun = 0;
			
			
			@Override
			public void run() {
				
				plugin.getLogger().info("meme");
				
				Location plt = pl;
				plt.setY(pl.getY()+yAbove);
				p.playSound(pl, Sound.ZOMBIE_UNFECT, 1, 2);
				
				yAbove++;
				timesRun++;
				
				if(timesRun >= 10) {
					plugin.getServer().getScheduler().cancelTask(timer.getTaskId());
				}
				
			}
			
		}, 0, 20);
	}
}
