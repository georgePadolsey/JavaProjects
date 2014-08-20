package superHeroFight.georgep.pokuit;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public class tempBlockReplace {
	//time
	private Integer timePast=0;
	private Integer timeToExpire;
	//Location
	private Location loc;
	//Block That it was
	private Material blockThatItWas;
	private Byte blockThatItWasData;
	//Sound && Effects
	private Sound soundWhenDestroyed;
	private Effect effectWhenDestroyed;
	//Plugin
	private Plugin plugin;
	
	public tempBlockReplace(Location loc, Material m, Byte d, Integer time, Plugin plugin, Sound s, Effect e) {
		this.loc=loc;
		this.timeToExpire=time;
		this.plugin=plugin;
		if(e != null && s != null) {
			plugin.getLogger().warning(ChatColor.RED+"CAN NOT USE SOUND AND EFFECT AT SAME TIME ON TEMPBLOCKREAPLCE");
		}
		this.effectWhenDestroyed = e;
		this.soundWhenDestroyed = s;
		
		this.blockThatItWas = loc.getBlock().getType();
		this.blockThatItWasData = loc.getBlock().getData();
		loc.getBlock().setType(m);
		loc.getBlock().setData(d);
	}
	public void run() {
		if(timePast >= timeToExpire && timePast != -1) {
			loc.getBlock().setType(blockThatItWas);
			loc.getBlock().setData(blockThatItWasData);
			if(this.soundWhenDestroyed != null && this.effectWhenDestroyed==null) { 
				loc.getWorld().playSound(loc, soundWhenDestroyed, (float) 0.1, 1);
			}
			if(this.effectWhenDestroyed != null && this.soundWhenDestroyed == null) {
				loc.getWorld().playEffect(loc, effectWhenDestroyed, 0);
			}
			timePast = -1;
		}
		if(timePast >= 10000) {
			plugin.getLogger().warning(ChatColor.RED+"Time must be smaller then 10000 in temp block changes");
		}
		if(timePast != -1) {
			timePast++;
		}
	}
}
