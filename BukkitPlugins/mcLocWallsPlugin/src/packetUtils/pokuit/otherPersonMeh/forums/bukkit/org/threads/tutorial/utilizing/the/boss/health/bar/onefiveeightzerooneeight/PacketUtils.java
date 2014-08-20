package packetUtils.pokuit.otherPersonMeh.forums.bukkit.org.threads.tutorial.utilizing.the.boss.health.bar.onefiveeightzerooneeight;
 
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
 
public class PacketUtils {
    public static Integer ENTITY_ID = 6000;
 
    //Other methods
    public static void displayTextBar(Plugin plugin, String text, final Player player, long length){
        final FakeWither wither = new FakeWither(text, ENTITY_ID++, player.getLocation());
        Object mobPacket = wither.getMobPacket();
 
        General.sendPacket(player, mobPacket);
 
        new BukkitRunnable(){
            @Override
            public void run(){
                Object destroyEntityPacket = wither.getDestroyEntityPacket();
 
                General.sendPacket(player, destroyEntityPacket);
            }
        }.runTaskLater(plugin, length);
    }
 
    public static void displayLoadingBar(final Plugin plugin, final String text, final Player player, final int healthAdd, final long delay, final boolean loadUp){
        final FakeWither wither = new FakeWither(text, ENTITY_ID++, player.getLocation(), (loadUp ? 1 : FakeWither.MAX_HEALTH), false);
        Object mobPacket = wither.getMobPacket();
 
        General.sendPacket(player, mobPacket);
 
        new BukkitRunnable(){
            int health = (loadUp ? 0 : FakeWither.MAX_HEALTH);
 
            @Override
            public void run(){
                if((loadUp ? health < FakeWither.MAX_HEALTH : health > 1)){
                    wither.name=text;
                    wither.health=health;
                    Object watcher = wither.getWatcher();
                    Object metaPacket = wither.getMetadataPacket(watcher);
 
                    General.sendPacket(player, metaPacket);
 
                    if(loadUp){
                        health += healthAdd;
                    } else {
                        health -= healthAdd;
                    }
                } else {
                    wither.name = text;
                    wither.health = (loadUp ? FakeWither.MAX_HEALTH : 1);
                    Object watcher = wither.getWatcher();
                    Object metaPacket = wither.getMetadataPacket(watcher);
 
                    General.sendPacket(player, metaPacket);
 
                    Object destroyEntityPacket = wither.getDestroyEntityPacket();
 
                    General.sendPacket(player, destroyEntityPacket);
 
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, delay, delay);
    }
 
    public static void displayLoadingBar(final Plugin plugin, final String text, final Player player, final int secondsDelay, final boolean loadUp){
        final int healthChangePerSecond = FakeWither.MAX_HEALTH / secondsDelay / 2;
 
        displayLoadingBar(plugin, text, player, healthChangePerSecond, 10L, loadUp);
    }
}