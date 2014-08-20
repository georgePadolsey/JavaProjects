package fatPlugin.pokuit.grimm235;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		
		ItemStack pizza = new ItemStack(Material.CARPET,1,(short) 15);
		ItemMeta pizzaMeta = pizza.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE.toString()+ChatColor.ITALIC+"FILLING");
		pizzaMeta.setDisplayName(ChatColor.RED+"P"+ChatColor.YELLOW+"I"+ChatColor.RED+"Z"+ChatColor.YELLOW+"Z"+ChatColor.RED+"A");
		pizzaMeta.setLore(lore);
		pizza.setItemMeta(pizzaMeta);
		
		final ShapedRecipe pizzaR = new ShapedRecipe(pizza);
        pizzaR.shape("AAA", "ABA", "AAA");
        pizzaR.setIngredient('A', Material.WHEAT);
        pizzaR.setIngredient('B', Material.MELON_BLOCK);
        getServer().addRecipe(pizzaR);
		
        getServer().getPluginManager().registerEvents(this, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false;
	}
	
	public void onDisable() {
		
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR &&
				e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED+"P"+ChatColor.YELLOW+"I"+ChatColor.RED+"Z"+ChatColor.YELLOW+"Z"+ChatColor.RED+"A") &&
				e.getItem().getItemMeta().getLore().contains(ChatColor.DARK_PURPLE.toString()+ChatColor.ITALIC+"FILLING")) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,5));
			e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.EAT,1,1);
			final Player player = e.getPlayer();
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					player.getItemInHand().setType(Material.AIR);
					for(PotionEffect pet:player.getActivePotionEffects()) {
						player.removePotionEffect(pet.getType());
						
					}
					
				}
				
			},50);
		}
	}	
}
