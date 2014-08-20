package com.researching.georgep.www;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener {

	public static HashMap<Location,Boolean> registeredResearchTable = new HashMap<Location,Boolean>();
	public static HashMap<Location,Integer> registeredResearchTableCounter = new HashMap<Location,Integer>();
	private IconMenu WarpToHomeMenu;
	private IconMenu hub;
	
	public void onEnable(){
		getLogger().info("Super Vanish Has been Activated!");
		getServer().getPluginManager().registerEvents(this, this);
		this.WarpToHomeMenu=new IconMenu(ChatColor.BOLD+""+ChatColor.GOLD+ "Warp To Home", 63, new IconMenu.OptionClickEventHandler() {
			@Override	
			public void onOptionClick(IconMenu.OptionClickEvent event) {
					event.setWillClose(true);
	            }
	        }, this)
		.setOption(2,new ItemStack(Material.BOOK,1) , "Information", "When You Shift Right Click With This Block It Will set home")
		.setOption(3,new ItemStack(Material.BOOK,1) , "Information", "To get Back To your home you right click with it.")
		.setOption(4,new ItemStack(Material.BOOK,1) , "Information", "Then you wait 5 seconds till you fully pass through the dimensions")
		.setOption(21,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(22,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(23,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(30,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(31,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(32,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(39,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(40,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House")
		.setOption(41,new ItemStack(90,1) , "Warp To Home", "This Will Make You Warp To Your House");
		this.hub=new IconMenu(ChatColor.BOLD+""+ChatColor.GOLD+ "Research Table", 63, new IconMenu.OptionClickEventHandler() {
			@Override
	            public void onOptionClick(IconMenu.OptionClickEvent event) {	
				if(event.getPosition() == 0) {
					event.getPlayer().closeInventory();
					WarpToHomeMenu.open(event.getPlayer());
					
	            }
				event.setWillClose(false);
			
	            }
	        }, this);
 	}
	
	public void onDisable() {
		getLogger().info("Super Vanish Has been Deactivated!");
	}
	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		/*if(event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
			IconMenu hub=new IconMenu(ChatColor.BOLD+""+ChatColor.GOLD+ "Research Table", 90, new IconMenu.OptionClickEventHandler() {
	                @Override
	                public void onOptionClick(IconMenu.OptionClickEvent event) {
	                    event.setWillClose(true);
	                }
	            }, this);
			hub.setOption(0, new ItemStack(Material.BOOK,1), ChatColor.GOLD + "Warp to Home", ChatColor.WHITE + "Click For Further Description");
			hub.open(event.getPlayer());
			event.setCancelled(true);
		}*/
		if((Functions.itemInHandIsA(Material.FEATHER, event.getPlayer()) ||
				Functions.itemInHandIsA(Material.INK_SACK, event.getPlayer())) &&
				!Functions.tableHasAllThatIsNeeded(event.getClickedBlock().getLocation()) &&
				event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
			if(registeredResearchTableCounter.containsKey(event.getClickedBlock().getLocation())) {
				registeredResearchTableCounter.put(event.getClickedBlock().getLocation(), registeredResearchTableCounter.get(event.getClickedBlock().getLocation())+1);
			} else {
				registeredResearchTableCounter.put(event.getClickedBlock().getLocation(), 1);
			}
			event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.ENDER_SIGNAL, null);
			event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, null);
			event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
		} else {
			if(event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE &&Functions.tableHasAllThatIsNeeded(event.getClickedBlock().getLocation())) {
		        Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
				    public void run() {
				    	event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.ENDER_SIGNAL, null);
				    	event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, null);
				    }
		        }, 0, 20);
				
				hub.setOption(0, new ItemStack(Material.BOOK,1), ChatColor.GOLD + "Warp to Home", ChatColor.WHITE + "Click For Further Description");
				hub.open(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
}
