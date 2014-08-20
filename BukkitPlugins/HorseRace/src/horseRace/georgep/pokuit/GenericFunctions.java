package horseRace.georgep.pokuit;

import horseRace.georgep.pokuit.storedSign.storedSignActivateEvent;
import horseRace.georgep.pokuit.storedSign.storedSignActivateHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class GenericFunctions {
	
	private Plugin plugin;
	
	public GenericFunctions(Plugin plugin) {
		this.plugin = plugin;
	}	
	
    
    public void saveSignData() {
    	debug("Saved Sign Data");
    	for(storedSign ss : Main.storedSigns) {
    		int i = Main.storedSigns.indexOf(ss);
    		Main.signData.set(ss.getArenaName()+"-"+i+".signType", ss.getSignType().toString());
    		Main.signData.set(ss.getArenaName()+"-"+i+".location", returnLocationAsString(ss.getLocation().getBlock().getLocation()));
    		Main.signData.set(ss.getArenaName()+"-"+i+".arenaName", ss.getArenaName());
    	}
    	Main.configManager.saveCustomConfig("signData");
    }
    
    public void readSignData() {
    	debug("read Sign Data");
    	Set<String> signData=Main.signData.getKeys(false);
		for(String storedSignTitle: signData) {
			try {
				final arena fa = getArenaFromString(Main.signData.getString(storedSignTitle+".arenaName"));
				Main.storedSigns.add(new storedSign(
					turnStringIntoLocation(Main.signData.getString(storedSignTitle+".location")),
					SignType.valueOf(Main.signData.getString(storedSignTitle+".signType")),
					Main.signData.getString(storedSignTitle+".arenaName"),
					plugin,
					new storedSignActivateHandler() {
						@Override
						public void onstoredSignActivateEvent(storedSignActivateEvent event) {
							Main.f.addPlayerToArena(fa, event.getPlayer().getName());
						}
					},false));
			} catch(Exception e) {
				log(horseRaceStrings.START+""+horseRaceStrings.DATA_FILE_EMPTY);
			}
		}
    }
	/**
	 * Save Arenas to Config
	*/
	public void saveArenaData() {
		debug("Saved arena data");
		for(arena arena:Main.arenas) {
			if(arena.getName() != null) {
				Main.arenaData.set(arena.getName()+".startSpawn", returnLocationAsString(arena.getStartSpawn().getBlock().getLocation()));
				Main.arenaData.set(arena.getName()+".arenaState", arena.getArenaState().toString());
			}
		}
		log(returnLocationAsString(Main.stableLocation));
		Main.arenaData.set("__stableData.location", returnLocationAsString(Main.stableLocation));
		Main.configManager.saveCustomConfig("arenaData");
	}
	/**
	 * Check config Values and update variables to the extent
	 */
	public void checkConfigValues() {
		debug("Checked Config Value");
		plugin.reloadConfig();
		HashMap<String,Class<?>> data = new HashMap<String,Class<?>>();
		data.put("horsesToSpawnInTheStables",Integer.class);
		data.put("timeToWaitTillStart", Integer.class);
		data.put("MaxPlayersPerArena", Integer.class);
		
		for(String d:data.keySet()) {
			if(plugin.getConfig().contains(d)) {
				if(data.get(d) == Integer.class) {
					Main.configData.put(d,plugin.getConfig().getInt(d));
				} else {
					Main.configData.put(d,plugin.getConfig().get(d));
				}
				
			} else {
				log(horseRaceStrings.START.toString()+ChatColor.RED+"Config Missing "+d+". Please either add it if you know what variable it requires or just delete the config and copy back the other details. It Requires: "+data.get(d).toString());
			}
		}
	}
	/**
	 * Checks if Block is within the minumum and maximum passed in
	 * @param block
	 * @param min
	 * @param max
	 * @return true if it is. False if it isn't
	 
	public Boolean blockIsWithinMinMax(Location block, Location min, Location max) {
		Location b=block.getBlock().getLocation();
		Location i=min.getBlock().getLocation();
		Location a=max.getBlock().getLocation();
		if(b.getY() <= a.getY() &&
				b.getY() >= i.getY() &&
				b.getX() <=a.getX() &&
				b.getX() >= i.getX() &&
				b.getZ() <= a.getZ() &&
				b.getZ() >= i.getZ()) {
			return true;
		} else {
			return false;
		}
	}*/
	/**
	 * Check if there are any arenas in the config
	 */
	public void readArenaData() {
		debug("Read arena data");
		Set<String> arenasList=Main.arenaData.getKeys(false);
		if(arenasList.size() <= 0) {
			log(horseRaceStrings.START+""+horseRaceStrings.NO_ARENAS_AT_ALL);
		} else {
			for(String arenaName: arenasList) {
				log(ChatColor.RED+arenaName);
				if(arenaName.equalsIgnoreCase("__stableData")) {
					log(ChatColor.RED+"true");
					Main.stableLocation = turnStringIntoLocation(Main.arenaData.getString("__stableData.location"));
				} else {
					try {
						Main.arenas.add(new arena(
							plugin,
							arenaName,
							turnStringIntoLocation(Main.arenaData.getString(arenaName+".startSpawn")),
							arenaState.valueOf(Main.arenaData.getString(arenaName+".arenaState"))
						));
					} catch(Exception e) {
						log(horseRaceStrings.START+""+horseRaceStrings.DATA_FILE_EMPTY);
					}
				}
			}
		}
	}
	
	/**
	 * Get an Arena From A String
	 * @param str
	 * 		String turned into arena
	 * @return
	 * 		Returns an arena Object
	 * 
	 * @throws NullPointerException
	 */
	public arena getArenaFromString(String str) throws NullPointerException {
		debug("turned "+str+" Into arena");
		arena farena = new arena(plugin, null, null, null);
		for(arena arena:Main.arenas) {
			
			if(arena.getName().equalsIgnoreCase(str)) {
				farena = arena;
			}
		}
		if(farena.getName() == null || !farena.getName().equalsIgnoreCase(str)) {
			throw  new NullPointerException("Arena Does Not Exsist");
		} else {
			return farena;
		}
	}
	
	/**
	 * Turns A Location Into A Parsable String
	 * @param loc
	 * 		Location to be turned into string
	 * @return
	 * 		String in format "World|x|y|z"
	 */
	
	public String returnLocationAsString(Location loc) {
		debug("turned "+loc+" into String");
		if(loc == null) {
			return null;
		} else {
			return (loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ());
		}
	}
	
	/**
	 * Turns A String back into a Location
	 * @param str
	 * 		String to be turned into Location
	 * @return
	 * 		Location
	 */
	
	public Location turnStringIntoLocation(String str) {
		debug("Turned "+str+" Into Location");
		if(str == null) {
			return null;
		}
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
	    	log(ChatColor.RED+"IMPORTANT-WARNING: NumberFormatException Check config");
	    }
	    return new Location(world, x, y, z);
	}
	
	/**
	 * Updates Arena Pages with new arenas!
	 */
	public void updateArenaPages() {
		debug("Updated Arena Pages");
		ArrayList<String> arenaInfo = new ArrayList<String>();
		for(arena arena: Main.arenas) {
			arenaInfo.add(ChatColor.GOLD+arena.getName()+ChatColor.BLUE+" - "+ChatColor.RED+"X: "+arena.getStartSpawn().getBlockX()+" Y: "+arena.getStartSpawn().getBlockY()+" Z: "+arena.getStartSpawn().getBlockZ()+"\n");
		}
		if(Main.arenaPages == null) {
			Main.arenaPages =new Pages(arenaInfo,5);
		} else {
			Main.arenaPages.changeData(arenaInfo);
			
		}
	}
	/**
	 * Reloads All Configs
	 */
	public void reloadAllConfigs() {
		debug("Reloaded all configs");
		plugin.reloadConfig();
		Main.configManager.reloadAllConfigs();
	}
	
	public void debug(String s) {
		plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE+s);
		
	}
	
	/**
	 * Log Something with colours!
	 * @param o - any object
	 */
	public void log(Object o) {
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		console.sendMessage(o+"");
	}
	/**
	 * Log A String Array With Colours!
	 * @param array
	 */
	public void logStringArray(String[] a) {
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		console.sendMessage(Arrays.toString(a));
	}
	/**
	 * Gets player from string 
	 * @param String - Player Name
	 * @return The Player or if not online null
	 */
	public Player getPlayerFromString(String p) {
		debug("Got Player "+p+" from string");
		for(Player player :plugin.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(p)) {
				return player;
			}
		}
		return null;
	}
	public void addPlayerToArena(arena a, String p) {
		debug("Added player "+p+" To arena: "+a.getName());
		Player s = getPlayerFromString(p);
		if(s == null) {
			return;
		}
		for(arena temp:Main.arenas) {
			if(temp.containsPlayer(p)) {
				temp.removePlayer(p);
			}
		}
		a.addPlayer(p);
	}
	public void goToStable(String player, String arenaName) {
		debug("Sent "+player+" to the stables. He wanted to then go to "+arenaName);
		Player pl = Main.f.getPlayerFromString(player);
		pl.teleport(Main.stableLocation);
		Main.playersCurrentlyChoosingAHorse.put(player,arenaName);
		hidePlayerToArrayOfPlayers(player, Arrays.copyOf(Main.playersCurrentlyChoosingAHorse.keySet().toArray(), Main.playersCurrentlyChoosingAHorse.keySet().toArray().length, String[].class));
		pl.sendMessage(horseRaceStrings.START.toString()+horseRaceStrings.FIRST_JOIN);
	}
	
	public void hidePlayerToArrayOfPlayers(String player, String[] op) {
		debug("Just hiding "+player+" From "+op.toString());
		Player p = getPlayerFromString(player);
		for(String top : op) {
			getPlayerFromString(top).hidePlayer(p);
		}
	}
	
	public boolean playerIsInArena(String p) {
		for(arena a : Main.arenas) {
			if(a.containsPlayer(p)) {
				return true;
			}
		}
		return false;
	}
	public boolean playerIsInArena(String p, boolean b) {
		for(arena a : Main.arenas) {
			if(a.containsPlayer(p)) { 
				if(b && a.getArenaState() == arenaState.RUNNING) {
					return true;
				}
				if(!b && a.getArenaState() != arenaState.RUNNING) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void showPlayerToArrayOfPlayers(String player, String[] op) {
		debug("Just showing "+player+" From "+op.toString());
		Player p = getPlayerFromString(player);
		for(String top : op) {
			getPlayerFromString(top).showPlayer(p);
		}
	}
	
	public HorseInfo randomHorseInfo() {
		debug("Got some random Horse info");
		return new HorseInfo(Horse.Style.values()[new Random().nextInt(Horse.Style.values().length)], Horse.Color.values()[new Random().nextInt(Horse.Color.values().length)]);
		
	}
	
	public void spawnRandomHorsesInStable() {
		debug("Spawned Random Horses in Stable");
		int h2sits = (Integer)Main.configData.get("horsesToSpawnInTheStables");
		try {
			for(int i = 0, l = h2sits; i < l; i++ ) {
					Entity EntityHorse= Main.stableLocation.getWorld().spawnEntity(Main.stableLocation, EntityType.HORSE);
					Horse Horse = (Horse) EntityHorse;
					HorseInfo hi = randomHorseInfo();
					Horse.setStyle(hi.getHorseStyle());
					Horse.setColor(hi.getHorseColor());
					Horse.setDomestication(Horse.getMaxDomestication());
					Horse.setRemoveWhenFarAway(false);
					Main.horsesInStables.add(Horse.getUniqueId());
				
			}
		} catch(NullPointerException e) {
			log(horseRaceStrings.START+ChatColor.RED.toString()+"Error: The Stable area is not set yet :/ SET IT!");
		}
	}
	public void spawnRandomHorsesInStable(int num) {
		debug("Spawned a certain number of horses in the stables: "+num);
		int h2sits = num;
		for(int i = 0, l = h2sits; i < l; i++ ) {
			Entity EntityHorse = Main.stableLocation.getWorld().spawnEntity(Main.stableLocation, EntityType.HORSE);
			Horse Horse = (Horse) EntityHorse;
			HorseInfo hi = randomHorseInfo();
			Horse.setStyle(hi.getHorseStyle());
			Horse.setColor(hi.getHorseColor());
			Horse.setDomestication(Horse.getMaxDomestication());
			Horse.setRemoveWhenFarAway(false);
			Main.horsesInStables.add(Horse.getUniqueId());
		}
	}
	/**
	 * Very Laggy Method. Try If you know the world add the world argument
	 * @param uuid
	 * @return
	 */
	public Entity getEntityFromUUID(UUID uuid) {
		debug("Got entity from UUID");
		for(World w : plugin.getServer().getWorlds()) {
			for(Entity e: w.getEntities()){
			    if(e.getUniqueId() == uuid){
			            return e;
			    }
			}
		}
		return null;
	}
	public Entity getEntityFromUUID(UUID uuid, String world) {
		debug("Got entity from UUID in world "+world);
		for(Entity e: plugin.getServer().getWorld(world).getEntities()){
		    if(e.getUniqueId() == uuid){
		            return e;
		    }
		}
		return null;
	}
	public void despawnStableHorses() {;
		debug("Despawned Stable Horses");
		ArrayList<UUID> id = Main.horsesInStables;
		for(UUID Uhorse : id) {
			if(Uhorse != null ) {
				Horse horse = (Horse) getEntityFromUUID(Uhorse);
				if(horse != null) {
					horse.remove();
				}
			}
		}
		Main.horsesInStables.clear();
	}
	
	public void updateScoreBoard(String player) {
		Main.f.debug("Updated Score Board for "+player);
		if(!Main.playerScore.containsKey(player)) Main.playerScore.put(player, 0);
		ScoreboardManager manager = plugin.getServer().getScoreboardManager();
		
		if(Main.playerScoreboard.containsKey(player)) {
			Main.f.debug("Player Scoreboard continas key and player is in an arena");
			Scoreboard sb = Main.playerScoreboard.get(player);
			
			Objective obj = sb.getObjective("Side Stats");
			
			Score score = obj.getScore(plugin.getServer().getOfflinePlayer(ChatColor.GOLD+"Score:"));
			
			score.setScore(Main.playerScore.get(player));
			
		} else {
			Main.f.debug("Player Scoreboard doesn't!! continas key and player is in an arena");
			Scoreboard sb = manager.getNewScoreboard();
			
			Main.playerScoreboard.put(player, sb);
			
			//////////////////////////////////////////////////////////////
			Objective obj = sb.registerNewObjective("Side Stats", "dummy");
			
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			obj.setDisplayName(ChatColor.AQUA+"Your Stats");
			
			Score score = obj.getScore(plugin.getServer().getOfflinePlayer(ChatColor.GOLD+"Score:"));
			
			Player p = Main.f.getPlayerFromString(player);
			
			
			
			score.setScore(Main.playerScore.get(player));
			
			p.setScoreboard(sb);
			/////////////////////////////////////////////////////////////////
			
		}
	}
	
	public void clearScoreBoard(String player) {
		Main.f.debug("CLEARED SCORE BOARD FOR "+player);
		ScoreboardManager manager = plugin.getServer().getScoreboardManager();
			
		Player p = Main.f.getPlayerFromString(player);
		p.setScoreboard(manager.getNewScoreboard());
		
		Main.playerScoreboard.remove(player);
	}
	
	public void saveUserData() {
		debug("Saved User Data");
		try {
			for(String playerName  : Main.playerHorseInfo.keySet()) {
				HorseInfo horseInfo = Main.playerHorseInfo.get(playerName);
				Main.userData.set(playerName+".horseStyle", horseInfo.getHorseStyle().toString());
				Main.userData.set(playerName+".horseColor", horseInfo.getHorseColor().toString());
				Main.userData.set(playerName+".score", Main.playerScore.get(playerName));
			}
		} catch(NullPointerException e) {}
		Main.configManager.saveCustomConfig("userData");
	}
	public void readUserData() {
		debug("Read User Data");
		Set<String> userData=Main.userData.getKeys(false);
		for(String userName: userData) {
			try {
				Main.playerHorseInfo.put(
					userName,
					new HorseInfo(
						Horse.Style.valueOf(Main.userData.getString(userName+".horseStyle")),
						Horse.Color.valueOf(Main.userData.getString(userName+".horseColor"))
					)
				);
				Main.playerScore.put(userName, Main.userData.getInt(userName+".score"));
			} catch(Exception e) {
				log(horseRaceStrings.START+""+horseRaceStrings.DATA_FILE_EMPTY);
			}
		}
	}
	
	public boolean arenaByThatName(String name) {
		debug("Checked if there's an arena by name: "+name);
		ArrayList<arena> tempArenas = Main.arenas;
		for(arena a : tempArenas) {
			if(a.getName().equalsIgnoreCase(name)) return true;
		}
		debug("There wasn't :/");
		return false;
	}
	
	public storedSign[] getSignsByArenaName(String arenaName) {
		debug("Got All signs by the arena: "+arenaName);
		ArrayList<storedSign> signsByArenaName= new ArrayList<storedSign>();
		for(storedSign ss: Main.storedSigns) {
			if(ss != null &&
					ss.getArenaName().equalsIgnoreCase(arenaName)) {
				signsByArenaName.add(ss);
			}
		}
		return Arrays.copyOf(signsByArenaName.toArray(), signsByArenaName.toArray().length, storedSign[].class);
	}
	
	public arena getArenaPlayerIsIn(String p) {
		arena temp = null;
		for(arena a:Main.arenas) {
			if(a.containsPlayer(p)) {
				temp = a;
			}
		}
		return temp;
	}
	public boolean blockInColumnIs(Location loc, Material mat, byte b) {
		
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			if(temp.getBlock().getType() == mat &&
					temp.getBlock().getData() == b) {
				return true;
			}
		}
		
		return false;
	}
	public boolean blockInColumnIs(Location loc, Material mat) {
		
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			if(temp.getBlock().getType() == mat) {
				return true;
			}
		}
		
		return false;
	}
	public boolean signInColumnIs(Location loc, String line1, String line2, String line3, String line4) {
		
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			
			if(temp.getBlock().getType() == Material.SIGN ||
					temp.getBlock().getType() == Material.SIGN_POST ||
					temp.getBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign)temp.getBlock().getState();
				String[] lines = sign.getLines();
				if(lines[0].equalsIgnoreCase(line1) &&
						lines[1].equalsIgnoreCase(line2) &&
						lines[2].equalsIgnoreCase(line3) &&
						lines[3].equalsIgnoreCase(line4)) {
						return true;
				}
			}
		}
		
		return false;
	}
	public boolean signInColumnIs(Location loc, String line1, String line2, String line3) {
			
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			
			if(temp.getBlock().getType() == Material.SIGN ||
					temp.getBlock().getType() == Material.SIGN_POST ||
					temp.getBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign)temp.getBlock().getState();
				String[] lines = sign.getLines();
				if(lines[0].equalsIgnoreCase(line1) &&
						lines[1].equalsIgnoreCase(line2) &&
						lines[2].equalsIgnoreCase(line3)) {
						return true;
				}
			}
		}
		
		return false;
	}
	public boolean signInColumnIs(Location loc, String line1, String line2) {
		
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			
			if(temp.getBlock().getType() == Material.SIGN ||
					temp.getBlock().getType() == Material.SIGN_POST ||
					temp.getBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign)temp.getBlock().getState();
				String[] lines = sign.getLines();
				if(lines[0].equalsIgnoreCase(line1) &&
						lines[1].equalsIgnoreCase(line2)) {
						return true;
				}
			}
		}
		return false;
	}
	public boolean signInColumnIs(Location loc, String line1) {
		
		Location startingLocation = loc;
		startingLocation.setY(255);
		
		for(int i = 0, l = 255; i < l; i++) {
			Location temp = startingLocation.subtract(0,i,0);
			
			if(temp.getBlock().getType() == Material.SIGN ||
					temp.getBlock().getType() == Material.SIGN_POST ||
					temp.getBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign)temp.getBlock().getState();
				String[] lines = sign.getLines();
				if(lines[0].equalsIgnoreCase(line1)) {
						return true;
				}
			}
		}
		
		return false;
	}
}