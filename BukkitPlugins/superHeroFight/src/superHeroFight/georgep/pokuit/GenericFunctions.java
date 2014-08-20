package superHeroFight.georgep.pokuit;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

public class GenericFunctions {

	private Plugin plugin;
	
	public GenericFunctions(Plugin plugin) {
		this.plugin = plugin;
	}
	public Entity getEntityFromUUID(UUID uuidfun) {
		for(Entity e: plugin.getServer().getWorld("world").getEntities()){
		    if(e.getUniqueId() == uuidfun){
		            return e;
		    }
		}
		return null;
	}
	public Boolean playerIsClass(String Class, Player s) {
		if(Main.heroClass.containsKey(s.getName()) && Main.heroClass.get(s.getName()) == Class) {
			return true;
		} else {
			return false;
		}
	}
	public Float getHeroStrength(Player s) {
		if(Main.heroStrength.containsKey(s.getName())) {
			return Main.heroStrength.get(s.getName());
		} else {
			return 0F;
		}
	}
	public ItemStack setName(ItemStack is, String name) {
		ItemMeta m = is.getItemMeta();
		m.setDisplayName(name);
		is.setItemMeta(m);
		return is;
	}
	public Player clearPotionEffects(Player p) {
		for(PotionEffect effect : p.getActivePotionEffects())
		{
		    p.removePotionEffect(effect.getType());
		}
		return p;
	}
	public Player clearAll(Player p) {
		PlayerInventory pi=p.getInventory();
		pi.clear();
		p.setLevel(0);
		p.setExp(0);
		clearPotionEffects(p);
		pi.setArmorContents(null);
		Main.heroClass.put(p.getName(), "none");
		return p;
	}
	public String addMagicLetter(ChatColor cc) {
		return ChatColor.RESET+""+cc+""+ChatColor.MAGIC+"k"+ChatColor.RESET;
	}
	public String returnLocationAsString(Location loc) {
		return (loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ());
	}
	public Location turnStringIntoLocation(String str) {
		String[] loc = str.split("\\|");
		 
	    World world = Bukkit.getWorld(loc[0].replaceAll("\\s",""));
	    Double x = (double) 0;
		Double y = (double) 0;
		Double z = (double) 0;
	    try {
			x = Double.parseDouble(loc[1].replaceAll("\\s",""));
			y = Double.parseDouble(loc[2].replaceAll("\\s",""));
			z = Double.parseDouble(loc[3].replaceAll("\\s",""));
			
	    } catch(NumberFormatException e) {
	    	plugin.getLogger().warning(ChatColor.RED+"IMPORTANT-WARNING: NumberFormatException Check config");
	    }
	    return new Location(world, x, y, z);
	}
	public void changeClass(Player p, HeroClasses c) {
		if(c ==HeroClasses.LIGHTNING) {
			if(Main.CapMinecraftiaClass.getPlayers().contains(p.getName())) {
				Main.CapMinecraftiaClass.removePlayer(p);
			}
			if(!Main.LightningClass.getPlayers().contains(p.getName())) {
				Main.LightningClass.addPlayer(p);
			}
			Main.heroClass.put(p.getName(), "lightning");
		}
		if(c == HeroClasses.CAPMINECRAFTIA) {	
			if(Main.LightningClass.getPlayers().contains(p.getName())) {
				Main.LightningClass.removePlayer(p);
			}
			if(!Main.CapMinecraftiaClass.getPlayers().contains(p.getName())) {
				Main.CapMinecraftiaClass.addPlayer(p);
			}
			Main.heroClass.put(p.getName(), "capMinecraftia");
		}
		if(c == HeroClasses.NONE) {
			if(Main.LightningClass.getPlayers().contains(p.getName())) {
				Main.LightningClass.removePlayer(p);
			} 
			if(Main.CapMinecraftiaClass.getPlayers().contains(p.getName())) {
				Main.CapMinecraftiaClass.removePlayer(p);
			}
			Main.heroClass.put(p.getName(), "none");
		}
	}
	public enum HeroClasses {
		LIGHTNING("lightning"),
		CAPMINECRAFTIA("capminecraftia"),
		NONE("none");
		
		String name;
		
		HeroClasses(final String name) {
			this.name=name;
		}
		public String getName() {
			return name;
		}
	}
	public void makeTempBlock(Location loc, Material m, Byte d, Integer time, Sound s, Effect e) {
		if(time > 10000) {
			plugin.getLogger().warning(ChatColor.RED+"Time must be smaller then 10000 in temp block changes");
		}
		Main.tempBlockReplacements.add(new tempBlockReplace(loc,m,d,time,plugin,s,e));
	}
}
