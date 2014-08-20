package threeBans.regiusCraft.pokuit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    private static HashMap<String, Integer> banCount = new HashMap<String, Integer>();

    public void onEnable() {
        getLogger().info("Three Bans Has been Activated!");


    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("threeban")) {
                Player s = (Player) sender; //Gets the Sender
                if (!banCount.containsKey(s.getName())) {
                    banCount.put(s.getName(), 2);
                } else {
                    int n = banCount.get(s.getName());
                    banCount.put(s.getName(), n - 1);
                }
                int n = banCount.get(s.getName());
                if (n <= 0) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tban " + s.getName() + " 1 d You Have Failed the spawn test 3 times, Try Again Tomorrow!");
                    banCount.put(s.getName(), 3);
                }
                sender.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You have Failed spawn test. Please check the signs again before repeating. You have " + banCount.get(s.getName()) + " tries Left. Use them wisely");
                return true;
            }
        } else {
            if (!cmd.getName().equalsIgnoreCase("tbs") && !cmd.getName().equalsIgnoreCase("tbg")) {
                getLogger().info(ChatColor.RED + "" + ChatColor.ITALIC + "This Command Can only be run In Game");
            }
        }
        if (cmd.getName().equalsIgnoreCase("tbs") && args.length > 0) {

            banCount.put(args[0], args.length >= 2 ? Integer.parseInt(args[1]) : 1000);

            sender.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Sucessfully set setting the warnings of " + args[0]);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("tbg") && args.length >= 1) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + args[0] + " Has " + banCount.get(args[0]) + " Tries Left");
            return true;
        }
        return false;
    }


    public void onDisable() {
        getLogger().info("Three Bans Has been Deactivated!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent evt) {
        Player p = evt.getPlayer();
        if (!banCount.containsKey(p.getName())) {
            banCount.put(p.getName(), 3);
        }
    }

}
