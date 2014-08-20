package rainbow.aidan818.adventureanya.pokuit.georgep;

import org.bukkit.scheduler.BukkitRunnable;

public class masterTimer extends BukkitRunnable {

	private Integer Counter=0;
	
	public masterTimer() {
		
	}
	public void run() {
		if(Counter % 10 == 0) {
			for(disco tbr:Main.discos) {
				tbr.run();
			}
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
