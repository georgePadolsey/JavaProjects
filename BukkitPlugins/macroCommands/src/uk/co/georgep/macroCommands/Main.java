package uk.co.georgep.macroCommands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener {
	
	private static HashMap<String, String> storeMacros = new HashMap<String, String>();
	private static HashMap<String, macroStorer> runnableMacros = new HashMap<String, macroStorer>();
	
	public SimpleConfigManager manager;
	 
    public SimpleConfig config;
    public SimpleConfig data;
	
	
	public void processMacro(String name) {
		String macro=storeMacros.get(name);
		String[] macroSplit;
		if(macro.contains(":")) {
			macroSplit=macro.split(":");
		} else {
			macroSplit = new String[1];
			macroSplit[0] = macro;
		}
		macroStorer ms = new macroStorer();
		
		for(String msd:macroSplit) {
			String msde = msd;
			if(!msd.isEmpty() && Character.isSpaceChar(msd.charAt(0))) {
				msde = msd.substring(1);
			}
			ms.add(msde);
		}
		
		runnableMacros.put(name, ms);
	}
	public void runMacro(String name, Player s) {
		macroStorer  ms = runnableMacros.get(name);
		for(String str:ms.getStorage()) {
			if(str.contains("*sms*")) {
				str = ChatColor.translateAlternateColorCodes('&', str);
				str = str.replace("*sms*", "");
				s.sendMessage(str);
				return;
			} 
			s.performCommand(str);
		}
	}
	
	public void removeMacro(String name) {
		if(runnableMacros.containsKey(name)) {
			runnableMacros.remove(name);
		}
		if(storeMacros.containsKey(name)){
			storeMacros.remove(name);
		}
	}
	
	public String[] returnInfo(Integer num, String string) {
		if(string == "credits") {
		
		}
		if(num == 1) {
			String[] help = {ChatColor.GOLD+"============== Macro Help ==============",
				ChatColor.GREEN+"To List Macro Commands and usage Type /macro commands ",
				ChatColor.GREEN+"To Show Makers of Plugin and Addition information type /macro information",
				ChatColor.GREEN+"To have a complete tutorial of the plugin type /macro 2",
				ChatColor.GREEN+"Please Enjoy the Plugin, Good luck :)",                 
				ChatColor.GOLD+"====== Help pg 1/3 for page 2 type /macro 2 ======"
			};
			return help;
		}
		if(num == 2) {
			String[] help = {
			    ChatColor.GOLD+"============== Macro Help ==============",
				ChatColor.GREEN+"Ok Lets Start with basic setting of macros:",
				ChatColor.GREEN+"To set a macro do /macro set <name> <command1> : <command2> : etc.",
				ChatColor.GREEN+"For Example /macro *console*set day time set day : *sms*[Gold]Here Comes the Sun",
				ChatColor.GREEN+"As you noticed I put *sms* and [Gold}. *sms* sends a message back to sender of macro",
				ChatColor.GREEN+"[Gold]Make the colour of the text gold. You may of also noticed *console*...",                 
				ChatColor.GOLD+"==== Help pg 2/3 for page 2 type /macro 3 ===="
			};
			return help;
		}
		String[] help = {ChatColor.RED+""+ChatColor.ITALIC+"No Help Pages By That Number :/"};
		return help;
	}
	
	
	public void onEnable(){
        String[] header = {"Macro Commands Config", "If you have troubles check out", "bukkit.org..."};
        String[] headerForData = {"Data Files for Macro Commands","Please do not touch","Unless you really have to delete a macro or change it"};
        
        this.manager = new SimpleConfigManager(this);
 
        this.config = manager.getNewConfig("config.yml", header);
        this.data = manager.getNewConfig("data.yml",headerForData);
        
        for(String s : this.data.getKeys()) {
        	storeMacros.put(s, this.data.getString(s));
        	processMacro(s);
        }

 	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){	
		if(sender instanceof Player ) {
			Player s = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("macro")) {
					if(args.length == 0) {
						sender.sendMessage(returnInfo(1, null));
						return true;
					} else {
						if(args.length == 1) {
							if(args[0].equalsIgnoreCase("list")) {
								s.sendMessage(ChatColor.GOLD+"▂▃▅▇█▓▒░۩۞۩ "+ChatColor.RED+"Macros"+ChatColor.GOLD+" ۩۞۩░▒▓█▇▅▃▂");
								
								for(String str:storeMacros.keySet()) {
									s.sendMessage(ChatColor.AQUA+"   "+str+" - "+storeMacros.get(str)+"\n");
								}
								s.sendMessage(ChatColor.GOLD+"▂▃▅▇█▓▒░۩۞۩ "+ChatColor.RED+"End"+ChatColor.GOLD+" ۩۞۩░▒▓█▇▅▃▂");
								
								return true;
							}
							try {
								   Integer number = Integer.parseInt(args[0]);
									sender.sendMessage(returnInfo(number, null));
									return true;
							} catch (NumberFormatException e) {
								sender.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Error Incorrect Command Usage. Type /macro for help");
							}
							
						}
						if(args.length == 2) {
							if(args[0].equalsIgnoreCase("set")) {
								sender.sendMessage(ChatColor.GOLD+""+ChatColor.ITALIC+"Procced to enter commands with /macro add <command1> : <command2> : etc.");
								return true;
							} else {
								if(args[0].equalsIgnoreCase("run")) {
									runMacro(args[1],s);
									return true;
								}
								if(args[0].equalsIgnoreCase("remove")) {
									if(storeMacros.containsKey(args[1])) {
										removeMacro(args[1]);
									}
								}
							}
						}
						if(args.length >= 3) {
							if(args[0].equalsIgnoreCase("set")){
								for(Integer i = 0,l = args.length;i<l;i++) {
									if(i >= 2) {
										if(storeMacros.containsKey(args[1])) {
											storeMacros.put(args[1], storeMacros.get(args[1])+" "+args[i]);
										} else {
											storeMacros.put(args[1], args[i]);
										}
										
									}
								}
								processMacro(args[1]);
								return true;
							}
						}
					}
				}
			return false;
		}
		return false;
	}
	public void onDisable() {
		for(String sm:storeMacros.keySet()) {
			this.data.set(sm,storeMacros.get(sm));
		}
		this.config.saveConfig();
		this.data.saveConfig();
	}
}
