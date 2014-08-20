package chatListener.regiusCraft.pokuit;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener {
	
	private static HashMap<String, ArrayList<String>> WordsToListenFor = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, Integer> wordCounter = new HashMap<String, Integer>();
	public void storeWordsToListenForInConfig() {
		for(String Word:WordsToListenFor.keySet()) {
			if(this.getConfig().getCharacterList("data."+Word).c)
			this.getConfig().set("data."+Word,WordsToListenFor.get(Word));
			this.saveConfig();
		}
	}
	
	public void onEnable() {
		getLogger().info("Chat Listener has now been activated!");
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		this.getConfig().options().header("Chat Listener Config");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			Player s = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("listenfor") && args.length ==2) {
				if(args[0].equalsIgnoreCase("add")) {
					String args1toLowerCase=args[1].toLowerCase();
					if(WordsToListenFor.containsKey(args1toLowerCase)) {
						ArrayList<String> players = WordsToListenFor.get(args1toLowerCase);
						players.add(s.getName());
						WordsToListenFor.put(args1toLowerCase, players);
						return true;
					} else {
						ArrayList<String> players = new ArrayList<String>();
						players.add(s.getName());
						WordsToListenFor.put(args1toLowerCase, players);
						return true;
					}
				}
			}
		}
		return false;
	}
	public void onDisable() {
		storeWordsToListenForInConfig();
		getLogger().info("Scary Shrap Has been Deactivated!");
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		for(String word:WordsToListenFor.keySet()) {
			String message = event.getMessage().toLowerCase();
			if(message.indexOf(word) != -1) {
				for(String playerName:WordsToListenFor.get(word)) {
					Player s = getServer().getPlayer(playerName);
					s.playSound(s.getLocation(), Sound.LEVEL_UP, 1, 3);
				}
		    }
		}
	}
}
