package WallsPlugin.mcLoc.pokuit.georgep;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum arenaTeams {
	TEAM_1(1,ChatColor.BLUE+"Team 1", ChatColor.GOLD+"Click To Join Team 1",DyeColor.BLUE),
	TEAM_2(2,ChatColor.RED+"Team 2", ChatColor.GOLD+"Click To Join Team 2",DyeColor.RED),
	TEAM_3(3,ChatColor.YELLOW+"Team 3", ChatColor.GOLD+"Click To Join Team 3",DyeColor.YELLOW),
	TEAM_4(4,ChatColor.LIGHT_PURPLE+"Team 4",ChatColor.GOLD+"Click To Join Team 4",DyeColor.PURPLE),
	SPEC(5,ChatColor.AQUA+"Spectator",ChatColor.GOLD+"Click To Spectate",Material.GOLD_INGOT);
	
	String s = null;
	String s2 = null;
	DyeColor dc = null;
	Material ma =null;
	int num = 0;
	
	arenaTeams(int num, String s, String s2, Material ma) {
		this.s = s;
		this.s2 = s2;
		this.ma= ma;
		this.num = num;
	}
	arenaTeams(int num, String s, String s2, DyeColor dc) {
		this.s = s;
		this.s2 = s2;
		this.num = num;
		this.dc = dc;
	}
	
	@Override
	public String toString() {
		return s;
	}
	
	public ItemStack getItem() {
		if(dc != null) {
			return new ItemStack(Material.WOOL, 1, this.dc.getWoolData());
		} else {
			return new ItemStack(this.ma, 1);
		}
	}
	public int getNum() {
		return this.num;
	}
	public String getName() {
		return this.s;
	}
	public String getDescriptionOfIconMenu() {
		return this.s2;
	}
}
