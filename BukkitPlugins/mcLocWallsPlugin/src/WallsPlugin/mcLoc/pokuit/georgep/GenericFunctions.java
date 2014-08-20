package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import WallsPlugin.mcLoc.pokuit.georgep.arena.arenaTeams;
import WallsPlugin.mcLoc.pokuit.georgep.storedSign.storedSignActivateEvent;
import WallsPlugin.mcLoc.pokuit.georgep.storedSign.storedSignActivateHandler;

public class GenericFunctions {
	
	private Plugin plugin;
	
	public GenericFunctions(Plugin plugin) {
		this.plugin = plugin;
	}	
	public void saveSignData() {
		
		for(int i = 0, l =Main.storeSigns.size(); i < l ; i++){
			storedSign ss = Main.storeSigns.get(i);
			Main.f.log(i);
			try {
				ss.getSignType().toString();
			} catch(NullPointerException e) {
				log(ChatColor.RED+"Yes :(");
			}
			Main.SignData.set(ss.getArenaName()+"-"+i+".location", Main.f.returnLocationAsString(ss.getLocation()));
			Main.SignData.set(ss.getArenaName()+"-"+i+".arenaName", ss.getArenaName());
			Main.SignData.set(ss.getArenaName()+"-"+i+".type", ss.getSignType().toString());
		}
		
		
		Main.configManager.saveCustomConfig("signData");
		
	}
	
	public void readSignData() {
		Set<String> signData=Main.SignData.getKeys(false);
		if(signData.size() > 0) {
			for(String signName: signData) {
				try {
					Main.storeSigns.add(new storedSign(
						turnStringIntoLocation(Main.SignData.getString(signName+".location")),
						SignType.valueOf(Main.SignData.getString(signName+".type")),
						Main.SignData.getString(signName+".arenaName"),
						this.plugin,
						new storedSignActivateHandler() {
							@Override
							public void onstoredSignActivateEvent(storedSignActivateEvent event) {
								Main.f.getArenaFromString(event.getArenaName()).openTeamChoice(event.getPlayer().getName());
							}
						},
						true
					));
				}catch(RuntimeException e) {
					log(wallStrings.START+""+wallStrings.SIGN_DATA_FILE_ERROR);
					log(e.toString());
				}
				catch(Exception e) {
					log(wallStrings.START+""+wallStrings.SIGN_DATA_FILE_EMPTY);
					
				}
			}
		}
	}
	
	/**
	 * Updates the arenaChoice
	 */
	/*public void updateArenasToArenaChoice() {
		ArrayList<arena> arenas = Main.arenas;
		int pagesNeeded = 1;
		 
		Main.arenaChoice.clear();
		
		pagesNeeded = (int)Math.ceil(Main.arenas.size()/14.0);
		
		for(int i = 0, l = pagesNeeded; i < l; i++) {
			IconMenu temp = new IconMenu("Arena Choice - Page "+(i+1), 45, 
				new OptionClickEventHandler() {
					@Override
					public void onOptionClick(OptionClickEvent event) {
						if(event.getItemType() == Material.IRON_SWORD &&
								Main.f.getArenaFromString(event.getName())!= null) {
							arena a = Main.f.getArenaFromString(event.getName());
							Main.f.log(ChatColor.GREEN+"Test Successful " + a.getName());
						}
						if(event.getItemType() == Material.ENCHANTED_BOOK &&
								event.getName().equalsIgnoreCase(ChatColor.GOLD+"Next Page")) {
							int pageNumber = 1;
							try {
								pageNumber = Integer.parseInt(event.getTitle().replaceAll("[^0-9.]", ""));
							} catch(NumberFormatException ex) {
								Main.f.log(ChatColor.RED+"A Major error has occured in WallsPlugin please report the following code to the plugin developer");
								Main.f.log(ChatColor.RED+ex.toString());
								Main.f.log(ChatColor.RED+"======= END =======");
							}
							event.setWillClose(true);
							Main.arenaChoice.get(pageNumber).open(event.getPlayer());
						}
						if(event.getItemType() == Material.ENCHANTED_BOOK &&
							event.getName().equalsIgnoreCase(ChatColor.GOLD+"Back Page")) {
							int pageNumber = 1;
							try {
								pageNumber = Integer.parseInt(event.getTitle().replaceAll("[^0-9.]", ""));
							} catch(NumberFormatException ex) {
								Main.f.log(ChatColor.RED+"A Major error has occured in WallsPlugin please report the following code to the plugin developer");
								Main.f.log(ChatColor.RED+ex.toString());
								Main.f.log(ChatColor.RED+"======= END =======");
							}
							event.setWillClose(true);
							Main.arenaChoice.get(pageNumber-2).open(event.getPlayer());
						}
						if(event.getItemType() == Material.WOOL &&
							event.getName().equalsIgnoreCase(ChatColor.RED+"Exit")) {
							event.setWillClose(true);
						}
					}
				},  (Plugin)plugin);
				for(arena a:arenas) {
					if(arenas.indexOf(a)+1 <= i +1* 14 && arenas.indexOf(a)+1 > i * 14) {
						temp.setOption(arenas.indexOf(a)*2, a.getArenaState().getItemStack(), ChatColor.GOLD+a.getName(), ChatColor.LIGHT_PURPLE+"Join this arena");
					}
				}
			if(pagesNeeded != 1) {
				temp.setOption(44, new ItemStack(Material.ENCHANTED_BOOK), ChatColor.GOLD+"Next Page", ChatColor.LIGHT_PURPLE+"Click to go to the Next Page");
			}
			if(i > 1) {
				temp.setOption(36, new ItemStack(Material.ENCHANTED_BOOK), ChatColor.GOLD+"Back Page", ChatColor.LIGHT_PURPLE+"Click to go Back a Page");
			}
			ItemStack redWool = new ItemStack(Material.WOOL);
			redWool.setData(new MaterialData(14));
			temp.setOption(40, redWool, ChatColor.RED+"Exit", ChatColor.LIGHT_PURPLE+"Click to Exit");
			Main.arenaChoice.add(i, temp);
		}
		
	}*/
	/**
	 * Save Arenas to Config
	 */
	public void saveArenasToDataFile() {
		for(arena arena:Main.arenas) {
			if(arena.getName() != null) {
				Main.DataConfig.set(arena.getName()+".wholeArenaMin", returnLocationAsString(arena.getWholeArenaMin()));
				Main.DataConfig.set(arena.getName()+".wholeArenaMax", returnLocationAsString(arena.getWholeArenaMax()));
				Main.DataConfig.set(arena.getName()+".teamSpawn1", returnLocationAsString(arena.getTeamSpawn1()));
				Main.DataConfig.set(arena.getName()+".teamSpawn2", returnLocationAsString(arena.getTeamSpawn2()));
				Main.DataConfig.set(arena.getName()+".teamSpawn3", returnLocationAsString(arena.getTeamSpawn3()));
				Main.DataConfig.set(arena.getName()+".teamSpawn4", returnLocationAsString(arena.getTeamSpawn4()));
				Main.DataConfig.set(arena.getName()+".teamSpawn1BoxMin", returnLocationAsString(arena.getTeamSpawn1BoxMin()));
				Main.DataConfig.set(arena.getName()+".teamSpawn1BoxMax", returnLocationAsString(arena.getTeamSpawn1BoxMax()));
				Main.DataConfig.set(arena.getName()+".teamSpawn2BoxMin", returnLocationAsString(arena.getTeamSpawn2BoxMin()));
				Main.DataConfig.set(arena.getName()+".teamSpawn2BoxMax", returnLocationAsString(arena.getTeamSpawn2BoxMax()));
				Main.DataConfig.set(arena.getName()+".teamSpawn3BoxMin", returnLocationAsString(arena.getTeamSpawn3BoxMin()));
				Main.DataConfig.set(arena.getName()+".teamSpawn3BoxMax", returnLocationAsString(arena.getTeamSpawn3BoxMax()));
				Main.DataConfig.set(arena.getName()+".teamSpawn4BoxMin", returnLocationAsString(arena.getTeamSpawn4BoxMin()));
				Main.DataConfig.set(arena.getName()+".teamSpawn4BoxMax", returnLocationAsString(arena.getTeamSpawn4BoxMax()));
				Main.DataConfig.set(arena.getName()+".arenaState", arena.getArenaState().toString());
			}
		}
		Main.configManager.saveCustomConfig("data");
	}
	/**
	 * Check config Values and update variables to the extent
	 */
	public void checkConfigValues() {
		plugin.reloadConfig();
		if(plugin.getConfig().contains("Max-Players-In-One-Arena")) {
			int MaxPlayersInOneArena = plugin.getConfig().getInt("Max-Players-In-One-Arena");
			if(MaxPlayersInOneArena == 0) {
				log(wallStrings.START.toString()+wallStrings.CONFIG_EMPTY);
			} else {
				Main.maxPlayersInArena = plugin.getConfig().getInt("Max-Players-In-One-Arena");
			}
		}
	}
	/**
	 * Checks if Block is within the minumum and maximum passed in
	 * @param block
	 * @param min
	 * @param max
	 * @return true if it is. False if it isn't
	 */
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
	}
	/**
	 * Reloads ALL configs - Custom and Bukkit default one
	 */
	public void reloadAllConfigs() {
		Main.configManager.reloadAllConfigs();
		plugin.reloadConfig();
	}
	/**
	 * Check if there are any arenas in the config
	 */
	public void scanThroughDataFileForArenas() {
		Set<String> arenasList=Main.DataConfig.getKeys(false);
		if(arenasList.size() <= 0) {
			log(wallStrings.START+""+wallStrings.NO_ARENAS_AT_ALL);
		} else {
			for(String arenaName: arenasList) {
				try {
					Main.arenas.add(new arena(
						plugin,
						arenaName,
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".wholeArenaMin")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".wholeArenaMax")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn1")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn2")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn3")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn4")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn1BoxMin")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn1BoxMax")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn2BoxMin")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn2BoxMax")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn3BoxMin")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn3BoxMax")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn4BoxMin")),
						turnStringIntoLocation(Main.DataConfig.getString(arenaName+".teamSpawn4BoxMax")),
						turnStringIntoArenaState(Main.DataConfig.getString(arenaName+".arenaState"))
					));
				} catch(Exception e) {
					log(wallStrings.START+""+wallStrings.DATA_FILE_EMPTY);
				}
			}
		}
	}
	
	/**
	 * Turn String into Arena State
	 * @param string
	 * @return arenaState
	 */
	public arenaState turnStringIntoArenaState(String string) {
		return arenaState.valueOf(string);
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
	 * Get A Colour From an Integer (1-17)
	 * @param i
	 * 		Integer to be turned into colour
	 * @return
	 * 		Color
	 */
	public Color getColor(int i) {
		Color c = null;
		if(i==1){
			c=Color.AQUA;
		}
		if(i==2){
			c=Color.BLACK;
		}
		if(i==3){
			c=Color.BLUE;
		}
		if(i==4){
			c=Color.FUCHSIA;
		}
		if(i==5){
			c=Color.GRAY;
		}
		if(i==6){
			c=Color.GREEN;
		}
		if(i==7){
			c=Color.LIME;
		}
		if(i==8){
			c=Color.MAROON;
		}
		if(i==9){
			c=Color.NAVY;
		}
		if(i==10){
			c=Color.OLIVE;
		}
		if(i==11){
			c=Color.ORANGE;
		}
		if(i==12){
			c=Color.PURPLE;
		}
		if(i==13){
			c=Color.RED;
		}
		if(i==14){
			c=Color.SILVER;
		}
		if(i==15){
			c=Color.TEAL;
		}
		if(i==16){
			c=Color.WHITE;
		}
		if(i==17){
			c=Color.YELLOW;
		}
		return c;
	}
	/**
	 * Updates Arena Pages with new arenas!
	 */
	public void updateArenaPages() {

		ArrayList<String> arenaInfo = new ArrayList<String>();
		for(arena arena: Main.arenas) {
			arenaInfo.add(ChatColor.GOLD+arena.getName()+ChatColor.BLUE+" - "+ChatColor.RED+"X: "+arena.getWholeArenaMin().getBlockX()+" Y: "+arena.getWholeArenaMin().getBlockY()+" Z: "+arena.getWholeArenaMin().getBlockZ()+"\n");
		}
		if(Main.arenaPages == null) {
			Main.arenaPages =new Pages(arenaInfo,5);
		} else {
			Main.arenaPages.changeData(arenaInfo);
		}
	}
	/**
	 * Reloads Configs
	 */
	public void reloadConfigs() {
		plugin.reloadConfig();
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
		for(Player player :plugin.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(p)) {
				return player;
			}
		}
		return null;
	}
	public void addPlayerToArena(arena a, String p, arenaTeams team) {
		Player s = getPlayerFromString(p);
		if(s == null) {
			return;
		}
		for(arena temp:Main.arenas) {
			if(temp.containsPlayer(p)) {
				temp.removePlayer(p);
			}
		}
		a.addPlayer(p, team);
	}
}
