package enhancedteleport.pokuit.georgep.v.one.zero.zero;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class teleportEventHandler implements Listener {

	public teleportEventHandler() {};
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
		if((e.getPlayer().hasPermission("enhancedTeleport.use") ||
				e.getPlayer().isOp()) &&
				Main.teleportMode == teleportMode.FUTURISTIC &&
				Main.hasRelevantTeleportCause(e.getCause())) {
			Main.ta.futuristicTeleportEffect(e.getPlayer(), e.getTo());
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onServerListPingEvent(ServerListPingEvent e) {
		e.setMotd("                                                             6789123456789");
	}
}
