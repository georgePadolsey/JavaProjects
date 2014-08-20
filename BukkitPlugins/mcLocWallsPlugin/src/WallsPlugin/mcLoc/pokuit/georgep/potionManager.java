package WallsPlugin.mcLoc.pokuit.georgep;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_6_R1.Packet41MobEffect;
import net.minecraft.server.v1_6_R1.Packet42RemoveMobEffect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import packetUtils.pokuit.otherPersonMeh.forums.bukkit.org.threads.tutorial.utilizing.the.boss.health.bar.onefiveeightzerooneeight.General;

public class potionManager {
	//Stores the players recently used effects
    private static Set<Integer> playersBuffs = new HashSet<Integer>();
 
    //Adds the potion effect without the graphical bubbles
    public static void addPotionEffectNoGraphic(Player p, PotionEffect pe) {
        playersBuffs.add(pe.getType().getId());
        Packet41MobEffect pm = new Packet41MobEffect();
        pm.a = p.getEntityId(); //The entity ID
        pm.b = (byte)pe.getType().getId(); //The potion effect type
        pm.c = (byte)pe.getAmplifier(); //The amplifier
        pm.d = (short)pe.getDuration(); //The duration
        General.sendPacket(p,pm);
        pm = null;
    }
 
    //Remove the potion effect
    public static void removePotionEffectNoGraphic(Player p, PotionEffectType pe) {
        playersBuffs.remove(pe.getId());
        Packet42RemoveMobEffect pr = new Packet42RemoveMobEffect();
        pr.a = p.getEntityId();
        pr.b = (byte)pe.getId();
        General.sendPacket(p,pr);
        pr = null;
    }
 
    //Removes all of the players stored potion effects
    public static void removeAllPotionEffectsNoGrapic(Player p){
        for(Integer i : playersBuffs){
            removePotionEffectNoGraphic(p, PotionEffectType.getById(i));
            playersBuffs.remove(i);
        }
    }
 
    //Checks if a player has a certain potion effect
    public static boolean hasEffectNoGraphic(PotionEffectType pet){
        if(playersBuffs.contains(pet.getId())){
            return true;
        }
        else{
            return false;
        }
    }
}
