package packetUtils.pokuit.otherPersonMeh.forums.bukkit.org.threads.tutorial.utilizing.the.boss.health.bar.onefiveeightzerooneeight;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
 
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
 
public class FakeWither {
   
    public static final int MAX_HEALTH = 25;
    public boolean visible;
    public int EntityID;
    public int x;
    public int y;
    public int z;
    public int pitch = 0;
    public int head_pitch = 0;
    public int yaw = 0;
    public byte xvel = 0;
    public byte yvel = 0;
    public byte zvel = 0;
    public float health;
    public String name;
   
    public FakeWither(String name, int EntityID, Location loc){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D));
    }
   
    public FakeWither(String name, int EntityID, Location loc, float health, boolean visible){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D), health, visible);
    }
   
    public FakeWither(String name, int EntityID, int x, int y, int z){
        this(name, EntityID, x, y, z, MAX_HEALTH, false);
    }
   
    public FakeWither(String name, int EntityID, int x, int y, int z, float health, boolean visible){
        this.name=name;
        this.EntityID=EntityID;
        this.x=x;
        this.y=y;
        this.z=z;
        this.health=health;
        this.visible=visible;
    }
   
    public Object getMobPacket(){
        Class<?> mob_class = General.getCraftClass("Packet24MobSpawn");
        Object mobPacket = null;
        try {
            mobPacket = mob_class.newInstance();
           
            Field a = General.getField(mob_class, "a");
            a.setAccessible(true);
            a.set(mobPacket, EntityID);//Entity ID
            Field b = General.getField(mob_class, "b");
            b.setAccessible(true);
            b.set(mobPacket, EntityType.WITHER.getTypeId());//Mob type (ID: 64)
            Field c = General.getField(mob_class, "c");
            c.setAccessible(true);
            c.set(mobPacket, x);//X position
            Field d = General.getField(mob_class, "d");
            d.setAccessible(true);
            d.set(mobPacket, y);//Y position
            Field e = General.getField(mob_class, "e");
            e.setAccessible(true);
            e.set(mobPacket, z);//Z position
            Field f = General.getField(mob_class, "f");
            f.setAccessible(true);
            f.set(mobPacket, pitch);//Pitch
            Field g = General.getField(mob_class, "g");
            g.setAccessible(true);
            g.set(mobPacket, head_pitch);//Head Pitch
            Field h = General.getField(mob_class, "h");
            h.setAccessible(true);
            h.set(mobPacket, yaw);//Yaw
            Field i = General.getField(mob_class, "i");
            i.setAccessible(true);
            i.set(mobPacket, xvel);//X velocity
            Field j = General.getField(mob_class, "j");
            j.setAccessible(true);
            j.set(mobPacket, yvel);//Y velocity
            Field k = General.getField(mob_class, "k");
            k.setAccessible(true);
            k.set(mobPacket, zvel);//Z velocity
   
            Object watcher = getWatcher();
            Field t = General.getField(mob_class, "t");
            t.setAccessible(true);
            t.set(mobPacket, watcher);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
 
        return mobPacket;
    }
 
    public Object getDestroyEntityPacket(){
        Class<?> packet_class = General.getCraftClass("Packet29DestroyEntity");
        Object packet = null;
        try {
            packet = packet_class.newInstance();
           
            Field a = General.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, new int[]{EntityID});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
 
        return packet;
    }
 
    public Object getMetadataPacket(Object watcher){
        Class<?> packet_class = General.getCraftClass("Packet40EntityMetadata");
        Object packet = null;
        try {
            packet = packet_class.newInstance();
           
            Field a = General.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, EntityID);
           
            Method watcher_c = General.getMethod(watcher.getClass(), "c");
            Field b = General.getField(packet_class, "b");
            b.setAccessible(true);
            b.set(packet, watcher_c.invoke(watcher));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
 
        return packet;
    }
 
    public Object getRespawnPacket(){
        Class<?> packet_class = General.getCraftClass("Packet205ClientCommand");
        Object packet = null;
        try {
            packet = packet_class.newInstance();
           
            Field a = General.getField(packet_class, "a");
            a.setAccessible(true);
            a.set(packet, 1);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
 
        return packet;
    }
 
    public Object getWatcher(){
        Class<?> watcher_class = General.getCraftClass("DataWatcher");
        Object watcher = null;
        try {
            watcher = watcher_class.newInstance();
           
            Method a = General.getMethod(watcher_class, "a", new Class<?>[] {int.class, Object.class});
            a.setAccessible(true);
           
            a.invoke(watcher, 0, visible ? (byte)0 : (byte)0x20);
            a.invoke(watcher, 6, (Float) (float) health);
            a.invoke(watcher, 7, (Integer) (int) 0);
            a.invoke(watcher, 8, (Byte) (byte) 0);
            a.invoke(watcher, 10, (String) name);
            a.invoke(watcher, 11, (Byte) (byte) 1);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
       
        return watcher;
    }
   
}