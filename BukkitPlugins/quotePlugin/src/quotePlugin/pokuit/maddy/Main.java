package quotePlugin.pokuit.maddy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public void onEnable(){
	
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("quote") && args.length >= 1) {
			Player player = (Player)sender;
			String str = "";
			for(String m:args) {
				str += m+" ";
			}
			Bukkit.broadcastMessage(ChatColor.WHITE+"\" "+ChatColor.GRAY+str+ChatColor.WHITE+"\" - "+player.getName());
			return true;
		}
		return false;
	}
	public void onDisable() {

	}

}
