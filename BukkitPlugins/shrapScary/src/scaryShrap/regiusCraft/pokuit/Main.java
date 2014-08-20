package scaryShrap.regiusCraft.pokuit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public final class Main extends JavaPlugin implements Listener {
	
	public static HashMap<String, Boolean> peopleBeingScared = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> peopleBeingScaredCounter = new HashMap<String, Integer>();
	
	public void onEnable(){
		getLogger().info("Scary Shrap Has been Activated!");
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
		    public void run() {
		    	for(Player player: getServer().getOnlinePlayers()) {
		    		if(peopleBeingScaredCounter.containsKey(player.getName())) {
		    			if(peopleBeingScaredCounter.get(player.getName()) > 0) {
		    				peopleBeingScaredCounter.put(player.getName(), peopleBeingScaredCounter.get(player.getName()) - 1);
		    			} else {
		    				if(peopleBeingScaredCounter.get(player.getName()) == 0) {
					    		peopleBeingScared.put(player.getName(), false);
					    	}
		    			}
		    		}
		    	}
		    }
		}, 0, 20); 
 	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			Player s = (Player)sender;
		if(cmd.getName().equalsIgnoreCase("scary") && (args.length == 1 || args.length == 2)) {
			Player r = getServer().getPlayer(args[0]);
				if (r == null) {
				    s.sendMessage(ChatColor.RED + "Player doesn't exist!");
				    return true;
				}
				r.playSound(r.getLocation(), Sound.ENDERMAN_STARE, 1, 1);
				r.playSound(r.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
				r.playEffect(s.getLocation(), Effect.RECORD_PLAY, 2266);
				r.playEffect(s.getLocation(), Effect.GHAST_SHRIEK, 0);		 
				peopleBeingScared.put(r.getName(),true);	
				if(args.length == 2) {
					try {
						   Integer number = Integer.parseInt(args[1]);
						   peopleBeingScaredCounter.put(r.getName(),number);
						   r.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, number*20, 0));
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Error Incorrect Command Usage. "+args[1]+" Is Not a Valid Number!");
						return true;
					}
				} else {
					peopleBeingScaredCounter.put(r.getName(),60);
					 r.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60*20, 0));
				}
				s.sendMessage(ChatColor.GOLD+"Successful Scare");
				return true;
			} else {
				if(cmd.getName().equalsIgnoreCase("stopscary") && args.length == 1) {
					Player r = getServer().getPlayer(args[0]);
					if (r == null) {
					    s.sendMessage(ChatColor.RED + "Player doesn't exist!");
					    return true;
					}
					if(peopleBeingScared.containsKey(r.getName())) {
						if(peopleBeingScared.get(r.getPlayer().getName())) {
							peopleBeingScared.put(r.getName(),false);
							peopleBeingScaredCounter.put(r.getName(),0);
							s.sendMessage(ChatColor.GOLD + "Sucessfully Cancelled Scare");
							for(PotionEffect effect : r.getActivePotionEffects())
							{
							    r.removePotionEffect(effect.getType());
							}
							return true;
						}
					} else {
						s.sendMessage(ChatColor.RED + "That person Is not being scared!");
						return true;
					}
				}
			}
		}
		return false;
	}
	public void onDisable() {
		getLogger().info("Scary Shrap Has been Deactivated!");
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if(peopleBeingScared.containsKey(event.getPlayer().getName())) {
			if(peopleBeingScared.get(event.getPlayer().getName())) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		if(event.getItem().isSimilar(new ItemStack(Material.MILK_BUCKET,1))) {
			if(peopleBeingScared.containsKey(event.getPlayer().getName())) {
				if(peopleBeingScared.get(event.getPlayer().getName())) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(peopleBeingScared.containsKey(event.getWhoClicked().getName())) {
			if(peopleBeingScared.get(event.getWhoClicked().getName())) {
				event.setCancelled(true);
			}
		}
	}
}
