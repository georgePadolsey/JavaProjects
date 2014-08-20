package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import WallsPlugin.mcLoc.pokuit.georgep.IconMenu.OptionClickEvent;
import WallsPlugin.mcLoc.pokuit.georgep.IconMenu.OptionClickEventHandler;
@SuppressWarnings("unused")
public class arena {

	//Player Storage
	HashMap<String,arenaTeams> players = new HashMap<String,arenaTeams>();
	
	//ARENA LOCATIONS
	//Name
	private String arenaName;
	//whole arena limits
	private Location wholeArenaMin;
	private Location wholeArenaMax;
	//Team Spawns
	private Location teamSpawn1;
	private Location teamSpawn2;
	private Location teamSpawn3;
	private Location teamSpawn4;
	//Team Spawn Boxes
	private Location teamSpawn1BoxMin;
	private Location teamSpawn1BoxMax;
	private Location teamSpawn2BoxMin;
	private Location teamSpawn2BoxMax;
	private Location teamSpawn3BoxMin;
	private Location teamSpawn3BoxMax;
	private Location teamSpawn4BoxMin;
	private Location teamSpawn4BoxMax;
	
	private arenaState curArenaState;
	
	private IconMenu teamChoice;
	
	private int countDown;
	
	private Plugin plugin;

	private arena self;
	
	private Scoreboard sb = null;
	private Team team1;
	//TODO
	private Team team2;
	private Team team3;
	private Team team4;
	
	
	/**
	 * Constructor
	 * @param plugin - The current Instsance of the plugin
	 * @param arenaName - The ID - Name of the Arena
	 * @param wholeArenaMin - The Basic Size of the whole arena *the minimum*
	 * @param wholeArenaMax - The Basic Size of the whole arena *the maximum*
	 */
	public arena(Plugin plugin, 
			String arenaName,
			Location wholeArenaMin,
			Location wholeArenaMax) {
		if(arenaName!=null) {
			this.arenaName=arenaName;
		}
		if(wholeArenaMin!=null) {
			this.wholeArenaMin=wholeArenaMin;
		}
		if(wholeArenaMax!=null) {
			this.wholeArenaMax=wholeArenaMax;
		}
		this.curArenaState = arenaState.OFFLINE;
		this.plugin = plugin;	
		this.countDown = Main.countDownTillStart;
	
	}
	/**
	 * Constructor
	 * @param plugin - The current Instance of the plugin
	 * @param arenaName -The ID - Name of the arena
	 * @param wholeArenaMin - The Basic Size of the whole arena *the minimum*
	 * @param wholeArenaMax - The Basic Size of the whole arena *the maximum*
	 * @param teamSpawn1 - The Spawn of team 1 
	 * @param teamSpawn2 - The Spawn of team 2
	 * @param teamSpawn3 - The Spawn of team 3
	 * @param teamSpawn4 - The Spawn of team 4
	 * @param teamSpawn1BoxMin - The Starting area for team 1 *the minimum*
	 * @param teamSpawn1BoxMax - The Starting area for team 1 *the maximum*
	 * @param teamSpawn2BoxMin - The Starting area for team 2 *the minimum*
	 * @param teamSpawn2BoxMax - The Starting area for team 2 *the maximum*
	 * @param teamSpawn3BoxMin - The Starting area for team 3 *the minimum*
	 * @param teamSpawn3BoxMax - The Starting area for team 3 *the maximum*
	 * @param teamSpawn4BoxMin - The Starting area for team 4 *the minimum*
	 * @param teamSpawn4BoxMax - The Starting area for team 4 *the maximum*
	 * @param arenaState - The current state of the arena
	 */
	public arena(Plugin plugin,
			String arenaName,
			Location wholeArenaMin,
			Location wholeArenaMax,
			Location teamSpawn1,
			Location teamSpawn2,
			Location teamSpawn3,
			Location teamSpawn4,
			Location teamSpawn1BoxMin,
			Location teamSpawn1BoxMax,
			Location teamSpawn2BoxMin,
			Location teamSpawn2BoxMax,
			Location teamSpawn3BoxMin,
			Location teamSpawn3BoxMax,
			Location teamSpawn4BoxMin,
			Location teamSpawn4BoxMax,
			arenaState arenaState) {
		
		this.plugin = plugin;
		
		if(arenaName!=null) {
			this.arenaName=arenaName;
		}
		if(wholeArenaMin!=null) {
			this.wholeArenaMin=wholeArenaMin;
		}
		if(wholeArenaMax!=null) {
			this.wholeArenaMax=wholeArenaMax;
		}
		if(teamSpawn1!=null) {
			this.teamSpawn1=teamSpawn1;
		}
		if(teamSpawn2!=null) {
			this.teamSpawn2=teamSpawn2;
		}
		if(teamSpawn3!=null) {
			this.teamSpawn3=teamSpawn3;
		}
		if(teamSpawn4!=null) {
			this.teamSpawn4=teamSpawn4;
		}
		if(teamSpawn1BoxMin!=null) {
			this.teamSpawn1BoxMin=teamSpawn1BoxMin;
		}
		if(teamSpawn1BoxMax!=null) {
			this.teamSpawn1BoxMax = teamSpawn1BoxMax;
		}
		if(teamSpawn2BoxMin!=null) {
			this.teamSpawn2BoxMin = teamSpawn2BoxMin;
		}
		if(teamSpawn2BoxMax!=null) {
			this.teamSpawn2BoxMax = teamSpawn2BoxMax;
		}
		if(teamSpawn3BoxMin!=null) {
			this.teamSpawn3BoxMin = teamSpawn3BoxMin;
		}
		if(teamSpawn3BoxMax!=null) {
			this.teamSpawn3BoxMax = teamSpawn3BoxMax;
		}
		if(teamSpawn4BoxMin!=null) {
			this.teamSpawn4BoxMin = teamSpawn4BoxMin;
		}
		if(teamSpawn4BoxMax!=null) {
			this.teamSpawn4BoxMax = teamSpawn4BoxMax;
		}
		this.curArenaState = arenaState;
		teamChoice = new IconMenu(ChatColor.YELLOW+"Choose your Team! - "+this.getName(), 9, new OptionClickEventHandler() {

			@Override
			public void onOptionClick(OptionClickEvent event) {
				arena a = Main.f.getArenaFromString(event.getTitle().replaceAll(" ", "").split("-")[1]);
				if(event.getItemType() != Material.DIAMOND && a != null) {
					if((event.getPosition()-1)/2+1 == 1) Main.f.addPlayerToArena(a, event.getPlayer().getName(), arenaTeams.TEAM_1);
					if((event.getPosition()-1)/2+1 == 2) Main.f.addPlayerToArena(a, event.getPlayer().getName(), arenaTeams.TEAM_2);			
					if((event.getPosition()-1)/2+1 == 3) Main.f.addPlayerToArena(a, event.getPlayer().getName(), arenaTeams.TEAM_3);		
					if((event.getPosition()-1)/2+1 == 4) Main.f.addPlayerToArena(a, event.getPlayer().getName(), arenaTeams.TEAM_4);		
				} else {
					return;
				}
			}
			
		}, plugin);
		this.updateTeamChoice();
		this.countDown = Main.countDownTillStart;
		
	}
	public void makeScoreboards() {
		ScoreboardManager manager = plugin.getServer().getScoreboardManager();
		if(this.sb == null) {
			this.sb = manager.getNewScoreboard();
		}
		this.team1 = sb.registerNewTeam("Team 1");
		this.team2 = sb.registerNewTeam("Team 2");
		this.team3 = sb.registerNewTeam("Team 3");
		this.team4 = sb.registerNewTeam("Team 4");
		
		ArrayList<Team> teams = new ArrayList<Team>();
		teams.add(team1);
		teams.add(team2);
		teams.add(team3);
		teams.add(team4);
		
		for(Team t:teams) {
			t.setDisplayName(ChatColor.GREEN+"Points");
			t.setAllowFriendlyFire(false);
			t.setCanSeeFriendlyInvisibles(true);
			
		}
	}
	public void updateScoreboard() {
		
	}
	/**
	 * Gives the name of the arena
	 * @return String - The Name of the arena - The ID of the arena
	 */
	public String getName() {
		return arenaName;
	}
	
	public Location getTeamSpawn1() {
		return teamSpawn1;
	}
	public Location getTeamSpawn2() {
		return teamSpawn2;
	}
	public Location getTeamSpawn3() {
		return teamSpawn3;
	}	
	public Location getTeamSpawn4() {
		return teamSpawn4;
	}
	public Location getWholeArenaMin() {
		return wholeArenaMin;
	}
	public Location getWholeArenaMax() {
		return wholeArenaMax;
	}
	public Location getTeamSpawn1BoxMin() {
		return teamSpawn1BoxMin;
	}
	public Location getTeamSpawn1BoxMax() {
		return teamSpawn1BoxMax;
	}
	public Location getTeamSpawn2BoxMin() {
		return teamSpawn2BoxMin;
	}
	public Location getTeamSpawn2BoxMax() {
		return teamSpawn2BoxMax;
	}
	public Location getTeamSpawn3BoxMin() {
			return teamSpawn3BoxMin;
	}
	public Location getTeamSpawn3BoxMax() {
		return teamSpawn3BoxMax;
	}
	public Location getTeamSpawn4BoxMin() {
		return teamSpawn4BoxMin;
	}
	public Location getTeamSpawn4BoxMax() {
		return teamSpawn4BoxMax;
	}

	/**
	 * Adds a player to the arena
	 * @param p - String name of player (p.getName())
	 * @param team - The number of the team
	 */
	public void addPlayer(String p, arenaTeams team) {
		players.put(p, team);
	}
	/**
	 * Gets The Amount Of Players in a certain team
	 * @param team - The integer id of the team
	 * @return int - Amount of players in that team
	 */
	public int getAmountOfPlayersInTeam(arenaTeams team) {
		int playersInTeam = 0;
		for(String p:players.keySet()) {
			if(players.get(p) == team) {
				playersInTeam++;
			}
		}
		return playersInTeam;
	}
	public void removePlayer(String p) {
		players.remove(p);
	}
	public String[] getPlayers() {
		return (String[])players.keySet().toArray();
	}
	public void updateTeamChoice() {
		for(arenaTeams at:arenaTeams.values()) {
			if(at.getNum() != 5) {
				if(!teamIsFull(at)) {
					teamChoice.setOption((at.getNum()-1)*2+1, at.getItem(), at.getName(), at.getDescriptionOfIconMenu());
				} else {
					teamChoice.setOption((at.getNum()-1)*2+1, new ItemStack(Material.DIAMOND), at.getName()+" is Full", ChatColor.GOLD+"Donators get to Join Full Games");
				}	
			}
		}
	}
	public void openTeamChoice(String p) {
		updateTeamChoice();
		Player player = Main.f.getPlayerFromString(p);
		if(player == null) {
			this.removePlayer(p);
			return;
		}
		if(this.getArenaState() == arenaState.OFFLINE) {
			player.sendMessage(wallStrings.START.toString()+wallStrings.ARENA_OFFLINE);
			return;
		}
		if(this.getArenaState() == arenaState.FULL) {
			player.sendMessage(wallStrings.START.toString()+wallStrings.ARENA_FULL);
			return;
		}
		
		this.teamChoice.open(Main.f.getPlayerFromString(p));
	}
	public boolean containsPlayer(String p) {
		return players.containsKey(p);
	}
	public void setWholeArena(Location min,
			Location max) {
		this.wholeArenaMin=min;
		this.wholeArenaMax=max;
	}
	public void setTeamSpawn1(Location loc) {
		this.teamSpawn1 = loc;
	}
	public void setTeamSpawn2(Location loc) {
		this.teamSpawn2 = loc;
	}
	public void setTeamSpawn3(Location loc) {
		this.teamSpawn3 = loc;
	}
	public void setTeamSpawn4(Location loc) {
		this.teamSpawn4 = loc;
	}
	public void setTeamSpawn1Box(Location min, Location max) {
		this.teamSpawn1BoxMin = min;
		this.teamSpawn1BoxMax = max;
	}
	public void setTeamSpawn2Box(Location min, Location max) {
		this.teamSpawn2BoxMin = min;
		this.teamSpawn2BoxMax = max;
	}
	public void setTeamSpawn3Box(Location min, Location max) {
		this.teamSpawn3BoxMin = min;
		this.teamSpawn3BoxMax = max;
	}
	public void setTeamSpawn4Box(Location min, Location max) {
		this.teamSpawn4BoxMin = min;
		this.teamSpawn4BoxMax = max;
	}
	
	public arenaState getArenaState() {
		return curArenaState;
	}
	
	public void setArenaState(arenaState as) {
		this.curArenaState = as;
	}
	
	public void generateOre() {
		for(int x = (int) teamSpawn1BoxMin.getX(); x <= teamSpawn1BoxMax.getX(); x++) { // Loop 1 for the X
	        for(int y = 0; y <= teamSpawn1BoxMax.getY(); y++) {// Loop 2 for the Y
	            for(int z = (int) teamSpawn1BoxMin.getZ(); z <= teamSpawn1BoxMax.getZ();z++) {// Loop 3 for the Z
	            	Block block = teamSpawn1BoxMin.getWorld().getBlockAt(x,y,z);
	                //COAL
	                if(block.getType() == Material.STONE) {
		                if(block.getY() <132) {
		                	int i=(int) Math.round((Math.random()*1500));
		                	if(i == 1) {
		                		block.setType(Material.COAL_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1000);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() <132) {
		                				blockPlusX.getBlock().setType(Material.COAL_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		                //IRON
		                if(block.getY() < 68) {
		                	int i=(int) Math.round((Math.random()*1800));
		                	if(i == 1) {
		                		block.setType(Material.IRON_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1200);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() < 68) {
		                				blockPlusX.getBlock().setType(Material.IRON_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		                //DIAMOND
		                if(block.getY() <16) {
		                	int i=(int) Math.round((Math.random()*2100));
		                	if(i == 1) {
		                		block.setType(Material.DIAMOND_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1600);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() <16) {
		                				blockPlusX.getBlock().setType(Material.DIAMOND_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		                //GOLD
		                if(block.getY() < 34) {
		                	int i=(int) Math.round((Math.random()*1900));
		                	if(i == 1) {
		                		block.setType(Material.GOLD_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1500);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() <34) {
		                				blockPlusX.getBlock().setType(Material.GOLD_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		                //REDSTONE
		                if(block.getY() < 16) {
		                	int i=(int) Math.round((Math.random()*1700));
		                	if(i == 1) {
		                		block.setType(Material.REDSTONE_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1200);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() <16) {
		                				blockPlusX.getBlock().setType(Material.REDSTONE_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		                //LAPIS
		                if(block.getY() < 34) {
		                	int i=(int) Math.round((Math.random()*1700));
		                	if(i == 1) {
		                		block.setType(Material.LAPIS_ORE);
		                		int b = 0;
		                		Location blockPlusX=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
		                		Boolean bl=false;
		                		while(b<=8 || !bl) {
		                			int random1 = (int) Math.round(Math.random()*1600);
		                			blockPlusX=new Location(blockPlusX.getWorld(),blockPlusX.getX()+Math.round(Math.random()),blockPlusX.getY()+Math.round(Math.random()),blockPlusX.getZ()+Math.round(Math.random()));
		                			if(random1 >= 0 &&
		                					random1 <= 800 &&
		                					Main.f.blockIsWithinMinMax(blockPlusX,teamSpawn1BoxMin,teamSpawn1BoxMax) &&
		                					blockPlusX.getBlock().getType() == Material.STONE &&
		                					blockPlusX.getY() <34) {
		                				blockPlusX.getBlock().setType(Material.LAPIS_ORE);
		                			} else {
		                				bl = true;
		                			}
		                			b++;
		                		}
		                	}
		                }
		            }
	            }
	        }
		}
	}
	

	public int getAmountOfPlayersInArena() {
		return players.size();
	}
	
	public boolean teamIsFull(arenaTeams at) {
		int i = 0;
		
		for(String p:players.keySet()) {
			arenaTeams atTemp = players.get(p);
			if(atTemp == at) {
				i++;
			}
		}
		if(i >= 4) {
			return true;
		} else {
			return false;
		}
	}
	
	public void countDownToStart() {
		self = this;
		plugin.getServer().getScheduler().runTaskTimer(plugin, new BukkitRunnable() {

			int currentNum = self.countDown;
			
			@Override
			public void run() {
				if(currentNum <= 0) {
					self.start();
					this.cancel();
				}
				
				sendMessageToPlayer();
				minusOne();
				
			}
			
			public void minusOne() {
				currentNum--;
			}
			
			public void sendMessageToPlayer() {
					for(String s:self.getPlayers()) {
						
						Player p = Main.f.getPlayerFromString(s);
						if(currentNum <= 5) {
							if(currentNum != 5) p.sendMessage(wallStrings.START.toString() + currentNum);
							if(currentNum == 5) p.sendMessage(wallStrings.START+"5 Seconds left till start!");
						}
						if(currentNum %60 == 0 && currentNum > 0) {
							p.sendMessage(wallStrings.START.toString() + currentNum/60 + " minutes left till start!");
						}
				}
			}
			
		}, 20, 1);
	}
	
	public void start() {
		for(String s:getPlayers()) {
			Player p = Main.f.getPlayerFromString(s);
			int atnum = players.get(p.getName()).getNum();
			 switch (atnum) {
	            case 1:  p.teleport(teamSpawn1);
	                     break;
	            case 2:  p.teleport(teamSpawn2);
	                     break;
	            case 3:  p.teleport(teamSpawn3);
	                     break;
	            case 4: p.teleport(teamSpawn4);
	                     break;
	            case 5: p.sendMessage(wallStrings.START.toString()+ChatColor.RED+"Sorry Your team is not supported"); //TODO
	            		break;
			 }
			p.sendMessage("The Game is A-Foot!");
		}
	}
	
	
	public enum arenaTeams {
		TEAM_1(1,ChatColor.BLUE+"Team 1", ChatColor.GOLD+"Click To Join Team 1",DyeColor.BLUE),
		TEAM_2(2,ChatColor.RED+"Team 2", ChatColor.GOLD+"Click To Join Team 2",DyeColor.RED),
		TEAM_3(3,ChatColor.YELLOW+"Team 3", ChatColor.GOLD+"Click To Join Team 3",DyeColor.YELLOW),
		TEAM_4(4,ChatColor.LIGHT_PURPLE+"Team 4",ChatColor.GOLD+"Click To Join Team 4",DyeColor.PURPLE),
		SPEC(5,ChatColor.AQUA+"Spectator",ChatColor.GOLD+"Click To Spectate",Material.GOLD_INGOT);
		
		String s = null;
		String s2 = null;
		DyeColor dc = null;
		Material ma =null;
		int num = 0;
		
		arenaTeams(int num, String s, String s2, Material ma) {
			this.s = s;
			this.s2 = s2;
			this.ma= ma;
			this.num = num;
		}
}
