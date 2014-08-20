package enhancedteleport.pokuit.georgep.v.one.zero.zero;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static teleportMode teleportMode;
	public static teleportActions ta;
	
	
	@SuppressWarnings("static-access")
	public void onEnable() {

		teleportMode  = teleportMode.FUTURISTIC;
		
		ta = new teleportActions(this);
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		this.getServer().getPluginManager().registerEvents(new teleportEventHandler(),this);
		
		checkConfigValues();
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false;
	}
	
	public void onDisable() {
		
	}
	
	public void checkConfigValues() {
		
	}
	
	public static boolean hasRelevantTeleportCause(TeleportCause tc) {
		if(tc == TeleportCause.ENDER_PEARL ||
				tc == TeleportCause.END_PORTAL ||
				tc == TeleportCause.NETHER_PORTAL) {
			return false;
		} else {
			return true;
		}
	}
}
