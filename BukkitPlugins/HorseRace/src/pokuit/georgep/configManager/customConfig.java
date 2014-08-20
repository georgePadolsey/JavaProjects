package pokuit.georgep.configManager;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class customConfig {
	
	private String name = "";
	private File file = null;
	private FileConfiguration config = null;
	
	public customConfig(String name) {
		this.name = name;
	}
	/**
	 * Returns name of config
	 * 
	 * @return Name of Config
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Change Name of config
	 * 
	 * @param String
	 */
	public void changeName(String str) {
		name = str;
	}
	
	/**
	 * Sets the file to The Passed in file
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * Gets the file
	 * 
	 * @return File
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Sets configuration file to the passed in Config
	 * 
	 * @param FileConfiguration
	 */
	
	public void setConfig(FileConfiguration config) {
		this.config = config;
	}
	
	/**
	 * Returns Configuration File
	 * @return YamlConfiguration
	 */
	public FileConfiguration getConfig() {
		return this.config;
	}
}
