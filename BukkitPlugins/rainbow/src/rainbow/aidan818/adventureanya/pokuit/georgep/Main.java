package rainbow.aidan818.adventureanya.pokuit.georgep;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Main extends JavaPlugin {
	
	WorldEditPlugin worldEdit;
	public static ArrayList<disco> discos= new ArrayList<disco>();
	
	public void onEnable() {
		this.worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		this.saveDefaultConfig();
		@SuppressWarnings("unused")
		BukkitTask task=new masterTimer().runTaskTimer(this, 0, 1);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			Player player = (Player)sender;
			Selection selection = worldEdit.getSelection(player);
			 if(cmd.getName().equalsIgnoreCase("rainbow")) {
				if (selection != null) {
				    Location min = selection.getMinimumPoint();
				    Location max = selection.getMaximumPoint();
				    for(int x = (int) min.getX(); x <= max.getX(); x++) { // Loop 1 for the X
				        for(int y = 0; y <= max.getY(); y++) {// Loop 2 for the Y
				            for(int z = (int) min.getZ(); z <= max.getZ();z++) {// Loop 3 for the Z
				            	Block b = new Location(min.getWorld(),x,y,z).getBlock();
				            	if(b.getType() == Material.WOOL) {
				            		discos.add(new disco(b.getLocation()));
				            	}
				            }
				        }
				    }
				} else {
					player.sendMessage(ChatColor.GOLD+"[DiscoTech] "+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
				}
				return true;
			} 
		}
		return false;
	
	}
	public void onDisable() {
	}
	
}
