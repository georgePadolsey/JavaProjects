package vanishTorch.georgep.pokuit;

import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				e.getItem() != null) {
			getLogger().info("rightclick");
			if(e.getItem().getType() == Material.REDSTONE_TORCH_ON) {
				getLogger().info("offredstonetorch");
				if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED+"Right Click To Vanish Players")) {
				getLogger().info("offredstonetorchanditemmeta");
				for(Player p : getServer().getOnlinePlayers()) {
					if(!e.getPlayer().getDisplayName().equalsIgnoreCase(p.getDisplayName())) {
						e.getPlayer().hidePlayer(p);
					}
				}
				e.getPlayer().getItemInHand().setType(Material.REDSTONE_TORCH_OFF);
				ItemMeta im = e.getPlayer().getItemInHand().getItemMeta();
				im.setDisplayName(ChatColor.RED+"Right Click To Un-Vanish Players");
				e.getPlayer().getItemInHand().setItemMeta(im);
			
				e.getPlayer().sendMessage(ChatColor.GOLD+"Players have been vanished!");
				e.setCancelled(true);
				return;
				}
			}
			if(e.getItem().getType() == Material.REDSTONE_TORCH_OFF &&
					e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED+"Right Click To Un-Vanish Players")) {
				
				for(Player p : getServer().getOnlinePlayers()) {
					if(!e.getPlayer().getDisplayName().equalsIgnoreCase(p.getDisplayName())) {
						e.getPlayer().hidePlayer(p);
					}
				}
				e.getPlayer().getItemInHand().setType(Material.REDSTONE_TORCH_ON);
				ItemMeta im = e.getPlayer().getItemInHand().getItemMeta();
				im.setDisplayName(ChatColor.RED+"Right Click To Vanish Players");
				e.getPlayer().getItemInHand().setItemMeta(im);
				e.getPlayer().sendMessage(ChatColor.GOLD+"Players have been un-vanished!");
				e.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(e.getPlayer().hasPermission("vanishTorch.clear")) {
			e.getPlayer().getInventory().clear();
		}
		if(e.getPlayer().hasPermission("vanishTorch.basic") || e.getPlayer().isOp()) {
			ItemStack redstoneTorch1 = new ItemStack(Material.REDSTONE_TORCH_ON);
			ItemMeta redstoneTorch1Meta = redstoneTorch1.getItemMeta();
			redstoneTorch1Meta.setDisplayName(ChatColor.RED+"Right Click To Vanish Players");
			redstoneTorch1.setItemMeta(redstoneTorch1Meta);
			ItemStack redstoneTorch2 = new ItemStack(Material.REDSTONE_TORCH_OFF);
			ItemMeta redstoneTorch2Meta = redstoneTorch2.getItemMeta();
			redstoneTorch2Meta.setDisplayName(ChatColor.RED+"Right Click To Un-Vanish Players");
			redstoneTorch2.setItemMeta(redstoneTorch2Meta);
			if(!(e.getPlayer().getInventory().contains(redstoneTorch1) || e.getPlayer().getInventory().contains(redstoneTorch2))) {
				e.getPlayer().getInventory().addItem(redstoneTorch1);
			}
		}
	}
}
