package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class playerEventHandler implements Listener {
	
	Plugin plugin;
	
	
	public playerEventHandler(Plugin plugin) {
		this.plugin=plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(e.getPlayer().getName().equalsIgnoreCase("pokuit")) {
			e.setJoinMessage(wallStrings.START+"The Creator of the walls plugin, pokuit has joined the game");
		}
	}
	@EventHandler
	void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = (Player)e.getEntity();
		final Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        
       
        //Our random generator
        Random r = new Random();   

        //Get the type
        int rt = r.nextInt(5) + 1;
        Type type = Type.BALL;       
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
       
        //Get our random colours   
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = Main.f.getColor(r1i);
        Color c2 = Main.f.getColor(r2i);
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
        	plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        		public void run() {
    				final EnderDragon ed= (EnderDragon) fw.getWorld().spawnEntity(fw.getLocation(), EntityType.ENDER_DRAGON);
    				ed.playEffect(EntityEffect.DEATH);
    				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    	        		public void run() {
    	    				ed.remove();
    	        		}
    	        	}, 260);
        		}
        	}, rp*30);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);      
	}
}
