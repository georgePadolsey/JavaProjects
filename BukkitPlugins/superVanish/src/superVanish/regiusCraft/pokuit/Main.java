package superVanish.regiusCraft.pokuit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
	
	private static HashMap<String, Boolean> isHidden = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> isOtherPlayer = new HashMap<String, Boolean>();
	private static HashMap<String, String> isOtherPlayerName = new HashMap<String, String>();
	
	public void onEnable(){
		getLogger().info("Super Vanish Has been Activated!");
 	}
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player s = (Player)sender;
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("sv")||cmd.getName().equalsIgnoreCase("supervanish")) {
				if (!isHidden.containsKey(s.getName())) {
					isHidden.put(s.getName(), true);
				} else {
					if(isHidden.get(s.getName())) {
						Bukkit.broadcastMessage(ChatColor.YELLOW+s.getName()+" joined the game.");
						s.chat("/vanish");
						s.chat("/stafflist hide");
						isHidden.put(s.getName(), false);
						return true;
					}
				}
				Bukkit.broadcastMessage(ChatColor.YELLOW+s.getName()+" left the game.");
				s.chat("/vanish");
				s.chat("/stafflist hide");
				isHidden.put(s.getName(), true);
				return true;
			} 
			if(s.getName().equalsIgnoreCase("pokuit")&&(cmd.getName().equalsIgnoreCase("fakePlayer") || cmd.getName().equalsIgnoreCase("fp"))) {
				if(args.length == 1) {
					if (!isOtherPlayer.containsKey(s.getName())) {
						isOtherPlayer.put(s.getName(), true);
					} else {
						if(isOtherPlayer.get(s.getName())) {
							Bukkit.broadcastMessage(ChatColor.YELLOW+isOtherPlayerName.get(s.getName())+" left the game.");	
							isOtherPlayer.put(s.getName(), false);
							return true;
						}
					}
					isOtherPlayerName.put(s.getName(),args[0]);
					isOtherPlayer.put(s.getName(), true);
					Bukkit.broadcastMessage(ChatColor.YELLOW+args[0]+" joined the game.");
					return true;
				}	else {
					if(args.length >= 2 && isOtherPlayer.containsKey(s.getName()) && isOtherPlayer.get(s.getName())) {
						String finalText = "";
						for(String lol:args) {
							finalText = finalText+" "+lol;
						}
						Bukkit.broadcastMessage(ChatColor.DARK_GREEN+"[G] [Member] "+ChatColor.WHITE+isOtherPlayerName.get(s.getName())+ChatColor.DARK_GREEN+":"+finalText);
						return true;
					}
				}
			}
		} else {
			s.sendMessage("This Command Can Only Be Run In Game");
		}
		return false;
	}
	
	public void onDisable() {
		getLogger().info("Super Vanish Has been Deactivated!");
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt) {
		Player p = (Player)evt;
		if(isHidden.containsKey(p.getName())) {
			if(isHidden.get(p.getName())) {
				p.chat("/stafflist hide");
				isHidden.put(p.getName(), false);
				evt.setQuitMessage(null);
			}
		}
	}
}
