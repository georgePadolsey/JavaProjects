package superHeroFight.georgep.pokuit;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class LightningClass implements Listener {
	
	private ArrayList<String> players= new ArrayList<String>();
	private Plugin plugin;
	
	public LightningClass(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	public void addPlayer(Player p) {
		players.add(p.getName());
		addPotions(p);
		giveItems(p);
	}
	public void removePlayer(Player p) {
		players.remove(p.getName());
		Main.f.clearAll(p);
	}
	public ArrayList<String> getPlayers() {
		return players;
	}
	public Boolean playerIsThisClass(Player p) {
		return players.contains(p.getName());
	}
	private void addPotions(Player p) {
		Main.f.clearPotionEffects(p);
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120000, 5));
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 120000, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120000, 3));
	}
	private void giveItems(Player p) {
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		ItemStack WoodenSword = new ItemStack(Material.WOOD_SWORD,1);
		WoodenSword.addEnchantment(new EnchantmentWrapper(19), 2);
		WoodenSword.addEnchantment(new EnchantmentWrapper(Enchantment.DAMAGE_ALL.getId()), 1);
		Main.f.setName(WoodenSword, ChatColor.GOLD+"Lightning's Fist of Fury");
		pi.addItem(WoodenSword);
		ItemStack BlazePower= new ItemStack(Material.BLAZE_POWDER);
		pi.addItem(BlazePower);
		ItemStack DiamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
		DiamondBoots.addEnchantment(new EnchantmentWrapper(2), 4);
		Main.f.setName(DiamondBoots, ChatColor.GOLD+""+ChatColor.UNDERLINE+""+ChatColor.MAGIC+"p "+ChatColor.RESET+ChatColor.GOLD+""+ChatColor.UNDERLINE+""+"The Boots Of No Falling"+ChatColor.MAGIC+" p");
		pi.setBoots(DiamondBoots);
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player && playerIsThisClass((Player)event.getEntity())) {
			if(event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
				event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
				event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(playerIsThisClass(event.getPlayer())) {
			if((event.getAction() == Action.RIGHT_CLICK_AIR ||
				event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				event.getPlayer().getItemInHand().getType() == Material.BLAZE_POWDER &&
				Main.f.getHeroStrength(event.getPlayer()) >= 1) {
				Main.heroIsChargingLightning.put(event.getPlayer().getName(),true);
				Vector v = event.getPlayer().getEyeLocation().getDirection();
				event.getPlayer().setVelocity(v.multiply(6));
				Main.heroStrength.put(event.getPlayer().getName(),(float) (Main.heroStrength.get(event.getPlayer().getName())-1));
				Main.heroStrengthHasHeardChimes.put(event.getPlayer().getName(), false);
				final PlayerInteractEvent fevent= event;
				Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
						Main.heroIsChargingLightning.put(fevent.getPlayer().getName(),false);
					}
				}, 20);
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (Main.heroIsChargingLightning.containsKey(event.getPlayer().getName()) && Main.heroIsChargingLightning.get(event.getPlayer().getName())) {
	        Block block = event.getPlayer().getLocation().getBlock();
	        if (block.getType() != Material.FIRE) {
	        	Main.f.makeTempBlock(block.getLocation(), Material.FIRE,(byte) 0, 80, null, Effect.EXTINGUISH);
	        }
		}
    }
}
