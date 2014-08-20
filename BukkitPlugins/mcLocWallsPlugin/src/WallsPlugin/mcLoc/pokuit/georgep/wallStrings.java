package WallsPlugin.mcLoc.pokuit.georgep;

import org.bukkit.ChatColor;

public enum wallStrings {
	START(ChatColor.DARK_GREEN+"["+ChatColor.GOLD+"Walls"+ChatColor.DARK_GREEN+"] "+ChatColor.RESET),
	NO_ARENA(ChatColor.RED+"Error: No arena by that name... go create one!"),
	NO_ARENAS_AT_ALL(ChatColor.RED+"Error: There are no arenas... go create some!"),
	NO_SELECTION(ChatColor.RED+"Error: You have no selection, go select something with your world edit wand and try this command again"),
	MUST_BE_NUMBER(ChatColor.RED+"Error: You must enter a numerical value!"),
    EXCEEDING_NUMBER(ChatColor.RED+"Error: This Number Exceeds the max number"),
    DATA_FILE_EMPTY(ChatColor.RED+"Error: Data File is Not set up Properly, Stop the server check/delete the data file and restart!"),
	ARENA_FULL(ChatColor.RED+"Sorry this arena is full. To Join full arenas become a donator today!"),
	ARENA_OFFLINE(ChatColor.RED+"Sorry this arena is offline. Check back later!"),
	ARENA_VIP_ONLY(ChatColor.AQUA+"Sorry This arena is VIP only. Become a donater today!"),
	REPORTERROR_START(ChatColor.RED+"[SEVERE] [ERROR] There has been an extreme error in the plugin \"walls\" please report the following error to the developer:"),
	REPORTERROR_END(ChatColor.RED+"=========END========="),
	SIGN_DATA_FILE_ERROR(ChatColor.RED+"An error has occured in the sign data file please delete the data file as it has been corrupted!"),
	SIGN_DATA_FILE_EMPTY(ChatColor.RED+"Error: Sign Data is Not set up Properly, Stop the server delete the config and restart!"),
    CONFIG_EMPTY(ChatColor.RED+"Error: Config is Not set up Properly, Stop the server delete the config and restart!");

    
    String s;
    
    wallStrings(String s) {
    	this.s = s;
    }
    
    @Override
    public String toString() {
    	return s;
    }
}
