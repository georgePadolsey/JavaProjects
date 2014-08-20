package superHeroFight.georgep.pokuit;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CapMinecraftiaClass implements Listener {
	
	private ArrayList<String> players= new ArrayList<String>();
	private Plugin plugin;
	
	public CapMinecraftiaClass(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
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
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 120000, 0));
	}
	private void giveItems(Player p) {
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		ItemStack Arrow = new ItemStack(Material.ARROW);
		Main.f.setName(Arrow,ChatColor.RED+"Captain "+ChatColor.BLUE+ "Minecraftia's "+ChatColor.RESET+"Shield");
		pi.addItem(Arrow);
		ItemStack brick = new ItemStack(336,1);
		Main.f.setName(brick,"Throw Shield");
		pi.addItem(brick);
		ItemStack DiamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
		DiamondBoots.addEnchantment(new EnchantmentWrapper(2), 4);
		Main.f.setName(DiamondBoots, ChatColor.GOLD+""+ChatColor.UNDERLINE+""+ChatColor.MAGIC+"p "+ChatColor.RESET+ChatColor.GOLD+""+ChatColor.UNDERLINE+""+"The Boots Of No Falling"+ChatColor.MAGIC+" p");
		pi.setBoots(DiamondBoots);
		ItemStack Sponge = new ItemStack(Material.SPONGE);
		Main.f.setName(Sponge, ChatColor.RED+"Captain "+ChatColor.BLUE+ "Minecraftia's "+ChatColor.RESET+"Head");
		pi.setHelmet(Sponge);
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player personGettingHurt = (Player)event.getEntity();
			if(event.getDamager() instanceof Player &&
				playerIsThisClass((Player)event.getDamager()) &&
				((Player)event.getDamager()).getItemInHand().getType() == Material.ARROW) {
				event.setDamage(8);
			}
			if(playerIsThisClass(personGettingHurt) &&
				event.getDamage() >= 2 &&
				personGettingHurt.getItemInHand().getType() == Material.ARROW) {
				event.setDamage(event.getDamage()-2);
			}
			if(playerIsThisClass(personGettingHurt) &&
				event.getDamager() instanceof Arrow &&
				((Arrow)event.getDamager()).getShooter() == personGettingHurt) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player s = (Player)event.getPlayer();
		Location spawningArrow=s.getEyeLocation();
		if((event.getAction() == Action.RIGHT_CLICK_AIR ||
			event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
			event.getMaterial().getId() == 336 &&
			playerIsThisClass(s) && 
			Main.heroStrength.get(s.getName()) >= 1) {
			Arrow a =s.getWorld().spawnArrow(spawningArrow, s.getEyeLocation().getDirection(), 0.6F, 12);
			a.setShooter(s);
			a.setTicksLived(5800);
			a.setFallDistance(10F);
			s.playSound(s.getLocation(), Sound.ARROW_HIT, 1, 5);
			Main.heroStrength.put(s.getName(), Main.heroStrength.get(s.getName())-1);
			Main.heroStrengthHasHeardChimes.put(s.getName(), false);
		}
	}
}
