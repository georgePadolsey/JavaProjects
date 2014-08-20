package horseRace.georgep.pokuit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class storedSign implements Listener {
	
	private Location location;
	private SignType signType;
	private String arenaName;
	private storedSignActivateHandler handler;
	@SuppressWarnings("unused")
	private Plugin plugin;
	private boolean setUp = false;
	
	public storedSign(Location location,
			SignType signType,
			String arenaName,
			Plugin plugin,
			storedSignActivateHandler handler,
			boolean setUp) {
		this.location = location;
		this.signType = signType;
		this.arenaName = arenaName;
		this.handler = handler;
		this.plugin = plugin;
		this.setUp = setUp;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		updateSign();
	}
	
	/**
	 * Updates The Signs
	 */
	public void updateSign() {
		if(!setUp) {
			if(this.signType == SignType.SIGN){ 
				//Main.f.log(ChatColor.RED+this.location.getBlock().getType().toString());
				Sign e = (Sign) this.location.getBlock().getState();
				e.setLine(0, horseRaceStrings.SIGN_START+"");
				//Main.f.log(this.getArenaName());
				arena a = Main.f.getArenaFromString(this.getArenaName());
				if(a != null) {
					if(a.getArenaState() == arenaState.OPEN) {
						e.setLine(1, ChatColor.GREEN+a.getName());
						e.setLine(2, ChatColor.GREEN+""+ChatColor.BOLD+"[ONLINE]");
						e.setLine(3, ChatColor.BLACK+""+a.getAmountOfPlayersInArena()+"/"+Main.configData.get("MaxPlayersPerArena"));
					} else {
						e.setLine(1, ChatColor.RED+a.getName());
						e.setLine(2, ChatColor.RED+""+ChatColor.BOLD+"[OFFLINE]");
					}
				} else {
					e.setLine(1, ChatColor.RED+"No Arena");
					e.setLine(2, ChatColor.RED+"By That");
					e.setLine(3, ChatColor.RED+"Name");
				}
				e.update();
			}
		} else {
			if(this.signType == SignType.SIGN){ 
				Sign e = (Sign) this.location.getBlock().getState();
				e.setLine(0, horseRaceStrings.SIGN_START+"");
				arena a = Main.f.getArenaFromString(this.getArenaName());
				if(a != null) {
					if(a.getArenaState() == arenaState.OPEN) {
						e.setLine(1, ChatColor.GREEN+a.getName());
						e.setLine(2, ChatColor.GREEN+""+ChatColor.BOLD+"[ONLINE]");
						e.setLine(3, ChatColor.BLACK+""+a.getAmountOfPlayersInArena()+"/"+Main.configData.get("MaxPlayersPerArena"));
					} else {
						if(a.getArenaState() == arenaState.FULL) {
							e.setLine(1, ChatColor.RED+a.getName());
							e.setLine(2, ChatColor.RED+""+ChatColor.BOLD+"[FULL]");
							e.setLine(3, ChatColor.BLACK+""+a.getAmountOfPlayersInArena()+"/"+Main.configData.get("MaxPlayersPerArena"));
						}
						if(a.getArenaState() == arenaState.RUNNING) {
							e.setLine(1, ChatColor.GOLD+a.getName());
							e.setLine(2, ChatColor.GOLD+""+ChatColor.BOLD+"[RUNNING]");
							e.setLine(3, ChatColor.BLACK+""+a.getAmountOfPlayersInArena()+"/"+Main.configData.get("MaxPlayersPerArena"));
						}
					}
				} else {
					e.setLine(1, ChatColor.RED+"No Arena");
					e.setLine(2, ChatColor.RED+"By That");
					e.setLine(3, ChatColor.RED+"Name");
				}
				e.update();
			}
		}
	}
	/**
	 * Get Location of storedSign
	 * @return Location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Sets the Location the the passed in location
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Get Sign Type of stored Sign
	 * @return SignType
	 */
	public SignType getSignType() {
		return this.signType;
	}
	
	/**
	 * Sets the signs type to the passed in signType
	 * @param signType
	 */
	public void setSignType(SignType signType) {
		this.signType = signType;
	}
	
	/**
	 * Get the Arena Name
	 * @return String - Arena Name
	 */
	public String getArenaName() {
		return arenaName;
	}
	
	/**
	 * Sets the arena name to the passed in string
	 * @param String - Arena Name
	 */
	public void setArenaName(String arenaName) {
		this.arenaName = arenaName;
	}
	
	/**
	 * Deletes the Stored Sign!
	 */
	public void delete() {
		HandlerList.unregisterAll(this);
		this.arenaName = null;
		this.location = null;
		this.signType = null;
		this.handler = null;
		Main.storedSigns.remove(this);
	}
	
	public interface storedSignActivateHandler {
        public void onstoredSignActivateEvent(storedSignActivateEvent event);       
    }
	
	@EventHandler
	void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				this.signType == SignType.SIGN &&
				(event.getClickedBlock().getType() == Material.WALL_SIGN ||
				event.getClickedBlock().getType() == Material.SIGN ||
				event.getClickedBlock().getType() == Material.SIGN_POST) &&
				location.distance(event.getClickedBlock().getLocation()) < 1) {
			storedSignActivateEvent e = new storedSignActivateEvent(event.getPlayer(), this.arenaName);
			handler.onstoredSignActivateEvent(e);
			if(e.willDestroy()) {
				delete();
			}
			return;
		}
		if(event.getAction() == Action.PHYSICAL&&
				this.signType == SignType.WOOD_PRESSURE_PLATE &&
				event.getClickedBlock().getType() == Material.WOOD_PLATE &&
				event.getClickedBlock() == location.getWorld().getBlockAt(location)) {
			storedSignActivateEvent e = new storedSignActivateEvent(event.getPlayer(), this.arenaName);
			handler.onstoredSignActivateEvent(e);
			if(e.willDestroy()) {
				delete();
			}
			return;
		}
		if(event.getAction() == Action.PHYSICAL &&
				this.signType == SignType.STONE_PRESSURE_PLATE &&
				event.getClickedBlock().getType() == Material.STONE_PLATE &&
				event.getClickedBlock() == location.getWorld().getBlockAt(location)) {
			storedSignActivateEvent e = new storedSignActivateEvent(event.getPlayer(), this.arenaName);
			handler.onstoredSignActivateEvent(e);
			if(e.willDestroy()) {
				delete();
			}
			return;
		}
	}
	
    
    public class storedSignActivateEvent {
        private Player player;
        private String arenaName;
        private boolean willDestroy;
       
        public storedSignActivateEvent(Player player, String arenaName) {
            this.player = player;
            this.arenaName = arenaName;
        }
        
        public boolean willDestroy() {
        	return willDestroy;
        }
        
        public void willDestroy(boolean b) {
        	this.willDestroy = b;
        }
       
        public Player getPlayer() {
        	return player;
        }
        
        public String getArenaName() {
        	return arenaName;
        }
        
    }
    @EventHandler
    void onBlockDestroyEvent(BlockBreakEvent e) {
    	if(location.distance(e.getBlock().getLocation()) < 1) {
    		this.delete();
    	}
    }
	
}
