package horseRace.georgep.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import pokuit.georgep.configManager.ConfigManager;

public class Main extends JavaPlugin {

	public static Pages arenaPages = null;
	
	public static ConfigManager configManager;
	public static FileConfiguration arenaData;
	public static FileConfiguration userData;
	public static FileConfiguration signData;
	
	public static Location stableLocation = null;
	
	public static HashMap<String, HorseInfo> playerHorseInfo = new HashMap<String,HorseInfo>();
	public static HashMap<String, Location> playerOrigins = new HashMap<String,Location>();
	public static HashMap <String, Integer> playerScore = new HashMap <String, Integer>();
	public static HashMap<String, Object> configData = new HashMap<String,Object>();
	public static ArrayList<UUID> horsesInStables = new ArrayList<UUID>();
	public static ArrayList<arena> arenas = new ArrayList<arena>();
	public static HashMap<String, String> playersCurrentlyChoosingAHorse = new HashMap<String, String>();
	public static ArrayList<storedSign> storedSigns = new ArrayList<storedSign>(); 
	public static HashMap<String,Scoreboard> playerScoreboard = new HashMap<String,Scoreboard>();
	
	public static GenericFunctions f;
	
	public void onEnable(){
		
		//Bukkit.broadcastMessage(horseRaceStrings.START.toString()+ChatColor.RED+"Debug Enabled");
		f = new GenericFunctions(this);
		
		this.getDataFolder();
		this.getConfig();
		configManager = new ConfigManager(this);
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		configManager.newConfig("signData");
		signData = configManager.getCustomConfig("signData");
		signData.options().copyDefaults(true);
		
		configManager.newConfig("arenaData");
		arenaData = configManager.getCustomConfig("arenaData");
		arenaData.options().copyDefaults(true);
		
		configManager.newConfig("userData");
		userData = configManager.getCustomConfig("userData");
		userData.options().copyDefaults(true);
		
		f.checkConfigValues();
		
		getCommand("stable").setTabCompleter(this);
		
		new signEventHandler(this);
		new vehicleEventHandler(this);
		new playerEventHandler(this);
		new entityEventHandler(this);
		
		f.readUserData();
		
		f.readArenaData();
		
		f.readSignData();
		
		f.spawnRandomHorsesInStable();
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("stable")) {
			Player player = (Player)sender;/*
			final Horse horse = (Horse)player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
			LivingEntity horse2 = (LivingEntity) horse;
			horse2.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120000, 1));
			horse.setPassenger(player);
			horse.getInventory().addItem(new ItemStack(Material.SADDLE,1));
			horse.setDomestication(horse.getMaxDomestication());
			final Entity cow= player.getWorld().spawnEntity(player.getLocation(), EntityType.GIANT);
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

				@Override
				public void run() {
					
					cow.setVelocity(horse.getVelocity());
					cow.teleport(horse);
					
					
					
				}
				
			},5, 0);*/
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				f.despawnStableHorses();
				
				f.reloadAllConfigs();
				f.checkConfigValues();
				f.readUserData();
				f.readArenaData();
				f.readSignData();
				f.spawnRandomHorsesInStable();
				
			}
			if(args.length == 2 && args[0].equalsIgnoreCase("create") && player.hasPermission("horseRace.admin")) {
				if(!f.arenaByThatName(args[1])) {
					arenas.add(new arena(this, args[1], player.getLocation()));
					player.sendMessage(horseRaceStrings.START+""+ChatColor.WHITE+"Congratulations you just created and set arena spawn to your location!");
					
				} else {
					player.sendMessage(horseRaceStrings.START.toString()+ChatColor.RED+"Error: Arena Already made by that name!");
				}
				return true;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("setstable") && player.hasPermission("horseRace.admin")) {
				if(Main.stableLocation != null) {
					f.despawnStableHorses();
				}
				
				Main.stableLocation = player.getLocation();
				
				
				f.spawnRandomHorsesInStable();
				
				player.sendMessage(horseRaceStrings.START+""+ChatColor.WHITE+"Congratulations you just set the stable!");
				return true;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("leave") && player.hasPermission("horseRace.basic")) {
				if(!Main.f.playerIsInArena(player.getName())) {
					player.sendMessage(horseRaceStrings.START.toString()+ChatColor.RED+"ERROR: Your not in an arena!");
				} else {
					Main.f.getArenaPlayerIsIn(player.getName()).removePlayer(player.getName());
				}
				return true;
			}
			if(args.length == 3 && args[0].equalsIgnoreCase("set") && player.hasPermission("horseRace.admin")) {
				arena a = f.getArenaFromString(args[2]);
				if(a != null) {
					String arena = args[2];
					String set = args[1];
					if(set.equalsIgnoreCase("startSpawn")) {
						a.setStartSpawn(player.getLocation());
						player.sendMessage(horseRaceStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your position");
						return true;
					}
				} else {
					player.sendMessage(horseRaceStrings.START+""+ChatColor.RED+"Error: That Arena Doesn't exist!");
				}
			}
			if((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("list") && player.hasPermission("horseRace.basic")) {
				if(args.length == 2) {
					if(arenas.size() == 0) {
						player.sendMessage(horseRaceStrings.START+""+ChatColor.WHITE+"No arenas made yet. Go off and make one!");
						return true;
					} else {
						f.updateArenaPages();
						int PageNum;
						try {
							PageNum = Integer.parseInt(args[1]);
						} catch(NumberFormatException e) {
							player.sendMessage(horseRaceStrings.START+""+horseRaceStrings.MUST_BE_NUMBER);
							return true;
						}
						if(PageNum > arenaPages.getPages()) {
							player.sendMessage(horseRaceStrings.EXCEEDING_NUMBER.toString());
							return true;
						} else {
							player.sendMessage(ChatColor.GOLD+"=== Arenas ===");
							player.sendMessage(arenaPages.getStringsToSend(-PageNum));
							player.sendMessage(ChatColor.GOLD+"=== pg "+PageNum+" / "+arenaPages.getPages()+" ===");
							return true;
						}
					}
				} else {
					if(arenas.size() == 0) {
						player.sendMessage(horseRaceStrings.START+""+ChatColor.WHITE+"No arenas made yet. Go off and make one!");
						return true;
					} else {
						f.updateArenaPages();
						player.sendMessage(ChatColor.GOLD+"=== Arenas ===");
						player.sendMessage(arenaPages.getStringsToSend(-1));
						player.sendMessage(ChatColor.GOLD+"=== pg 1 / "+arenaPages.getPages()+" ===");
						return true;
					}
				}
			}
		}
		return false;
	}
	public void onDisable() {
		for(arena a : Main.arenas) {
			a.sendPlayersHome();
		}
		try {
			for(String p : Main.playersCurrentlyChoosingAHorse.keySet()) {
				Main.f.getPlayerFromString(p).teleport(Main.playerOrigins.get(p));
			}
		} catch(NullPointerException e) {};
		f.despawnStableHorses();
		f.saveSignData();
		f.saveUserData();
		f.saveArenaData();
	}
	public List<String> onTabComplete(CommandSender sender, Command command,
	          String alias,
	          String[] args){
	       
	    List<String> al = new ArrayList<String>();
	    if(args.length == 1) {
	    	al.add("setStable");
	    	al.add("set");
	    	al.add("list");
	    	al.add("create");
	    	al.add("reload");
	    	al.add("leave");
	    }
	    if(args.length == 2) {
	    	if(args[0].equalsIgnoreCase("set")) {
	    		al.add("startSpawn");
	    	}
	    }
	   return al;     
	}
}