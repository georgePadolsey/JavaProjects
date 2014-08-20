package superHeroFight.georgep.pokuit;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class masterTimer extends BukkitRunnable {

	private Integer Counter=0;
	heroStrengthTimer heroTime;
	
	public masterTimer(Plugin plugin) {
		this.heroTime = Main.heroTime;
	}
	public void run() {
		if(Counter % 10 == 0) {
			heroTime.run();
		}
		for(tempBlockReplace tbr:Main.tempBlockReplacements) {
			tbr.run();
		}
		addOne();
	}
	private void addOne() {
		if(Counter >= 100) {
			Counter=0;
		} else {
			Counter++;
		}
	}
}
