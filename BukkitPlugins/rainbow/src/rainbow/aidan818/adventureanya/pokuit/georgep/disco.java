package rainbow.aidan818.adventureanya.pokuit.georgep;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class disco {
	
	Block b;
	Byte d = (byte)0;
	
	public disco(Location bl) {
		this.b=bl.getBlock();
		b.setType(Material.WOOL);
	}
	public void run() {
		if(b != null && d != null) {
			b.setData(d);
			if(d >= 15) {
				d=0;
			} else {
				d++;
			}
		}
	}
	void onBlockBreakEvent(BlockBreakEvent e) {
		if(e.getBlock().equals(b)) {
			this.b = null;
			this.d = null;
			Main.discos.remove(this);
		}
	}
}
