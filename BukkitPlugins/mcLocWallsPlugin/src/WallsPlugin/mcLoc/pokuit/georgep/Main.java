package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import WallsPlugin.mcLoc.pokuit.georgep.configManager.ConfigManager;

import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.bukkit.selections.*;

public class Main extends JavaPlugin {

	WorldEditPlugin worldEdit;
	public static ArrayList<arena> arenas= new ArrayList<arena>();
	
	public static GenericFunctions f;
	
	public static int maxPlayersInArena = 16;
	
	public static Pages arenaPages = null;
	
	public static ConfigManager configManager;
	public static FileConfiguration DataConfig; 
	public static FileConfiguration SignData;
	
	public static ArrayList<storedSign> storeSigns = new ArrayList<storedSign>();
	
	public static IconMenu teamChoice;
	
	public static int countDownTillStart = 120;
	
	public void onEnable(){
		f = new GenericFunctions(this);
		
		configManager = new ConfigManager(this);
		configManager.newConfig("data");
		DataConfig = configManager.getCustomConfig("data");
		DataConfig.options().copyDefaults(true);
		
		f.scanThroughDataFileForArenas();
		
		configManager.newConfig("signData");
		SignData = configManager.getCustomConfig("signData");
		SignData.options().copyDefaults(true);
		f.readSignData();
		
		this.worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		
		
		new signEventHandler(this);
		new entityEventHandler(this);
		new playerEventHandler(this);
		
		getCommand("arena").setTabCompleter(this);
		
		f.checkConfigValues();
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("arena")) {
			Player player = (Player)sender;
			
			getLogger();
			if(args.length == 2 && args[0].equalsIgnoreCase("create")) {
				Selection selection = worldEdit.getSelection(player);
				if (selection != null) {
				    Location min = selection.getMinimumPoint();
				    Location max = selection.getMaximumPoint();
				    if(f.getArenaFromString(args[1]) == null) {
				    	Main.arenas.add(new arena(this, args[1], min, max));
				    	player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just created "+args[1]);
				    } else {
				    	player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: There is already an arena called "+args[1]+", please choose another name");
				    }
				} else {
					player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
				}
				return true;
			}
			if(args.length == 3 && args[0].equalsIgnoreCase("setState")) {
				arena a = f.getArenaFromString(args[1]);
				if(a != null) {
					try {
						String argument3 = args[2].toUpperCase();
						a.setArenaState(arenaState.valueOf(argument3));
						return true;
					} catch(IllegalArgumentException ex) {
						player.sendMessage(wallStrings.START+""+ChatColor.RED+"No State by that name!");
						return true;
					}
				} else {
					player.sendMessage(wallStrings.START+""+ChatColor.RED+"No Arena By That Name. Go Make One!");
				}
			}
			if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
				arena a = f.getArenaFromString(args[1]);
				if(a != null) {
					String arena = args[2];
					String set = args[1];
					if(set.equalsIgnoreCase("teamSpawn1")) {
						a.setTeamSpawn1(player.getLocation());
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your position");
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn2")) {
						a.setTeamSpawn2(player.getLocation());
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your position");
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn3")) {
						a.setTeamSpawn3(player.getLocation());
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your position");
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn4")) {
						a.setTeamSpawn4(player.getLocation());
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your position");
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn1Box")) {
						Selection selection = worldEdit.getSelection(player);
						if (selection != null) {
						    Location min = selection.getMinimumPoint();
						    Location max = selection.getMaximumPoint();
						    a.setTeamSpawn1Box(min, max);
							player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your current WE selection!");				
						} else {
							player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
						}
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn2Box")) {
						Selection selection = worldEdit.getSelection(player);
						if (selection != null) {
						    Location min = selection.getMinimumPoint();
						    Location max = selection.getMaximumPoint();
						    a.setTeamSpawn2Box(min, max);
							player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your current WE selection!");				
						} else {
							player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
						}
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn3Box")) {
						Selection selection = worldEdit.getSelection(player);
						if (selection != null) {
						    Location min = selection.getMinimumPoint();
						    Location max = selection.getMaximumPoint();
						    a.setTeamSpawn3Box(min, max);
							player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your current WE selection!");				
						} else {
							player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
						}
						return true;
					}
					if(set.equalsIgnoreCase("teamSpawn4Box")) {
						Selection selection = worldEdit.getSelection(player);
						if (selection != null) {
						    Location min = selection.getMinimumPoint();
						    Location max = selection.getMaximumPoint();
						    a.setTeamSpawn4Box(min, max);
							player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"Congratulations you just set "+set+"for arena "+arena+" at your current WE selection!");				
						} else {
							player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: No Selection, please make a selection with your world edit wand and try the command again.");
						}
						return true;
					}
				} else {
					player.sendMessage(wallStrings.START+""+ChatColor.RED+"Error: That Arena Doesn't exist!");
				}
			}
			if((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("list")) {
				if(args.length == 2) {
					if(arenas.size() == 0) {
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"No arenas made yet. Go off and make one!");
						return true;
					} else {
						f.updateArenaPages();
						int PageNum;
						try {
							PageNum = Integer.parseInt(args[1]);
						} catch(NumberFormatException e) {
							player.sendMessage(wallStrings.START+""+wallStrings.MUST_BE_NUMBER);
							return true;
						}
						if(PageNum > arenaPages.getPages()) {
							player.sendMessage(wallStrings.EXCEEDING_NUMBER.toString());
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
						player.sendMessage(wallStrings.START+""+ChatColor.WHITE+"No arenas made yet. Go off and make one!");
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
		f.saveArenasToDataFile();
		f.saveSignData();
	}
	public List<String> onTabComplete(CommandSender sender, Command command,
	          String alias,
	          String[] args){
	       
	    List<String> al = new ArrayList<String>();
	    if(args.length == 1) {
	    	al.add("setState");
	    	al.add("set");
	    	al.add("list");
	    }
	    if(args.length == 2) {
	    	if(args[0].equalsIgnoreCase("set")) {
	    		al.add("teamSpawn1");
	    		al.add("teamSpawn2");
	    		al.add("teamSpawn3");
	    		al.add("teamSpawn4");
	    		al.add("teamSpawn1Box");
	    		al.add("teamSpawn2Box");
	    		al.add("teamSpawn3Box");
	    		al.add("teamSpawn4Box");
	    	}
	    	if(args[0].equalsIgnoreCase("setState")) {
	    		
	    	}
	    }
	   return al;     
	}

}
