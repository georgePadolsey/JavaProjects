package WallsPlugin.mcLoc.pokuit.georgep;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum arenaState {
	FULL(Material.WOOL,14),
	OPEN(Material.IRON_SWORD, 0),
	VIP_ONLY(Material.DIAMOND, 0),
	OFFLINE(Material.WOOL, 14);
	
	Material material;
	Byte data;
	
	arenaState(Material material, int data) {
		this.material = material;
		this.data = (byte) data;
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(material,1,data);
	}
}
