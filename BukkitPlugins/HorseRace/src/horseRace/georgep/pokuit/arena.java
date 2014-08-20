package horseRace.georgep.pokuit;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class arena implements Listener {

	//Player Storage
	ArrayList<String> players = new ArrayList<String>();
	
	//ARENA LOCATIONS
	//Name
	private boolean isCounting;
	private String arenaName;
	
	private Location startSpawn;
	
	private BukkitTask countDownScheduler;
	
	private arenaState curArenaState;
	
	private int countDown;
	
	private Plugin plugin;

	private arena self;
	
	private ArrayList<String> orderOfPeopleWhoHaveWon = new ArrayList<String>();
	
	
	private int amountOfPlayersInArena = 0;
	//@SuppressWarnings("unused")
	//private Scoreboard sb = null;

	/**
	 * Constructor
	 * @param plugin - The current Instance of the plugin
	 * @param arenaName - The ID - Name of the Arena
	 */
	public arena(Plugin plugin, 
			String arenaName,
			Location startSpawn) {
		Main.f.debug(arenaName);
		if(arenaName!=null) {
			this.arenaName=arenaName;
		}
		this.startSpawn = startSpawn;
		
		this.curArenaState = arenaState.OPEN;
		this.plugin = plugin;	
		this.countDown = (Integer)Main.configData.get("timeToWaitTillStart");
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	/**
	 * Constructor
	 * @param plugin - The current Instance of the plugin
	 * @param arenaName -The ID - Name of the arena
	 * @param startSpawn - The location where people start
	 * @param arenaState - The current state of the arena
	 */
	public arena(Plugin plugin,
			String arenaName,
			Location startSpawn,
			arenaState arenaState) {
		
		this.plugin = plugin;
		
		if(arenaName!=null) {
			this.arenaName=arenaName;
		}
		if(startSpawn != null) {
			this.startSpawn = startSpawn;
		}
		this.curArenaState = arenaState;
		this.countDown = (Integer)Main.configData.get("timeToWaitTillStart");
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	/*public void makeScoreboards() {
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
		
	}*/
	public void setStartSpawn(Location startSpawn) {
		this.startSpawn = startSpawn;
	}
	
	public Location getStartSpawn() {
		return this.startSpawn;
	}
	/**
	 * Gives the name of the arena
	 * @return String - The Name of the arena - The ID of the arena
	 */
	public String getName() {
		return arenaName;
	}

	/**
	 * Adds a player to the arena
	 * @param p - String name of player (p.getName())
	 * @param team - The number of the team
	 */
	public void addPlayer(String p) {
		Main.f.debug("Added player "+p+"to Arena");
		Player player = Main.f.getPlayerFromString(p);
		Main.f.updateScoreBoard(p);
		
		if(this.getArenaState() != arenaState.RUNNING ) {
			if(players.size() < (Integer)Main.configData.get("MaxPlayersPerArena") ||
					player.hasPermission("horseRace.vip")) {
				
				if(!Main.playerHorseInfo.containsKey(p) &&
						Main.stableLocation != null) {
					Main.f.debug("hello:"+this.getName());
					Main.playerOrigins.put(p, player.getLocation());
					Main.f.goToStable(p,this.getName());

				} else {
					
					Main.f.debug("Player is now added to the arena permanantly");
					if(!Main.playerOrigins.containsKey(p)) {
						Main.playerOrigins.put(p, player.getLocation());
					}
					players.add(p);
					
					
					
					player.teleport(startSpawn);
					Entity Ehorse = player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
					Horse horse = (Horse) Ehorse;
					
					Ehorse.setPassenger(player);
					
					HorseInfo horI = Main.playerHorseInfo.get(p);
					try {
						horse.setStyle(horI.getHorseStyle());
					} catch(NullPointerException e) {};
					try {
						horse.setColor(horI.getHorseColor());
					} catch(NullPointerException e) {};
					try{
						horse.setDomestication(horse.getMaxDomestication());
					} catch(NullPointerException e) {};
					Inventory hinv = horse.getInventory();
					
					hinv.addItem(new ItemStack(Material.SADDLE));
					
					checkAmountOfPlayers();
					
				}
			} else {
				checkAmountOfPlayers();
				player.sendMessage(horseRaceStrings.START.toString()+ChatColor.RED+"Arena Is Full Sorry :/ Vip's can join full games");
			}
		} else {
			player.sendMessage(horseRaceStrings.START.toString()+ChatColor.RED+"Arena Is Running Sorry :/");
		}
	}

	public void removePlayer(String p) {
		players.remove(p);
		Main.f.clearScoreBoard(p);
		if(Main.f.getPlayerFromString(p).isInsideVehicle() &&
				Main.f.getPlayerFromString(p).getVehicle() instanceof Horse) {
			Player player = Main.f.getPlayerFromString(p);
			Horse horse = (Horse)player.getVehicle();
			horse.eject();
			horse.remove();
			player.teleport(Main.playerOrigins.get(p));
		}
		Main.playerOrigins.remove(p);
		checkAmountOfPlayers();
	}
	
	public void checkAmountOfPlayers() { 
		if(players.size() >= 2 && !isCounting && getArenaState() != arenaState.RUNNING ) { 
			countDownToStart();
			this.isCounting = true;
		}
		if(players.size() >= (Integer)Main.configData.get("MaxPlayersPerArena")) {
			setArenaState(arenaState.FULL);
		}
		for(storedSign ss:Main.f.getSignsByArenaName(getName())) {
			ss.updateSign();
		}
	}
	public String[] getPlayers() {
		return Arrays.copyOf(players.toArray(), players.toArray().length, String[].class);
	}
	public boolean containsPlayer(String p) {
		return players.contains(p);
	}
	public arenaState getArenaState() {
		return curArenaState;
	}
	
	public void setArenaState(arenaState as) {
		this.curArenaState = as;
	}

	public int getAmountOfPlayersInArena() {
		return players.size();
	}
	
	public void countDownToStart() {
		self = this;
		this.countDownScheduler = plugin.getServer().getScheduler().runTaskTimer(plugin, new BukkitRunnable() {

			int currentNum = self.countDown;
			
			@Override
			public void run() {
				if(currentNum <= 0) {
					if(self.getAmountOfPlayersInArena() >= 2) { 
						
						self.cancelCountDownEvent();
						amountOfPlayersInArena = players.size();
						self.start();

					} else {
						sendMessageToPlayer(horseRaceStrings.START.toString()+horseRaceStrings.NOT_ENOUGH_PLAYERS_IN_THE_ARENA);
						currentNum = self.countDown;
					}
				} else {
					if(currentNum < 5) {
						sendMessageToPlayer(horseRaceStrings.START.toString() + currentNum);
						sendSoundToPlayer(Sound.BURP,1,8);
					}
					if(currentNum == 5) sendMessageToPlayer(horseRaceStrings.START+"5 Seconds left till start! Remember to Line up on the Start Line!");
				
					if(currentNum %60 == 0 && currentNum > 0) {
						sendMessageToPlayer(horseRaceStrings.START.toString() + currentNum/60 + " minutes left till start!");
					}
					if(currentNum %60 != 0 && currentNum > 0 && currentNum %30 == 0) {
						sendMessageToPlayer(horseRaceStrings.START.toString() + Math.floor(currentNum/60) + " minutes and 30 seconds left till start!");
					}
				}
				minusOne();
			}
				
			public void minusOne() {
				currentNum--;
			}
			
			public void sendMessageToPlayer(String Message) {
				for(String s:self.getPlayers()) {
					
					Player p = Main.f.getPlayerFromString(s);
					p.sendMessage(Message);
				}
			}
			public void sendSoundToPlayer(Sound sound, int v, int p) {
				for(String so:self.getPlayers()) {
					
					Player pl = Main.f.getPlayerFromString(so);
					
					pl.playSound(pl.getLocation(), sound, v, p);
				}
			}
		}, 1, 20);
	}
	private void cancelCountDownEvent() {
		
		self.isCounting = false;
		this.countDownScheduler.cancel();
	}
	
	public void start() {
		setArenaState(arenaState.RUNNING);
		for(String s:getPlayers()) {
			Player p = Main.f.getPlayerFromString(s);
			
			
			p.sendMessage(horseRaceStrings.START+ChatColor.GREEN.toString()+ChatColor.BOLD+"GO!");
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 2);
			
			
		}
	}
	
	public void sendPlayersHome() {
		try {
			for(String p : players) {
				
				removePlayer(p);
				
			}
		} catch(NullPointerException e) {}
		if(getArenaState() == arenaState.RUNNING || getArenaState() == arenaState.FULL) setArenaState(arenaState.OPEN);
	}
	
	public void clearPlayerOriginsForThisArena() {
		try{	
			for(String p  : players) {
				Main.playerOrigins.remove(p);
			}
		} catch (NullPointerException e) {}
	}
	
	public void congratulateWinners() {
		Main.f.debug("Congratulating winners");
		for(String p : orderOfPeopleWhoHaveWon) {
			if(!Main.playerScore.containsKey(p)) Main.playerScore.put(p, 0);
			int num = orderOfPeopleWhoHaveWon.indexOf(p)+1;
			if(num == 1) {
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"CONGRATULATIONS! You Came First!");
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"10 Points Have been added to your score!");
				
				Main.playerScore.put(p, Main.playerScore.get(p)+10);
			}
			if(num == 2) {
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"CONGRATULATIONS! You Came Second!");
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"8 Points Have been added to your score!");
				Main.playerScore.put(p, Main.playerScore.get(p)+8);
			}
			if(num == 3) {
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"CONGRATULATIONS! You Came Third!");
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"5 Points Have been added to your score!");
				Main.playerScore.put(p, Main.playerScore.get(p)+5);
			}
			if(num >= 4) {
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"CONGRATULATIONS! You came "+num+" out of "+amountOfPlayersInArena+"!");
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.GREEN+"1 Point Has been added to your score!");
				Main.playerScore.put(p, Main.playerScore.get(p)+1);
			
			}
			if(num ==orderOfPeopleWhoHaveWon.size()) {
				Main.f.getPlayerFromString(p).sendMessage(horseRaceStrings.START.toString()+ChatColor.RED+"But... You Also Came Last :(");
			}
		}
	}
	
	public void addWinner(String p) {
		
		
		
		Main.f.debug("added winner "+p);
		
	
		
		for(String people: orderOfPeopleWhoHaveWon) {
			Main.f.debug(people);
		}
		
		if(!orderOfPeopleWhoHaveWon.contains(p)) orderOfPeopleWhoHaveWon.add(p);
		
		Main.f.debug((orderOfPeopleWhoHaveWon.size() >= players.size())+"");
		
		Boolean b = orderOfPeopleWhoHaveWon.size() >= amountOfPlayersInArena;
		
		removePlayer(p);
		
		if(b) {
			congratulateWinners();
			
			orderOfPeopleWhoHaveWon.clear();
			
			sendPlayersHome();
			
			
			clearPlayerOriginsForThisArena();
			
			
			
			players.clear();
			
			
			
			
			setArenaState(arenaState.OPEN);
			
			checkAmountOfPlayers();
		}
		
	}
	public boolean getIfPlayerIsWinner(String p) {
		return orderOfPeopleWhoHaveWon.contains(p);
	}
	
	@EventHandler
	void onPlayerQuitEvent(PlayerQuitEvent e) {
		if(players.contains(e.getPlayer().getName())) {
			removePlayer(e.getPlayer().getName());
		}
	}
}