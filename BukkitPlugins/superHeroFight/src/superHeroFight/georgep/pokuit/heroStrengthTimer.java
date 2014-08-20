package superHeroFight.georgep.pokuit;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class heroStrengthTimer {
	private final Plugin plugin;
    public heroStrengthTimer(Plugin plugin) {
    	this.plugin = plugin;
    }
    public void run() {
        // What you want to schedule goes here
    	for(Player player: plugin.getServer().getOnlinePlayers()) {
    		if(Main.heroClass.containsKey(player.getName()) && Main.heroClass.get(player.getName()) != "none") {
	    		if(!Main.heroStrength.containsKey(player.getName())) {
	    			Main.heroStrength.put(player.getName(), (float) 0.1);
	    		} else {
	    			if(Main.heroStrength.get(player.getName()) < 1) {
	    				if(player.isSprinting()) {
	    					Main.heroStrength.put(player.getName(), (float) (Main.heroStrength.get(player.getName())+0.01));
	    				} else {
	    					Main.heroStrength.put(player.getName(), (float) (Main.heroStrength.get(player.getName())+0.05));
	    				}
	    			} else {
	    				if(!Main.heroStrengthHasHeardChimes.containsKey(player.getName()) || Main.heroStrengthHasHeardChimes.get(player.getName()) == false) {
	    					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 2F);
	    					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1.5F);
	    					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
	    					player.playSound(player.getLocation(), Sound.LEVEL_UP, 2F, 0.5F);
	    					Main.heroStrengthHasHeardChimes.put(player.getName(), true);
	    					PlayerInventory pi = player.getInventory();
	    					if(pi.first(Material.SNOW_BALL) != -1) {
	    						pi.getItem(pi.first(Material.SNOW_BALL.getId())).addUnsafeEnchantment(new EnchantmentWrapper(Enchantment.DAMAGE_ALL.getId()), 1);
	    					}
	    				}
	    			}
	    		}
	    		player.setExp(Main.heroStrength.get(player.getName()));
	    	}
	    }
    }
}
