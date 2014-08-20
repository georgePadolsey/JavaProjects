package superHeroFight.georgep.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class arena implements Listener {

	//HashMaps
	
	private HashMap<String,Integer> players=new HashMap<String,Integer>();
	private HashMap<Location,Material> blocksToReplace= new HashMap<Location,Material>();
	
	//ArayLists
	
	private ArrayList<String> EditingPlayers=new ArrayList<String>();
	
	//Variables
	
	private int ArenaNum;
	private String name;
	private Location spawnChooseTeam;
	private Location spawnTeam1;
	private Location spawnTeam2;
	private Location specSpawn;
	private Location spawnChooseTeam1;
	private Location spawnChooseTeam2;
	//private Plugin plugin;
	
	public arena(Integer ArenaNum, Location spawnChooseTeam, Location spawnTeam1, Location spawnTeam2, Location specSpawn, Location spawnChooseTeam1, Location spawnChooseTeam2, Plugin plugin, String name) {
		this.ArenaNum=ArenaNum;
		this.spawnChooseTeam=spawnChooseTeam;
		this.spawnTeam1=spawnTeam1;
		this.spawnTeam2=spawnTeam2;
		this.specSpawn=specSpawn;
		this.spawnChooseTeam1=spawnChooseTeam1;
		this.spawnChooseTeam2=spawnChooseTeam2;
		this.name = name;
		//this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		colourSpawnBlocks();
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public HashMap<String,Integer> getPlayers() {
		return players;
	}
	public void addPlayer(Player player) {
		players.put(player.getName(),0);
		player.teleport(spawnChooseTeam.getBlock().getLocation());
	}
	public void removePlayer(Player player) {
		players.remove(player);
	}
	public Integer getArenaNum() {
		return ArenaNum;
	}
	public void setArenaNum(Integer i) {
		ArenaNum = i;
	}
	public Location getSpawnChooseTeam() {
		return spawnChooseTeam;
	}
	public void setSpawnChooseTeam(Location l) {
		spawnChooseTeam=l;
		colourSpawnBlocks();
	}
	public Location getSpawnChooseTeam1() {
		return spawnChooseTeam1;
	}
	public void setSpawnChooseTeam1(Location l) {
		spawnChooseTeam1=l;
		colourSpawnBlocks();
	}
	public Location getSpawnChooseTeam2() {
		return spawnChooseTeam2;
	}
	public void setSpawnChooseTeam2(Location l) {
		spawnChooseTeam2=l;
		colourSpawnBlocks();
	}
	public Location getSpawnTeam1() {
		return spawnTeam1;
	}
	public void setSpawnTeam1(Location l) {
		spawnTeam1=l;
		colourSpawnBlocks();
	}
	public Location getSpawnTeam2() {
		return spawnTeam2;
	}
	public void setSpawnTeam2(Location l) {
		spawnTeam2=l;
		colourSpawnBlocks();
	}
	public Location getSpecSpawn() {
		return specSpawn;
	}
	public void setSpecSpawn(Location l) {
		specSpawn=l;
		colourSpawnBlocks();
	}
	public void setPlayerTeam(Player player, Integer team) {
	//	player.put();
	}
	public ArrayList<String> getEditingPlayers() {
		return EditingPlayers;
	}
	public void addEditingPlayer(Player s) {
		EditingPlayers.add(s.getName());
	}
	public void removeEditingPlayer(Player s) {
		EditingPlayers.remove(s.getName());
	}
	@EventHandler(priority=EventPriority.MONITOR)
	void onPlayerMoveEvent(PlayerMoveEvent event) {
		if(event.getTo().getBlock().getLocation().distance(this.spawnChooseTeam1) <= 1 && !EditingPlayers.contains(event.getPlayer().getName())) {
			players.put(event.getPlayer().getName(),1);
			event.getPlayer().teleport(spawnTeam1);
		}
		if(event.getTo().getBlock().getLocation().distance(this.spawnChooseTeam2) <= 1 && !EditingPlayers.contains(event.getPlayer().getName())) {
			players.put(event.getPlayer().getName(),2);
			event.getPlayer().teleport(spawnTeam2);
		}
	}
	public void replaceSpawnBlocks() {
		HashSet<Location> locationList = new HashSet<Location>();
		locationList.addAll(blocksToReplace.keySet());
		for(Location loc:locationList) {
			loc.getBlock().setType(blocksToReplace.get(loc));
			blocksToReplace.remove(loc);
		}
	}
	public void colourSpawnBlocks() {
		//RELOAD CURRENT BLOCKS
		
		replaceSpawnBlocks();
		
		//SET BLOCKS TO WOOL
	    //Set blockBelowSpawnChooseTeam to purple wool and save it
		Block blockBelowSpawnChooseTeam= new Location(spawnChooseTeam.getWorld(), spawnChooseTeam.getBlockX(), spawnChooseTeam.getBlockY()-1, spawnChooseTeam.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpawnChooseTeam.getLocation(), blockBelowSpawnChooseTeam.getType());
		blockBelowSpawnChooseTeam.setType(Material.WOOL);
		blockBelowSpawnChooseTeam.setData((byte) 10);
		
		//Set blockBelowSpawnChooseTeam1 to blue wool and save it
		Block blockBelowSpawnChooseTeam1 = new Location(spawnChooseTeam1.getWorld(), spawnChooseTeam1.getBlockX(), spawnChooseTeam1.getBlockY()-1, spawnChooseTeam1.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpawnChooseTeam1.getLocation(), blockBelowSpawnChooseTeam1.getType());
		blockBelowSpawnChooseTeam1.setType(Material.WOOL);
		blockBelowSpawnChooseTeam1.setData((byte) 11);

		//Set blockBelowSpawnChooseTeam2 to red wool and save it
		Block blockBelowSpawnChooseTeam2 = new Location(spawnChooseTeam2.getWorld(), spawnChooseTeam2.getBlockX(), spawnChooseTeam2.getBlockY()-1, spawnChooseTeam2.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpawnChooseTeam2.getLocation(), blockBelowSpawnChooseTeam2.getType());
		blockBelowSpawnChooseTeam2.setType(Material.WOOL);
		blockBelowSpawnChooseTeam2.setData((byte) 14);

		//Set blockBelowSpecSpawn to Glass and save it
		Block blockBelowSpecSpawn = new Location(specSpawn.getWorld(), specSpawn.getBlockX(), specSpawn.getBlockY()-1, specSpawn.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpecSpawn.getLocation(), blockBelowSpecSpawn.getType());
		blockBelowSpecSpawn.setType(Material.GLASS);

		//Set blockBelowSpawnTeam1 to blue wool and save it
		Block blockBelowSpawnTeam1 = new Location(spawnTeam1.getWorld(), spawnTeam1.getBlockX(), spawnTeam1.getBlockY()-1, spawnTeam1.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpawnTeam1.getLocation(), blockBelowSpawnTeam1.getType());
		blockBelowSpawnTeam1.setType(Material.WOOL);
		blockBelowSpawnTeam1.setData((byte) 11);	

		//Set blockBelowSpawnTeam2 to red wool and save it
		Block blockBelowSpawnTeam2 = new Location(spawnTeam2.getWorld(), spawnTeam2.getBlockX(), spawnTeam2.getBlockY()-1, spawnTeam2.getBlockZ()).getBlock();
		blocksToReplace.put(blockBelowSpawnTeam2.getLocation(), blockBelowSpawnTeam2.getType());
		blockBelowSpawnTeam2.setType(Material.WOOL);
		blockBelowSpawnTeam2.setData((byte) 14);
		
	}
}
