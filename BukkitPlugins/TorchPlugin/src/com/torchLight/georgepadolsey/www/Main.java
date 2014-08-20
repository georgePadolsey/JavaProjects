package com.torchLight.georgepadolsey.www;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener {
	
	private static ArrayList<Location> blockLocation = new ArrayList<Location>();
	private static ArrayList<Material> blockMaterial = new ArrayList <Material>();
	
	public void onEnable(){
		getLogger().info("Super Vanish Has been Activated!");
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
		    public void run() {
		    	for(Player s: getServer().getOnlinePlayers()) {
		    		Location sLol=s.getEyeLocation();
		    		Location slolFinal = sLol;
		    		getLogger().info(""+sLol.getYaw());
		    		if(sLol.getYaw() >= 315 || sLol.getYaw() <= 45)  {
		    			slolFinal.setX(slolFinal.getX()-2);
		    			getLogger().info("1");
		    		}
		    		if(sLol.getYaw() >= 46  && sLol.getYaw() <= 135)  {
		    			slolFinal.setZ(slolFinal.getZ()-2);
		    			getLogger().info("2");
		    		}
		    		if(sLol.getYaw() >= 136 && sLol.getYaw()<= 225) {
		    			slolFinal.setX(slolFinal.getX()+2);
		    			getLogger().info("3");
		    		}
		    		if(sLol.getYaw() >= 226 && sLol.getYaw() <= 314) {
		    			slolFinal.setZ(slolFinal.getZ()+2);
		    			getLogger().info("4");
		    		}
		    		for(Integer i = 0, l = blockLocation.size();i < l;i++) {
		    			Location loc = blockLocation.get(i);
		    			Material mat = blockMaterial.get(i);
		    			s.sendBlockChange(loc, mat, (byte) 0);
		    			blockLocation.remove(i);
		    			blockMaterial.remove(i);
		    		}
		    		blockLocation.add(slolFinal);
		    		blockMaterial.add(slolFinal.getBlock().getType());
		    		s.sendBlockChange(slolFinal, Material.GLOWSTONE, (byte) 0);
		    	//	sLolFinal.getBlock().setType(Material.GLOWSTONE);
		    	}
		    }    
		}, 0, 10); 
 	}
	
	public void onDisable() {
		getLogger().info("Super Vanish Has been Deactivated!");
	}
	
}
