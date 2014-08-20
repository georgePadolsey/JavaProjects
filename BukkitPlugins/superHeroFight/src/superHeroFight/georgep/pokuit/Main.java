package superHeroFight.georgep.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import superHeroFight.georgep.pokuit.GenericFunctions.HeroClasses;


public final class Main extends JavaPlugin implements Listener {
	//Hash Maps
	public static HashMap<String, String> heroClass = new HashMap<String, String>();
	public static HashMap<String, Float> heroStrength = new HashMap<String, Float>();
	public static HashMap<String, Boolean> heroStrengthHasHeardChimes = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> heroIsChargingLightning = new HashMap<String, Boolean>();
	public static HashMap<String, UUID> mobsToWatchOutFor = new HashMap<String, UUID>();
	public static ArrayList<UUID> cowwyMobs = new ArrayList<UUID>();
	public static ArrayList<tempBlockReplace> tempBlockReplacements = new ArrayList<tempBlockReplace>();
	
	public static World DefaultWorld;
	private static arena arena1;
	private static ArrayList<arena> ArenasUsed = new ArrayList<arena>();
	public static LightningClass LightningClass;
	public static CapMinecraftiaClass CapMinecraftiaClass;
	public static GenericFunctions f;
	public static heroStrengthTimer heroTime;
	
	public void onEnable(){
		f = new GenericFunctions(this);
		heroTime = new heroStrengthTimer(this);
		DefaultWorld = getServer().getWorld("world");
		LightningClass =  new LightningClass(this);
		CapMinecraftiaClass = new CapMinecraftiaClass(this);
		getLogger().info("Super Hero Has been Activated!");
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		@SuppressWarnings("unused")
		BukkitTask task=new masterTimer(this).runTaskTimer(this, 0, 1);
		DefaultWorld.setGameRuleValue("keepInventory" , "true");
		DefaultWorld.setDifficulty(Difficulty.PEACEFUL);
		Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
		    public void run() {
		    	for(UUID cowwy: cowwyMobs) {
		    		Entity cow=f.getEntityFromUUID(cowwy);
		    		if(cow!=null) {
			    		if(cow.getPassenger() instanceof Player) {
			    			Player cowMan = (Player)cow.getPassenger(); 
			    			if(!cowMan.isSneaking()) {
			    				Vector v = cowMan.getEyeLocation().getDirection();
			    				v.setY(0);
			    				cow.setVelocity(v.multiply(0.5));
			    	
			    			} else {
			    				Vector v = cowMan.getEyeLocation().getDirection();
			    				v.setY(0);
			    				v.setX(0);
			    				v.setZ(0);
			    				cow.setVelocity(v);//onPlayerJoinCowWarriorFix :)
			    			}
			    		}
		    		}
		    	}
		    }
		}, 0, 1);
		
		if(this.getConfig().contains("arenas.arena1")) {
			arena1 = new arena(
					1,
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.spawnChooseTeam")),
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.SpawnTeam1")),
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.spawnTeam2")),
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.specSpawn")),
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.SpawnChooseTeam1")),
					f.turnStringIntoLocation(this.getConfig().getString("arenas.arena1.Locations.spawnChooseTeam2")),
					this,
					"arena1"
		    );
		} else {
			arena1 = new arena(
					1,
					new Location(DefaultWorld, 899, 27, -639),
					new Location(DefaultWorld, 898, 24, -644),
					new Location(DefaultWorld, 900, 24, -649),
					new Location(DefaultWorld, 899, 27, -646),
					new Location(DefaultWorld, 897, 27, -643),
					new Location(DefaultWorld, 901, 27, -643),
					this,
					"arena1"
		    );
		}
		ArenasUsed.add(arena1);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			Player s = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("class") && args.length >= 1) {
				if(args[0].equalsIgnoreCase("lightning")) {
					f.changeClass(s, HeroClasses.LIGHTNING);
					return true;
				}
				if(args[0].equalsIgnoreCase("capMinecraftia")) {
					f.changeClass(s, HeroClasses.CAPMINECRAFTIA);
					return true;
				}
				if(args[0].equalsIgnoreCase("thor")) {
					f.changeClass(s, HeroClasses.NONE);
					f.clearPotionEffects(s);
					PlayerInventory pi = s.getInventory();
					pi.clear();
					pi.setArmorContents(null);
					return true;
				}
				if(args[0].equalsIgnoreCase("reset")) {
					f.changeClass(s, HeroClasses.NONE);
					f.clearPotionEffects(s);
					PlayerInventory pi = s.getInventory();
					pi.clear();
					pi.setArmorContents(null);
					return true;
				}
			} else {
				if(cmd.getName().equalsIgnoreCase("texturereload")) {
					s.setTexturePack("http\\://www.georgep.co.uk/working.zip");
					return true;
				}
				if(cmd.getName().equalsIgnoreCase("spawnpiggy")) {
					Entity pig = (Entity)s.getWorld().spawnEntity(s.getLocation(), EntityType.PIG);
					LivingEntity pigl = (LivingEntity)pig;
					pigl.setCustomName("Porky");
					pigl.setCustomNameVisible(true);
					return true;
				}
				if(cmd.getName().equalsIgnoreCase("cowwarrior")) {
					Entity cow = s.getWorld().spawnEntity(s.getLocation(), EntityType.COW);
					cow.setPassenger(s);
					cowwyMobs.add(cow.getUniqueId());
					return true;
				}
				
			//	if(cmd.getName().equalsIgnoreCase("witherwarrior")) {
			//		Entity wither = s.getWorld().spawnEntity(s.getLocation(), EntityType.WITHER);
			//		wither.setPassenger(s);
			//		return true;
			//	}
				if(cmd.getName().equalsIgnoreCase("giantwarrior")) {
					Entity giant = s.getWorld().spawnEntity(s.getLocation(), EntityType.GIANT);
					giant.setPassenger(s);
					return true;
				}
				if(cmd.getName().equalsIgnoreCase("setArena")) {
					for(arena arenaFun: ArenasUsed) {
						if(args.length == 2) {
							if(args[1].equalsIgnoreCase(arenaFun.getName())) {
								if(args[0].equalsIgnoreCase("spawnChooseTeam")) {
									arenaFun.setSpawnChooseTeam(s.getLocation());
									s.sendMessage("Sucessfully set SpawnChooseTeam");
									return true;
								}
								if(args[0].equalsIgnoreCase("spawnTeam1")) {
									arenaFun.setSpawnTeam1(s.getLocation());
									s.sendMessage("Sucessfully set SpawnTeam1");
									return true;
								}
								if(args[0].equalsIgnoreCase("spawnTeam2")) {
									arenaFun.setSpawnTeam2(s.getLocation());
									s.sendMessage("Sucessfully set SpawnTeam2");
									return true;
								}
								if(args[0].equalsIgnoreCase("specSpawn")) {
									arenaFun.setSpecSpawn(s.getLocation());
									s.sendMessage("Sucessfully set SpecSpawn");
									return true;
								}
								if(args[0].equalsIgnoreCase("spawnChooseTeam1")) {
									arenaFun.setSpawnChooseTeam1(s.getLocation());
									s.sendMessage("Sucessfully set SpawnChooseTeam1");
									return true;
								}
								if(args[0].equalsIgnoreCase("spawnChooseTeam2")) {
									arenaFun.setSpawnTeam2(s.getLocation());
									s.sendMessage("Sucessfully set SpawnChooseTeam2");
									return true;
								}
							}
						} else {
							if(args.length == 1 && args[0].equalsIgnoreCase("toggleEditing")) {
								if(arenaFun.getEditingPlayers().contains(s.getName())) {
									arenaFun.removeEditingPlayer(s);
								} else {
									arenaFun.addEditingPlayer(s);
								}
								s.sendMessage("Sucessfully Toggle Editing");
								return true;
							}
						}
					}
				}
			}
		} else {
			sender.sendMessage("This Command Can Only Be Run In Game");
		}
		return false;
	}
	public void onDisable() {
		getLogger().info("Saving Data...");
		FileConfiguration config=this.getConfig();
		for(arena ta:ArenasUsed) {
			config.set("arenas.arena"+ta.getArenaNum()+".info.arenaNum", ta.getArenaNum());
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.spawnChooseTeam", f.returnLocationAsString(ta.getSpawnChooseTeam()));
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.SpawnChooseTeam1", f.returnLocationAsString(ta.getSpawnChooseTeam1()));
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.spawnChooseTeam2", f.returnLocationAsString(ta.getSpawnChooseTeam2()));
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.SpawnTeam1", f.returnLocationAsString(ta.getSpawnTeam1()));
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.spawnTeam2", f.returnLocationAsString(ta.getSpawnTeam2()));
			config.set("arenas.arena"+ta.getArenaNum()+".Locations.specSpawn", f.returnLocationAsString(ta.getSpecSpawn()));
			ta.replaceSpawnBlocks();
		}
		this.saveConfig();
		getLogger().info("Super Hero Has been Deactivated!");
		
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){		
		if(event.getEntity() instanceof Player) {
			Player s = (Player)event.getEntity();
			if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				if(!heroClass.containsKey(s.getName()) || heroClass.get(s.getName()) == "none") {
					event.setCancelled(true);
				}
			} else {
				if(event.getCause() == EntityDamageEvent.DamageCause.DROWNING || event.getCause() == EntityDamageEvent.DamageCause.STARVATION) {
					event.setCancelled(true);
				}
			}
		} else {
			if(event.getEntity() instanceof Cow && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				for(UUID cowwy: cowwyMobs) {
		    		if(cowwy == event.getEntity().getUniqueId()) {
		    			event.setCancelled(true);
		    		}
				}
			}
			if(event.getEntity() instanceof Pig) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			//Player r = (Player)event.getDamager();
			if(event.getEntity() instanceof Cow) {
				event.setDamage(20);
			}
			if(event.getEntity() instanceof Player) {
				Player s = (Player)event.getEntity();
				if(s.getItemInHand().getType() == Material.ARROW && heroClass.containsKey(s.getName())) {
					
					if(heroClass.get(s.getName()) == "capMinecraftia") {
						event.setDamage(event.getDamage()-2);	
					}
				}
			}
		}
		if (event.getDamager() instanceof Arrow) {
			event.setDamage(16);
		}
		
	}
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if(event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if(event.getPlayer().getName().equals("pokuit")) {
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) this, new Runnable() {
				public void run() {
					getServer().broadcastMessage(f.addMagicLetter(ChatColor.GOLD)+" "+ChatColor.GOLD+""+"Pokuit the Creator"+ChatColor.GREEN+""+ChatColor.ITALIC+" of the superhero plugin has joined the game "+f.addMagicLetter(ChatColor.GOLD));
				}
			}, 3);
		} 
		int x = Main.this.getConfig().getInt("Location.x");
		int y = Main.this.getConfig().getInt("Location.y");
		int z = Main.this.getConfig().getInt("Location.z");
		World w = getServer().getWorld(Main.this.getConfig().getString("defaultWorld"));
		Location spawn = new Location(w, x, y, z);
		event.getPlayer().teleport(spawn);
		f.clearAll(event.getPlayer());
		ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta ism = (BookMeta)is.getItemMeta();
		ism.setAuthor("Pokuit");
		ism.setTitle("Server Information");
		ArrayList<String> pages = new ArrayList<String>();
		pages.add("Welcome To SuperHero Craft.\nMade by Pokuit");
		ism.setPages(pages);
		is.setItemMeta(ism);
		event.getPlayer().getInventory().addItem(is);
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 120000, 0));
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		heroClass.put(event.getPlayer().getName(), "none");
		heroStrength.put(event.getPlayer().getName(), 0F);
		heroStrengthHasHeardChimes.put(event.getPlayer().getName(), false);
	}
	@EventHandler
	public void onWeatherChangeEvent(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
	//	getLogger().info(""+heroIsChargingLightning.get(event.getPlayer().getName()));
		if(event.getTo().getBlockY() <= 20 && event.getTo().getBlockX() >=820 && event.getTo().getBlockX() <= 874 && event.getTo().getBlockZ() <= -758 && event.getTo().getBlockZ() >= -820) {
			World w = getServer().getWorld("world");
			Double x = 819.37218;
			Integer y=27;
			Integer z=-764;
			float yaw=-90;
			float p=45;
			Location spawn = new Location(w, x, y, z);
			spawn.setYaw(yaw);
			spawn.setPitch(p);
			event.getPlayer().teleport(spawn);
		}
		Location Arena1Entrance = new Location(getServer().getWorld("world"),785,28,-724);
		if(event.getTo().getBlock().getLocation().distance(Arena1Entrance) <= 1 && !arena1.getEditingPlayers().contains(event.getPlayer().getName())) {
			getLogger().info("Enter Arena 1");
			arena1.addPlayer(event.getPlayer());
		}
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.getPlayer().sendMessage(ChatColor.RED+"Block Glitching is NOT allowed!");
		//	event.getPlayer().setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(-2).setY(0));
			event.getPlayer().setHealth(0);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		if(!heroClass.containsKey(event.getPlayer().getName()) || heroClass.get(event.getPlayer().getName()) == "none") {
			int x = Main.this.getConfig().getInt("Location.x");
			int y = Main.this.getConfig().getInt("Location.y");
			int z = Main.this.getConfig().getInt("Location.z");
			World w = getServer().getWorld(Main.this.getConfig().getString("defaultWorld"));
			Location spawn = new Location(w, x, y, z);
			event.getPlayer().teleport(spawn);
			f.clearAll(event.getPlayer());
			ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
			BookMeta ism = (BookMeta)is.getItemMeta();
			ism.setAuthor("Pokuit");
			ism.setTitle("Server Information");
			ArrayList<String> pages = new ArrayList<String>();
			pages.add("Welcome To SuperHero Craft.\nMade by Pokuit");
			ism.setPages(pages);
			is.setItemMeta(ism);
			event.getPlayer().getInventory().addItem(is);
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 120000, 0));
		}
	}
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		event.setDroppedExp(0);
	}
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() instanceof Cow) {
			for(Integer i = 0, l = cowwyMobs.size();i < l;i++ ) {
	    		if(cowwyMobs.get(i) == event.getEntity().getUniqueId()) {
	    			cowwyMobs.remove(i+0);
	    		}
			}
		}
		event.setDroppedExp(0);
	}
}

