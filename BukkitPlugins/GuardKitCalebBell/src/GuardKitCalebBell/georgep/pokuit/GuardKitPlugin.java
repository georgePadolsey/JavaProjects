package GuardKitCalebBell.georgep.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GuardKitPlugin extends JavaPlugin implements Listener {
	
	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	HashMap<String,Integer> playersInCoolDown = new HashMap<String,Integer>();
	ItemStack taserItem = null;
	int requiredCoolDownTime = 0;

	
	public void onEnable() {
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		readConfig();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {

			@Override
			public void run() {
				
				for(String  p: playersInCoolDown.keySet()) {
					if(playersInCoolDown.get(p) < requiredCoolDownTime && requiredCoolDownTime != 0) {
						playersInCoolDown.put(p, (playersInCoolDown.get(p)+1));
					} else {
						if(requiredCoolDownTime != 0) {
							playersInCoolDown.remove(p);
						}
					}
				}
				
			}
			
		}, 0, 1200);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if(label.equalsIgnoreCase("guard") &&
				args.length >= 1) {
			if(args[0].equalsIgnoreCase("kit") && (sender.hasPermission("guard.kit") || sender.isOp())) {
				if(sender instanceof Player) {
					Player p = (Player)sender;
					if(!playersInCoolDown.containsKey(p.getName())) {
						for(ItemStack item : items) {
							log(item.toString());
							p.getInventory().addItem(item);
						}
						if(taserItem != null) {
							p.getInventory().addItem(taserItem);
						}
						if(requiredCoolDownTime != 0) {
							playersInCoolDown.put(p.getName(), 0);
						}
					} else {
						p.sendMessage(ChatColor.GOLD+"[GuardKit]"+ChatColor.RED+" ERROR: You can't use this kit for another "+(requiredCoolDownTime-playersInCoolDown.get(p.getName()))+" minute(s)");
					}
					return true;
				} else {
					sender.sendMessage(ChatColor.GOLD+"[GuardKit]"+ChatColor.RED+" ERROR: This Command must be run from ingame");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("reload") && (sender.hasPermission("guard.reload") || sender.isOp())) {
				readConfig();
				return true;
			}
		}
		
		sender.sendMessage(ChatColor.GOLD+"[GuardKit]"+ChatColor.RED+" ERROR: Appropriate commands = /guard kit or /guard reload");
		return true;
	}
	
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && taserItem != null) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getItemInHand();
			if(item.getType() == taserItem.getType()) {
				log("Type true1");
				if(item.getItemMeta().getDisplayName().equals(taserItem.getItemMeta().getDisplayName())) {
					log("Type true2");
					if(e.getEntity() instanceof Player) {
						Player __p = (Player) e.getEntity();
						__p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
						__p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5));
						__p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		ArrayList<ItemStack> itemsWithTaser =items;
		if(taserItem != null) {
			
			items.add(taserItem);
		}
		for(ItemStack item : itemsWithTaser) {
			ItemStack compare = e.getItemDrop().getItemStack();
			log("Type true0");
			if(item.getType() == compare.getType()) {
				log("Type true1");
				if(item.getItemMeta().getDisplayName().equals(compare.getItemMeta().getDisplayName())) {
					log("Type true2");
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		ArrayList<ItemStack> itemsWithTaser =items;
		if(taserItem != null) {
			items.add(taserItem);
		}
		ArrayList<ItemStack> drops= new ArrayList<ItemStack>();
		drops.addAll(e.getDrops());
		for(ItemStack item : itemsWithTaser) {
			for(ItemStack d : e.getDrops()) {
				ItemStack compare = d;
				log("Type true0");
				if(item.getType() == compare.getType()) {
					log("Type true1");
					if(item.getItemMeta().getDisplayName().equals(compare.getItemMeta().getDisplayName())) {
						log("Type true2");
						drops.remove(d);
					}
				}
			}
		}
		e.getDrops().clear();
		e.getDrops().addAll(drops);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		if(playersInCoolDown.containsKey(e.getPlayer().getName())) {
			playersInCoolDown.remove(e.getPlayer().getName());
		}
	}
	
	private void readConfig() {
		
		try {this.requiredCoolDownTime = this.getConfig().getInt("Time");} catch(Exception e) {}
		List<String> s__items = this.getConfig().getStringList("Items");
		log("test");
		for(String item : s__items) {
			log(item);
			if(!item.equalsIgnoreCase("")) {
				String[] itemSplit = item.split(":");
				if(itemSplit.length == 3) {
					log("3");
					ItemStack __item = null;
					try {
						int nt = Integer.parseInt(itemSplit[0]);
						__item = new ItemStack(Material.getMaterial(nt));
						log(__item.toString());
					} catch(NumberFormatException e) {
						log("test3");
						__item = new ItemStack(Material.getMaterial(itemSplit[0].toUpperCase()));
						log(__item.toString());
					}
					try {					
						ItemMeta im = __item.getItemMeta();
						
						im.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemSplit[2]));
						__item.setItemMeta(im);
						
						
						if(!itemSplit[1].equalsIgnoreCase("")) {
							String[] ench = itemSplit[1].split(",");
							for(String e :  ench) {
								String[] ench2 = e.split("\\.");
								log("Ench2: "+ench2[0]+" :PPPPPPP "+ench2[1] + " "+Enchantment.getByName(ench[0]));
								try {
									__item.addEnchantment(Enchantment.getByName(ench2[0].toUpperCase()), Integer.parseInt(ench2[1]));
								} catch(IllegalArgumentException excep) {
									log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"Your enchantments are wrong check the config for avaliable enchantments.");
								}
							}
						}
						items.add(__item);
					} catch(Exception e) {
						log(ChatColor.GOLD+"[GuardKit]"+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
					}
				} else {
					log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
				}
			} else {
				log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
			}
			
		}
		try { 
			String str = this.getConfig().getString("Taser");
			if(!str.equalsIgnoreCase("")) {
				String[] strSplit = str.split(":");
				if(strSplit.length == 3) {
					log("3");
					ItemStack str__item = null;
					try {
						int strnt = Integer.parseInt(strSplit[0]);
						str__item = new ItemStack(Material.getMaterial(strnt));
						log(str__item.toString());
					} catch(NumberFormatException e) {
						log("test3");
						str__item = new ItemStack(Material.getMaterial(strSplit[0].toUpperCase()));
						log(str__item.toString());
					}				
						ItemMeta im = str__item.getItemMeta();
						
						im.setDisplayName(ChatColor.translateAlternateColorCodes('&', strSplit[2]));
						str__item.setItemMeta(im);
						
						
						if(!strSplit[1].equalsIgnoreCase("")) {
							String[] strench = strSplit[1].split(",");
							for(String e :  strench) {
								String[] strench2 = e.split("\\.");
								log("Ench2: "+strench2[0]+" :PPPPPPP "+strench2[1] + " "+Enchantment.getByName(strench[0]));
								try {
									str__item.addEnchantment(Enchantment.getByName(strench2[0].toUpperCase()), Integer.parseInt(strench2[1]));
								} catch(IllegalArgumentException excep) {
									log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"Your enchantments are wrong check the config for avaliable enchantments.");
								}
							}
						}
						taserItem = str__item;
				} else {
					log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
				}
			} else {
				log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
			}
		} catch(Exception e) {
			log(ChatColor.GOLD+"[GuardKit] "+ChatColor.RED+"The Config Is Not Setup Right please delete the config and restart the server");
		}
	}

	public static void log(String s) {
	//	Bukkit.getServer().getConsoleSender().sendMessage(s);
	}
	
}

