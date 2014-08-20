package WallsPlugin.mcLoc.pokuit.georgep.configManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	ArrayList<customConfig> fileStorage = new ArrayList<customConfig>();
	
	private Plugin plugin;
	
	public ConfigManager(Plugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * Add a new Custom Config
	 * @param String - Name of config you wish to create
	 * @return boolean - True if created config file, false if file already by that name
	 */
	public boolean newConfig(String name) {
		if(!configByName(name)) {
			
			customConfig cc = new customConfig(name);
			
			fileStorage.add(cc);
			
			this.saveDefaultConfig(name);
			
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Reloads the config with the name passed in
	 * @param name
	 * @return boolean true if file with that name false if file not with that name
	 */
	public boolean reloadCustomConfig(String name) {
		customConfig cc = getConfigFromName(name);
		if(cc != null) {
			if (cc.getFile() == null) {
			    cc.setFile(new File(plugin.getDataFolder(), cc.getName()+".yml"));
			}
			cc.setConfig(YamlConfiguration.loadConfiguration(cc.getFile()));
			 
		    // Look for defaults in the jar
		    InputStream defConfigStream = plugin.getResource(cc.getName()+".yml");
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        cc.getConfig().setDefaults(defConfig);
		    }
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets custom config
	 * @param name of Custom configuration
	 * @return FileConfiguration
	 */
	public FileConfiguration getCustomConfig(String name) {
		customConfig cc = getConfigFromName(name);
		if(cc != null) {
			if (cc.getConfig() == null) {
				if(!this.reloadCustomConfig(cc.getName())) {
					return null;
				}
		    }
		    return cc.getConfig();
		} else {
			return null;
		}
	}
	/**
	 * Saves custom Config 
	 * @param name - Name of Config to Save
	 * @return boolean - true if could save false if could not
	 */
	public boolean saveCustomConfig(String name) {
		customConfig cc = getConfigFromName(name);
		if(cc != null) {
		    if (cc.getConfig() == null || cc.getFile()== null) {
		    	return false;
		    }
		    try {
		        getCustomConfig(cc.getName()).save(cc.getFile());
		    } catch (IOException ex) {
		       log(ChatColor.RED+"Could not save config to " + cc.getFile()+"  \n"+ex.toString());
		        return false;
		    }
		    return true;
		} else {
			return false;
		}
	} 
	/**
	 * Save Default Config
	 * 
	 * @param Name of config to save
	 * @return
	 */
	public boolean saveDefaultConfig(String name) {
		customConfig cc = getConfigFromName(name);
		if(cc != null) {
			if (cc.getConfig() == null) {
		        cc.setFile(new File(plugin.getDataFolder(), cc.getName()+".yml"));
		    }
		    if (!cc.getFile().exists()) {            
		         this.plugin.saveResource(cc.getName()+".yml", false);
		     }
		    return true;
		} else {
			return false;
		}
	}
	/**
	 * Checks if theres a config by that name
	 * @param String - Name of config you wish to find
	 * @return boolean - True if there is a config by that name. False otherwise
	 */
	public boolean configByName(String name) {
		for(customConfig cc:fileStorage){
			if(cc.getName() == name) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Gets the config from a name
	 * @param String - Name of config you wish to find
	 * @return customConfig - The Config || returns null if no config by that name
	 */
	public customConfig getConfigFromName(String Name) {
		for(customConfig cc:fileStorage){
			if(cc.getName() == Name) {
				return cc;
			}
		}
		return null;
	}
	/**
	 * Reload all Configs, except default bukkit config
	 */
	public void reloadAllConfigs() {
		for(customConfig cc:fileStorage) {
			this.reloadCustomConfig(cc.getName());
		}
	}
	/**
	 * Log Something to the console with colours
	 * @param o
	 *   A thing to Log to the console - Preferably a string or an object that can easily be turned into a string
	 */
	private void log(Object o) {
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		console.sendMessage(o+"");
	}
}
