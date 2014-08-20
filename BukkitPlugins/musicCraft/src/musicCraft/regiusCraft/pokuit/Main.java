package musicCraft.regiusCraft.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

	private static List<Float> Pitch = new ArrayList<Float>();
	private static HashMap<String, Integer> timeThroughPeice = new HashMap<String, Integer>();
	
	
	public void onEnable(){
		getLogger().info("Music Craft Has been Activated!");
		Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
			public void run() { 
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(timeThroughPeice.containsKey(player.getName())) {
						if(timeThroughPeice.get(player.getName()) > 0) {
							Float pitcher = Pitch.get(Pitch.size()-(timeThroughPeice.get(player.getName())));
							player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, pitcher);
							timeThroughPeice.put(player.getName(),timeThroughPeice.get(player.getName())-1);
						}
					}
				}
			}
		}, 0,30);
 	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("listen")) {
				Player s = (Player)sender;
				String test = "0.95^ 1v 1.05^";
				String[] parts = test.split(" ");
				for(int i = 0, l = parts.length;i<l;i++) {
					getLogger().info(parts[i].getChars(parts[i].length(),parts[i].length(), null, l));
				//	float f = Float.parseFloat(parts[i]);
				//	Pitch.add(f);
				}
			//	timeThroughPeice.put(s.getName(), Pitch.size());
				/*s.playSound(s.getLocation(), Sound.NOTE_PIANO, 1, 0);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
				}
				s.playSound(s.getLocation(), Sound.NOTE_PIANO, 1, 0);
		        */return true;
			}
		}
		return false;
	}
	
	public void onDisable(){
		getLogger().info("Music Craft Has been Deactivated!");
	}
}
